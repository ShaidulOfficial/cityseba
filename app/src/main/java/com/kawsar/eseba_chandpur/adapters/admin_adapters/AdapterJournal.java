package com.kawsar.eseba_chandpur.adapters.admin_adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.views.activities.Web_browser;
import com.kawsar.eseba_chandpur.views.fragments.edit_fragments.JournalEditFragment;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.List;

public class AdapterJournal extends RecyclerView.Adapter<AdapterJournal.ViewHolderJournal> {
    Context context;
    List<CommonModel> journalModelList;


    public AdapterJournal(Context context, List<CommonModel> modelList) {
        this.context = context;
        this.journalModelList = modelList;
    }

    public void setFilterList_journal(List<CommonModel> filterList) {
        this.journalModelList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderJournal onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay_journal, parent, false);
        return new ViewHolderJournal(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderJournal holder, int position) {
        CommonModel journalModel = journalModelList.get(position);

        String userId = journalModel.getUserId();
        String dataType = journalModel.getDataType();
        String channelLink = journalModel.getWebUrl();
        String initial_TextCommon = String.valueOf(journalModel.getName().charAt(0));
        holder.iconTv_itemJournal.setText(initial_TextCommon);
        holder.name_tv_itemJournal.setText(journalModel.getName());
        holder.channelName.setText(journalModel.getChannelName());
        holder.mobile_tv_itemJournal.setText(journalModel.getMobile());
        holder.idNumber_tv_itemJournal.setText("ID No : " + journalModel.getUserId());

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundlePolice = new Bundle();
                bundlePolice.putString("userId", userId);
                bundlePolice.putString("dataType", dataType);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment journalEditFragment = new JournalEditFragment();
                journalEditFragment.setArguments(bundlePolice);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_admin_dashboard, journalEditFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vieDelete) {
                AlertDialog.Builder builderDialog = new AlertDialog.Builder(context);
                builderDialog.setIcon(R.drawable.danger);
                builderDialog.setTitle("Notice");
                builderDialog.setMessage("Are you sure to delete?");
                builderDialog.setCancelable(false);

                builderDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyApplication.removeCommonData(context, dataType, userId);

                    }
                });
                builderDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderDialog.show();
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
        holder.call_btn_itemJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNo = journalModel.getMobile();
                String call = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return journalModelList.size();
    }

    public class ViewHolderJournal extends RecyclerView.ViewHolder {

        TextView name_tv_itemJournal, iconTv_itemJournal, channelName,
                mobile_tv_itemJournal, idNumber_tv_itemJournal;
        LottieAnimationView call_btn_itemJournal;
        ImageView editBtn, deleteBtn, channelBtn;

        public ViewHolderJournal(@NonNull View itemView) {
            super(itemView);
            name_tv_itemJournal = itemView.findViewById(R.id.name_tv_itemJournal);
            mobile_tv_itemJournal = itemView.findViewById(R.id.mobile_tv_itemJournal);
            call_btn_itemJournal = itemView.findViewById(R.id.admin_call_btn_itemJournal);
            editBtn = itemView.findViewById(R.id.edit_item_Journal);
            deleteBtn = itemView.findViewById(R.id.delete_item_Journal);
            channelBtn = itemView.findViewById(R.id.channel_item_Journal);
            channelName = itemView.findViewById(R.id.channelName_itemJournal);
            idNumber_tv_itemJournal = itemView.findViewById(R.id.idNumber_tv_itemJournal);
            iconTv_itemJournal = itemView.findViewById(R.id.iconTv_itemJournal);
        }
    }


}


