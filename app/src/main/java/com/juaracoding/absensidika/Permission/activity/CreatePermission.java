package com.juaracoding.absensidika.Permission.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.juaracoding.absensidika.ApiService.APIClient;
import com.juaracoding.absensidika.ApiService.APIInterfacesRest;
import com.juaracoding.absensidika.Permission.model.PermissionCategory;
import com.juaracoding.absensidika.R;
import com.juaracoding.absensidika.Utility.SaveModel;
import com.juaracoding.absensidika.Utility.Tools;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spencerstudios.com.ezdialoglib.EZDialog;
import spencerstudios.com.ezdialoglib.EZDialogListener;

public class CreatePermission extends AppCompatActivity {

    public static int CAMERA1_REQUEST = 101;
    public static int CAMERA2_REQUEST = 102;
    public static int CAMERA3_REQUEST = 103;

    public static int FOLDER1_REQUEST = 104;
    public static int FOLDER2_REQUEST = 105;
    public static int FOLDER3_REQUEST = 106;

    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;
    private ImageButton img1, img2, img3;

    TextView txtTanggal,txtTanggal2;

    String tanggal1 ="";
    String tanggal2 ="";
    ArrayAdapter<String> spinnerArrayAdapter;
    EditText txtPerihal, txtKeterangan;
    Spinner spinner;
    Button btnKirim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ijin);
        spinner = (Spinner) findViewById(R.id.spinner);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);
        btnKirim = findViewById(R.id.btnKirim);
        txtPerihal = findViewById(R.id.txtPerihal);
        txtKeterangan = findViewById(R.id.txtKeterangan);
        txtTanggal = findViewById(R.id.txtTanggal);
        txtTanggal2 = findViewById(R.id.txtTanggal2);

        txtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDatePickerLight();
            }
        });

        txtTanggal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDatePickerLight2();
            }
        });


        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             StringBuilder sb = new StringBuilder();

                if(txtTanggal.getText().toString().equalsIgnoreCase("")){
                    sb.append("Masukan tanggal \n");
                }

                if(txtPerihal.getText().toString().equalsIgnoreCase("")){
                    sb.append("Masukan Perihal \n");
                }
                if(txtKeterangan.getText().toString().equalsIgnoreCase("")){
                    sb.append("Masukan Keterangan \n");
                }

                if (imageBitmap1==null ){
                    sb.append("Masukan minimal 1 attachment \n");
                }



                if(sb.toString().length()>1){
                    new EZDialog.Builder(CreatePermission.this)
                            .setTitle("Lengkapi Isian")
                            .setMessage(sb.toString())
                            .setPositiveBtnText("OK")
                            .setHeaderColor(Color.parseColor("#ff0000"))
                            .setCancelableOnTouchOutside(false)
                            .OnPositiveClicked(new EZDialogListener() {
                                @Override
                                public void OnClick() {


                                }
                            })

                            .build();
                }else{
                    uploadMultiFile();
                }




            }
        });


        initToolbar();
        checkPanic();
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog(CAMERA1_REQUEST);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog(CAMERA2_REQUEST);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog(CAMERA3_REQUEST);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {

        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ABSEN SAYA");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void dialogDatePickerLight() {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date_ship_millis = calendar.getTimeInMillis();
                        ((TextView) findViewById(R.id.txtTanggal)).setText(Tools.getFormattedDateSiapp3(date_ship_millis));
                        tanggal1 = Tools.getFormattedDateSiapp2(date_ship_millis);
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorfontsiap));
        datePicker.setMinDate(cur_calender);
        datePicker.show(getSupportFragmentManager(),"Datepickerdialog");
        //datePicker.show(getFragmentManager(), "Datepickerdialog");
    }

    private void dialogDatePickerLight2() {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date_ship_millis = calendar.getTimeInMillis();
                        ((TextView) findViewById(R.id.txtTanggal2)).setText(Tools.getFormattedDateSiapp3(date_ship_millis));
                        tanggal2 = Tools.getFormattedDateSiapp2(date_ship_millis);
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorfontsiap));
        datePicker.setMinDate(cur_calender);
        datePicker.show(getSupportFragmentManager(),"Datepickerdialog");
        //datePicker.show(getFragmentManager(), "Datepickerdialog");
    }

    private void showBottomSheetDialog(int data) {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View view = getLayoutInflater().inflate(R.layout.pickimage, null);
        ((ImageView) view.findViewById(R.id.btnCamera)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.Companion.with(CreatePermission.this)
                        .cameraOnly()
                        .start(data);

                mBottomSheetDialog.dismiss();
            }
        });


        ((ImageView) view.findViewById(R.id.btnFolder)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.Companion.with(CreatePermission.this)
                        .galleryOnly()
                        .start(data+3);

                mBottomSheetDialog.dismiss();
            }
        });


        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }

    Bitmap imageBitmap1, imageBitmap2, imageBitmap3;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA1_REQUEST || requestCode == FOLDER1_REQUEST) {
                //   String path = ImageUtil.getPath(CreateIjin.this,data.getData());
                //   if(path!=null) {
                //       Bitmap bm = ImageUtil.loadBitmap(path);
                try {
                    imageBitmap1 = MediaStore.Images.Media.getBitmap(CreatePermission.this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Picasso.get()
                        .load(data.getData())
                        .resize(180, 180)
                        .placeholder(R.drawable.photo_camera)
                        .into(img1);
            } else if (requestCode == CAMERA2_REQUEST || requestCode == FOLDER2_REQUEST) {
                try {
                    imageBitmap2 = MediaStore.Images.Media.getBitmap(CreatePermission.this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Picasso.get()
                        .load(data.getData())
                        .resize(180, 180)
                        .placeholder(R.drawable.photo_camera)
                        .into(img2);

            } else if (requestCode == CAMERA3_REQUEST || requestCode == FOLDER3_REQUEST) {
                try {
                    imageBitmap3 = MediaStore.Images.Media.getBitmap(CreatePermission.this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Picasso.get()
                        .load(data.getData())
                        .resize(180, 180)
                        .placeholder(R.drawable.photo_camera)
                        .into(img3);
            }

        }

        //  super.onActivityResult(requestCode, resultCode, data);

    }

    APIInterfacesRest apiInterface;

    PermissionCategory kategori;

    private void checkPanic() {
        apiInterface = APIClient.getClientWithApi().create(APIInterfacesRest.class);


        Call<PermissionCategory> merchantCall = apiInterface.getPermissionCategory();
        merchantCall.enqueue(new Callback<PermissionCategory>() {
            @Override
            public void onResponse(Call<PermissionCategory> call, Response<PermissionCategory> response) {
                // progress_bar.setVisibility(View.GONE);

                PermissionCategory kategori = response.body();

                if (kategori != null) {

                    ArrayList<String> lstKatgori = new ArrayList<String>();

                    for(int x = 0 ; x <kategori.getData().getPermissionCategory().size();x++){
                        lstKatgori.add(kategori.getData().getPermissionCategory().get(x).getPermission());
                    }


                    // Initializing an ArrayAdapter2
                    spinnerArrayAdapter = new ArrayAdapter<String>(
                            CreatePermission.this, R.layout.spinner_item,  lstKatgori);
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                    spinner.setAdapter(spinnerArrayAdapter);


                } else {


                    try {

                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                        Toast.makeText(CreatePermission.this, "Save Token Failed ,  " + response.errorBody().string(), Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        try {

                            Toast.makeText(CreatePermission.this, "Save Token Failed, " + response.errorBody().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }


                    }
                }

            }

            @Override
            public void onFailure(Call<PermissionCategory> call, Throwable t) {


                Toast.makeText(getApplicationContext(), "Maaf koneksi bermasalah", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }

    SaveModel createPermission;
    File file1, file2, file3;

    LottieAlertDialog progressDialog;

    private void uploadMultiFile() {

        progressDialog = new LottieAlertDialog.Builder(CreatePermission.this,DialogTypes.TYPE_LOADING)
                .setTitle("Loading")
                .setDescription("Please Wait")
                .build();
        progressDialog.setCancelable(false);

        progressDialog.show();

        apiInterface = APIClient.getClientWithApi().create(APIInterfacesRest.class);







        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("user_id", "dewa");
        builder.addFormDataPart("manager_id", "brahma");
        builder.addFormDataPart("status", "open");
        builder.addFormDataPart("from_date", tanggal1);
        builder.addFormDataPart("to_date", tanggal2);
        builder.addFormDataPart("permission_id", String.valueOf(spinner.getSelectedItemPosition() + 1));
        builder.addFormDataPart("comment", "");



        if (imageBitmap1 != null) {

            file1 = createTempFile(imageBitmap1);
            builder.addFormDataPart("picture1", file1.getName()+".jpg", RequestBody.create(MediaType.parse("multipart/form-data"), file1));
        }
        if (imageBitmap2 != null) {
            file2 = createTempFile(imageBitmap2);
            builder.addFormDataPart("picture2", file2.getName()+".jpg", RequestBody.create(MediaType.parse("multipart/form-data"), file2));
        }
        if (imageBitmap3 != null) {
            file3 = createTempFile(imageBitmap3);
            builder.addFormDataPart("picture3", file3.getName()+".jpg", RequestBody.create(MediaType.parse("multipart/form-data"), file3));
        }


        MultipartBody requestBody = builder.build();
        Call<SaveModel> call = apiInterface.sendPermission(requestBody);
        call.enqueue(new Callback<SaveModel>() {
            @Override
            public void onResponse(Call<SaveModel> call, Response<SaveModel> response) {

                progressDialog.dismiss();
                createPermission = response.body();

                if (createPermission.getStatus()) {




                        Toast.makeText(CreatePermission.this, "Ok", Toast.LENGTH_LONG).show();
                        // showCustomDialog();
                        finish();





                } else {

                    progressDialog.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //     Toast.makeText(ShoppingProductGrid.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        String error = jObjError.get("status_detail").toString();
                        Toast.makeText(CreatePermission.this, error, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        try {
                            Toast.makeText(CreatePermission.this, "Send Failed, " + response.errorBody().string(), Toast.LENGTH_LONG).show();
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
                Toast.makeText(CreatePermission.this, "Maaf koneksi bermasalah", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });


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

}