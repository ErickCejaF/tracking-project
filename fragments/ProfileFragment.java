package io.pixan.systramer.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.pixan.systramer.R;
import io.pixan.systramer.activities.EditProfileActivity;
import io.pixan.systramer.activities.LoginActivity;
import io.pixan.systramer.activities.SplashActivity;
import io.pixan.systramer.adapters.ProfileAdapter;
import io.pixan.systramer.callbacks.BaseCallback;
import io.pixan.systramer.callbacks.ProfileCallback;
import io.pixan.systramer.data.ProfileData;
import io.pixan.systramer.dialog.BaseDialog;
import io.pixan.systramer.models.AdapterProfileModel;
import io.pixan.systramer.models.ClientModel;
import kotlin.jvm.internal.PropertyReference0Impl;
import okhttp3.ResponseBody;

import static io.pixan.systramer.utils.Globals.carLocation;
import static io.pixan.systramer.utils.Globals.serviceLocation;
import static io.pixan.systramer.utils.SharedPreferences.getSharedServiceId;
import static io.pixan.systramer.utils.SharedPreferences.getSharedTripStatus;
import static io.pixan.systramer.utils.Utils.formatDate;
import static io.pixan.systramer.utils.Utils.logOut;
import static io.pixan.systramer.utils.WebServices.wsPostAlert;
import static io.pixan.systramer.utils.WebServices.wsProfile;

public class ProfileFragment extends Fragment {

    private ImageView ivMainOutsideBackground;
    private ImageView ivMiddleBackground;
    private ImageView ivInnerBackground;
    private ImageView ivUserProfile;
    private ImageView ivGrayPlaceholder;
    private ImageView ivIconPlaceholder;
    private SwipeRefreshLayout srlProfile;

    private LinearLayout llMainContainer;

    private RecyclerView rvProfile;
    private ProgressBar pbLoader;
    private ClientModel clientModel;
    private ProfileAdapter profileAdapter;
    private RelativeLayout rlProfileImage;
    private ArrayList<AdapterProfileModel> userInfo;

    private LinearLayout llEditProfile;
    private LinearLayout llLogOut;

    private ProfileData profileData;

    private TextView tvUsername;
    private boolean hasPadding;

    private LinearLayout llItemSos;
    private RelativeLayout lItemSos;

    public ProfileFragment() {
    }

    @SuppressLint("ValidFragment")
    public ProfileFragment(boolean hasPadding) {
        this.hasPadding = hasPadding;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false).getRoot();

        ivMainOutsideBackground = view.findViewById(R.id.iv_main_outside_background);
        ivMiddleBackground = view.findViewById(R.id.iv_middle_background);
        ivInnerBackground = view.findViewById(R.id.iv_inner_background);
        rvProfile = view.findViewById(R.id.rv_profile);
        ivUserProfile = view.findViewById(R.id.iv_user_profile_picture);
        tvUsername = view.findViewById(R.id.tv_username);
        rlProfileImage = view.findViewById(R.id.rl_placeholder_profile_image);
        pbLoader = view.findViewById(R.id.pb_loader);
        srlProfile = view.findViewById(R.id.srl_profile);
        llEditProfile = view.findViewById(R.id.ll_editprofile);
        llLogOut = view.findViewById(R.id.ll_logout);
        ivGrayPlaceholder = view.findViewById(R.id.iv_gray_placeholder);
        ivIconPlaceholder = view.findViewById(R.id.iv_icon_placeholder);
        llMainContainer = view.findViewById(R.id.ll_main_container);

        userInfo = new ArrayList<>();

        Glide
                .with(getContext())
                .load(R.drawable.style_light_black)
                .apply(RequestOptions.circleCropTransform())
                .into(ivMainOutsideBackground);

        Glide
                .with(getContext())
                .load(R.drawable.style_middle_black)
                .apply(RequestOptions.circleCropTransform())
                .into(ivMiddleBackground);

        Glide
                .with(getContext())
                .load(R.drawable.style_background_main_white)
                .apply(RequestOptions.circleCropTransform())
                .into(ivInnerBackground);

        Glide
                .with(getContext())
                .load(R.drawable.style_background_gray)
                .apply(RequestOptions.circleCropTransform())
                .into(ivGrayPlaceholder);


        llEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        lItemSos = view.findViewById(R.id.item_sos);
        llItemSos = lItemSos.findViewById(R.id.ll_button);

