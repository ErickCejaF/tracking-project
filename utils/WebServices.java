package io.pixan.systramer.utils;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import io.pixan.systramer.BuildConfig;
import io.pixan.systramer.callbacks.BaseCallback;
import io.pixan.systramer.callbacks.ImageCallback;
import io.pixan.systramer.callbacks.InitTripCallback;
import io.pixan.systramer.callbacks.LoginCallback;
import io.pixan.systramer.callbacks.ProfileCallback;
import io.pixan.systramer.callbacks.ResumeCallback;
import io.pixan.systramer.callbacks.RouteCallback;
import io.pixan.systramer.callbacks.ServiceCallback;
import io.pixan.systramer.callbacks.TimesCallback;
import io.pixan.systramer.dialog.LoaderDialog;
import io.pixan.systramer.responses.InitTripResponse;
import io.pixan.systramer.responses.LoginResponse;
import io.pixan.systramer.responses.MediaResponse;
import io.pixan.systramer.responses.ProfileResponse;
import io.pixan.systramer.responses.ResponseResume;
import io.pixan.systramer.responses.RouteResponse;
import io.pixan.systramer.responses.ServiceResponse;
import io.pixan.systramer.responses.TimesResponse;
import io.pixan.systramer.rest.ApiRestAdapter;
import io.pixan.systramer.services.TripService;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static io.pixan.systramer.utils.SharedPreferences.getSharedToken;

public class WebServices {

    private static LoaderDialog loaderDialog;

