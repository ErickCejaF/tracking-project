package io.pixan.systramer.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.pixan.systramer.R;
import io.pixan.systramer.callbacks.BaseCallback;
import io.pixan.systramer.dialog.AlertDialog;
import io.pixan.systramer.dialog.BaseDialog;
import io.pixan.systramer.models.AlertModel;
import okhttp3.ResponseBody;

import static io.pixan.systramer.utils.Globals.carLocation;
import static io.pixan.systramer.utils.Globals.serviceLocation;
import static io.pixan.systramer.utils.WebServices.wsPostAlert;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.ViewHolder> {


    public Activity activity;
    private ArrayList<AlertModel> alerts;
    private int serviceId;
    private AlertDialog alertDialog;

    public AlertAdapter(Activity activity,
                        AlertDialog alertDialog,
                        ArrayList<AlertModel> alerts,
                        int serviceId) {
        this.activity = activity;
        this.alerts = alerts;
        this.alertDialog = alertDialog;
        this.serviceId = serviceId;
    }

    @Override
    public AlertAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context parentContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parentContext);

        // Inflate the custom layout
        View homeView = inflater.inflate(R.layout.item_alert, parent, false);

        // Return a new holder instance
        return new AlertAdapter.ViewHolder(homeView);
    }

    @Override
    public void onBindViewHolder(AlertAdapter.ViewHolder holder, int position) {

        Glide
                .with(this.activity)
                .load(activity.getResources().getDrawable(R.color.colorMainBackground))
                .apply(RequestOptions.circleCropTransform())
                .into(holder.ivBackgroundGray);

        Glide
                .with(this.activity)
                .load(activity.getResources().getDrawable(android.R.color.white))
                .apply(RequestOptions.circleCropTransform())
                .into(holder.ivBackgroundWhite);

        Glide
                .with(this.activity)
                .load(alerts.get(position).getIcon())
                .into(holder.ivMainIcon);

        holder.tvMainDescription.setText(alerts.get(position).getAlertTitle());
        holder.rlMainContainer.setOnClickListener(v -> {
            if (alerts.get(position).getAlertItems().length == 0) {
                if (carLocation != null) {
                    float speed = 0;
                    if (serviceLocation != null)
                        speed = serviceLocation.getSpeed();

                    wsPostAlert(activity, serviceId,
                            alerts.get(position).getAlertId(),
                            carLocation.getLatitude(),
                            carLocation.getLongitude(),
                            (int) speed,
                            true,
                            new BaseCallback() {
                                @Override
                                public void baseResponse(ResponseBody responseBody) {
                                    BaseDialog baseDialog = new BaseDialog(activity);
                                    baseDialog.setCancelable(false);
                                    baseDialog.setCanceledOnTouchOutside(false);
                                    baseDialog.setOnAcceptClicListener(v1 -> {
                                        baseDialog.dismiss();
                                    });
                                    baseDialog
                                            .setIvDialog(activity.getDrawable(R.drawable.icn_red_alert))
                                            .setTvButton("ACEPTAR")
                                            .setTvMainInfo("Se mandó un mensaje a la central de alertas")
                                            .setOnAcceptClicListener(v13 -> {
                                                baseDialog.dismiss();
                                                if (alertDialog != null && alertDialog.isShowing()) {
                                                    alertDialog.dismiss();
                                                }
                                            })
                                            .show();

                                }

                                @Override
                                public void baseError() {
                                    Toast.makeText(activity, "Ocurrio un error al mandar tu alerta, intentalo nuevamente", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            } else {
                BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
                View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_bottom_selector, null);
                mBottomSheetDialog.setContentView(sheetView);
                mBottomSheetDialog.show();

                RecyclerView rvAlerts;
                TextView tvCancel;

                rvAlerts = mBottomSheetDialog.findViewById(R.id.rv_alert_selector);
                tvCancel = mBottomSheetDialog.findViewById(R.id.tv_cancel);

                tvCancel.setOnClickListener(v12 -> mBottomSheetDialog.dismiss());
                rvAlerts.setLayoutManager(new LinearLayoutManager(activity));
                rvAlerts.setAdapter(
                        new AlertDialogSelectorAdapter(
                                activity,
                                mBottomSheetDialog,
                                this.alertDialog,
                                serviceId,
                                alerts.get(position).getAlertItems()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlMainContainer;
        TextView tvMainDescription;
        ImageView ivMainIcon;
        ImageView ivBackgroundWhite;
        ImageView ivBackgroundGray;

        // Your holder should contain a member variable
        // for any view that will be set as you render a row


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            super(itemView);
            tvMainDescription = itemView.findViewById(R.id.tv_main_description);
            rlMainContainer = itemView.findViewById(R.id.rl_main_container);
            ivMainIcon = itemView.findViewById(R.id.iv_main_icon);
            ivBackgroundWhite = itemView.findViewById(R.id.iv_white);
            ivBackgroundGray = itemView.findViewById(R.id.iv_background_gray);
        }
    }

}
