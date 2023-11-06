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

public class AdapterDoctorUser extends RecyclerView.Adapter<AdapterDoctorUser.ViewHolderDoctor> {
    Context context;
    List<CommonModel> modelListDoctorUser;

    public AdapterDoctorUser(Context context, List<CommonModel> modelListDoctor) {
        this.context = context;
        this.modelListDoctorUser = modelListDoctor;
    }

    public void setFilterDoctorUser(List<CommonModel> filterList) {
        this.modelListDoctorUser = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderDoctor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay_doctor_user
                , parent, false);

        return new ViewHolderDoctor(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDoctor holder, int position) {
        CommonModel commonModel = modelListDoctorUser.get(position);
        Animation animationOfDoctorItemV = AnimationUtils.loadAnimation(holder.itemView.getContext(),
                R.anim.anim1);
        holder.itemView.setAnimation(animationOfDoctorItemV);
        holder.nameDoctorItem.setText(commonModel.getName());
        holder.hopitalDoctorItem.setText(commonModel.getHospit_name());
        holder.timeDoctorItem.setText(commonModel.getChemberTime());
        holder.postDoctorItem.setText(commonModel.getQualification());
        holder.mobileDoctorItem.setText("সিরিয়ালের জন্যঃ- " + commonModel.getMobile());
        holder.calbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNo = commonModel.getMobile();
                String call = "tel:" + mobileNo.trim();
                Intent intent_call = new Intent(Intent.ACTION_DIAL);
                intent_call.setData(Uri.parse(call));
                context.startActivity(intent_call);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelListDoctorUser.size();
    }

    public class ViewHolderDoctor extends RecyclerView.ViewHolder {
        TextView nameDoctorItem, hopitalDoctorItem, timeDoctorItem, postDoctorItem, mobileDoctorItem;
        LottieAnimationView calbtn;

        public ViewHolderDoctor(@NonNull View itemView) {
            super(itemView);
            nameDoctorItem = itemView.findViewById(R.id.nameDoctorItemUser);
            hopitalDoctorItem = itemView.findViewById(R.id.hospitalDoctorItemUser);
            timeDoctorItem = itemView.findViewById(R.id.timeDoctorItemUser);
            postDoctorItem = itemView.findViewById(R.id.postDoctorItemUser);
            mobileDoctorItem = itemView.findViewById(R.id.mobileDoctorItemUser);
            calbtn = itemView.findViewById(R.id.call_btn_itemDoctorAdminUser);
        }
    }
}
