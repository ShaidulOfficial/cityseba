package com.kawsar.eseba_chandpur.adapters.admin_adapters;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.List;

public class AdapterFeedBack extends RecyclerView.Adapter<AdapterFeedBack.ViewHolderFeedback> {
    Context context;
    List<CommonModel> feedbackModelList;


    public AdapterFeedBack(Context context, List<CommonModel> modelList) {
        this.context = context;
        this.feedbackModelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolderFeedback onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay_feedback, parent, false);
        return new ViewHolderFeedback(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderFeedback holder, int position) {
        CommonModel feedbackModel = feedbackModelList.get(position);
        String initial_TextCommon = String.valueOf(feedbackModel.getName().charAt(0));
        holder.iconTv_itemCommon.setText(initial_TextCommon);
        holder.name_tv_itemCommon.setText(feedbackModel.getName());
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);
        holder.descriptionFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog seeMore_dialog = new Dialog(context);
                seeMore_dialog.setContentView(R.layout.custom_dialog_feedback_seemore);
                seeMore_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                seeMore_dialog.setCanceledOnTouchOutside(true);
                TextView nameFeedback = seeMore_dialog.findViewById(R.id.name_tv_Feedback);
                TextView email_tv_Feedback = seeMore_dialog.findViewById(R.id.email_tv_Feedback);
                TextView descriptionFeedback = seeMore_dialog.findViewById(R.id.description_tv_Feedback);
                email_tv_Feedback.setText(feedbackModel.getEmail());
                nameFeedback.setText(feedbackModel.getName());
                descriptionFeedback.setText(feedbackModel.getMessage());
                seeMore_dialog.create();
                seeMore_dialog.show();
            }
        });
        holder.call_btn_itemCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNo = feedbackModel.getMobile();
                String call = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return feedbackModelList.size();
    }

    public class ViewHolderFeedback extends RecyclerView.ViewHolder {

        TextView name_tv_itemCommon, iconTv_itemCommon;
        LottieAnimationView call_btn_itemCommon;
        ImageView descriptionFeedback;

        public ViewHolderFeedback(@NonNull View itemView) {
            super(itemView);
            name_tv_itemCommon = itemView.findViewById(R.id.name_tv_itemFeedback);
            call_btn_itemCommon = itemView.findViewById(R.id.Feedback_call_btn_item);
            iconTv_itemCommon = itemView.findViewById(R.id.iconTv_item_Feedback);
            descriptionFeedback = itemView.findViewById(R.id.descriptionFeedback);
        }
    }

}


