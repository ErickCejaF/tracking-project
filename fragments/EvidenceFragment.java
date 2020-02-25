package io.pixan.systramer.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.pixan.systramer.R;
import io.pixan.systramer.activities.EditProfileActivity;
import io.pixan.systramer.activities.MainActivity;
import io.pixan.systramer.activities.TripActivity;
import io.pixan.systramer.adapters.EvidenceAdapter;
import io.pixan.systramer.callbacks.BaseCallback;
import io.pixan.systramer.callbacks.ImageCallback;
import io.pixan.systramer.dialog.AlertDialog;
import io.pixan.systramer.dialog.BaseDialog;
import io.pixan.systramer.dialog.DialogEvidence;
import io.pixan.systramer.models.EvidenceContainerModel;
import io.pixan.systramer.models.EvidenceModel;
import io.pixan.systramer.models.ServiceModel;
import io.pixan.systramer.responses.MediaResponse;
import io.realm.Realm;
import io.realm.RealmList;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static io.pixan.systramer.utils.Constants.EVIDENCE_TYPE_DOCUMENT;
import static io.pixan.systramer.utils.Constants.EVIDENCE_TYPE_INCIDENT;
import static io.pixan.systramer.utils.Constants.EVIDENCE_TYPE_SEAL;
import static io.pixan.systramer.utils.Globals.carLocation;
import static io.pixan.systramer.utils.Globals.serviceLocation;
import static io.pixan.systramer.utils.SharedPreferences.getSharedServiceId;
import static io.pixan.systramer.utils.SharedPreferences.getSharedTripStatus;
import static io.pixan.systramer.utils.Utils.callEmergency;
import static io.pixan.systramer.utils.WebServices.wsEvidence;
import static io.pixan.systramer.utils.WebServices.wsPostAlert;
import static io.pixan.systramer.utils.WebServices.wsUploadPhoto;

public class EvidenceFragment extends Fragment {

    private SectionedRecyclerViewAdapter evidenceAdapter;
    private TextView tvEvidenceTitle;
    private RecyclerView rvEvidence;
    private String title;
    private EvidenceContainerModel selectedEvidence;
    private File selectedImage;
    private ServiceModel serviceInfo;
    private RelativeLayout lItemEmergency;
    private RelativeLayout lItemSos;
    private LinearLayout llEmergencyButton;
    private LinearLayout llItemSos;

    public EvidenceFragment() {
    }

    @SuppressLint("ValidFragment")
    public EvidenceFragment(String title, ServiceModel serviceInfo) {
        this.title = title;
        this.serviceInfo = serviceInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = DataBindingUtil.inflate(inflater, R.layout.fragment_evidence, container, false).getRoot();

        rvEvidence = view.findViewById(R.id.rv_evidence);
        tvEvidenceTitle = view.findViewById(R.id.tv_evidence_title);

        lItemEmergency = view.findViewById(R.id.item_emergency);
        lItemSos = view.findViewById(R.id.item_sos);
        llItemSos = lItemSos.findViewById(R.id.ll_button);
        llEmergencyButton = lItemEmergency.findViewById(R.id.ll_button);

        llEmergencyButton.setOnClickListener(v -> callEmergency(getActivity()));
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
//            new AlertDialog(getContext(), (getActivity()), serviceInfo.getId()).show();
        });


        tvEvidenceTitle.setText(title);

        evidenceAdapter = new SectionedRecyclerViewAdapter();

        evidenceAdapter.addSection("De Sello", new EvidenceAdapter("De Sello",
                getResources().getDrawable(R.drawable.icn_seal),
                getActivity(), this,
                EVIDENCE_TYPE_SEAL));

        evidenceAdapter.addSection("De documento", new EvidenceAdapter("De documento",
                getResources().getDrawable(R.drawable.icn_document),
                getActivity(),
                this,
                EVIDENCE_TYPE_DOCUMENT));

        evidenceAdapter.addSection("De incidente", new EvidenceAdapter("De incidente",
                getResources().getDrawable(R.drawable.icn_incident),
                getActivity(),
                this,
                EVIDENCE_TYPE_INCIDENT));

        rvEvidence.setAdapter(evidenceAdapter);
        rvEvidence.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    public void startAddEvidence(Intent data) {
        this.selectedImage = ImagePicker.Companion.getFile(data);

        DialogEvidence dialogEvidence = new DialogEvidence(getActivity(), selectedEvidence.getName());

        dialogEvidence.setReportListener(v -> {

            MultipartBody.Part multipart = null;

            try {
                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse("image/jpg"),
                                Objects.requireNonNull(ImagePicker.Companion.getFile(data))
                        );
                multipart = MultipartBody.Part.createFormData("file",
                        ImagePicker.Companion.getFilePath(data), requestFile);

            } catch (Exception ex) {
                ex.printStackTrace();
            }


            wsUploadPhoto(getActivity(), multipart, new ImageCallback() {
                @Override
                public void baseResponse(MediaResponse imageResponse) {
                    wsEvidence(getContext(),
                            dialogEvidence.getEtComments().getText().toString(),
                            ((TripActivity) getActivity()).getServiceInfo().getId(),
                            imageResponse.getData().getId(),
                            selectedEvidence.getType(),
                            serviceInfo.getUnit_id(),
                            true,
                            new BaseCallback() {
                                @Override
                                public void baseResponse(ResponseBody responseBody) {

                                    Realm mRealm = Realm.getDefaultInstance();
                                    mRealm.beginTransaction();

                                    selectedEvidence.getEvidences().add((
                                            new EvidenceModel(imageResponse.getData().getUrl(),
                                                    (dialogEvidence.getEtComments().getText().toString())))
                                    );

                                    mRealm.copyToRealmOrUpdate(selectedEvidence);
                                    evidenceAdapter.notifyItemInsertedInSection(
                                            selectedEvidence.getName(),
                                            selectedEvidence.getEvidences().size() - 1);

                                    mRealm.commitTransaction();
                                    mRealm.close();

                                    dialogEvidence.dismiss();
                                }

                                @Override
                                public void baseError() {
                                    Toast.makeText(getContext(),
                                            "Ocurrio un error al subir su evidencia, intentelo nuevamente",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }

                @Override
                public void baseError() {
                    Toast.makeText(getContext(),
                            "Ocurrio un error al subir su imagen, intentelo nuevamente",
                            Toast.LENGTH_SHORT).show();
                }
            });
        })
                .setImageView(selectedImage)
                .show();
    }

    public void setSelectedEvidence(EvidenceContainerModel evidences) {
        this.selectedEvidence = evidences;
    }
}
