package io.pixan.systramer.rest;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.pixan.systramer.R;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static io.pixan.systramer.BuildConfig.BASE_URL;
import static io.pixan.systramer.utils.Constants.GOOGLE_BASE;
import static io.pixan.systramer.utils.Utils.getSavedUser;
import static io.pixan.systramer.utils.Utils.logOut;


public class ApiRestAdapter {
    private OkHttpClient okHttpClient;

    public ApiRestTasks getApiRest() {
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Response origResponse = chain.proceed(request);
                    String responseStringOrig = origResponse.body().string();
                    return origResponse
                            .newBuilder()
                            .body(ResponseBody.create(origResponse.body().contentType(), responseStringOrig))
                            .build();
                })
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(ApiRestTasks.class);
    }

    public ApiRestTasks getAuthenticatedApiRest(Context context) {

        String authToken = getSavedUser().getAccess_token();

        System.out.println("auth token " + authToken);

        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request.Builder request = chain.request().newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Authorization", "Bearer " + authToken);
                    Response origResponse = chain.proceed(request.build());
                    String responseStringOrig = origResponse.body().string();

                    switch (origResponse.code()) {
                        case 401:
                            logOut(context);
                            break;
                    }

                    return origResponse
                            .newBuilder()
                            .body(ResponseBody.create(origResponse.body().contentType(), responseStringOrig))
                            .build();
                })
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(ApiRestTasks.class);

    }

    public ApiRestTasks getGoogleApiRest(Context context) {


        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    HttpUrl url = chain.request().url().newBuilder()
                            .addQueryParameter("key", context.getResources()
                                    .getString(R.string.api_key))
                            .addQueryParameter("language", String.valueOf(Locale.getDefault()))
                            .build();
                    Response origResponse = chain.proceed(chain.request().newBuilder().url(url).build());
                    String responseStringOrig = origResponse.body().string();

                    return origResponse
                            .newBuilder()
                            .body(ResponseBody.create(origResponse.body().contentType(), responseStringOrig))
                            .build();
                })
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GOOGLE_BASE)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(ApiRestTasks.class);

    }


}
