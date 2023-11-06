package com.kawsar.eseba_chandpur.adapters.admin_adapters;


import android.app.AlertDialog;
import android.app.Dialog;
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
import com.bumptech.glide.Glide;
import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.views.activities.Web_browser;
import com.kawsar.eseba_chandpur.views.fragments.edit_fragments.ResturentEditFragment;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.List;

public class AdapterResturent extends RecyclerView.Adapter<AdapterResturent.ViewHolderResturentAdmin> {
    Context context;
    List<CommonModel> resturentModelList;

    public AdapterResturent(Context context, List<CommonModel> modelList) {
        this.context = context;
        this.resturentModelList = modelList;
    }

    public void setFilterList_resturentAdmin(List<CommonModel> filterList) {
        this.resturentModelList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderResturentAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay_resturent, parent, false);
        return new ViewHolderResturentAdmin(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderResturentAdmin holder, int position) {
        CommonModel resturentModel = resturentModelList.get(position);

        String userId = resturentModel.getUserId();
        String dataType = resturentModel.getDataType();
        String webUrl = resturentModel.getWebUrl();
        String restuName = resturentModel.getName();
        String restu_description = resturentModel.getMessage();

        holder.name_tv_itemResturent.setText(resturentModel.getName());
        holder.idNumber_tv_itemResturent.setText(userId);
        Glide.with(context).load(resturentModel.getPicUrl()).placeholder(R.drawable.chandpur)
                .into(holder.photoResturent);
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);
        holder.photoResturent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog seeMore_dialog = new Dialog(context);
                seeMore_dialog.setContentView(R.layout.custom_dialog_resturent_seemore);
                seeMore_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                seeMore_dialog.setCanceledOnTouchOutside(true);
                ImageView restuDialogPhoto = seeMore_dialog.findViewById(R.id.restuDialogPhoto);
                TextView nameRestu = seeMore_dialog.findViewById(R.id.name_tv_Resturent);
                TextView descriptionRestu = seeMore_dialog.findViewById(R.id.description_tv_Resturent);
                nameRestu.setText(restuName);
                Glide.with(context).load(resturentModel.getPicUrl()).placeholder(R.drawable.chandpur)
                        .into(restuDialogPhoto);
                descriptionRestu.setText(restu_description);
                seeMore_dialog.create();
                seeMore_dialog.show();
            }
        });
        holder.webBtnResturent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_TITLE = restuName;
                Web_browser.WEBSITE_LINK = webUrl;
                context.startActivity(new Intent(context, Web_browser.class));
            }
        });
        holder.editBtnResturent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundlePolice = new Bundle();
                bundlePolice.putString("userId", userId);
                bundlePolice.putString("dataType", dataType);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment resturentEditFragment = new ResturentEditFragment();
                resturentEditFragment.setArguments(bundlePolice);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_admin_dashboard, resturentEditFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.deleteBtnResturent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        holder.call_btn_itemResturent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNo = resturentModel.getMobile();
                String call = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return resturentModelList.size();
    }

    public class ViewHolderResturentAdmin extends RecyclerView.ViewHolder {

        TextView name_tv_itemResturent,
                idNumber_tv_itemResturent;
        LottieAnimationView call_btn_itemResturent;
        ImageView editBtnResturent, deleteBtnResturent, webBtnResturent, photoResturent;

        public ViewHolderResturentAdmin(@NonNull View itemView) {
            super(itemView);
            name_tv_itemResturent = itemView.findViewById(R.id.name_tv_itemResturent);
            call_btn_itemResturent = itemView.findViewById(R.id.resturent_call_btn_item);
            editBtnResturent = itemView.findViewById(R.id.edit_item_resturent);
            deleteBtnResturent = itemView.findViewById(R.id.delete_item_resturent);
            webBtnResturent = itemView.findViewById(R.id.web_item_resturent);
            idNumber_tv_itemResturent = itemView.findViewById(R.id.idNumber_itemResturent);
            photoResturent = itemView.findViewById(R.id.resturentPicItem);
        }
    }


}


