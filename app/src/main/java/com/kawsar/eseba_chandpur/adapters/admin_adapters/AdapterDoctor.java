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
import com.kawsar.eseba_chandpur.views.fragments.edit_fragments.DoctorEditFragment;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.List;

public class AdapterDoctor extends RecyclerView.Adapter<AdapterDoctor.ViewHolderDoctor> {
    Context context;
    List<CommonModel> modelListDoctor;

    public AdapterDoctor(Context context, List<CommonModel> modelListDoctor) {
        this.context = context;
        this.modelListDoctor = modelListDoctor;
    }

    public void setFilterDoctor(List<CommonModel> filterList) {
        this.modelListDoctor = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderDoctor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay_doctor_admin
                , parent, false);
        return new ViewHolderDoctor(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDoctor holder, int position) {
        CommonModel doctorModel = modelListDoctor.get(position);
        Animation animationOfDoctorItemV = AnimationUtils.loadAnimation(holder.itemView.getContext(),
                R.anim.anim1);
        String userId = doctorModel.getUserId();
        String dataType = doctorModel.getDataType();
        holder.itemView.setAnimation(animationOfDoctorItemV);
        holder.idnumberDoctorItem.setText("ID Number:- " + doctorModel.getUserId());
        holder.nameDoctorItem.setText(doctorModel.getName());
        holder.postDoctorItem.setText(doctorModel.getQualification());
        holder.hopitalDoctorItem.setText(doctorModel.getHospit_name());
        holder.mobileDoctorItem.setText("সিরিয়ালের জন্যঃ- " + doctorModel.getMobile());
        holder.timeDoctorItem.setText(doctorModel.getChemberTime());
        holder.calbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNo = doctorModel.getMobile();
                String call = "tel:" + mobileNo.trim();
                Intent intent_call = new Intent(Intent.ACTION_DIAL);
                intent_call.setData(Uri.parse(call));
                context.startActivity(intent_call);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundlePolice = new Bundle();
                bundlePolice.putString("userId", userId);
                bundlePolice.putString("dataType", dataType);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment doctorEditFragment = new DoctorEditFragment();
                doctorEditFragment.setArguments(bundlePolice);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.framlayout_admin_dashboard, doctorEditFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public int getItemCount() {
        return modelListDoctor.size();
    }

    public class ViewHolderDoctor extends RecyclerView.ViewHolder {
        TextView idnumberDoctorItem, nameDoctorItem, hopitalDoctorItem, timeDoctorItem, postDoctorItem, mobileDoctorItem;
        LottieAnimationView calbtn;
        ImageView delete, edit;

        public ViewHolderDoctor(@NonNull View itemView) {
            super(itemView);
            idnumberDoctorItem = itemView.findViewById(R.id.idDoctorItem);
            nameDoctorItem = itemView.findViewById(R.id.nameDoctorItem);
            hopitalDoctorItem = itemView.findViewById(R.id.hospitalDoctorItem);
            timeDoctorItem = itemView.findViewById(R.id.timeDoctorItem);
            postDoctorItem = itemView.findViewById(R.id.qualification_DoctorItem);
            mobileDoctorItem = itemView.findViewById(R.id.mobileDoctorItem);
            calbtn = itemView.findViewById(R.id.call_btn_itemDoctorAdmin);
            delete = itemView.findViewById(R.id.deleteItemDoctor);
            edit = itemView.findViewById(R.id.editItemDoctor);
        }
    }
}
