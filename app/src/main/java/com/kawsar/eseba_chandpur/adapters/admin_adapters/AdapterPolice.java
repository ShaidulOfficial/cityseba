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
import com.kawsar.eseba_chandpur.views.fragments.edit_fragments.PoliceEditFragment;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.List;

public class AdapterPolice extends RecyclerView.Adapter<AdapterPolice.ViewHolderPolice> {
    Context context;
    List<CommonModel> policeModelList;

    public AdapterPolice(Context context, List<CommonModel> modelList) {
        this.context = context;
        this.policeModelList = modelList;
    }
    public void setFilterList_police(List<CommonModel> filterList) {
        this.policeModelList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderPolice onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay, parent, false);
        return new ViewHolderPolice(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPolice holder, int position) {
        CommonModel policeModel = policeModelList.get(position);

        String userId = policeModel.getUserId();
        String dataType = policeModel.getDataType();
        String initial_TextCommon = String.valueOf(policeModel.getName().charAt(0));
        holder.iconTv_itemCommon.setText(initial_TextCommon);
        holder.name_tv_itemCommon.setText(policeModel.getName());
        holder.location_tv_itemCommon.setText(policeModel.getPost_type()+", "+policeModel.getLocation());
        holder.mobile_tv_itemCommon.setText(policeModel.getMobile());
        holder.idNumber_tv_itemCommon.setText("ID No : " + policeModel.getUserId());

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);

        holder.editBtnCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundlePolice = new Bundle();
                bundlePolice.putString("userId", userId);
                bundlePolice.putString("dataType", dataType);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment policeEditFragment = new PoliceEditFragment();
                policeEditFragment.setArguments(bundlePolice);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_admin_dashboard, policeEditFragment)
                        .addToBackStack(null).commit();
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
                        MyApplication.removeCommonData(context,dataType,userId);
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

    public class ViewHolderPolice extends RecyclerView.ViewHolder {

        TextView name_tv_itemCommon, location_tv_itemCommon, iconTv_itemCommon,
                mobile_tv_itemCommon, idNumber_tv_itemCommon;
        LottieAnimationView call_btn_itemCommon;
        ImageView editBtnCommon, deleteBtnCommon;

        public ViewHolderPolice(@NonNull View itemView) {
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


