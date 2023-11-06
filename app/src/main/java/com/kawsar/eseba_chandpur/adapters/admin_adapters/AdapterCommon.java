package com.kawsar.eseba_chandpur.adapters.admin_adapters;


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
import com.kawsar.eseba_chandpur.views.activities.EditActivity;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.List;

public class AdapterCommon extends RecyclerView.Adapter<AdapterCommon.ViewHolderCommon> {
    Context context;
    List<CommonModel> commonModelList;

    public AdapterCommon(Context context, List<CommonModel> modelList) {
        this.context = context;
        this.commonModelList = modelList;
    }

    public void setFilterList_admin(List<CommonModel> filterList) {
        this.commonModelList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderCommon onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay, parent, false);
        return new ViewHolderCommon(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCommon holder, int position) {
        CommonModel commonModel = commonModelList.get(position);

        String userId = commonModel.getUserId();
        String dataType = commonModel.getDataType();
        String initial_TextCommon = String.valueOf(commonModel.getName().charAt(0));
        holder.iconTv_itemCommon.setText(initial_TextCommon);
        holder.name_tv_itemCommon.setText(commonModel.getName());
        holder.location_tv_itemCommon.setText(commonModel.getLocation());
        holder.mobile_tv_itemCommon.setText(commonModel.getMobile());
        holder.idNumber_tv_itemCommon.setText("ID No : " + commonModel.getUserId());
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);
        holder.editBtnCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("dataType", dataType);
                context.startActivity(intent);
            }
        });
        holder.deleteBtnCommon.setOnClickListener(new View.OnClickListener() {
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

    public class ViewHolderCommon extends RecyclerView.ViewHolder {
        TextView name_tv_itemCommon, location_tv_itemCommon, iconTv_itemCommon,
                mobile_tv_itemCommon, idNumber_tv_itemCommon;
        LottieAnimationView call_btn_itemCommon;
        ImageView editBtnCommon, deleteBtnCommon;

        public ViewHolderCommon(@NonNull View itemView) {
            super(itemView);
            name_tv_itemCommon = itemView.findViewById(R.id.name_tv_item);
            location_tv_itemCommon = itemView.findViewById(R.id.location_tv_item);
            mobile_tv_itemCommon = itemView.findViewById(R.id.mobile_tv_item);
            call_btn_itemCommon = itemView.findViewById(R.id.admin_call_btn_item);
            editBtnCommon = itemView.findViewById(R.id.edit_item_common);
            deleteBtnCommon = itemView.findViewById(R.id.delete_item_common);
            idNumber_tv_itemCommon = itemView.findViewById(R.id.idNumber_tv_item);
            iconTv_itemCommon = itemView.findViewById(R.id.iconTv_item);
        }
    }
}


