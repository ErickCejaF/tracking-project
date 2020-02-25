package io.pixan.systramer.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.pixan.systramer.R;
import io.pixan.systramer.activities.MainActivity;
import io.pixan.systramer.adapters.ScheduleAdapter;
import io.pixan.systramer.callbacks.BaseCallback;
import io.pixan.systramer.data.RouteData;
import io.pixan.systramer.dialog.AlertDialog;
import io.pixan.systramer.dialog.BaseDialog;
import io.pixan.systramer.models.InfoModel;
import io.pixan.systramer.models.ScheduleListModel;
import io.pixan.systramer.models.ServiceModel;
import io.pixan.systramer.utils.Globals;
import io.realm.RealmList;
import okhttp3.ResponseBody;

import static io.pixan.systramer.utils.Globals.carLocation;
import static io.pixan.systramer.utils.Globals.scheduleModels;
import static io.pixan.systramer.utils.Globals.serviceLocation;
import static io.pixan.systramer.utils.SharedPreferences.getSharedServiceId;
import static io.pixan.systramer.utils.SharedPreferences.getSharedTotalDistance;
import static io.pixan.systramer.utils.SharedPreferences.getSharedTripStatus;
import static io.pixan.systramer.utils.Utils.callEmergency;
import static io.pixan.systramer.utils.Utils.getScheduleListModel;
import static io.pixan.systramer.utils.Utils.takeScreenshot;
import static io.pixan.systramer.utils.WebServices.wsPostAlert;

public class ScheduleFragment extends Fragment {

    private String title;
    private ScheduleAdapter scheduleAdapter;
    private TextView tvScheduleTitle;
    private RecyclerView rvSchedule;
    private RouteData routeInfo;
    private ServiceModel serviceInfo;
    private RelativeLayout rlAnimationTravelling;
    private RelativeLayout lItemEmergency;
    private RelativeLayout lItemSos;
    private LinearLayout llEmergencyButton;
    private LinearLayout llItemSos;
    private ArrayList<Float> distances;
    private ImageView ivReport;
    private ArrayList<View> itemsSchedule;
    private boolean isCreated;

    public ScheduleFragment() {
    }

    @SuppressLint("ValidFragment")
    public ScheduleFragment(RouteData routeInfo, ServiceModel serviceInfo, String title) {
        this.routeInfo = routeInfo;
        this.serviceInfo = serviceInfo;
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false).getRoot();

        tvScheduleTitle = view.findViewById(R.id.tv_schedule_title);
        rvSchedule = view.findViewById(R.id.rv_schedule);
        ivReport = view.findViewById(R.id.iv_report);
        rlAnimationTravelling = view.findViewById(R.id.rl_animation_travelling);
        isCreated = true;

        lItemEmergency = view.findViewById(R.id.item_emergency);
        lItemSos = view.findViewById(R.id.item_sos);
        llItemSos = lItemSos.findViewById(R.id.ll_button);
        llEmergencyButton = lItemEmergency.findViewById(R.id.ll_button);
        ivReport = view.findViewById(R.id.iv_report);

        ivReport.setOnClickListener(v -> takeScreenshot(getActivity()));

        llEmergencyButton.setOnClickListener(v -> callEmergency(getActivity()));
        llItemSos.setOnClickListener(v -> {
//            new AlertDialog(getContext(), (getActivity()), serviceInfo.getId()).show();
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

        scheduleModels = getScheduleListModel(serviceInfo.getId());

        if (scheduleModels != null && scheduleModels.getScheduleModels().size() > 0) {
            scheduleAdapter = new ScheduleAdapter(getContext(), scheduleModels.getScheduleModels());
            rvSchedule.setAdapter(scheduleAdapter);
            rvSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        tvScheduleTitle.setText(title);


        if (this.distances != null) {
            createViews(distances);
        }

        return view;
    }

    public void updateDistances(ArrayList<Float> distances) {

        if (getContext() != null) {
            if (itemsSchedule == null) {
                createViews(distances);
            } else {
                if (distances.size() != itemsSchedule.size()) {
                    for (View view : itemsSchedule) {
                        rlAnimationTravelling.removeView(view);
                    }
                    createViews(distances);
                } else {
                    updateViews(distances);
                }
            }
        }
    }

    private void updateViews(ArrayList<Float> distances) {

        float totalDistance = getSharedTotalDistance(getContext());

        for (int j = distances.size() - 1; j >= 0; j--) {
            LinearLayout itemList = (LinearLayout) itemsSchedule.get(distances.size() - 1 - j);

            Space itemSpace = itemList.findViewById(R.id.s_travelling_schedule);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) itemSpace.getLayoutParams();

            if (j == distances.size() - 1) {
                params.weight = 1;
            } else {
                params.weight = (1 - (distances.get(j) / getSharedTotalDistance(getContext())));
            }
            itemSpace.setLayoutParams(params);
        }

        System.out.println(totalDistance);


    }

    private void createViews(ArrayList<Float> distances) {
        if (isCreated) {
            itemsSchedule = new ArrayList<>();
            LayoutInflater ltInflater = getLayoutInflater();
            this.distances = distances;

            for (int j = distances.size() - 1; j >= 0; j--) {

                LinearLayout itemList = (LinearLayout)
                        ltInflater.inflate(R.layout.item_travelling_schedule,
                                rlAnimationTravelling,
                                false);

                Space itemSpace = itemList.findViewById(R.id.s_travelling_schedule);
                ImageView ivIcon = itemList.findViewById(R.id.iv_icon);

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) itemSpace.getLayoutParams();
                if (j == distances.size() - 1) {
                    ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.icn_location_green));
                    params.weight = 1;
                } else if (j == 0) {
                    params.weight = (1 - (distances.get(j) / getSharedTotalDistance(getContext())));
                } else {
                    ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.icn_circle));
                    params.weight = (1 - (distances.get(j) / getSharedTotalDistance(getContext())));
                }

                itemSpace.setLayoutParams(params);
                itemsSchedule.add(itemList);
            }

            for (View view : itemsSchedule) {
                rlAnimationTravelling.addView(view);
            }
        }
    }

    public void updateSchedulePositions(){
        if(scheduleAdapter!=null){
            scheduleAdapter.notifyDataSetChanged();
        }
    }
}
