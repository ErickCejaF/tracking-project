package io.pixan.systramer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import io.pixan.systramer.R;
import io.pixan.systramer.activities.TripActivity;
import io.pixan.systramer.callbacks.RouteCallback;
import io.pixan.systramer.data.RouteData;
import io.pixan.systramer.models.DatesServicesModels;
import io.pixan.systramer.models.ServiceModel;

import static io.pixan.systramer.utils.SharedPreferences.getSharedRouteId;
import static io.pixan.systramer.utils.SharedPreferences.getSharedServiceId;
import static io.pixan.systramer.utils.SharedPreferences.getSharedTripStatus;
import static io.pixan.systramer.utils.Utils.getSavedRouteData;
import static io.pixan.systramer.utils.Utils.getSavedServiceModel;
import static io.pixan.systramer.utils.Utils.saveRouteData;
import static io.pixan.systramer.utils.Utils.saveServiceData;
import static io.pixan.systramer.utils.WebServices.wsGetRouteInfo;

public class HomeMainAdapter extends StatelessSection {

    private DatesServicesModels datesServicesModels;
    private Activity context;

    public HomeMainAdapter(Activity context, DatesServicesModels datesServicesModels) {
        // call constructor with layout resources for this Section header and items
        super(SectionParameters.builder()
                .itemResourceId(R.layout.item_trip)
                .headerResourceId(R.layout.item_header_trip)
                .build());

        this.datesServicesModels = datesServicesModels;
        this.context = context;
    }

    @Override
    public int getContentItemsTotal() {
        return datesServicesModels.getServiceModels().size(); // number of items of this section
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyItemViewHolder itemHolder = (MyItemViewHolder) holder;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        if (context != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(datesServicesModels.getDate());

            if (position == 0) {
                itemHolder.vBlueLine.setVisibility(View.VISIBLE);
                itemHolder.tvDayNumber.setText(String.format("%s", calendar.get(Calendar.DAY_OF_MONTH)));
                itemHolder.tvDayLetter.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
                if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) &&
                        calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                    itemHolder.tvDayLetter.setTextColor(context.getResources().getColor(R.color.main_blue));
                    itemHolder.tvDayNumber.setTextColor(context.getResources().getColor(R.color.main_blue));
                } else {
                    itemHolder.tvDayLetter.setTextColor(context.getResources().getColor(R.color.secondary_gray));
                    itemHolder.tvDayNumber.setTextColor(context.getResources().getColor(R.color.secondary_gray));
                }

            } else {
                itemHolder.vBlueLine.setVisibility(View.INVISIBLE);
            }

            String outputDate = "";

            try {
                Date date;
                date = format.parse(datesServicesModels.getServiceModels().get(position).getDeparture_date());
                SimpleDateFormat spf = new SimpleDateFormat("hh:mm a");
                outputDate = spf.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            itemHolder.tvRoute.setText(datesServicesModels.getServiceModels().get(position).getRoute().getName());
            itemHolder.tvDate.setText(outputDate);

            itemHolder.llItemContainer.setOnClickListener(v -> {

                if (getSharedTripStatus(context)) {

                    if (datesServicesModels.getServiceModels().get(position).getId() == getSharedServiceId(context)) {
                        ((TripActivity) context).showCurrentTrip();
                    } else {
                        Toast.makeText(context, "Ese no es tu servicio activo", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    wsGetRouteInfo(context, datesServicesModels.getServiceModels().get(position).getRoute_id(), true, new RouteCallback() {
                        @Override
                        public void baseResponse(RouteData data) {
                            Intent intent = new Intent(context, TripActivity.class);
                            intent.putExtra("service_info_id", datesServicesModels.getServiceModels().get(position).getId());
                            intent.putExtra("route_info_id", data.getId());

                            saveRouteData(data);
                            saveServiceData(datesServicesModels.getServiceModels().get(position));

                            context.startActivity(intent);
                        }

                        @Override
                        public void baseError() {

                        }
                    });
                }
            });
        }
    }

    class MyItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDayNumber;
        private TextView tvDayLetter;
        private TextView tvRoute;
        private TextView tvDate;
        private CardView cvDay;
        private View vBlueLine;
        private LinearLayout llItemContainer;

        public MyItemViewHolder(View itemView) {
            super(itemView);

            tvDayLetter = itemView.findViewById(R.id.tv_day_letter);
            tvDayNumber = itemView.findViewById(R.id.tv_day_number);
            tvRoute = itemView.findViewById(R.id.tv_route);
            tvDate = itemView.findViewById(R.id.tv_date);
            cvDay = itemView.findViewById(R.id.cv_day);
            vBlueLine = itemView.findViewById(R.id.v_blue_line);
            llItemContainer = itemView.findViewById(R.id.ll_item_container);

        }
    }

}