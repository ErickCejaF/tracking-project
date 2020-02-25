package io.pixan.systramer.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.xml.transform.Templates;

import androidx.recyclerview.widget.RecyclerView;
import io.pixan.systramer.R;
import io.pixan.systramer.models.AdapterProfileModel;
import io.pixan.systramer.models.MessageModel;
import io.pixan.systramer.models.UserModel;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


    public Context context;
    private ArrayList<MessageModel> messages;
    private int userId;

    public ChatAdapter(Context context, ArrayList<MessageModel> messages, int userId) {
        this.context = context;
        this.messages = messages;
        this.userId = userId;
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View homeView = inflater.inflate(R.layout.item_send_message, parent, false);

        // Return a new holder instance
        return new ChatAdapter.ViewHolder(homeView);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder holder, int position) {
        if (messages.get(position).getCreated_by() == userId) {
            holder.llMainContainer.setGravity(Gravity.END);
            holder.tvMessage.setBackground(context.getResources().getDrawable(R.drawable.style_rounded_black));
            holder.tvMessage.setTextColor(context.getResources().getColor(R.color.main_white));
        } else {
            holder.llMainContainer.setGravity(Gravity.START);
            holder.tvMessage.setBackground(context.getResources().getDrawable(R.drawable.style_rounded_gray));
            holder.tvMessage.setTextColor(context.getResources().getColor(R.color.main_black));
        }

        SimpleDateFormat spf = new SimpleDateFormat("EEEE · hh:mm:ss", Locale.getDefault());
        holder.tvDate.setText(spf.format(messages.get(position).getCreated_at()));
        holder.tvMessage.setText(messages.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private LinearLayout llMainContainer;
        private TextView tvMessage;
        private TextView tvDate;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            super(itemView);
            llMainContainer = itemView.findViewById(R.id.ll_main_container);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvDate = itemView.findViewById(R.id.tv_date);

        }
    }

}
