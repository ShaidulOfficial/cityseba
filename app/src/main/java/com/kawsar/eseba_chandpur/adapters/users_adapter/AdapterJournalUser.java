package com.kawsar.eseba_chandpur.adapters.users_adapter;


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
import com.kawsar.eseba_chandpur.views.activities.Web_browser;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.List;

public class AdapterJournalUser extends RecyclerView.Adapter<AdapterJournalUser.ViewHolderJournalUser> {
    Context context;
    List<CommonModel> journalUserModelList;


    public AdapterJournalUser(Context context, List<CommonModel> modelList) {
        this.context = context;
        this.journalUserModelList = modelList;
    }

    public void setFilterList_journalUser(List<CommonModel> filterList) {
        this.journalUserModelList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderJournalUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay_journal_user, parent, false);
        return new ViewHolderJournalUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderJournalUser holder, int position) {
        CommonModel journalModelUser = journalUserModelList.get(position);

        String idNumber = journalModelUser.getIdnumber();
        String channelLink = journalModelUser.getWebUrl();
        String initial_TextCommon = String.valueOf(journalModelUser.getName().charAt(0));
        holder.iconTv_itemCommon.setText(initial_TextCommon);
        holder.name_tv_itemCommon.setText(journalModelUser.getName());
        holder.mobile_tv_itemCommon.setText(journalModelUser.getMobile());
        holder.channelName.setText(journalModelUser.getChannelName());

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);

        holder.call_btn_itemCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNo = journalModelUser.getMobile();
                String call = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                context.startActivity(intent);
            }
        });
        holder.channelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_TITLE = "Channel";
                Web_browser.WEBSITE_LINK = "" + channelLink;
                context.startActivity(new Intent(context, Web_browser.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return journalUserModelList.size();
    }

    public class ViewHolderJournalUser extends RecyclerView.ViewHolder {

        TextView name_tv_itemCommon, iconTv_itemCommon, channelName,
                mobile_tv_itemCommon;
        LottieAnimationView call_btn_itemCommon;
        ImageView channelBtn;

        public ViewHolderJournalUser(@NonNull View itemView) {
            super(itemView);
            name_tv_itemCommon = itemView.findViewById(R.id.name_tv_itemJournaluser);
            mobile_tv_itemCommon = itemView.findViewById(R.id.mobile_tv_itemJournaluser);
            call_btn_itemCommon = itemView.findViewById(R.id.admin_call_btn_itemJournaluser);
            channelBtn = itemView.findViewById(R.id.channel_item_Journaluser);
            channelName = itemView.findViewById(R.id.channelName_itemJournaluser);
            iconTv_itemCommon = itemView.findViewById(R.id.iconTv_itemJournaluser);
        }
    }

}


