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
import com.kawsar.eseba_chandpur.views.fragments.edit_fragments.EmergencyEditFragment;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.List;

public class AdapterEmergency extends RecyclerView.Adapter<AdapterEmergency.ViewHolderEmergency> {
    private Context context;
    private List<CommonModel> commonModelList;


    public AdapterEmergency(Context context, List<CommonModel> commonModelList) {
        this.context = context;
        this.commonModelList = commonModelList;

    }

    @NonNull
    @Override
    public ViewHolderEmergency onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay_emergency, parent, false);
        return new ViewHolderEmergency(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEmergency holder, int position) {
        CommonModel commonModel = commonModelList.get(position);

        String userId = commonModel.getUserId();
        String dataType = commonModel.getDataType();
        String initial_TextCommon = String.valueOf(commonModel.getName().charAt(0));
        holder.iconTv_itemCommon.setText(initial_TextCommon);
        holder.name_tv_itemCommon.setText(commonModel.getName());
        holder.mobile_tv_itemCommon.setText(commonModel.getMobile());
        holder.idNumber_tv_itemCommon.setText("ID No : " + commonModel.getUserId());

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);

        holder.editBtnCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putString("dataType", dataType);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment emergencyEditFragment = new EmergencyEditFragment();
                emergencyEditFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_admin_dashboard, emergencyEditFragment)
                        .addToBackStack(null)
                        .commit();
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

    public class ViewHolderEmergency extends RecyclerView.ViewHolder {

        TextView name_tv_itemCommon, iconTv_itemCommon,
                mobile_tv_itemCommon, idNumber_tv_itemCommon;
        LottieAnimationView call_btn_itemCommon;
        ImageView editBtnCommon, deleteBtnCommon;

        public ViewHolderEmergency(@NonNull View itemView) {
            super(itemView);
            name_tv_itemCommon = itemView.findViewById(R.id.name_tv_itemEmergency);
            mobile_tv_itemCommon = itemView.findViewById(R.id.mobile_tv_itemEmergency);
            call_btn_itemCommon = itemView.findViewById(R.id.Emergency_call_btn_item);
            editBtnCommon = itemView.findViewById(R.id.edit_item_Emergency);
            deleteBtnCommon = itemView.findViewById(R.id.delete_item_Emergency);
            idNumber_tv_itemCommon = itemView.findViewById(R.id.idNumber_tv_itemEmergency);
            iconTv_itemCommon = itemView.findViewById(R.id.iconTv_itemEmergency);
        }
    }

    public interface onItemClickListener {
        public void onItemClick(CommonModel modelClick);
    }

}


