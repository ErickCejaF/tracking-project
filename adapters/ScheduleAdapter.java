package io.pixan.systramer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;
import io.pixan.systramer.R;
import io.pixan.systramer.models.AdapterProfileModel;
import io.pixan.systramer.models.InfoModel;
import io.pixan.systramer.models.ScheduleModel;
import io.realm.RealmList;

import static io.pixan.systramer.utils.Globals.carSpeed;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {


    public Context context;
    private RealmList<ScheduleModel> scheduleModels;

    public ScheduleAdapter(Context context, RealmList<ScheduleModel> scheduleModels) {

        this.context = context;
        this.scheduleModels = scheduleModels;
    }

    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View homeView = inflater.inflate(R.layout.item_schedule, parent, false);

        // Return a new holder instance
        return new ScheduleAdapter.ViewHolder(homeView);
    }

    @Override
    public void onBindViewHolder(ScheduleAdapter.ViewHolder holder, int position) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
        SimpleDateFormat sdfHour = new SimpleDateFormat("hh:mm", Locale.getDefault());
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            cal.setTime(sdf.parse(scheduleModels.get(position).getDate()));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tvTitle.setText(scheduleModels.get(position).getTitle());


        holder.tvStartDate.setText(sdfDate.format(cal.getTime()));
        holder.tvEndDate.setText(sdfHour.format(cal.getTime()) + " hrs");

        holder.tvSpeed.setText(String.format(Locale.getDefault(),
                "%.0f",
                ((Double.parseDouble(scheduleModels.get(position).getSpeed())) * 3.6) / 1) + " KM");
    }

    @Override
    public int getItemCount() {
        return scheduleModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        private TextView tvTitle;
        private TextView tvStartDate;
        private TextView tvEndDate;
        private TextView tvSpeed;
        private TextView tvTimeToArrive;
        // for any view that will be set as you render a row

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvStartDate = itemView.findViewById(R.id.tv_start_date);
            tvEndDate = itemView.findViewById(R.id.tv_end_date);
            tvSpeed = itemView.findViewById(R.id.tv_speed);
            tvTimeToArrive = itemView.findViewById(R.id.tv_time_to_arrive);
        }
    }

}
