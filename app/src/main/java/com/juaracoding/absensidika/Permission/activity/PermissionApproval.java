package com.juaracoding.absensidika.Permission.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.nikartm.button.FitButton;
import com.juaracoding.absensidika.ApiService.APIClient;
import com.juaracoding.absensidika.ApiService.APIInterfacesRest;
import com.juaracoding.absensidika.Permission.adapter.AdapterListAttendenceMyApproval;
import com.juaracoding.absensidika.Permission.model.approval.PermissionActivity;
import com.juaracoding.absensidika.Permission.model.approval.PermissionApprovalModel;
import com.juaracoding.absensidika.R;
import com.juaracoding.absensidika.Utility.AppUtil;
import com.juaracoding.absensidika.Utility.SaveModel;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PermissionApproval extends AppCompatActivity {
    FitButton btnIjin;
    Spinner spinnnerKategori;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppUtil.getSetting(PermissionApproval.this,"showApproval","0").equalsIgnoreCase("0")) {
            setContentView(R.layout.activity_permission_approval);
        }else{
            setContentView(R.layout.activity_permission_approvalx);
        }

        btnIjin = (FitButton) findViewById(R.id.btnPengajuanIjin);

        if(AppUtil.getSetting(PermissionApproval.this,"showApproval","0").equalsIgnoreCase("1")) {
            btnIjin.setVisibility(View.GONE);
        }
        btnIjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PermissionApproval.this,CreatePermission.class));
            }
        });

        spinnnerKategori = (Spinner)findViewById(R.id.spinner2);
        spinnnerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(AppUtil.getSetting(PermissionApproval.this,"showApproval","0").equalsIgnoreCase("1")) {
            getData2();
        }else{

            getData();
        }


    }

    private RecyclerView recyclerView;
    private AdapterListAttendenceMyApproval mAdapter;
    private List<PermissionActivity> dataApprove;
    private List<PermissionActivity> dataProgress;
    private List<PermissionActivity> dataReject;
    private void initComponent(List<PermissionActivity> lstReport) {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(PermissionApproval.this));
        recyclerView.setHasFixedSize(true);




        dataApprove = new ArrayList<PermissionActivity>();
        dataProgress = new ArrayList<PermissionActivity>();
        dataReject = new ArrayList<PermissionActivity>();

        for (int x = 0; x< lstReport.size();x++){
            PermissionActivity p = lstReport.get(x);


            if(p.getStatus()!=null ){
                if(p.getStatus().equalsIgnoreCase("Approved")) {
                    dataApprove.add(p);

                }
            }

            if(p.getStatus()!=null){
                if(p.getStatus().toString().equalsIgnoreCase("Not Approved")) {
                    dataReject.add(p);

                }
            }



            if(p.getStatus()!=null ){
                if((p.getStatus().toString().equalsIgnoreCase("open") ) ) {
                                                dataProgress.add(p);


                }
            }
        }


        if(spinnnerKategori.getSelectedItem().toString().equalsIgnoreCase("Approved")){
            mAdapter = new AdapterListAttendenceMyApproval(PermissionApproval.this, dataApprove);
        }else if(spinnnerKategori.getSelectedItem().toString().equalsIgnoreCase("Not Approved")){
            mAdapter = new AdapterListAttendenceMyApproval(PermissionApproval.this, dataReject);
        }else{
            mAdapter = new AdapterListAttendenceMyApproval(PermissionApproval.this, dataProgress);
        }


        //set data and list adapter
        //   mAdapter = new AdapterListAttendenceMyApproval(getActivity(), lstReport);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListAttendenceMyApproval.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PermissionActivity obj, int position) {

                //Toast.makeText(PermissionApproval.this, view.getTag().toString(),Toast.LENGTH_SHORT).show();
                sendApproval(view.getTag().toString(),position);

            }
        });

    }

    APIInterfacesRest apiInterface;
    ProgressDialog progressDialog;
    PermissionApprovalModel permissionApproval;


    private void getData(){
        apiInterface = APIClient.getClientWithApi().create(APIInterfacesRest.class);




        Call<PermissionApprovalModel> approvalCall = apiInterface.getApprovalList("user_id", AppUtil.getSetting(PermissionApproval.this,"username",""));
        approvalCall.enqueue(new Callback<PermissionApprovalModel>() {
            @Override
            public void onResponse(Call<PermissionApprovalModel> call, Response<PermissionApprovalModel> response) {


                permissionApproval = response.body();

                if (permissionApproval !=null) {







                        initComponent(permissionApproval.getData().getPermissionActivity());










                }else{


                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //     Toast.makeText(ShoppingProductGrid.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        Toast.makeText(PermissionApproval.this, "Login Failed ,  "+response.errorBody().string(), Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        try {
                            Toast.makeText(PermissionApproval.this, "Login Failed, "+response.errorBody().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        //    Toast.makeText(ShoppingProductGrid.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<PermissionApprovalModel> call, Throwable t) {


                Toast.makeText(PermissionApproval.this,"Maaf koneksi bermasalah",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }





    private void getData2(){
        apiInterface = APIClient.getClientWithApi().create(APIInterfacesRest.class);




        Call<PermissionApprovalModel> approvalCall = apiInterface.getApprovalList2();
        approvalCall.enqueue(new Callback<PermissionApprovalModel>() {
            @Override
            public void onResponse(Call<PermissionApprovalModel> call, Response<PermissionApprovalModel> response) {


                permissionApproval = response.body();

                if (permissionApproval !=null) {







                    initComponent(permissionApproval.getData().getPermissionActivity());










                }else{


                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //     Toast.makeText(ShoppingProductGrid.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        Toast.makeText(PermissionApproval.this, "Login Failed ,  "+response.errorBody().string(), Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        try {
                            Toast.makeText(PermissionApproval.this, "Login Failed, "+response.errorBody().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        //    Toast.makeText(ShoppingProductGrid.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<PermissionApprovalModel> call, Throwable t) {


                Toast.makeText(PermissionApproval.this,"Maaf koneksi bermasalah",Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }



    SaveModel createPermission;
    File file1, file2, file3;

    LottieAlertDialog progressDialog2;

    private void sendApproval(String typeApproval, int indexId) {

        progressDialog2 = new LottieAlertDialog.Builder(PermissionApproval.this, DialogTypes.TYPE_LOADING)
                .setTitle("Loading")
                .setDescription("Please Wait")
                .build();
        progressDialog2.setCancelable(false);

        progressDialog2.show();

        apiInterface = APIClient.getClientWithApi().create(APIInterfacesRest.class);







        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("user_id", permissionApproval.getData().getPermissionActivity().get(indexId).getUserId());
        builder.addFormDataPart("manager_id", AppUtil.getSetting(PermissionApproval.this,"username",""));
        builder.addFormDataPart("status", typeApproval);
        builder.addFormDataPart("from_date", permissionApproval.getData().getPermissionActivity().get(indexId).getFromDate());
        builder.addFormDataPart("to_date", permissionApproval.getData().getPermissionActivity().get(indexId).getToDate());
        builder.addFormDataPart("permission_id", permissionApproval.getData().getPermissionActivity().get(indexId).getPermissionId());
        builder.addFormDataPart("id", permissionApproval.getData().getPermissionActivity().get(indexId).getId());






        MultipartBody requestBody = builder.build();
        Call<SaveModel> call = apiInterface.updatePermission(requestBody);
        call.enqueue(new Callback<SaveModel>() {
            @Override
            public void onResponse(Call<SaveModel> call, Response<SaveModel> response) {

                progressDialog2.dismiss();
                createPermission = response.body();

                if (createPermission.getStatus()) {




                    Toast.makeText(PermissionApproval.this, "Ok", Toast.LENGTH_LONG).show();
                   getData();





                } else {

                    progressDialog2.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //     Toast.makeText(ShoppingProductGrid.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        String error = jObjError.get("status_detail").toString();
                        Toast.makeText(PermissionApproval.this, error, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        try {
                            Toast.makeText(PermissionApproval.this, "Send Failed, " + response.errorBody().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        //    Toast.makeText(ShoppingProductGrid.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<SaveModel> call, Throwable t) {

                progressDialog2.dismiss();
                Toast.makeText(PermissionApproval.this, "Maaf koneksi bermasalah", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });


    }
}
