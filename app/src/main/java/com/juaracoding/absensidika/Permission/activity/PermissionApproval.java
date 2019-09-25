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

import com.juaracoding.absensidika.ApiService.APIClient;
import com.juaracoding.absensidika.ApiService.APIInterfacesRest;
import com.juaracoding.absensidika.Permission.adapter.AdapterListAttendenceMyApproval;
import com.juaracoding.absensidika.Permission.model.approval.PermissionActivity;
import com.juaracoding.absensidika.Permission.model.approval.PermissionApprovalModel;
import com.juaracoding.absensidika.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PermissionApproval extends AppCompatActivity {
    Button btnIjin;
    Spinner spinnnerKategori;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_approval);
        btnIjin = (Button) findViewById(R.id.btnPengajuanIjin);

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
        getData();

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

            int y = 0;
            if(p.getStatus()!=null ){
                if(p.getStatus().equalsIgnoreCase("approved")) {
                    dataApprove.add(p);
                    y++;
                }
            }

            if(p.getStatus()!=null){
                if(p.getStatus().toString().equalsIgnoreCase("reject")) {
                    dataReject.add(p);
                    y++;
                }
            }



            if(p.getStatus()!=null ){
                if((p.getStatus().toString().equalsIgnoreCase("open") ) && y <1) {
                    dataProgress.add(p);
                }
            }
        }


        if(spinnnerKategori.getSelectedItem().toString().equalsIgnoreCase("Approved")){
            mAdapter = new AdapterListAttendenceMyApproval(PermissionApproval.this, dataApprove);
        }else  if(spinnnerKategori.getSelectedItem().toString().equalsIgnoreCase("Not Approved")){
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


            }
        });

    }

    APIInterfacesRest apiInterface;
    ProgressDialog progressDialog;
    PermissionApprovalModel permissionApproval;


    private void getData(){
        apiInterface = APIClient.getClientWithApi().create(APIInterfacesRest.class);




        Call<PermissionApprovalModel> approvalCall = apiInterface.getApprovalList("user_id","lana");
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
}
