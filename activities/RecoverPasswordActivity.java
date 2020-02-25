package io.pixan.systramer.activities;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import io.pixan.systramer.R;
import io.pixan.systramer.callbacks.BaseCallback;
import io.pixan.systramer.dialog.BaseDialog;
import okhttp3.ResponseBody;

import static io.pixan.systramer.utils.WebServices.wsRecoverPassword;

public class RecoverPasswordActivity extends BaseActivity {

    private View vUserItem;
    private View vButtonRecover;
    private EditText etUser;
    private LinearLayout llLogin;
    private RelativeLayout rlContainer;
    private TextView tvUserTitle;
    private LinearLayout llItemContainerEmail;
    private ImageView ivBack;
    private LinearLayout llButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_recover_password);

        vUserItem = findViewById(R.id.v_user_recover);
        vButtonRecover = findViewById(R.id.v_user_recover_button);

        etUser = findViewById(R.id.et_input);
        llLogin = findViewById(R.id.ll_button);

        rlContainer = findViewById(R.id.rl_container);

        tvUserTitle = vUserItem.findViewById(R.id.tv_title);
        llItemContainerEmail = vUserItem.findViewById(R.id.ll_item_container);
        llButton = vButtonRecover.findViewById(R.id.ll_button);

        ivBack = findViewById(R.id.iv_back);

        etUser.setOnFocusChangeListener((v, hasFocus) -> {
            TransitionManager.beginDelayedTransition(rlContainer);
            if (hasFocus) {
                tvUserTitle.setVisibility(View.VISIBLE);
                llItemContainerEmail.setBackground(getDrawable(R.drawable.style_border_blue_blue));
            } else {
                tvUserTitle.setVisibility(View.GONE);
                llItemContainerEmail.setBackground(getDrawable(R.drawable.style_border_gray));
            }
        });

        llButton.setOnClickListener(v -> wsRecoverPassword(RecoverPasswordActivity.this, etUser.getText().toString(), true, new BaseCallback() {
            @Override
            public void baseResponse(ResponseBody responseBody) {
                BaseDialog baseDialog = new BaseDialog(RecoverPasswordActivity.this);
                baseDialog.setCancelable(false);
                baseDialog.setCanceledOnTouchOutside(false);
                baseDialog.setOnAcceptClicListener(v1 -> {
                    baseDialog.dismiss();
                    RecoverPasswordActivity.this.finish();
                });
                baseDialog
                        .setIvDialog(getDrawable(R.drawable.icn_correct))
                        .setTvButton("ACEPTAR")
                        .setTvMainInfo("Las instrucciones de cambio de contraseña fueron enviadas al correo electrónico registrado")
                        .show();
            }

            @Override
            public void baseError() {

            }
        }));


        ivBack.setOnClickListener(v -> finish());


    }
}
