package io.pixan.systramer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.recyclerview.widget.RecyclerView;
import io.pixan.systramer.R;
import io.pixan.systramer.models.AdapterProfileModel;
import io.pixan.systramer.models.InfoModel;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {


    public Context context;
    private ArrayList<AdapterProfileModel> adapterProfileModels;

    public ProfileAdapter(Context context, ArrayList<AdapterProfileModel> adapterProfileModels) {

        this.context = context;
        this.adapterProfileModels = adapterProfileModels;
    }

    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View homeView = inflater.inflate(R.layout.item_profile, parent, false);

        // Return a new holder instance
        return new ProfileAdapter.ViewHolder(homeView);
    }

    @Override
    public void onBindViewHolder(ProfileAdapter.ViewHolder holder, int position) {
        holder.tvDescription.setText(adapterProfileModels.get(position).getDescription());
        holder.tvTitle.setText(adapterProfileModels.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return adapterProfileModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView tvTitle;
        TextView tvDescription;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);

        }
    }

}
