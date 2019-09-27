package com.juaracoding.absensidika.MainMenu.activity;

import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;


import com.github.nikartm.button.FitButton;
import com.juaracoding.absensidika.CheckIn.activity.ChooseAbsen;
import com.juaracoding.absensidika.CheckIn.activity.QRActivity;
import com.juaracoding.absensidika.Login.activity.LoginActivity;
import com.juaracoding.absensidika.Permission.activity.PermissionApproval;
import com.juaracoding.absensidika.R;
import com.juaracoding.absensidika.Utility.AppUtil;

import org.json.JSONObject;

import java.io.IOException;

import br.com.safety.locationlistenerhelper.core.LocationTracker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenu extends AppCompatActivity {
    private LocationTracker locationTracker;



    ImageButton btnMenuAtas;

    FitButton btnCheckIn;
    FitButton btnCheckout;
    FitButton btnIjin;
    FitButton btnApproval;
    FitButton btnShowQR;
    FitButton btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        // StartBackgroundTask();
        btnMenuAtas = findViewById(R.id.more);
        btnCheckIn = findViewById(R.id.btnCheckIn);
        btnCheckout = findViewById(R.id.btnCheckOut);
        btnIjin = findViewById(R.id.btnIjin);
        btnApproval = findViewById(R.id.btnApproval);
        btnShowQR = findViewById(R.id.btnShowQR);
        btnLogout = findViewById(R.id.btnLogout);


        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, ChooseAbsen.class);
                startActivity(intent);
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, ChooseAbsen.class);
                startActivity(intent);
            }
        });

        btnIjin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, PermissionApproval.class);
                startActivity(intent);
            }
        });

        btnApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, PermissionApproval.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.setSetting(MainMenu.this, "isLogin", "0");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });


        btnMenuAtas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(MainMenu.this, btnMenuAtas);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.profile) {
                            // startActivity(new Intent(MainMenu.this,ProfileMenuActivity.class));
                        }

                        if (item.getItemId() == R.id.logout) {
                            AppUtil.setSetting(MainMenu.this, "isLogin", "0");
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        return true;
                    }
                });
                popupMenu.inflate(R.menu.menu_login);
                popupMenu.show();

            }
        });


    }

    /*
    private JobScheduler jobScheduler;
    private ComponentName componentName;
    private JobInfo jobInfo;

    public void StartBackgroundTask() {
        jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
        componentName = new ComponentName(getApplicationContext(), MyService.class);
        jobInfo = new JobInfo.Builder(1, componentName)
                .setMinimumLatency(18000) //10 sec interval
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).setRequiresCharging(false).build();
        jobScheduler.schedule(jobInfo);
    }

*/





    private void onMoreButtonClick(final View view) {

    }

}
