package com.kawsar.eseba_chandpur.adapters.users_adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.models.CommonModel;
import com.kawsar.eseba_chandpur.views.activities.EditActivity;

import java.util.List;

public class AdapterParibahanUser extends RecyclerView.Adapter<AdapterParibahanUser.ViewHolderParibahanUser> {
    Context context;
    List<CommonModel> paribahanModelList;

    public AdapterParibahanUser(Context context, List<CommonModel> modelList) {
        this.context = context;
        this.paribahanModelList = modelList;
    }

    public void setFilterList_userParibahan(List<CommonModel> filterList) {
        this.paribahanModelList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderParibahanUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay_paribahan_user, parent, false);
        return new ViewHolderParibahanUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderParibahanUser holder, int position) {
        CommonModel commonModel = paribahanModelList.get(position);

        holder.name_tv_itemParibahan.setText(commonModel.getName());
        holder.journey_tv_itemParibahan.setText(commonModel.getMessage());
        holder.location_tv_itemParibahan.setText(commonModel.getLocation());
        holder.mobile_tv_itemParibahan.setText(commonModel.getMobile());
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);
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

    public class ViewHolderParibahanUser extends RecyclerView.ViewHolder {
        TextView name_tv_itemParibahan, location_tv_itemParibahan,
                mobile_tv_itemParibahan, journey_tv_itemParibahan;
        LottieAnimationView admin_call_btn_Paribahan;
        ImageView   iconImv_itemParibahan;

        public ViewHolderParibahanUser(@NonNull View itemView) {
            super(itemView);
            name_tv_itemParibahan = itemView.findViewById(R.id.name_tv_itemParibahanUser);
            journey_tv_itemParibahan = itemView.findViewById(R.id.journey_tv_itemParibahanUser);
            location_tv_itemParibahan = itemView.findViewById(R.id.location_tv_itemParibahanUser);
            mobile_tv_itemParibahan = itemView.findViewById(R.id.mobile_tv_itemParibahanUser);
            admin_call_btn_Paribahan = itemView.findViewById(R.id.admin_call_btn_ParibahanUser);
            iconImv_itemParibahan = itemView.findViewById(R.id.iconImv_itemParibahanUser);
        }
    }
}


