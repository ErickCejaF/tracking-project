package io.pixan.systramer.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.pixan.systramer.R;
import io.pixan.systramer.activities.MainActivity;
import io.pixan.systramer.adapters.AlertAdapter;
import io.pixan.systramer.models.AlertItem;
import io.pixan.systramer.models.AlertModel;

public class AlertDialog extends Dialog {

    private ArrayList<AlertModel> alerts;
    private AlertAdapter alertAdapter;
    private RecyclerView rvAlerts;
    private RelativeLayout flRoot;
    private Activity mainActivity;
    private ViewGroup root;
    private ImageView ivBack;
    private int serviceId;

    public AlertDialog(@NonNull Context context, Activity mainActivity, int serviceId) {
        super(context);

        this.mainActivity = mainActivity;
        this.serviceId = serviceId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_alerts);

        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }

        rvAlerts = findViewById(R.id.rv_alerts);
        flRoot = findViewById(R.id.fl_root);
        ivBack = findViewById(R.id.iv_back);

        ivBack.setOnClickListener(v -> dismiss());

        rvAlerts.setOverScrollMode(View.OVER_SCROLL_NEVER);

        alerts = new ArrayList<>();

        alerts.add(new AlertModel(getContext().getDrawable(R.drawable.icn_shield),
                "Todo bien",
                21, new AlertItem[]{
                new AlertItem(35, "WC"),
                new AlertItem(31, "Alimentos"),
                new AlertItem(34, "Descanso obligatorio"),
                new AlertItem(33, "Pernocta"),
                new AlertItem(32, "Abasto de combustible"),
        }));

        alerts.add(new AlertModel(getContext().getDrawable(R.drawable.icn_parking),
                "Paradas Autorizadas",
                34, new AlertItem[]{}));

        alerts.add(new AlertModel(getContext().getDrawable(R.drawable.icn_traffic),
                "Trafico",
                31,
                new AlertItem[]{
                        new AlertItem(41, "Posible accidente"),
                        new AlertItem(42, "Hora pico"),
                        new AlertItem(43, "Motivo desconocido"),
                        new AlertItem(44, "Obra pública"),
                }));

        alerts.add(new AlertModel(getContext().getDrawable(R.drawable.icn_officer),
                "Inspección Oficial",
                5,
                new AlertItem[]{
                        new AlertItem(51, "Retén militar"),
                        new AlertItem(55, "Policia federal"),
                        new AlertItem(54, "SAT/Aduana"),
                        new AlertItem(53, "Fitosanitaria"),
                        new AlertItem(52, "Transito y vialidad"),
                }));

        alerts.add(new AlertModel(getContext().getDrawable(R.drawable.icn_colored_sos),
                "SOS",
                11,
                new AlertItem[]{}));

        alerts.add(new AlertModel(getContext().getDrawable(R.drawable.icn_insecure),
                "Me siento Inseguro",
                61,
                new AlertItem[]{}));

        alerts.add(new AlertModel(getContext().getDrawable(R.drawable.icn_sick),
                "Enfermo",
                27,
                new AlertItem[]{
                        new AlertItem(71, "Leve"),
                        new AlertItem(72, "Fuerte"),
                        new AlertItem(73, "Grave")
                }));

        alerts.add(new AlertModel(getContext().getDrawable(R.drawable.icn_mechanical_faulire),
                "Falla Mecánica",
                8,
                new AlertItem[]{
                        new AlertItem(81, "Calentamiento"),
                        new AlertItem(82, "Frenos"),
                        new AlertItem(83, "Luces"),
                        new AlertItem(84, "Pinchadura"),
                        new AlertItem(85, "Motor"),
                        new AlertItem(86, "Tren motriz"),
                }));

        alerts.add(new AlertModel(getContext().getDrawable(R.drawable.icn_hit),
                "Accidente",
                9,
                new AlertItem[]{
                        new AlertItem(91, "Daños leves"),
                        new AlertItem(92, "Daños fuertes"),
                        new AlertItem(93, "Daños graves")
                }));

        alertAdapter = new AlertAdapter(mainActivity, this, alerts, serviceId);

        rvAlerts.setAdapter(alertAdapter);
        rvAlerts.setLayoutManager(new GridLayoutManager(getContext(), 3));
    }
}