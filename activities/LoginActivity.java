package io.pixan.systramer.activities;

import androidx.databinding.DataBindingUtil;
import io.pixan.systramer.R;
import io.pixan.systramer.callbacks.LoginCallback;
import io.pixan.systramer.responses.LoginResponse;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static io.pixan.systramer.utils.Utils.getSavedUser;
import static io.pixan.systramer.utils.Utils.saveUser;
import static io.pixan.systramer.utils.WebServices.wsLogin;

public class LoginActivity extends BaseActivity {

    private EditText etEmail;
    private EditText etPassword;

    private View lInputEmail;
    private View lInputPassword;
    private View lButtonLogin;

    private TextView tvTitleEmail;
    private TextView tvTitlePassword;

    private LinearLayout llItemContainerEmail;
    private LinearLayout llItemContainerPassword;

    private LinearLayout llLogin;

    private RelativeLayout rlContainerEmail;
    private RelativeLayout rlContainerPassword;

    private TextView tvRecoverPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_login);

        lInputEmail = findViewById(R.id.item_email);
        lInputPassword = findViewById(R.id.item_password);
        lButtonLogin = findViewById(R.id.item_login);

        tvRecoverPassword = findViewById(R.id.tv_recover_password);

        llLogin = lButtonLogin.findViewById(R.id.ll_button);

        etEmail = lInputEmail.findViewById(R.id.et_input);
        etPassword = lInputPassword.findViewById(R.id.et_input);

        tvTitleEmail = lInputEmail.findViewById(R.id.tv_title);
        tvTitlePassword = lInputPassword.findViewById(R.id.tv_title);

        llItemContainerEmail = lInputEmail.findViewById(R.id.ll_item_container);
        llItemContainerPassword = lInputPassword.findViewById(R.id.ll_item_container);

        rlContainerEmail = lInputEmail.findViewById(R.id.rl_item_container);
        rlContainerPassword = lInputPassword.findViewById(R.id.rl_item_container);

        etEmail.setOnFocusChangeListener((v, hasFocus) -> {
            TransitionManager.beginDelayedTransition(rlContainerEmail);
            if (hasFocus) {
                tvTitleEmail.setVisibility(View.VISIBLE);
                llItemContainerEmail.setBackground(getDrawable(R.drawable.style_border_blue_blue));
            } else {
                tvTitleEmail.setVisibility(View.GONE);
                llItemContainerEmail.setBackground(getDrawable(R.drawable.style_border_gray));
            }
        });

        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            TransitionManager.beginDelayedTransition(rlContainerEmail);
            if (hasFocus) {
                tvTitlePassword.setVisibility(View.VISIBLE);
                llItemContainerPassword.setBackground(getDrawable(R.drawable.style_border_blue_blue));
            } else {
                tvTitlePassword.setVisibility(View.GONE);
                llItemContainerPassword.setBackground(getDrawable(R.drawable.style_border_gray));
            }
        });

        tvRecoverPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RecoverPasswordActivity.class);
            startActivity(intent);
        });

        llLogin.setOnClickListener(v ->
                wsLogin(
                        LoginActivity.this,
                        etEmail.getText().toString(),
                        etPassword.getText().toString(),
                        loginResponse -> {
                            saveUser(loginResponse.getData());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                )
        );

    }
}
