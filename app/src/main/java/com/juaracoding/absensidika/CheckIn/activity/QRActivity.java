package com.juaracoding.absensidika.CheckIn.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.juaracoding.absensidika.ApiService.APIClient;
import com.juaracoding.absensidika.ApiService.APIInterfacesRest;
import com.juaracoding.absensidika.R;
import com.juaracoding.absensidika.Utility.AppUtil;
import com.juaracoding.absensidika.Utility.ImageUtil;
import com.juaracoding.absensidika.Utility.SaveModel;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QRActivity extends AppCompatActivity {

    private ProgressBar progress_bar;
    private FloatingActionButton fab;
    private EditText et_search;
    private String shipNo, orderNo;
    SurfaceView cameraView;
    private Boolean isScan;

    String latitude = "";
    String longitude = "";

    LottieAlertDialog progressDialog;

    public void setProgressDialog() {
        progressDialog = new LottieAlertDialog.Builder(QRActivity.this, DialogTypes.TYPE_LOADING)
                .setTitle("Loading")
                .setDescription("Please Wait")
                .build();
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode);
        isScan = true;


        setProgressDialog();

         cameraView = (SurfaceView) findViewById(R.id.camera_view);


         setDimension();


        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)

                        .build();

        final CameraSource cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)


                .setRequestedPreviewSize(640, 480)
                .build();
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraSource.start(cameraView.getHolder());

                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }




            });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0 && isScan) {
                    isScan=false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showCustomDialog( barcodes.valueAt(0).displayValue);
                        }
                    });



                }
            }
        });
    }









    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }




    private void setDimension() {
        // Adjust the size of the video
        // so it fits on the screen
        float videoProportion = getVideoProportion();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        float screenProportion = (float) screenHeight / (float) screenWidth;
        android.view.ViewGroup.LayoutParams lp = cameraView.getLayoutParams();

        if (videoProportion < screenProportion) {
            lp.height= screenHeight;
            lp.width = (int) ((float) screenHeight / videoProportion);
        } else {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth * videoProportion);
        }
        cameraView.setLayoutParams(lp);

    }

    // This method gets the proportion of the video that you want to display.
// I already know this ratio since my video is hardcoded, you can get the
// height and width of your video and appropriately generate  the proportion
//    as :height/width
    private float getVideoProportion(){
        return 1.5f;
    }


    private void showCustomDialogEnd() {
        final Dialog dialog = new Dialog(QRActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_light);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
/*
        if(login.getStatusDetail().equalsIgnoreCase("Personnel Successfully Leaving Attendance!")){
            ((TextView) dialog.findViewById(R.id.txtJam)).setText(txtJamPulang.getText().toString());
        }else if(login.getStatusDetail().equalsIgnoreCase("Personnel Successfully Attend!")){
            ((TextView) dialog.findViewById(R.id.txtJam)).setText(txtJamMasuk.getText().toString());
        }else{
            ((TextView) dialog.findViewById(R.id.txtJam)).setText(txtJamPulang.getText().toString());
        }
*/
        ((TextView) dialog.findViewById(R.id.txtJam)).setText(AppUtil.Now());
        ((TextView) dialog.findViewById(R.id.txtTanggal)).setText(AppUtil.NowX());
        ((TextView) dialog.findViewById(R.id.txtKeterangan)).setText("ABSEN QR BERHASIL");

        ((TextView) dialog.findViewById(R.id.txtPilihAbsen)).setText("DATANG");

        ((TextView) dialog.findViewById(R.id.txtName)).setText("NAMA");


     //   ImageUtil.displayImage(((ImageView) dialog.findViewById(R.id.imgProfile)),com.juaracoding.absensidika.ApiService.AppUtil.BASE_URL +"uploads/absent_activity/"+filename,null);

        ((ImageView) dialog.findViewById(R.id.imgProfile)).setVisibility(View.GONE);
        ((AppCompatButton) dialog.findViewById(R.id.btnOk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                //    Intent data = new Intent();
                //   data.putExtra("tipe",MainActivity.TIPEABSEN);
                //     setResult(666,data);
                finish();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private void showCustomDialog(String qr) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ImageView imageSelfie = dialog.findViewById(R.id.imgSelfie);
        imageSelfie.setVisibility(View.GONE);

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                sendData(qr);

            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    APIInterfacesRest apiInterface;



    private void sendData(String qr) {

        progressDialog.show();


        apiInterface = APIClient.getClientWithApi().create(APIInterfacesRest.class);



        Call<SaveModel> absentAdd = apiInterface.absenPhoto(
                toRequestBody(AppUtil.replaceNull("Userid")),
                toRequestBody(AppUtil.replaceNull("ManagerID")),
                toRequestBody(AppUtil.replaceNull("open")),
                toRequestBody(AppUtil.replaceNull(AppUtil.Now())),
                toRequestBody(AppUtil.replaceNull(latitude)),
                toRequestBody(AppUtil.replaceNull(longitude)),
                toRequestBody(AppUtil.replaceNull("address")),
                toRequestBody(AppUtil.replaceNull("qr")),
                toRequestBody(AppUtil.replaceNull(qr)),
                null);

        absentAdd.enqueue(new Callback<SaveModel>() {
            @Override
            public void onResponse(Call<SaveModel> call, Response<SaveModel> response) {

                progressDialog.dismiss();

                SaveModel login = response.body();

                if (login != null) {

                    if (login.getStatus()) {
                        showCustomDialogEnd();

                    } else {
                        progressDialog.dismiss();
                        try {
                            JSONObject jObjError = new JSONObject(response.body().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                } else {

                    progressDialog.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //     Toast.makeText(ShoppingProductGrid.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        String error = jObjError.get("status_detail").toString();
                        Toast.makeText(QRActivity.this, error, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        try {
                            progressDialog.dismiss();
                            Toast.makeText(QRActivity.this, "Send Failed, " + response.errorBody().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        //    Toast.makeText(ShoppingProductGrid.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<SaveModel> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(QRActivity.this, "Maaf koneksi bermasalah", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }

    public RequestBody toRequestBody(String value) {
        if (value == null) {
            value = "";
        }
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

}
