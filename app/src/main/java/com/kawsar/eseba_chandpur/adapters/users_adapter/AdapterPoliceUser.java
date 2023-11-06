package com.kawsar.eseba_chandpur.adapters.users_adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.List;

public class AdapterPoliceUser extends RecyclerView.Adapter<AdapterPoliceUser.ViewHolderPoliceUser> {
    Context context;
    List<CommonModel> policeModelList;

    public AdapterPoliceUser(Context context, List<CommonModel> modelList) {
        this.context = context;
        this.policeModelList = modelList;
    }

    public void setFilterList_policeUser(List<CommonModel> filterList) {
        this.policeModelList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderPoliceUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay_user, parent, false);
        return new ViewHolderPoliceUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPoliceUser holder, int position) {
        CommonModel policeModel = policeModelList.get(position);
        String initial_TextCommon = String.valueOf(policeModel.getName().charAt(0));
        holder.iconTv_itemCommon.setText(initial_TextCommon);
        holder.name_tv_itemCommon.setText(policeModel.getName());
        holder.location_tv_itemCommon.setText(policeModel.getPost_type() + ", " + policeModel.getLocation());
        holder.mobile_tv_itemCommon.setText(policeModel.getMobile());
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);
        holder.call_btn_itemCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNo = policeModel.getMobile();
                String call = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return policeModelList.size();
    }

    public class ViewHolderPoliceUser extends RecyclerView.ViewHolder {
        TextView name_tv_itemCommon, location_tv_itemCommon, iconTv_itemCommon,
                mobile_tv_itemCommon;
        LottieAnimationView call_btn_itemCommon;

        public ViewHolderPoliceUser(@NonNull View itemView) {
            super(itemView);
            name_tv_itemCommon = itemView.findViewById(R.id.name_tv_item_user);
            location_tv_itemCommon = itemView.findViewById(R.id.location_tv_item_user);
            mobile_tv_itemCommon = itemView.findViewById(R.id.mobile_tv_item_user);
            call_btn_itemCommon = itemView.findViewById(R.id.call_btn_item_user);
            iconTv_itemCommon = itemView.findViewById(R.id.iconTv_item_user);
        }
    }


}


