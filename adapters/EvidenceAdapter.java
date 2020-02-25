package io.pixan.systramer.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import io.pixan.systramer.R;
import io.pixan.systramer.fragments.EvidenceFragment;
import io.pixan.systramer.models.EvidenceContainerModel;
import io.realm.RealmList;

import static io.pixan.systramer.utils.Utils.getSavedEvidence;


public class EvidenceAdapter extends StatelessSection {

    private EvidenceContainerModel evidenceContainerModel;
    private Drawable dHeaderIcon;
    private Activity context;
    private EvidenceFragment evidenceFragment;

    public EvidenceAdapter(
            String title,
            Drawable dHeaderIcon,
            Activity context,
            EvidenceFragment evidenceFragment,
            int type) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.item_evidence)
                .headerResourceId(R.layout.item_header_evidence)
                .build());

        this.dHeaderIcon = dHeaderIcon;
        this.context = context;
        this.evidenceFragment = evidenceFragment;

        if (getSavedEvidence(title) != null) {
            evidenceContainerModel = getSavedEvidence(title);
        } else {
            evidenceContainerModel = new EvidenceContainerModel();
            evidenceContainerModel.setName(title);
            evidenceContainerModel.setEvidences(new RealmList<>());
            evidenceContainerModel.setType(type);
        }
    }

    @Override
    public int getContentItemsTotal() {
        return evidenceContainerModel.getEvidences().size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;

        String name = evidenceContainerModel.getName();
        itemHolder.tvDescription.setText(evidenceContainerModel.getEvidences().get(position).getComment());

        Glide.with(context)
                .load(evidenceContainerModel.getEvidences().get(position).getImageUrl())
                .apply(new RequestOptions()
                        .circleCrop())
                .into(((ItemViewHolder) holder).ivItemIcon);

        itemHolder.llMainContainer.setOnClickListener(v -> {
        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);

    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        final HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.tvDescription.setText(evidenceContainerModel.getName());

        headerHolder.llMainContainer.setOnClickListener(v -> {
            evidenceFragment.setSelectedEvidence(evidenceContainerModel);
            ImagePicker.Companion
                    .with(context)
                    .cameraOnly()
                    .crop(1f, 1f)                //Crop Square image(Optional)
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .start(201);
        });


        headerHolder.ivItemIcon.setImageDrawable(dHeaderIcon);
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout llMainContainer;
        private final ImageView ivItemIcon;
        private final TextView tvDescription;


        HeaderViewHolder(View view) {
            super(view);

            llMainContainer = view.findViewById(R.id.ll_item_container);
            ivItemIcon = view.findViewById(R.id.iv_item_icon);
            tvDescription = view.findViewById(R.id.tv_description);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout llMainContainer;
        private final ImageView ivItemIcon;
        private final TextView tvDescription;

        ItemViewHolder(View view) {
            super(view);

            llMainContainer = view.findViewById(R.id.ll_item_container);
            ivItemIcon = view.findViewById(R.id.iv_item_icon);
            tvDescription = view.findViewById(R.id.tv_description);
        }
    }


}

