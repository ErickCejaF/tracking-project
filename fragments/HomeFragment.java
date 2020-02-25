package io.pixan.systramer.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.pixan.systramer.R;
import io.pixan.systramer.activities.MainActivity;
import io.pixan.systramer.activities.TripActivity;
import io.pixan.systramer.adapters.HomeMainAdapter;
import io.pixan.systramer.callbacks.ServiceCallback;
import io.pixan.systramer.dialog.AlertDialog;
import io.pixan.systramer.dialog.BaseDialog;
import io.pixan.systramer.models.DatesServicesModels;
import io.pixan.systramer.models.ServiceModel;
import io.pixan.systramer.responses.ServiceResponse;
import jdroidcoder.ua.paginationrecyclerview.PaginationRecyclerView;

import static io.pixan.systramer.utils.Utils.callEmergency;
import static io.pixan.systramer.utils.Utils.isSameDay;
import static io.pixan.systramer.utils.WebServices.wsServices;

public class HomeFragment extends Fragment {

    private PaginationRecyclerView rvRoutes;
    private ImageView ivCalendar;
    private ProgressBar pbHome;
    private LinearLayout llCurrentMonth;
    private SwipeRefreshLayout srlServices;
    private View lItemEmergency;
    private TextView tvMonthName;
    private View vItemGoback;
    private View lItemSos;
    private LinearLayout llEmergencyButton;
    private LinearLayout llItemSos;
    private LinearLayout llEmptySpace;
    private LinearLayout llOpenCalendar;
    private boolean loading;
    private SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter;
    private ArrayList<DatesServicesModels> datesServicesModels;
    private String month;
    private Calendar localCalendar;
    private ServiceModel serviceInfo;
    private Boolean addPadding;
    private LinearLayout llHomeFragmentContainer;
    private SimpleDateFormat format;


    public HomeFragment() {
    }

    @SuppressLint("ValidFragment")
    public HomeFragment(Boolean addPadding) {
        this.addPadding = addPadding;
    }

    @SuppressLint("ValidFragment")
    public HomeFragment(ServiceModel serviceInfo, Boolean addPadding) {
        this.serviceInfo = serviceInfo;
        this.addPadding = addPadding;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false).getRoot();

        loading = true;

        rvRoutes = view.findViewById(R.id.rv_routes);
        ivCalendar = view.findViewById(R.id.iv_calendar);
        llCurrentMonth = view.findViewById(R.id.ll_current_month);
        pbHome = view.findViewById(R.id.pb_home);
        srlServices = view.findViewById(R.id.srl_services);
        lItemEmergency = view.findViewById(R.id.item_emergency);
        lItemSos = view.findViewById(R.id.item_sos);
        tvMonthName = view.findViewById(R.id.tv_month_name);
        llEmptySpace = view.findViewById(R.id.ll_empty_space);
        vItemGoback = view.findViewById(R.id.item_login);
        llHomeFragmentContainer = view.findViewById(R.id.ll_main_container_home);

        llItemSos = lItemSos.findViewById(R.id.ll_button);
        llEmergencyButton = lItemEmergency.findViewById(R.id.ll_button);
        llOpenCalendar = vItemGoback.findViewById(R.id.ll_button);

        Calendar calendar = Calendar.getInstance();

        if (localCalendar == null) {
            localCalendar = Calendar.getInstance();
        }

        if (addPadding != null && addPadding) {
            llHomeFragmentContainer.setPadding(0, 0, 0, 147);
        }