        llItemSos.setOnClickListener(v -> {
            if (carLocation != null && getSharedTripStatus(getContext())) {
                float speed = 0;
                if (serviceLocation != null)
                    speed = serviceLocation.getSpeed();

                wsPostAlert(getActivity(),
                        getSharedServiceId(getActivity()),
                        11,
                        carLocation.getLatitude(),
                        carLocation.getLongitude(),
                        (int) speed,
                        true,
                        new BaseCallback() {
                            @Override
                            public void baseResponse(ResponseBody responseBody) {
                                BaseDialog baseDialog = new BaseDialog(getContext());
                                baseDialog.setCancelable(false);
                                baseDialog.setCanceledOnTouchOutside(false);
                                baseDialog.setOnAcceptClicListener(v1 -> {
                                    baseDialog.dismiss();
                                });
                                baseDialog
                                        .setIvDialog(getResources().getDrawable(R.drawable.icn_red_alert))
                                        .setTvButton("ACEPTAR")
                                        .setTvMainInfo("Se mandó un mensaje a la central de alertas")
                                        .show();
                            }

                            @Override
                            public void baseError() {
                                Toast.makeText(getActivity(), "Ocurrio un error al mandar tu alerta, intentalo nuevamente", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        if (hasPadding) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) llMainContainer.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 120);
            llMainContainer.setLayoutParams(layoutParams);
        }

        llLogOut.setOnClickListener(v -> {
            if (!getSharedTripStatus(getContext())) {
                BaseDialog baseDialog = new BaseDialog(getContext());
                baseDialog.setCancelable(false);
                baseDialog.setCanceledOnTouchOutside(false);
                baseDialog
                        .setIvDialog(getActivity().getDrawable(R.drawable.icn_logout))
                        .setTvCancelButton("CANCELAR")
                        .setTvButton("ACEPTAR")
                        .setTvMainInfo("¿Estás seguro de querer cerrar sesión?")
                        .setOnAcceptClicListener(v1 -> {
                            logOut(getContext());
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            getActivity().finish();
                            baseDialog.dismiss();
                        })
                        .setOnCancelClickListener(v12 -> baseDialog.dismiss())
                        .show();
            } else {
                Toast.makeText(getContext(), "No se puede cerrar sesión con un viaje en progreso", Toast.LENGTH_SHORT).show();
            }
        });

        srlProfile.setOnRefreshListener(() -> {
            userInfo.clear();
            profileAdapter.notifyDataSetChanged();
            queryWs();
        });

        if (profileData == null) {
            queryWs();
        } else {
            pbLoader.setVisibility(View.GONE);
            rvProfile.setLayoutManager(new LinearLayoutManager(getContext()));
            rvProfile.setAdapter(profileAdapter);
            correctResponse(profileData);
        }

        return view;
    }

    private void correctResponse(ProfileData data) {
        pbLoader.setVisibility(View.GONE);
        rvProfile.setVisibility(View.VISIBLE);
        srlProfile.setRefreshing(false);

        tvUsername.setText(data.getName());
        profileData = data;

        if (profileData.getModification_request() != null) {
            Toast.makeText(getContext(), "Tu información aun se encuentra en revisión", Toast.LENGTH_SHORT).show();
        }

        if (getContext() != null)
            Glide
                    .with(getContext())
                    .load(data.getProfile_url())
                    .apply(RequestOptions.circleCropTransform())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            rlProfileImage.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(ivUserProfile);

        clientModel = data.getClient();

        if (clientModel != null) {
            for (Field field : clientModel.getClass().getDeclaredFields()) {
                try {
                    if (field.get(clientModel) != null) {
                        switch (field.getName()) {
                            case "commercial_name":
                                userInfo.add(new AdapterProfileModel("Cliente", field.get(clientModel).toString()));
                                break;
                            default:
                                break;
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        if (data.getDate_of_birth() != null)
            userInfo.add(new AdapterProfileModel("Fecha de nacimiento", formatDate(data.getDate_of_birth(), dateFormat)));

        if (data.getEmergency_phone() != null)
            userInfo.add(new AdapterProfileModel("Teléfono de emergencia", data.getEmergency_phone()));

        if (data.getRfc() != null)
            userInfo.add(new AdapterProfileModel("RFC", data.getRfc()));

        if (data.getNss() != null)
            userInfo.add(new AdapterProfileModel("Número seguro social", data.getNss()));

        if (data.getLicense_type_name() != null)
            userInfo.add(new AdapterProfileModel("Tipo de licencia", data.getLicense_type_name()));

        userInfo.add(new AdapterProfileModel("Número de licencia", data.getLicense_number()));

        profileAdapter = new ProfileAdapter(getContext(), userInfo);
        rvProfile.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProfile.setAdapter(profileAdapter);
    }

    private void badResponse() {
        pbLoader.setVisibility(View.GONE);
        rlProfileImage.setVisibility(View.VISIBLE);
        srlProfile.setRefreshing(false);
    }

    public void setBottomPadding() {
        if (llMainContainer == null) {
            hasPadding = true;
        } else {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) llMainContainer.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 120);
            llMainContainer.setLayoutParams(layoutParams);
        }
    }

    private void queryWs() {
        wsProfile(getContext(), false, new ProfileCallback() {
            @Override
            public void baseResponse(ProfileData data) {
                correctResponse(data);
            }

            @Override
            public void baseError() {
                badResponse();
            }
        });
    }

}