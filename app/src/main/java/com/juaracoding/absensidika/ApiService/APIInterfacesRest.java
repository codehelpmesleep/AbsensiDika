package com.juaracoding.absensidika.ApiService;

/**
 * Created by user on 1/10/2018.
 */




import com.juaracoding.absensidika.Login.model.ModelLogin;
import com.juaracoding.absensidika.Utility.SaveModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by dewabrata on 18/09/2019.
 */

public interface APIInterfacesRest {


    @FormUrlEncoded
    @POST("api/user/login")
    Call<ModelLogin> getLogin(@Field("username") String username, @Field("password") String password);



    @Multipart
    @POST("api/absent_activity/add")
    Call<SaveModel> absenPhoto(

            @Part("user_id") RequestBody attend_for,
            @Part("manager_id") RequestBody latitude,
            @Part("status") RequestBody status,
            @Part("date_time") RequestBody date_time,
            @Part("lattiude") RequestBody lattiude,
            @Part("longitude") RequestBody longitude,
            @Part("address") RequestBody address,
            @Part("absent_type") RequestBody absent_type,
            @Part MultipartBody.Part picture

    );






//contoh
/*
    @FormUrlEncoded
    @POST("api/auth/signin")
    Call<UserModel> getLogin(@Field("email") String username, @Field("password") String password, @Field("device_id") String imei);

    @FormUrlEncoded
    @POST("api/change_password")
    Call<ForgotPassword> lupaPassword(@Field("old_password") String old_password, @Field("new_password") String new_password, @Field("new_password_confirmation") String new_password_confirmation);

    @FormUrlEncoded
    @POST("api/store_firebase_token")
    Call<FirebaseTokenSave> setFirebaseToken(@Field("firebase_token") String token);

    @GET("api/my_attendance")
    Call<AttendenceModel> getAttendence(@Query("year_month") String year_month);

    @GET("api/my_group_attendance")
    Call<AttendenceGroupModel> getAttendenceGroup(@Query("date") String year_month);

    @GET("api/danru_attendance_qr_code")
    Call<ShowQR> getShowQR();

    @GET("api/get_permission_category")
    Call<List<PermissionCategory>> getPermissionCategory();

    @GET("api/get_report_category")
    Call<List<PermissionCategory>> getPermissionCategory2();



    @GET("api/get_instruction_category")
    Call<ListInstruksiCategory> getInstructionCategory();

    @GET("api/my_report")
    Call<ListReport> getMyListReport();

    @GET("api/my_group_report")
    Call<ListReport> getMyGroupReport();

    @GET("api/instruction_by_me")
    Call<Instruksi> getInstruksiByMe();

    @GET("api/instruction_to_me")
    Call<Instruksi> getInstruksiToMe();

    @GET("api/my_group_permission")
    Call<ListGroupPermission> getListApprovalGroup();

    @GET("api/my_permission")
    Call<ListMyPermission> getMyListApproval();

    @GET("api/do_i_have_unfinished_panic")
    Call<HavePanic> getHavePanic();

    @FormUrlEncoded
    @POST("api/qr_attend")
    Call<AbsenQR> absenQR(@Field("qr_code") String qrcode);

    @FormUrlEncoded
    @POST("api/approve_permission_by_danru")
    Call<Approve> sendApprove(@Field("permission_id") int permission_id);

    @FormUrlEncoded
    @POST("api/decline_permission_by_danru")
    Call<Decline> sendReject(@Field("permission_id") int permission_id);

    @GET("api/my_group")
    Call<ListRegu> getMyListRegu();

    @FormUrlEncoded
    @POST("api/danru_do_absent_personnel")
    Call<Mangkir> createMangkir(@Field("personnel_id") int personnel_id, @Field("absent_date") String absent_date, @Field("description") String description);

    @GET("api/my_group_patrol")
    Call<MyGroupPatrol> getMyGroupPatrol();

    @GET("api/my_patrol")
    Call<MyPatrol> getMyPatrol();

    @GET("api/get_zone_duty_lists")
    Call<DutyList> getDutyList(@Query("zone_qr_code") String zone_qr_code);

    @FormUrlEncoded
    @POST("api/scan_qr_zone")
    Call<PatrolScanQR> createPatroliQR(@Field("zone_qr_code") String zone_qr_code, @Field("subject") String subject, @Field("message") String message, @Field("duty_id[]") ArrayList<Integer> duty_id);

    @FormUrlEncoded
    @POST("api/send_panic")
    Call<SendPanic> SendPanic(@Field("panic_type_id") String panicType);

    @FormUrlEncoded
    @POST("api/danru_patrol_feedback")
    Call<Feedback> createFeedback(@Field("patrol_check_time_id") int patrol_check_time_id, @Field("status") String status, @Field("subject") String subject, @Field("message") String message);

    @Multipart
    @POST("api/photo_attend")
    Call<AbsenQR> absenPhoto(

            @Part("attend_for") RequestBody attend_for,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part MultipartBody.Part img1

    );


    @POST("api/finish_panic")
    Call<FinishPanic> sendFinsihPanic(@Body RequestBody file);

    @GET("api/my_panic")
    Call<MyGroupPanic> getMyPanic();

    @GET("api/get_panic_by_id")
    Call<GetPanicById> getPanicById(@Query("panic_id") int panic_id);

    @GET("api/my_group_panic")
    Call<MyGroupPanic> getMyGroupPanic();

    @POST("api/create_report")
    Call<CreateReport> sendReport(@Body RequestBody file);

    @POST("api/create_instruction")
    Call<CreateInstruction> createInstruction(@Body RequestBody file);

    @POST("api/create_permission")
    Call<CreatePermission> sendPermission(@Body RequestBody file);




 /*@Headers("Content-Type: application/json")
 @POST("secure/entry")
 Call<UserModel> getLogin(@Body String body);


 @Headers("Content-Type: application/json")
 @POST("nomerst/getNomer")
 Call<Order> getOrder(@Body String body);

 @Headers("Content-Type: application/json")
 @POST("citdata/inputcitdata")
 Call<CITDataResult> sendCIT(@Body CITData body);
*/

 /*@Multipart
 @POST("api/tbl_pegawai/add")
 Call<Pegawai> addData(
         @Part("nama") RequestBody nama,
         @Part("no_pegawai") RequestBody no_pegawai,
         @Part("foto\"; filename=\"image.jpeg\"") RequestBody foto

 );*/



/*
 @GET("api/tbl_tukar_point/all")
 Call<TukarPoint> getTukarPoint(@Query("filter") String filter, @Query("field") String field, @Query("start") String start, @Query("limit") String limit);

 @GET("api/tbl_motor_bekas/all")
 Call<MotorBekas> getMotorBekas(@Query("filter") String filter, @Query("field") String field, @Query("start") String start, @Query("limit") String limit);

 @GET("api/tbl_elektronik/all")
 Call<Elektronik> getElektronik(@Query("filter") String filter, @Query("field") String field, @Query("start") String start, @Query("limit") String limit);

 @GET("api/tbl_info_kamm/all")
 Call<InfoKamm> getInfoKamm(@Query("filter") String filter, @Query("field") String field, @Query("start") String start, @Query("limit") String limit);
*/


}

