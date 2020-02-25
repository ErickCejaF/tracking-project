package io.pixan.systramer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import androidx.annotation.NonNull;
import io.pixan.systramer.R;
import io.pixan.systramer.activities.EditProfileActivity;

public class DialogEvidence extends Dialog {

    private TextView tvReport;
    private TextView tvCancel;
    private ImageView ivEvidence;
    private EditText etComments;
    private File imageDrawable;
    private View.OnClickListener vocReport;
    private View.OnClickListener vocCancel;
    private TextView tvTitle;
    private String title;


    public DialogEvidence(@NonNull Context context, String title) {
        super(context);
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_evidence);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        ivEvidence = findViewById(R.id.iv_evidence);
        tvCancel = findViewById(R.id.tv_cancel);
        tvReport = findViewById(R.id.tv_report);
        etComments = findViewById(R.id.et_comments);
        tvTitle = findViewById(R.id.tv_title);

        if (title != null && title.length() > 0) {
            tvTitle.setText(title);
        }
    }

    public DialogEvidence setImageView(File imageDrawable) {
        this.imageDrawable = imageDrawable;
        return this;
    }

    public DialogEvidence setReportListener(View.OnClickListener vocReport) {
        this.vocReport = vocReport;
        return this;
    }

    public EditText getEtComments() {
        return this.etComments;
    }

    @Override
    public void show() {
        super.show();

        if (imageDrawable != null) {
            Glide
                    .with(getContext())
                    .load(imageDrawable)
                    .into(ivEvidence);
        }

        if (vocReport != null) {
            tvReport.setOnClickListener(vocReport);
        }

        tvCancel.setOnClickListener(v -> dismiss());

    }
}