        tvMonthName.setText(String.format(Locale.getDefault(), "%tB", localCalendar));

        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(),
                (selectedMonth, selectedYear) -> {
                    localCalendar.set(Calendar.MONTH, selectedMonth);
                    localCalendar.set(Calendar.YEAR, selectedYear);
                    tvMonthName.setText(String.format(Locale.getDefault(), "%tB", localCalendar));
                    srlServices.setRefreshing(true);
                    sectionedRecyclerViewAdapter.removeAllSections();
                    sectionedRecyclerViewAdapter.notifyDataSetChanged();
                    wsRequest();
                },
                calendar.get((Calendar.YEAR)), calendar.get(Calendar.MONTH));

        rvRoutes.setOnPageChangeListener(i -> {
        });

        ivCalendar.setOnClickListener(v -> {
//            Intent intent = new Intent(getContext(), EditProfileActivity.class);
//            startActivity(intent);
            builder
                    .setMinYear(2018)
                    .setActivatedYear(calendar.get(Calendar.YEAR))
                    .setMaxYear(calendar.get(Calendar.YEAR) + 5)
                    .setTitle("Escoge el mes para visualizar las rutas")
                    .build()
                    .show();
        });

        llOpenCalendar.setOnClickListener(v -> builder
                .setMinYear(2018)
                .setActivatedYear(calendar.get(Calendar.YEAR))
                .setMaxYear(calendar.get(Calendar.YEAR) + 5)
                .setTitle("Escoge el mes para visualizar las rutas")
                .build()
                .show());


        datesServicesModels = new ArrayList<>();

        llEmergencyButton.setOnClickListener(v -> callEmergency(getActivity()));
        llItemSos.setOnClickListener(v -> {
            if (serviceInfo != null) {
                new AlertDialog(getContext(), getActivity(), serviceInfo.getId()).show();
            } else {
                BaseDialog baseDialog = new BaseDialog(getActivity());
                baseDialog.setCancelable(false);
                baseDialog.setCanceledOnTouchOutside(false);
                baseDialog.setOnAcceptClicListener(v1 -> {
                    baseDialog.dismiss();
                });
                baseDialog
                        .setIvDialog(getResources().getDrawable(R.drawable.icn_alerts))
                        .setTvButton("ACEPTAR")
                        .setTvMainInfo("No se ha encontrado un servicio activo")
                        .show();
            }

        });

        if (sectionedRecyclerViewAdapter == null || sectionedRecyclerViewAdapter.getCopyOfSectionsMap().size() == 0) {
            sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
            rvRoutes.setAdapter(sectionedRecyclerViewAdapter);
            rvRoutes.setLayoutManager(new LinearLayoutManager(getContext()));
            wsRequest();
        } else {
            pbHome.setVisibility(View.GONE);
            llCurrentMonth.setVisibility(View.VISIBLE);
            rvRoutes.setAdapter(sectionedRecyclerViewAdapter);
            rvRoutes.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        srlServices.setOnRefreshListener(() -> {
            sectionedRecyclerViewAdapter.removeAllSections();
            sectionedRecyclerViewAdapter.notifyDataSetChanged();
            wsRequest();
        });

        return view;
    }

    private void wsRequest() {
        wsServices(getContext(),
                false,
                localCalendar.get(Calendar.YEAR),
                localCalendar.get(Calendar.MONTH) + 1,
                new ServiceCallback() {
                    @Override
                    public void baseResponse(ServiceResponse serviceResponse) {
                        correctResponse(serviceResponse);
                    }

                    @Override
                    public void baseError() {
                        badResponse();
                    }
                });
    }

    private void correctResponse(ServiceResponse serviceResponse) {

        if (srlServices.isRefreshing()) {
            if (datesServicesModels != null) {
                datesServicesModels.clear();
            }
        }

        loading = false;
        pbHome.setVisibility(View.GONE);
        srlServices.setRefreshing(false);
        rvRoutes.setVisibility(View.VISIBLE);
        llCurrentMonth.setVisibility(View.VISIBLE);
        llEmptySpace.setVisibility(View.GONE);

        format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        ArrayList<ServiceModel> serviceModels = serviceResponse.getData().getData();

        //PAST SERVICES REMOVAL
//        Date currentDay = Calendar.getInstance().getTime();
//        List<ServiceModel> datesBefore = new ArrayList<>();
//        for (ServiceModel serviceModel : serviceModels) {
//            Date serviceDate = null;
//            try {
//                serviceDate = format.parse(serviceModel.getDeparture_date());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            if (serviceDate.compareTo(currentDay) < 0) {
//                datesBefore.add(serviceModel);
//            }
//        }
//
//        serviceModels.removeAll(datesBefore);

        Collections.sort(serviceModels, (o1, o2) -> {
            // ## Ascending order
            Date date1 = null;
            Date date2 = null;
            try {
                date1 = format.parse(o1.getDeparture_date());
                date2 = format.parse(o2.getDeparture_date());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date1.after(date2) ? 0 : 1; // To compare string values
        });

        if (serviceModels != null && serviceModels.size() > 0) {
            for (ServiceModel serviceModel : serviceModels) {
                Date date = null;
                Boolean added = false;

                try {
                    date = format.parse(serviceModel.getDeparture_date());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for (DatesServicesModels datesServicesModel : datesServicesModels) {
                    if (isSameDay(datesServicesModel.getDate(), date)) {
                        datesServicesModel.getServiceModels().add(serviceModel);
                        added = true;
                    }
                }

                if (!added) {
                    datesServicesModels.add(new DatesServicesModels(date, serviceModel));
                }
            }

            for (DatesServicesModels datesServicesModel : datesServicesModels) {
                sectionedRecyclerViewAdapter.addSection(
                        new HomeMainAdapter(getActivity(), datesServicesModel));
            }

            sectionedRecyclerViewAdapter.notifyDataSetChanged();
        } else {
            llEmptySpace.setVisibility(View.VISIBLE);
        }
    }

    public void setBottomPadding() {
        if (llHomeFragmentContainer == null) {
            addPadding = true;
        } else {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) llHomeFragmentContainer.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 120);
            llHomeFragmentContainer.setLayoutParams(layoutParams);
        }
    }


    private void badResponse() {
        loading = false;
        if (getContext() != null)
            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
        pbHome.setVisibility(View.GONE);
        srlServices.setRefreshing(false);
    }
}
