package com.juaracoding.absensidika.Login.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.rxgps.RxGps;

import com.google.gson.Gson;
import com.juaracoding.absensidika.ApiService.APIClient;
import com.juaracoding.absensidika.ApiService.APIInterfacesRest;
import com.juaracoding.absensidika.ApiService.AppUtil;
import com.juaracoding.absensidika.Application.AppController;
import com.juaracoding.absensidika.Login.model.ModelLogin;
import com.juaracoding.absensidika.MainMenu.activity.MainMenu;
import com.juaracoding.absensidika.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    ProgressBar progress_bar;
    EditText txtUsername, txtPassword;
    Button btnLogin;
    TextView btnBantuan, lupaPassword;
    private ImageView image;
    private AppCompatCheckBox chkIngat;
    private final String TAG="Test3DRotateActivity";


    private Rotate3dAnimation rotation;
    private StartNextRotate startNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        image = findViewById(R.id.logosiapp);

        chkIngat = findViewById(R.id.chkIngat);
        lupaPassword = findViewById(R.id.lupaPassword);

        lupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(LoginActivity.this,LupaPasswordActivity.class));
            }
        });


        txtUsername.setText(AppUtil.getSetting(LoginActivity.this, "username", ""));
        txtPassword.setText(AppUtil.getSetting(LoginActivity.this, "password", ""));

        if (AppUtil.getSetting(LoginActivity.this, "check", "").equalsIgnoreCase("true")) {
            chkIngat.setChecked(true);
        } else {
            chkIngat.setChecked(false);
        }


        btnLogin = findViewById(R.id.btnLogin);
        btnBantuan = findViewById(R.id.btnBantuanLogin);

        checkAndRequestPermissions(LoginActivity.this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progress_bar.setVisibility(View.VISIBLE);

                if (chkIngat.isChecked()) {
                    AppUtil.setSetting(LoginActivity.this, "username", txtUsername.getText().toString());
                    AppUtil.setSetting(LoginActivity.this, "password", txtPassword.getText().toString());
                    AppUtil.setSetting(LoginActivity.this, "check", "true");

                } else {
                    AppUtil.setSetting(LoginActivity.this, "username", "");
                    AppUtil.setSetting(LoginActivity.this, "password", "");
                    AppUtil.setSetting(LoginActivity.this, "check", "false");

                }

                startRotation(0,360);
                getData();

            }
        });
    }

    private void startRotation(float start, float end) {
        // Calculating center point
        final float centerX = image.getWidth() / 2.0f;
        final float centerY = image.getHeight() / 2.0f;
        Log.d(TAG, "centerX="+centerX+", centerY="+centerY);
        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        //final Rotate3dAnimation rotation =new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        //Z axis is scaled to 0
        rotation =new Rotate3dAnimation(start, end, centerX, centerY, 0f, true);
        rotation.setDuration(2000);
        rotation.setFillAfter(true);
        //rotation.setInterpolator(new AccelerateInterpolator());
        //Uniform rotation
        rotation.setInterpolator(new LinearInterpolator());
        //Monitor settings
        startNext = new StartNextRotate();

        rotation.setAnimationListener(startNext);
        image.startAnimation(rotation);
    }

    private class StartNextRotate implements Animation.AnimationListener {

        public void onAnimationEnd(Animation animation) {
            // TODO Auto-generated method stub
            Log.d(TAG, "onAnimationEnd......");
            // image.startAnimation(rotation);
        }

        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }

    }


    String latitude = "";
    String longitude = "";

    public void setGPS() {
        final RxGps rxGps = new RxGps(this);

        rxGps.lastLocation()


                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(location -> {
                    // Toast.makeText(LoginActivity.this,location.getLatitude() + ", " + location.getLongitude(),Toast.LENGTH_LONG).show();
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

    public boolean checkAndRequestPermissions(Context context) {

        List<String> listPermissionsNeeded = new ArrayList();
        int locationFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int locationAccess = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        int writefilePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        int cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (locationFine != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (locationAccess != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (writefilePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 6969);
            return false;
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    public String getIMEI(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    APIInterfacesRest apiInterface;
    ProgressDialog progressDialog;
    ModelLogin login;

    private void getData() {
        apiInterface = APIClient.getClientWithApi().create(APIInterfacesRest.class);

        Call<ModelLogin> merchantCall = apiInterface.getLogin(txtUsername.getText().toString(), txtPassword.getText().toString());
        merchantCall.enqueue(new Callback<ModelLogin>() {
            @Override
            public void onResponse(Call<ModelLogin> call, Response<ModelLogin> response) {
                // progress_bar.setVisibility(View.GONE);
                image.clearAnimation();
                rotation.cancel();
                login = response.body();

                if (login != null) {

                    if (login.getStatus()) {


                        if(login.getGroups()!=null){

                            for(int x= 0; x < login.getGroups().size();x++){
                                if(login.getGroups().get(x).equalsIgnoreCase("8")){

                                    AppUtil.setSetting(LoginActivity.this,"isManager","1");
                                }
                            }
                        }
                        AppUtil.setSetting(LoginActivity.this,"isLogin","1");
                        AppUtil.setSetting(LoginActivity.this,"username",login.getData().getUsername());
                        startActivity(new Intent(LoginActivity.this, MainMenu.class));
                        finish();


                    }


                } else {

                    image.clearAnimation();
                    rotation.cancel();
                    progress_bar.setVisibility(View.GONE);


                    InputStream i = response.errorBody().byteStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(i));
                    StringBuilder errorResult = new StringBuilder();
                    String line;
                    try {
                        while ((line = r.readLine()) != null) {
                            errorResult.append(line).append('\n');
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this, "Login gagal " + errorResult, Toast.LENGTH_LONG).show();


                }
            }


            @Override
            public void onFailure(Call<ModelLogin> call, Throwable t) {
                progress_bar.setVisibility(View.GONE);
                image.clearAnimation();
                rotation.cancel();
                Toast.makeText(getApplicationContext(), "Maaf koneksi bermasalah", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }

}
