package com.juaracoding.absensidika.Permission.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.nikartm.button.FitButton;
import com.google.zxing.WriterException;
import com.juaracoding.absensidika.ApiService.APIClient;
import com.juaracoding.absensidika.ApiService.APIInterfacesRest;


import com.juaracoding.absensidika.Login.activity.LoginActivity;
import com.juaracoding.absensidika.R;
import com.juaracoding.absensidika.Utility.AppUtil;
import com.juaracoding.absensidika.Utility.SaveModel;
import com.juaracoding.absensidika.Utility.SharedPreferencesUtil;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRgeneratorActivity extends AppCompatActivity {
  Bitmap bitmap;
  Date currentTime;
  SimpleDateFormat formatDateTime;
  String kode, now, status;
  ImageView qrImage;
  FitButton btnQR;
  String username;
  SaveModel saveModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_qrgenerator);

    username = com.juaracoding.absensidika.ApiService.AppUtil.getSetting(QRgeneratorActivity.this,"username","");
    qrImage = findViewById(R.id.qrImage);
    btnQR = findViewById(R.id.btnQR);
    btnQR.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        formatDateTime = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        currentTime = Calendar.getInstance().getTime();
        kode = AppUtil.md5("juaracoding" + username + formatDateTime.format(currentTime));
        now = AppUtil.Now();

        btnQR.setEnabled(false);
        btnQR.postDelayed(new Runnable() {
          @Override
          public void run() {
            btnQR.setEnabled(true);
          }
        }, 5000);

        callQRCodeManagerAPI();
        generateQR();
      }
    });
  }

  APIInterfacesRest apiInterface;
  ProgressDialog progressDialog;
  public void callQRCodeManagerAPI() {
    apiInterface = APIClient.getClientWithApi().create(APIInterfacesRest.class);
    progressDialog = new ProgressDialog(QRgeneratorActivity.this);
    progressDialog.setTitle("Loading");
    progressDialog.show();
    Call<SaveModel> mulaiRequest = apiInterface.qrCodeManager(kode, now, "unscanned",username);
    mulaiRequest.enqueue(new Callback<SaveModel>() {
      @Override
      public void onResponse(Call<SaveModel> call, Response<SaveModel> response) {
        progressDialog.dismiss();
        saveModel = response.body();
        if (saveModel != null) {
          Toast.makeText(QRgeneratorActivity.this, saveModel.getMessage(), Toast.LENGTH_LONG).show();
        } else {
          try {
            JSONObject jObjError = new JSONObject(response.errorBody().string());
            Toast.makeText(QRgeneratorActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
          } catch (Exception e) {
            Toast.makeText(QRgeneratorActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
          }
        }
      }

      @Override
      public void onFailure(Call<SaveModel> call, Throwable t) {
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), "Maaf koneksi bermasalah", Toast.LENGTH_LONG).show();
        call.cancel();
      }
    });
  }

  public void generateQR() {
    // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
    QRGEncoder qrgEncoder = new QRGEncoder(kode, null, QRGContents.Type.TEXT, 1000);
    try {
      // Getting QR-Code as Bitmap
      bitmap = qrgEncoder.encodeAsBitmap();
      // Setting Bitmap to ImageView
      qrImage.setImageBitmap(bitmap);
    } catch (WriterException e) {
      Log.v("error", e.toString());
    }
  }
}
