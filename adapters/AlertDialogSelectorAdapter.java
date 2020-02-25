package io.pixan.systramer.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;
import io.pixan.systramer.R;
import io.pixan.systramer.callbacks.BaseCallback;
import io.pixan.systramer.dialog.AlertDialog;
import io.pixan.systramer.dialog.BaseDialog;
import io.pixan.systramer.models.AlertItem;
import io.pixan.systramer.models.MessageModel;
import okhttp3.ResponseBody;

import static io.pixan.systramer.utils.Globals.carLocation;
import static io.pixan.systramer.utils.Globals.serviceLocation;
import static io.pixan.systramer.utils.WebServices.wsPostAlert;

public class AlertDialogSelectorAdapter extends RecyclerView.Adapter<AlertDialogSelectorAdapter.ViewHolder> {


    public Context context;
    private AlertItem[] alertItems;
    private int serviceId;
    private AlertDialog alertDialog;
    private BottomSheetDialog mBottomSheetDialog;

    public AlertDialogSelectorAdapter(Context context, BottomSheetDialog mBottomSheetDialog,
                                      AlertDialog alertDialog, int serviceId, AlertItem[] alertItems) {
        this.context = context;
        this.alertDialog = alertDialog;
        this.alertItems = alertItems;
        this.mBottomSheetDialog = mBottomSheetDialog;
        this.serviceId = serviceId;
    }

    @Override
    public AlertDialogSelectorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View homeView = inflater.inflate(R.layout.item_alert_option, parent, false);

        // Return a new holder instance
        return new AlertDialogSelectorAdapter.ViewHolder(homeView);
    }

    @Override
    public void onBindViewHolder(AlertDialogSelectorAdapter.ViewHolder holder, int position) {
        holder.tvDescription.setText(alertItems[position].getDescription());
        holder.llContainer.setOnClickListener(v -> {
            if (carLocation != null) {
                float speed = 0;
                if (serviceLocation != null)
                    speed = serviceLocation.getSpeed();

                wsPostAlert(context, serviceId,
                        alertItems[position].getId(),
                        carLocation.getLatitude(),
                        carLocation.getLongitude(),
                        (int) speed,
                        true,
                        new BaseCallback() {
                            @Override
                            public void baseResponse(ResponseBody responseBody) {
                                BaseDialog baseDialog = new BaseDialog(context);
                                baseDialog.setCancelable(false);
                                baseDialog.setCanceledOnTouchOutside(false);
                                baseDialog.setOnAcceptClicListener(v1 -> {
                                    baseDialog.dismiss();
                                });
                                baseDialog
                                        .setIvDialog(context.getDrawable(R.drawable.icn_red_alert))
                                        .setTvButton("ACEPTAR")
                                        .setTvMainInfo("Se mandó un mensaje a la central de alertas")
                                        .setOnAcceptClicListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                baseDialog.dismiss();
                                                mBottomSheetDialog.dismiss();
                                                alertDialog.dismiss();
                                            }
                                        })
                                        .show();

                            }

                            @Override
                            public void baseError() {
                                Toast.makeText(context, "Ocurrio un error al mandar tu alerta, intentalo nuevamente", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return alertItems.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView tvDescription;
        LinearLayout llContainer;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tv_description);
            llContainer = itemView.findViewById(R.id.ll_container);
        }
    }

}