    public static void wsUploadPhoto(Context context, MultipartBody.Part file, ImageCallback baseCallback) {
        loaderDialog = new LoaderDialog(context);
        Call<MediaResponse> loginResponse = new ApiRestAdapter().getAuthenticatedApiRest(context).wsMedia(file);
        loaderDialog.show();
        loginResponse.enqueue(new Callback<MediaResponse>() {
            @Override
            public void onResponse(Call<MediaResponse> call, Response<MediaResponse> response) {
                loaderDialog.dismiss();
                switch (response.code()) {
                    case 200:
                    case 201:
                        baseCallback.baseResponse(response.body());
                        break;

                    default:
                        baseCallback.baseError();
                        try {
                            Toast.makeText(context, new JSONObject(response.errorBody().string())
                                    .get("messages").toString().replace("[", "")
                                    .replace("]", "")
                                    .replace("\"", "")
                                    .replace(",", "\n\n"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<MediaResponse> call, Throwable t) {
                loaderDialog.dismiss();
                if (!call.isCanceled()) {
//                    baseCallback.baseError();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            }
        });

        loaderDialog.setOnCancelListener(dialogInterface -> loginResponse.cancel());
    }


    public static void wsLogin(Context context, String email, String password, LoginCallback loginCallback) {
        loaderDialog = new LoaderDialog(context);
        Call<LoginResponse> loginResponse = new ApiRestAdapter().getApiRest().wsLogIn(email, password, getSharedToken(context));
        loaderDialog.show();
        loginResponse.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loaderDialog.dismiss();
                switch (response.code()) {
                    case 200:
                    case 201:
                        loginCallback.baseCallback(response.body());
                        break;

                    default:
                        try {
                            Toast.makeText(context, new JSONObject(response.errorBody().string()).get("messages").toString().replace("[", "").replace("]", "").replace("\"", "").replace(",", "\n\n"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loaderDialog.dismiss();
                if (!call.isCanceled()) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            }
        });

        loaderDialog.setOnCancelListener(dialogInterface -> loginResponse.cancel());
    }

    public static void wsServices(Context context, boolean showProgressBar, int year, int month, ServiceCallback serviceCallback) {
        loaderDialog = new LoaderDialog(context);
        Call<ServiceResponse> servicesResponse = new ApiRestAdapter().getAuthenticatedApiRest(context).wsGetServices(year, month);
        if (showProgressBar) {
            loaderDialog.show();
        }
        servicesResponse.enqueue(new Callback<ServiceResponse>() {
            @Override
            public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                loaderDialog.dismiss();
                switch (response.code()) {
                    case 200:
                    case 201:
                        serviceCallback.baseResponse(response.body());
                        break;

                    default:
                        serviceCallback.baseError();
                        try {
                            Toast.makeText(context, new JSONObject(response.errorBody().string()).get("messages").toString().replace("[", "").replace("]", "").replace("\"", "").replace(",", "\n\n"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<ServiceResponse> call, Throwable t) {
                loaderDialog.dismiss();
                if (!call.isCanceled()) {
                    serviceCallback.baseError();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            }
        });


        loaderDialog.setOnCancelListener(dialogInterface -> servicesResponse.cancel());
    }

    public static void wsRecoverPassword(Context context, String user, boolean showProgressBar, BaseCallback baseCallback) {
        loaderDialog = new LoaderDialog(context);
        Call<ResponseBody> servicesResponse = new ApiRestAdapter().getApiRest().wsForgotPassword(user);
        if (showProgressBar) {
            loaderDialog.show();
        }
        servicesResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loaderDialog.dismiss();
                switch (response.code()) {
                    case 200:
                    case 201:
                        baseCallback.baseResponse(response.body());
                        break;

                    default:
                        baseCallback.baseError();
                        try {
                            Toast.makeText(context, new JSONObject(response.errorBody().string()).get("messages").toString().replace("[", "").replace("]", "").replace("\"", "").replace(",", "\n\n"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loaderDialog.dismiss();
                if (!call.isCanceled()) {
                    baseCallback.baseError();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            }
        });

        loaderDialog.setOnCancelListener(dialogInterface -> servicesResponse.cancel());
    }


    public static void wsPostAlert(Context context, int serviceId, int alertId, double latitude, double longitude, int velocity, boolean showProgressBar, BaseCallback baseCallback) {
        loaderDialog = new LoaderDialog(context);
        Call<ResponseBody> servicesResponse = new ApiRestAdapter().getAuthenticatedApiRest(context).wsAlerts(serviceId, alertId, latitude, longitude, velocity);
        if (showProgressBar) {
            loaderDialog.show();
        }
        servicesResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loaderDialog.dismiss();
                switch (response.code()) {
                    case 200:
                    case 201:
                        baseCallback.baseResponse(response.body());
                        break;

                    default:
                        baseCallback.baseError();
                        try {
                            Toast.makeText(context, new JSONObject(response.errorBody().string()).get("messages").toString().replace("[", "").replace("]", "").replace("\"", "").replace(",", "\n\n"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loaderDialog.dismiss();
                if (!call.isCanceled()) {
                    baseCallback.baseError();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            }
        });

        loaderDialog.setOnCancelListener(dialogInterface -> servicesResponse.cancel());
    }

    public static void wsProfile(Context context, boolean showProgressBar, ProfileCallback callback) {
        loaderDialog = new LoaderDialog(context);
        Call<ProfileResponse> servicesResponse = new ApiRestAdapter().getAuthenticatedApiRest(context).wsGetProfile();
        if (showProgressBar) {
            loaderDialog.show();
        }
        servicesResponse.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                loaderDialog.dismiss();
                switch (response.code()) {
                    case 200:
                    case 201:
                        callback.baseResponse(response.body().getData());
                        break;

                    default:
                        callback.baseError();
                        try {
                            Toast.makeText(context, new JSONObject(response.errorBody().string()).get("messages").toString().replace("[", "").replace("]", "").replace("\"", "").replace(",", "\n\n"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                loaderDialog.dismiss();
                if (!call.isCanceled()) {
                    callback.baseError();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            }
        });

        loaderDialog.setOnCancelListener(dialogInterface -> servicesResponse.cancel());
    }

    public static void wsUpdateProfile(Context context, String email, String name, MultipartBody.Part profilePicture, boolean showProgressBar, ProfileCallback callback) {
        loaderDialog = new LoaderDialog(context);
        Call<ProfileResponse> servicesResponse = new ApiRestAdapter().getAuthenticatedApiRest(context).wsUpdateProfile(email, name, profilePicture);
        if (showProgressBar) {
            loaderDialog.show();
        }
        servicesResponse.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                loaderDialog.dismiss();
                switch (response.code()) {
                    case 200:
                    case 201:
                        callback.baseResponse(response.body().getData());
                        break;

                    default:
                        callback.baseError();
                        try {
                            Toast.makeText(context, new JSONObject(response.errorBody().string()).get("messages").toString().replace("[", "").replace("]", "").replace("\"", "").replace(",", "\n\n"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                loaderDialog.dismiss();
                if (!call.isCanceled()) {
                    callback.baseError();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            }
        });

        loaderDialog.setOnCancelListener(dialogInterface -> servicesResponse.cancel());
    }

    public static void wsGetRouteInfo(Context context, int route, boolean showProgressBar, RouteCallback callback) {
        loaderDialog = new LoaderDialog(context);
        Call<RouteResponse> servicesResponse = new ApiRestAdapter().getAuthenticatedApiRest(context).wsGetRoute(route);
        if (showProgressBar) {
            loaderDialog.show();
        }
        servicesResponse.enqueue(new Callback<RouteResponse>() {
            @Override
            public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                loaderDialog.dismiss();
                switch (response.code()) {
                    case 200:
                    case 201:
                        callback.baseResponse(response.body().getData());
                        break;

                    default:
                        callback.baseError();
                        try {
                            Toast.makeText(context, new JSONObject(response.errorBody().string()).get("messages").toString().replace("[", "").replace("]", "").replace("\"", "").replace(",", "\n\n"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<RouteResponse> call, Throwable t) {
                loaderDialog.dismiss();
                if (!call.isCanceled()) {
                    callback.baseError();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            }
        });

        loaderDialog.setOnCancelListener(dialogInterface -> servicesResponse.cancel());
    }


    public static void wsGethours(Context context, String start, String end, boolean showProgressBar, TimesCallback callback) {
        loaderDialog = new LoaderDialog(context);
        Call<TimesResponse> servicesResponse = new ApiRestAdapter().getGoogleApiRest(context).wsGetTimes(start, end);
        if (showProgressBar) {
            loaderDialog.show();
        }
        servicesResponse.enqueue(new Callback<TimesResponse>() {
            @Override
            public void onResponse(Call<TimesResponse> call, Response<TimesResponse> response) {
                loaderDialog.dismiss();
                switch (response.code()) {
                    case 200:
                    case 201:
                        callback.baseResponse(response.body());
                        break;

                    default:
                        callback.baseError();
                        try {
                            Toast.makeText(context, new JSONObject(response.errorBody().string())
                                    .get("messages").toString().replace("[", "")
                                    .replace("]", "").replace("\"", "")
                                    .replace(",", "\n\n"), Toast.LENGTH_SHORT)
                                    .show();
                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<TimesResponse> call, Throwable t) {
                loaderDialog.dismiss();
                if (!call.isCanceled()) {
                    callback.baseError();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            }
        });

        loaderDialog.setOnCancelListener(dialogInterface -> servicesResponse.cancel());
    }

    public static void wsEvidence(Context context, String comments, int serviceId, int mediaId, int type, int unitId, boolean showProgressBar, BaseCallback callback) {
        loaderDialog = new LoaderDialog(context);
        Call<ResponseBody> servicesResponse = new ApiRestAdapter().getAuthenticatedApiRest(context).wsEvidences(serviceId, mediaId, comments, unitId, type);
        if (showProgressBar) {
            loaderDialog.show();
        }
        servicesResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loaderDialog.dismiss();
                switch (response.code()) {
                    case 200:
                    case 201:
                        callback.baseResponse(response.body());
                        break;

                    default:
                        callback.baseError();
                        try {
                            Toast.makeText(context, new JSONObject(response.errorBody().string())
                                    .get("messages").toString().replace("[", "")
                                    .replace("]", "").replace("\"", "")
                                    .replace(",", "\n\n"), Toast.LENGTH_SHORT)
                                    .show();
                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loaderDialog.dismiss();
                if (!call.isCanceled()) {
                    callback.baseError();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            }
        });

        loaderDialog.setOnCancelListener(dialogInterface -> servicesResponse.cancel());
    }

    public static void wsStartTripWithoutCode(Context context,
                                              int serviceId,
                                              String pushToken,
                                              double latitude,
                                              double longitude,
                                              boolean showProgressBar,
                                              BaseCallback callback) {

        loaderDialog = new LoaderDialog(context);
        if (showProgressBar) {
            loaderDialog.show();
        }
        loaderDialog.setCancelable(false);
        loaderDialog.setCanceledOnTouchOutside(false);

        Call<ResponseBody> baseResponse;
        baseResponse = new ApiRestAdapter().getAuthenticatedApiRest(context).wsInitService(
                serviceId,
                pushToken,
                latitude,
                longitude);

        Callback<ResponseBody> baseCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loaderDialog.dismiss();
                callback.baseResponse(response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loaderDialog.dismiss();
                callback.baseError();
            }
        };
        baseResponse.enqueue(baseCallback);
    }

    public static void wsStartTripWhitCode(Context context,
                                           int serviceId,
                                           String pushToken,
                                           String serviceToken,
                                           String initCode,
                                           double latitude,
                                           double longitude,
                                           boolean showProgressBar,
                                           InitTripCallback callback) {

        loaderDialog = new LoaderDialog(context);
        if (showProgressBar) {
            loaderDialog.show();
        }
        loaderDialog.setCancelable(false);
        loaderDialog.setCanceledOnTouchOutside(false);

        Call<InitTripResponse> servicesResponse;
        servicesResponse = new ApiRestAdapter().getAuthenticatedApiRest(context).wsInitService(
                serviceId,
                pushToken,
                serviceToken,
                initCode,
                latitude,
                longitude);
        Callback<InitTripResponse> codedCallback = new Callback<InitTripResponse>() {
            @Override
            public void onResponse(Call<InitTripResponse> call, Response<InitTripResponse> response) {
                loaderDialog.dismiss();
                switch (response.code()) {
                    case 200:
                    case 201:
                        callback.baseResponse(response.body().getData());
                        break;

                    case 422:
                        Toast.makeText(context, "El código de inicio no es válido", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        callback.baseError();
                        try {
                            Toast.makeText(context, new JSONObject(response.errorBody().string())
                                    .get("messages").toString().replace("[", "")
                                    .replace("]", "").replace("\"", "")
                                    .replace(",", "\n\n"), Toast.LENGTH_SHORT)
                                    .show();
                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<InitTripResponse> call, Throwable t) {
                loaderDialog.dismiss();
                if (!call.isCanceled()) {
                    callback.baseError();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            }
        };
        servicesResponse.enqueue(codedCallback);

    }

    public static void wsStopRoute(Context context, int serviceId, int siteOfInterestId, double latitude, double longitude, boolean showProgressBar, BaseCallback baseCallback) {
        loaderDialog = new LoaderDialog(context);
        Call<ResponseBody> servicesResponse = new ApiRestAdapter().getAuthenticatedApiRest(context).wsStopRoute(serviceId, latitude, longitude, siteOfInterestId);
        if (showProgressBar) {
            loaderDialog.show();
        }
        servicesResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loaderDialog.dismiss();
                switch (response.code()) {
                    case 200:
                    case 201:
                        baseCallback.baseResponse(response.body());
                        break;

                    default:
                        baseCallback.baseError();
                        try {
                            Toast.makeText(context, new JSONObject(response.errorBody().string()).get("messages").toString().replace("[", "").replace("]", "").replace("\"", "").replace(",", "\n\n"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loaderDialog.dismiss();
                if (!call.isCanceled()) {
                    baseCallback.baseError();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            }
        });

        loaderDialog.setOnCancelListener(dialogInterface -> servicesResponse.cancel());
    }


    public static void wsResumeTrip(Context context, int serviceId, int siteOfInterestId, double latitude, double longitude, boolean showProgressBar, ResumeCallback baseCallback) {
        loaderDialog = new LoaderDialog(context);
        Call<ResponseResume> servicesResponse = new ApiRestAdapter().getAuthenticatedApiRest(context).wsResumeRoute(serviceId, latitude, longitude, siteOfInterestId);
        if (showProgressBar) {
            loaderDialog.show();
        }
        servicesResponse.enqueue(new Callback<ResponseResume>() {
            @Override
            public void onResponse(Call<ResponseResume> call, Response<ResponseResume> response) {
                loaderDialog.dismiss();
                switch (response.code()) {
                    case 200:
                    case 201:
//                      baseCallback.baseResponse(response.body());
                        baseCallback.baseResponse(response.body());
                        break;

                    default:
                        baseCallback.baseError();
                        try {
                            Toast.makeText(context, new JSONObject(response.errorBody().string()).get("messages").toString().replace("[", "").replace("]", "").replace("\"", "").replace(",", "\n\n"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResponseResume> call, Throwable t) {
                loaderDialog.dismiss();
                if (!call.isCanceled()) {
                    baseCallback.baseError();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            }
        });

        loaderDialog.setOnCancelListener(dialogInterface -> servicesResponse.cancel());
    }


}


