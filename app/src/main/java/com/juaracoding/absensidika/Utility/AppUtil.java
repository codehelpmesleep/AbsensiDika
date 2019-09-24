package com.juaracoding.absensidika.Utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


/**
 * Created by Mounzer on 8/16/2017.
 */

public class AppUtil {
//192.168.3.10
    //202.56.171.19:8080
     //192.168.56.1
    ///192.168.3.203
    //local 192.168.43.171



    public static boolean isEmpty(EditText editText) {
        return editText.getText() == null
                || editText.getText().toString().isEmpty();

    }

    public static boolean isValidPassword(String pass) {
        if (pass.length() >= 6) {
            return true;
        }
        return false;
    }

    /**
     * @param context the application context
     * @return true or false
     * @brief methods for identifying the device is supported for calling feature or not
     */
    public static boolean isDeviceCallSupported(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            Toast.makeText(context, "No Call Supported",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    /**
     * @param context the application context
     * @param number  the specified phone number
     * @brief methods for doing a phone call with specified phone number
     */
    public static void phoneCall(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    public static String formatCurrency(String s){
        if( s== null){

            s="0";
        }else{
            if (s.equalsIgnoreCase("")){
                s="0";
            }
        }
        return insertStringRev(s, ",", 3);
    }
    public static String formatCurrency(int s){
        return insertStringRev(s+"", ",", 3);
    }
    public static String insertStringRev(String original, String sInsert, int igroup) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < original.length(); i++) {

            if (((original.length()-i)%igroup)==0 && igroup!=0 && i!=0) {
                sb.append(sInsert+original.substring(i, i+1));
            }else {
                sb.append(original.substring(i, i+1));
            }
        }
        return sb.toString();
    }
    public static int getIntCut(String s){
        if (s==null){
            s="0";
        }
        StringBuffer sbBuffer = new StringBuffer("");
        for (int i = 0; i < s.length(); i++) {
            if ("1234567890".indexOf(s.substring(i,i+1))>=0) {
                sbBuffer.append(s.substring(i,i+1));
            }else{
                break;
            }
        }
        return getInt(sbBuffer.toString());
    }
    public static int getInt(String s){
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }
    public static String Now(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return sdf.format(calendar.getTime());
    }
    public static String NowX(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd yyyy");

        return sdf.format(calendar.getTime());
    }

    public final static Map<String, Object> pojo2Map(Object obj) {
        Map<String, Object> hashMap = new HashMap<String, Object>();
        try {
            Class<? extends Object> c = obj.getClass();
            Method m[] = c.getMethods();
            for (int i = 0; i < m.length; i++) {
                if (m[i].getName().indexOf("get") == 0) {
                    if (m[i].getName().equalsIgnoreCase("getDendasisa")){
                        String x="";
                    }
                    String name = m[i].getName().toLowerCase().substring(3, 4) + m[i].getName().substring(4);
                    hashMap.put(name, m[i].invoke(obj, new Object[0]));
                }
            }
        } catch (Throwable e) {
            //log error
        }
        return hashMap;
    }
    public static Vector<String> spliter(String original, String separator) {
        Vector<String> nodes = new Vector<String>();
        int index = original.indexOf(separator);
        while(index >= 0) {
            nodes.addElement( original.substring(0, index) );
            original = original.substring(index+separator.length());
            index = original.indexOf(separator);
        }
        nodes.addElement( original );
        return nodes;
    }

    public static String getSetting(Context context, String key, String def){//baca data yang disimpan(string)
        SharedPreferences settings = context.getApplicationContext().getSharedPreferences("rkrzmail.cctv", 0);
        String silent = settings.getString(key, def);
        return silent;
    }
    public static void setSetting(Context context, String key, String val){//Simpat data string
        SharedPreferences settings = context.getApplicationContext().getSharedPreferences("rkrzmail.cctv", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, val);
        editor.commit();
    }
    public static int getNumber(String s){
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            if ("01234567890".indexOf(s.charAt(i))!=-1) {
                buf.append(s.charAt(i));
            }
        }
        try {
            return Integer.parseInt(buf.toString());
        } catch (Exception e) { }
        return 0;
    }


    public static File savebitmap(Bitmap bmp, String imagefilename) throws IOException {
        String format = new SimpleDateFormat("yyyyMMddHHmmss",
                java.util.Locale.getDefault()).format(new Date());

        if (imagefilename==null){

            imagefilename = format ;
        }else{
            imagefilename+=format;
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + imagefilename+".jpg");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();

        return f;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static String replaceNull(String input) {
        return input == null ? "" : input;
    }

    public static String setZero(String input){

        if(input !=null){
            if(input.length() <2){
                input = "0"+input;
            }
        }
        return input;
    }


    public static byte [] FiletoByteArray  (File file){


        //init array with file length
        byte[] bytesArray = new byte[(int) file.length()];

        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(bytesArray); //read file into bytes[]
            fis.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytesArray;

    }



}
