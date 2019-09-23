package com.juaracoding.absensidika.CheckIn.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.florent37.rxgps.RxGps;
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions;
import com.juaracoding.absensidika.ApiService.APIClient;
import com.juaracoding.absensidika.ApiService.APIInterfacesRest;
import com.juaracoding.absensidika.Application.AppController;
import com.juaracoding.absensidika.R;
import com.juaracoding.absensidika.Utility.AppUtil;
import com.juaracoding.absensidika.Utility.SaveModel;
import com.juaracoding.absensidika.Utility.Tools;
import com.juaracoding.absensidika.mlkit.common.CameraSource;
import com.juaracoding.absensidika.mlkit.common.CameraSourcePreview;
import com.juaracoding.absensidika.mlkit.common.GraphicOverlay;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelfieActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,
        CompoundButton.OnCheckedChangeListener, DataFromGraphic {

    private static final String TAG = "SelfieActivity";
    private static final int PERMISSION_REQUESTS = 1;

    private CameraSource cameraSource = null;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;

    private Button btnCommand;

    private Boolean isStartScanning = false;

    private static final String IMAGE_DIRECTORY = "/CustomImage/";

    String latitude = "";
    String longitude = "";

    LottieAlertDialog progressDialog;

    public void setProgressDialog() {
        progressDialog = new LottieAlertDialog.Builder(SelfieActivity.this, DialogTypes.TYPE_LOADING)
                .setTitle("Loading")
                .setDescription("Please Wait")
                .build();
        progressDialog.setCancelable(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie);


        createRandomVerification();
        setProgressDialog();
        setGPS();
        btnCommand = findViewById(R.id.btnCommand);
        btnCommand.setEnabled(true);
        btnCommand.setText("Start Selfie");

        btnCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnCommand.getText().toString().equalsIgnoreCase("Selfie OK, Kirim Absent?")) {
                    cameraSource.takePicture();
                } else {
                    isStartScanning = true;
                    btnCommand.setText("Ikuti Petunjuk!");
                    createVerification();
                    btnCommand.setEnabled(false);
                }
            }
        });

        preview = findViewById(R.id.firePreview);
        if (preview == null) {
            Log.d(TAG, "Preview is null");
        }
        graphicOverlay = findViewById(R.id.fireFaceOverlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }


        ToggleButton facingSwitch = findViewById(R.id.facingSwitch);
        facingSwitch.setOnCheckedChangeListener(this);
        // Hide the toggle button if there is only 1 camera
        if (Camera.getNumberOfCameras() == 1) {
            facingSwitch.setVisibility(View.GONE);
        }

        if (allPermissionsGranted()) {
            createCameraSource();
        } else {
            getRuntimePermissions();
        }


    }


    private void createCameraSource() {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay, this);
        }

        try {

            Log.i(TAG, "Using Face Detector Processor");
            cameraSource.setMachineLearningFrameProcessor(new FaceDetectionProcessor(getResources(), this));

        } catch (Exception e) {
            Log.e(TAG, "Can not create image processor: ", e);
            Toast.makeText(
                    getApplicationContext(),
                    "Can not create image processor: " + e.getMessage(),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }


    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "Permission granted!");
        if (allPermissionsGranted()) {
            createCameraSource();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TAG, "Set facing");
        if (cameraSource != null) {
            if (isChecked) {
                cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT);
            } else {
                cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);
            }
        }
        preview.stop();
        startCameraSource();
    }

    @Override
    public void isFacingLeft(Boolean facing) {

        if (facing && isStartScanning) {
            if (btnCommand.getText().toString().equalsIgnoreCase("HADAP KIRI")) {
                isStartScanning = false;
                btnCommand.setText("OK");
                createVerification();
            }
        }
    }

    @Override
    public void isFacingRight(Boolean facing) {
        if (facing && isStartScanning) {
            if (btnCommand.getText().toString().equalsIgnoreCase("HADAP KANAN")) {
                isStartScanning = false;
                btnCommand.setText("OK");
                createVerification();
            }
        }
    }

    @Override
    public void takePicture(Bitmap bitmap) {

        showCustomDialog(bitmap);
    }


    List<String> listVerification;
    int manyTimeVerification = -1;

    private List<String> createRandomVerification() {

        listVerification = new ArrayList<String>();
        listVerification.add("HADAP KANAN");
        listVerification.add("HADAP KIRI");

        return listVerification;

    }

    private void createVerification() {
        manyTimeVerification++;

        if (manyTimeVerification < listVerification.size()) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnCommand.setText(listVerification.get(manyTimeVerification));
                    isStartScanning = true;
                }
            }, 2000L);
        } else {
            isStartScanning = false;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnCommand.setText("Selfie OK, Kirim Absent?");
                    btnCommand.setEnabled(true);
                }
            }, 2000L);


        }
    }


    private void showCustomDialog(Bitmap bitmap) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ImageView imageSelfie = dialog.findViewById(R.id.imgSelfie);
        imageSelfie.setImageBitmap(rotateImage(bitmap, -90));

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendData(rotateImage(bitmap, -90));

            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }


    APIInterfacesRest apiInterface;


    private void sendData(Bitmap bitmap) {

        progressDialog.show();
        File file = createTempFile(bitmap);
        byte[] bImg1 = AppUtil.FiletoByteArray(file);

        RequestBody requestFile1 = RequestBody.create(MediaType.parse("image/jpeg"), compressCapture(bImg1, 900));
        MultipartBody.Part bodyImg1 =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile1);


        apiInterface = APIClient.getClientWithApi().create(APIInterfacesRest.class);


        Call<SaveModel> absentAdd = apiInterface.absenPhoto(
                toRequestBody(AppUtil.replaceNull("Userid")),
                toRequestBody(AppUtil.replaceNull("ManagerID")),
                toRequestBody(AppUtil.replaceNull("open")),
                toRequestBody(AppUtil.replaceNull(AppUtil.Now())),
                toRequestBody(AppUtil.replaceNull(latitude)),
                toRequestBody(AppUtil.replaceNull(longitude)),
                toRequestBody(AppUtil.replaceNull("address")),
                toRequestBody(AppUtil.replaceNull("selfie")),
                bodyImg1);

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
                        Toast.makeText(SelfieActivity.this, error, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        try {
                            progressDialog.dismiss();
                            Toast.makeText(SelfieActivity.this, "Send Failed, " + response.errorBody().string(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(SelfieActivity.this, "Maaf koneksi bermasalah", Toast.LENGTH_LONG).show();
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

    public static byte[] compressCapture(byte[] capture, int maxSizeKB) {

        // This should be different based on the original capture size
        int compression = 12;

        Bitmap bitmap = BitmapFactory.decodeByteArray(capture, 0, capture.length);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, compression, outputStream);
        return outputStream.toByteArray();
    }

    private File createTempFile(Bitmap bitmap) {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                , System.currentTimeMillis() + "_image.webp");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.WEBP, 0, bos);
        byte[] bitmapdata = bos.toByteArray();
        //write the bytes in file

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public void setGPS() {
        final RxGps rxGps = new RxGps(this);

        rxGps.lastLocation()


                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(location -> {
                    Toast.makeText(SelfieActivity.this, location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();
                    latitude = location.getLatitude() + "";
                    longitude = location.getLongitude() + "";
                }, throwable -> {
                    if (throwable instanceof RxGps.PermissionException) {
                        // displayError(throwable.getMessage());
                    } else if (throwable instanceof RxGps.PlayServicesNotAvailableException) {
                        // displayError(throwable.getMessage());
                    }
                });

    }


    private void showCustomDialogEnd() {
        final Dialog dialog = new Dialog(SelfieActivity.this);
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
        ((TextView) dialog.findViewById(R.id.txtKeterangan)).setText("ABSEN FOTO BERHASIL");

        //   ((TextView) dialog.findViewById(R.id.txtPilihAbsen)).setText(login.getStatusDetail());

    //    ((TextView) dialog.findViewById(R.id.txtName)).setText(AppController.getUserProfile().getSuccess().getUser().getPersonnel().getCompleteName());


        //ImageUtil.displayImage(((ImageView) dialog.findViewById(R.id.imgProfile)),AppController.getUserProfile().getSuccess().getUser().getPersonnel().getPhotoUrl(),null);


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


}


