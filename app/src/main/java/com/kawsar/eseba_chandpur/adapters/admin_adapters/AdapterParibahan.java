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
import com.kawsar.eseba_chandpur.models.CommonModel;
import com.kawsar.eseba_chandpur.views.activities.EditActivity;
import com.kawsar.eseba_chandpur.views.fragments.edit_fragments.NewsEditFragment;
import com.kawsar.eseba_chandpur.views.fragments.edit_fragments.ParibahanEditFragment;

import java.util.List;

public class AdapterParibahan extends RecyclerView.Adapter<AdapterParibahan.ViewHolderParibahan> {
    Context context;
    List<CommonModel> paribahanModelList;

    public AdapterParibahan(Context context, List<CommonModel> modelList) {
        this.context = context;
        this.paribahanModelList = modelList;
    }

    public void setFilterList_Paribahan_admin(List<CommonModel> filterList) {
        this.paribahanModelList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderParibahan onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay_paribahan, parent, false);
        return new ViewHolderParibahan(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderParibahan holder, int position) {
        CommonModel commonModel = paribahanModelList.get(position);

        String name = commonModel.getName();
        String userId = commonModel.getUserId();
        String dataType = commonModel.getDataType();
        holder.name_tv_itemParibahan.setText(commonModel.getName());
        holder.journey_tv_itemParibahan.setText(commonModel.getMessage());
        holder.location_tv_itemParibahan.setText(commonModel.getLocation());
        holder.mobile_tv_itemParibahan.setText(commonModel.getMobile());
        holder.idNumber_tv_itemParibahan.setText("ID No : " + commonModel.getUserId());
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);
        holder.edit_item_Paribahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundleNews = new Bundle();
                bundleNews.putString("userId_Paribahan", userId);
                bundleNews.putString("data_Type_paribahan", dataType);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment paribahanEditFragment = new ParibahanEditFragment();
                paribahanEditFragment.setArguments(bundleNews);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_admin_dashboard, paribahanEditFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.delete_item_Paribahan.setOnClickListener(new View.OnClickListener() {
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
                        MyApplication.removeParibahanData(context, dataType, userId,name);
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
        holder.admin_call_btn_Paribahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNo = commonModel.getMobile();
                String call = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return paribahanModelList.size();
    }

    public class ViewHolderParibahan extends RecyclerView.ViewHolder {
        TextView name_tv_itemParibahan, location_tv_itemParibahan,
                mobile_tv_itemParibahan, idNumber_tv_itemParibahan, journey_tv_itemParibahan;
        LottieAnimationView admin_call_btn_Paribahan;
        ImageView edit_item_Paribahan, delete_item_Paribahan, iconImv_itemParibahan;

        public ViewHolderParibahan(@NonNull View itemView) {
            super(itemView);
            name_tv_itemParibahan = itemView.findViewById(R.id.name_tv_itemParibahan);
            journey_tv_itemParibahan = itemView.findViewById(R.id.journey_tv_itemParibahan);
            location_tv_itemParibahan = itemView.findViewById(R.id.location_tv_itemParibahan);
            mobile_tv_itemParibahan = itemView.findViewById(R.id.mobile_tv_itemParibahan);
            admin_call_btn_Paribahan = itemView.findViewById(R.id.admin_call_btn_Paribahan);
            edit_item_Paribahan = itemView.findViewById(R.id.edit_item_Paribahan);
            delete_item_Paribahan = itemView.findViewById(R.id.delete_item_Paribahan);
            idNumber_tv_itemParibahan = itemView.findViewById(R.id.idNumber_tv_itemParibahan);
            iconImv_itemParibahan = itemView.findViewById(R.id.iconImv_itemParibahan);
        }
    }
}


