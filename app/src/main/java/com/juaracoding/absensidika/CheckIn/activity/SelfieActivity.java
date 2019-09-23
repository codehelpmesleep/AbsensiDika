package com.juaracoding.absensidika.CheckIn.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions;
import com.juaracoding.absensidika.R;
import com.juaracoding.absensidika.mlkit.common.CameraSource;
import com.juaracoding.absensidika.mlkit.common.CameraSourcePreview;
import com.juaracoding.absensidika.mlkit.common.GraphicOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelfieActivity extends AppCompatActivity  implements ActivityCompat.OnRequestPermissionsResultCallback,
        CompoundButton.OnCheckedChangeListener , DataFromGraphic{

    private static final String TAG = "SelfieActivity";
    private static final int PERMISSION_REQUESTS = 1;

    private CameraSource cameraSource = null;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;

    private Button btnCommand;

    private Boolean isStartScanning = false;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie);


        createRandomVerification();
        btnCommand = findViewById(R.id.btnCommand);
        btnCommand.setEnabled(true);
        btnCommand.setText("Start Selfie");

        btnCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartScanning =true;
                btnCommand.setText("Ikuti Petunjuk!");
                createVerification();
                btnCommand.setEnabled(false);
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
            cameraSource = new CameraSource(this, graphicOverlay);
        }

        try {

                    Log.i(TAG, "Using Face Detector Processor");
                    cameraSource.setMachineLearningFrameProcessor(new FaceDetectionProcessor(getResources(),this));

        } catch (Exception e) {
            Log.e(TAG, "Can not create image processor: " , e);
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
            if (isChecked ) {
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

        if(facing && isStartScanning) {
            if(btnCommand.getText().toString().equalsIgnoreCase("HADAP KIRI")){
                isStartScanning=false;
                btnCommand.setText("OK");
                createVerification();
            }
        }
    }

    @Override
    public void isFacingRight(Boolean facing) {
        if(facing && isStartScanning) {
            if(btnCommand.getText().toString().equalsIgnoreCase("HADAP KANAN")){
                isStartScanning=false;
                btnCommand.setText("OK");
                createVerification();
            }
        }
    }


    List<String> listVerification;
    int manyTimeVerification =-1;
    private List<String> createRandomVerification(){

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
                    isStartScanning=true;
                }
            }, 2000L);
        }else{
            isStartScanning = false;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnCommand.setText("Selfie OK, Kirim Absent?");
                                        btnCommand.setEnabled(true);
                                    }
                                },2000L);


        }
    }
}


