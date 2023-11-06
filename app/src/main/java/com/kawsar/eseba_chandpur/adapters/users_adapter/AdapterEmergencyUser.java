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

public class AdapterEmergencyUser extends RecyclerView.Adapter<AdapterEmergencyUser.ViewHolderEmergencyUser> {
    private Context context;
    private List<CommonModel> commonModelList;


    public AdapterEmergencyUser(Context context, List<CommonModel> commonModelList) {
        this.context = context;
        this.commonModelList = commonModelList;

    }

    @NonNull
    @Override
    public ViewHolderEmergencyUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay_emergency_user, parent, false);
        return new ViewHolderEmergencyUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEmergencyUser holder, int position) {
        CommonModel commonModel = commonModelList.get(position);

        String initial_TextCommon = String.valueOf(commonModel.getName().charAt(0));
        holder.iconTv_itemCommon.setText(initial_TextCommon);
        holder.name_tv_itemCommon.setText(commonModel.getName());
        holder.mobile_tv_itemCommon.setText(commonModel.getMobile());
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);

        holder.call_btn_itemCommon.setOnClickListener(new View.OnClickListener() {
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
        return commonModelList.size();
    }

    public class ViewHolderEmergencyUser extends RecyclerView.ViewHolder {

        TextView name_tv_itemCommon, iconTv_itemCommon,
                mobile_tv_itemCommon;
        LottieAnimationView call_btn_itemCommon;

        public ViewHolderEmergencyUser(@NonNull View itemView) {
            super(itemView);
            name_tv_itemCommon = itemView.findViewById(R.id.name_tv_itemEmergencyUser);
            mobile_tv_itemCommon = itemView.findViewById(R.id.mobile_tv_itemEmergencyUser);
            call_btn_itemCommon = itemView.findViewById(R.id.EmergencyUser_call_btn_item);
            iconTv_itemCommon = itemView.findViewById(R.id.iconTv_itemEmergencyUser);
        }
    }

}


