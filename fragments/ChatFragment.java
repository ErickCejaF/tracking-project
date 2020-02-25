package io.pixan.systramer.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.pixan.systramer.R;
import io.pixan.systramer.adapters.ChatAdapter;
import io.pixan.systramer.callbacks.BaseCallback;
import io.pixan.systramer.dialog.BaseDialog;
import io.pixan.systramer.models.MessageModel;
import io.pixan.systramer.models.UserModel;
import okhttp3.ResponseBody;

import static io.pixan.systramer.utils.Globals.carLocation;
import static io.pixan.systramer.utils.Globals.messages;
import static io.pixan.systramer.utils.Globals.rvMessages;
import static io.pixan.systramer.utils.Globals.serviceLocation;
import static io.pixan.systramer.utils.SharedPreferences.getSharedServiceId;
import static io.pixan.systramer.utils.SharedPreferences.getSharedTripStatus;
import static io.pixan.systramer.utils.Utils.getSavedUser;
import static io.pixan.systramer.utils.Utils.sendMessage;
import static io.pixan.systramer.utils.WebServices.wsPostAlert;

public class ChatFragment extends Fragment {

    private TextView tvChatTitle;
    private EditText etMessage;
    private UserModel user;
    private String title;
    private ImageView ivSendMessage;
    private int serviceId;
    private ChatAdapter chatAdapter;
    private LinearLayout llItemSos;
    private RelativeLayout lItemSos;

    public ChatFragment() {
    }

    @SuppressLint("ValidFragment")
    public ChatFragment(String title, int serviceId) {
        this.title = title;
        this.serviceId = serviceId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false).getRoot();

        tvChatTitle = view.findViewById(R.id.tv_chat_title);
        rvMessages = view.findViewById(R.id.rv_messages);
        etMessage = view.findViewById(R.id.et_message);
        ivSendMessage = view.findViewById(R.id.iv_send_message);

        user = getSavedUser();

        lItemSos = view.findViewById(R.id.item_sos);
        llItemSos = lItemSos.findViewById(R.id.ll_button);

        llItemSos.setOnClickListener(v -> {
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

        tvChatTitle.setText(title);

        if (messages == null) {
            messages = new ArrayList<>();
        }

        chatAdapter = new ChatAdapter(getActivity(), messages, user.getId());

        ivSendMessage.setOnClickListener(v -> {
            if (etMessage.getText().toString().length() > 0) {
                sendMessage(
                        serviceId,
                        new MessageModel(
                                user.getId(),
                                false,
                                etMessage.getText().toString(),
                                Calendar.getInstance().getTime()
                        ));

                etMessage.setText("");
            }
        });

        rvMessages.setAdapter(chatAdapter);
        rvMessages.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public void updateChat(boolean firstUpdate, int pos) {
        if (chatAdapter != null) {
            if (firstUpdate) {
                chatAdapter.notifyDataSetChanged();
                rvMessages.smoothScrollToPosition(pos);
            } else {
                chatAdapter.notifyItemInserted(pos);
                rvMessages.smoothScrollToPosition(pos);
            }
        }
    }
}
