package io.pixan.systramer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import io.pixan.systramer.R;

public class BaseDialog extends Dialog {

    private ImageView ivDialog;
    private TextView tvMainInfo;
    private TextView tvButton;
    private TextView tvCancel;

    private String sMainInfo;
    private String sCancel;
    private Drawable dMainIcon;
    private String sButtonText;

    private View.OnClickListener onClickListener;
    private View.OnClickListener oclCancel;

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_base);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        ivDialog = findViewById(R.id.iv_dialog);
        tvButton = findViewById(R.id.tv_button);
        tvMainInfo = findViewById(R.id.tv_main_info);
        tvCancel = findViewById(R.id.tv_cancel);

        ivDialog.setImageDrawable(dMainIcon);
        tvMainInfo.setText(sMainInfo);
        tvButton.setText(sButtonText);
        tvCancel.setText(sCancel);

        if (oclCancel != null) {
            tvCancel.setVisibility(View.VISIBLE);
        }

        tvButton.setOnClickListener(this.onClickListener);
        tvCancel.setOnClickListener(this.oclCancel);
    }

    public BaseDialog setIvDialog(Drawable dDialog) {
        dMainIcon = dDialog;
        return this;
    }

    public BaseDialog setTvMainInfo(String tvMainInfo) {
        this.sMainInfo = (tvMainInfo);
        return this;
    }

    public BaseDialog setTvButton(String sButtonText) {
        this.sButtonText = sButtonText;
        return this;
    }

    public BaseDialog setTvCancelButton(String sButtonText) {
        this.sCancel = sButtonText;
        return this;
    }

    public BaseDialog setOnAcceptClicListener(View.OnClickListener onclickListener) {
        this.onClickListener = onclickListener;
        return this;
    }

    public BaseDialog setOnCancelClickListener(View.OnClickListener onCancelClickListener) {
        this.oclCancel = onCancelClickListener;
        return this;
    }
}