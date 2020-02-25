package io.pixan.systramer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import io.pixan.systramer.R;
import io.pixan.systramer.callbacks.BaseCallback;
import io.pixan.systramer.callbacks.FinishedUploadingPictureCallback;
import io.pixan.systramer.callbacks.ImageCallback;
import io.pixan.systramer.callbacks.ProfileCallback;
import io.pixan.systramer.data.ProfileData;
import io.pixan.systramer.responses.MediaResponse;
import io.pixan.systramer.utils.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static io.pixan.systramer.utils.WebServices.wsUpdateProfile;
import static io.pixan.systramer.utils.WebServices.wsUploadPhoto;

public class EditProfileActivity extends BaseActivity {

    private ImageView ivBackgroundBlue;
    private EditText etUser;
    private LinearLayout llContainer;
    private ImageView ivBack;
    private ImageView ivFilterGray;
    private View lInputEmail;
    private TextView tvTitle;
    private LinearLayout llContainerUser;
    private RelativeLayout rlUserPicture;
    private ImageView ivImage;
    private ImageView ivUserPicture;
    private MultipartBody.Part multipart;
    private View vSaveNewUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        multipart = null;

        ivBackgroundBlue = findViewById(R.id.iv_background_blue);

        lInputEmail = findViewById(R.id.item_input_edit_user);
        llContainer = findViewById(R.id.ll_container);
        ivFilterGray = findViewById(R.id.iv_filter_gray);
        ivBack = findViewById(R.id.iv_back);
        etUser = lInputEmail.findViewById(R.id.et_input);
        ivImage = ivUserPicture = findViewById(R.id.iv_user_picture);
        rlUserPicture = findViewById(R.id.rl_change_photo);
        vSaveNewUser = findViewById(R.id.v_save_new_user);

        tvTitle = lInputEmail.findViewById(R.id.tv_title);
        llContainerUser = lInputEmail.findViewById(R.id.ll_item_container);

        vSaveNewUser = vSaveNewUser.findViewById(R.id.ll_button);

        vSaveNewUser.setOnClickListener(v -> wsUpdateMainProfile());

        ivBack.setOnClickListener(v -> finish());

        rlUserPicture.setOnClickListener(v -> ImagePicker.Companion
                .with(EditProfileActivity.this)
                .crop(1f, 1f)                //Crop Square image(Optional)
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .start(101));

        Glide
                .with(EditProfileActivity.this)
                .load(R.drawable.style_border_blue_without_corners)
                .apply(RequestOptions.circleCropTransform())
                .into(ivBackgroundBlue);

        Glide
                .with(EditProfileActivity.this)
                .load(R.drawable.style_filter_gray)
                .apply(RequestOptions.circleCropTransform())
                .into(ivFilterGray);

        etUser.setOnFocusChangeListener((v, hasFocus) -> {
            TransitionManager.beginDelayedTransition(llContainer);
            if (hasFocus) {
                tvTitle.setVisibility(View.VISIBLE);
                llContainerUser.setBackground(getDrawable(R.drawable.style_background_white));
            } else {
                tvTitle.setVisibility(View.GONE);
                llContainerUser.setBackground(getDrawable(R.drawable.style_border_gray));
            }

        });
    }


    private void wsUpdateMainProfile() {

        if (multipart != null) {
            wsUpdateProfile(EditProfileActivity.this, etUser.getText().toString(), "", multipart, true, new ProfileCallback() {
                @Override
                public void baseResponse(ProfileData data) {
                    finish();
                    Toast.makeText(EditProfileActivity.this, "Solicitud enviada correctamente", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void baseError() {
                    Toast.makeText(EditProfileActivity.this, "Hubo un error al mandar la solicitud de tu perfil", Toast.LENGTH_SHORT).show();
                }
            });
        } else {

            wsUpdateProfile(EditProfileActivity.this, etUser.getText().toString(), "", null, true, new ProfileCallback() {
                @Override
                public void baseResponse(ProfileData data) {
                    finish();
                    Toast.makeText(EditProfileActivity.this, "Solicitud enviada correctamente", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void baseError() {
                    Toast.makeText(EditProfileActivity.this, "Hubo un error al mandar la solicitud de tu perfil", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            File file = ImagePicker.Companion.getFile(data);

            try {
                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse("image/jpg"),
                                file
                        );
                multipart = MultipartBody.Part.createFormData("profile_picture", ImagePicker.Companion.getFilePath(data), requestFile);
                Glide
                        .with(EditProfileActivity.this)
                        .load(file)
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivUserPicture);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }

    }

}
