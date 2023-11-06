package com.kawsar.eseba_chandpur.adapters.users_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.List;

public class AdapterCommonUser extends RecyclerView.Adapter<AdapterCommonUser.ViewHolderUser> {
    Context context;
    List<CommonModel> commonModelList;
    String mobileNo;

    public AdapterCommonUser(Context context, List<CommonModel> commonModelList) {
        this.context = context;
        this.commonModelList = commonModelList;
    }

    public void setFilterList(List<CommonModel> filterList) {
        this.commonModelList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay_user, parent, false);

        return new ViewHolderUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUser holder, int position) {
        CommonModel commonModel = commonModelList.get(position);

        String initialText = String.valueOf(commonModel.getName().charAt(0));
        holder.iconTv_item_user.setText(initialText);
        holder.name_tv_item_user.setText(commonModel.getName());
        holder.location_tv_item_user.setText(commonModel.getLocation());
        holder.mobile_tv_item_user.setText(commonModel.getMobile());
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);
        holder.call_btn_item_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileNo = commonModel.getMobile();
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

    public class ViewHolderUser extends RecyclerView.ViewHolder {
        TextView name_tv_item_user, location_tv_item_user, iconTv_item_user,
                mobile_tv_item_user;
        LottieAnimationView call_btn_item_user;

        public ViewHolderUser(@NonNull View itemView) {
            super(itemView);
            name_tv_item_user = itemView.findViewById(R.id.name_tv_item_user);
            location_tv_item_user = itemView.findViewById(R.id.location_tv_item_user);
            mobile_tv_item_user = itemView.findViewById(R.id.mobile_tv_item_user);
            call_btn_item_user = itemView.findViewById(R.id.call_btn_item_user);
            iconTv_item_user = itemView.findViewById(R.id.iconTv_item_user);
        }
    }


}
