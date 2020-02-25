package io.pixan.systramer.rest;

import androidx.constraintlayout.widget.ConstraintLayout;
import io.pixan.systramer.responses.InitTripResponse;
import io.pixan.systramer.responses.LoginResponse;
import io.pixan.systramer.responses.MediaResponse;
import io.pixan.systramer.responses.ProfileResponse;
import io.pixan.systramer.responses.ResponseResume;
import io.pixan.systramer.responses.RouteResponse;
import io.pixan.systramer.responses.ServiceResponse;
import io.pixan.systramer.responses.TimesResponse;
import io.pixan.systramer.utils.Constants;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiRestTasks {

    @FormUrlEncoded
    @POST(Constants.ENDPOINT_LOGIN)
    Call<LoginResponse> wsLogIn(
            @Field("email") String email,
            @Field("password") String password,
            @Field("push_token") String pushToken
    );

    @FormUrlEncoded
    @POST(Constants.ENDPOINT_EVIDENCES)
    Call<ResponseBody> wsLogout();


    @GET(Constants.ENDPOINT_SERVICES)
    Call<ServiceResponse> wsGetServices(
            @Query("year") int year,
            @Query("month") int month
    );

    @FormUrlEncoded
    @POST(Constants.ENDPOINT_FORGOT_PASSWORD)
    Call<ResponseBody> wsForgotPassword(
            @Field("email") String email
    );

    @Multipart
    @POST(Constants.ENDPOINT_FILE)
    Call<MediaResponse> wsMedia(
            @Part MultipartBody.Part file
    );

    @FormUrlEncoded
    @POST(Constants.ENDPOINT_ALERTS)
    Call<ResponseBody> wsAlerts(
            @Path("serviceId") int serviceId,
            @Field("alert_id") int alertId,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("velocity") int velocity
    );

    @GET(Constants.ENDPOINT_PROFILE)
    Call<ProfileResponse> wsGetProfile(
    );

    @Multipart
    @POST(Constants.ENDPOINT_PROFILE)
    Call<ProfileResponse> wsUpdateProfile(
            @Query("email") String email,
            @Query("name") String name,
            @Part MultipartBody.Part file
    );

    @GET(Constants.ENDPOINT_ROUTES)
    Call<RouteResponse> wsGetRoute(
            @Path("route_id") int routeId
    );

    //GOOGLE
    @GET(Constants.ENDPOINT_TIMES)
    Call<TimesResponse> wsGetTimes(
            @Query("origins") String origin,
            @Query("destinations") String destinations
    );

    @FormUrlEncoded
    @POST(Constants.ENDPOINT_INIT_SERVICE)
    Call<ResponseBody> wsInitService(
            @Path("serviceId") int serviceId,
            @Field("push_token") String pushToken,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude
    );

    @FormUrlEncoded
    @POST(Constants.ENDPOINT_INIT_SERVICE)
    Call<InitTripResponse> wsInitService(
            @Path("serviceId") int serviceId,
            @Field("push_token") String pushToken,
            @Field("service_token") String serviceToken,
            @Field("init_code") String initCode,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude
    );

    @FormUrlEncoded
    @POST(Constants.ENDPOINT_FINISH_SERVICE)
    Call<ResponseBody> wsFinishService(
            @Field("serviceId") String serviceId
    );

    @FormUrlEncoded
    @POST(Constants.ENDPOINT_EVIDENCES)
    Call<ResponseBody> wsEvidences(
            @Path("serviceId") int serviceId,
            @Field("media_id") int mediaId,
            @Field("comments") String comments,
            @Field("unit_id") int unitId,
            @Field("type") int type
    );

    @FormUrlEncoded
    @POST(Constants.ENDPOINT_STOP_ROUTE)
    Call<ResponseBody> wsStopRoute(
            @Path("serviceId") int serviceId,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("site_of_interest_id") int siteOfInterestId
    );

    @FormUrlEncoded
    @POST(Constants.ENDPOINT_RESUME_ROUTE)
    Call<ResponseResume> wsResumeRoute(
            @Path("serviceId") int serviceId,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("site_of_interest_id") int siteOfInterestId
    );


}
