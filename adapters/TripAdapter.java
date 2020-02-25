package io.pixan.systramer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.psoffritti.slidingpanel.SlidingPanel;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import io.pixan.systramer.R;
import io.pixan.systramer.models.InfoModel;
import io.realm.RealmList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {


    public Context context;
    private RealmList<InfoModel> infoModels;
    private GoogleMap gmMap;

    public TripAdapter(Context context, RealmList<InfoModel> infoModels, GoogleMap gmMap) {
        this.context = context;
        this.infoModels = infoModels;
        this.gmMap = gmMap;
    }

    @Override
    public TripAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View homeView = inflater.inflate(R.layout.item_info, parent, false);

        // Return a new holder instance
        return new TripAdapter.ViewHolder(homeView);
    }

    @Override
    public void onBindViewHolder(TripAdapter.ViewHolder holder, int position) {

        if (position == infoModels.size() - 1) {
            holder.vBottomSeparator.setVisibility(View.GONE);
            holder.llMainContainer.setBackground(context.getResources().getDrawable(R.drawable.style_bottom_borders));
        } else {
            holder.llMainContainer.setBackground(context.getResources().getDrawable(R.drawable.style_left_right_borders));
        }


        if (position == infoModels.size() - 1) {
            holder.tvMainInfo.setText("Destino");
        } else if (position == 0) {
            holder.tvMainInfo.setText("Origen");
        } else {
            holder.tvMainInfo.setText(infoModels.get(position).getName());
        }

        holder.llSelectableContainer.setOnClickListener(v -> gmMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                        .target(new LatLng(
                                infoModels.get(position).getLocation().getCoordinates().get(1),
                                infoModels.get(position).getLocation().getCoordinates().get(0)))
                        .zoom(15)
                        .build())));

        holder.tvSecondInfo.setText(infoModels.get(position).getAddress());

    }

    @Override
    public int getItemCount() {
        return infoModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        LinearLayout llMainContainer;
        View vBottomSeparator;
        TextView tvMainInfo;
        LinearLayout llSelectableContainer;
        TextView tvSecondInfo;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            super(itemView);

            tvMainInfo = itemView.findViewById(R.id.tv_main_info);
            tvSecondInfo = itemView.findViewById(R.id.tv_secondary_info);
            llMainContainer = itemView.findViewById(R.id.ll_main_container);
            llSelectableContainer = itemView.findViewById(R.id.ll_selectable_container);
            vBottomSeparator = itemView.findViewById(R.id.v_bottom_separator);

        }
    }

}
