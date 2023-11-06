package com.kawsar.eseba_chandpur.adapters.admin_adapters;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.views.activities.Web_browser;
import com.kawsar.eseba_chandpur.views.fragments.edit_fragments.TouristEditFragment;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.List;

public class AdapterTourist extends RecyclerView.Adapter<AdapterTourist.ViewHolderTouristAdmin> {
    Context context;
    List<CommonModel> resturentModelList;

    public AdapterTourist(Context context, List<CommonModel> modelList) {
        this.context = context;
        this.resturentModelList = modelList;
    }

    public void setFilterList_touristAdmin(List<CommonModel> filterList) {
        this.resturentModelList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderTouristAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay_tourist, parent, false);
        return new ViewHolderTouristAdmin(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTouristAdmin holder, int position) {
        CommonModel resturentModel = resturentModelList.get(position);

        String userId = resturentModel.getUserId();
        String dataType = resturentModel.getDataType();
        String webUrl = resturentModel.getWebUrl();
        String touristName = resturentModel.getName();
        String tourist_description = resturentModel.getMessage();

        holder.name_tv_itemtourist.setText(resturentModel.getName());
        holder.idNumber_tv_itemtourist.setText(userId);
        Glide.with(context).load(resturentModel.getPicUrl()).placeholder(R.drawable.chandpur)
                .into(holder.phototourist);
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);
        holder.phototourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog seeMore_dialog = new Dialog(context);
                seeMore_dialog.setContentView(R.layout.custom_dialog_tourist_seemore);
                seeMore_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                seeMore_dialog.setCanceledOnTouchOutside(true);
                ImageView touristDialogPhoto = seeMore_dialog.findViewById(R.id.touristDialogPhoto);
                TextView touristWebUrl = seeMore_dialog.findViewById(R.id.touristWebUrl);
                TextView nametourist = seeMore_dialog.findViewById(R.id.name_tv_tourist);
                TextView descriptiontourist = seeMore_dialog.findViewById(R.id.description_tv_tourist);
                nametourist.setText(touristName);
                touristWebUrl.setText(webUrl);
                touristWebUrl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Web_browser.WEBSITE_TITLE = resturentModel.getName();
                        Web_browser.WEBSITE_LINK = webUrl;
                        context.startActivity(new Intent(context, Web_browser.class));
                    }
                });
                Glide.with(context).load(resturentModel.getPicUrl()).placeholder(R.drawable.chandpur)
                        .into(touristDialogPhoto);
                descriptiontourist.setText(tourist_description);
                seeMore_dialog.create();
                seeMore_dialog.show();
            }
        });

        holder.editBtntourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putString("dataType", dataType);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment touristEditFragment = new TouristEditFragment();
                touristEditFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_admin_dashboard, touristEditFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.deleteBtntourist.setOnClickListener(new View.OnClickListener() {
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
        return resturentModelList.size();
    }

    public class ViewHolderTouristAdmin extends RecyclerView.ViewHolder {

        TextView name_tv_itemtourist,
                idNumber_tv_itemtourist;

        ImageView editBtntourist, deleteBtntourist, phototourist;

        public ViewHolderTouristAdmin(@NonNull View itemView) {
            super(itemView);
            name_tv_itemtourist = itemView.findViewById(R.id.name_tv_itemtourist);
            editBtntourist = itemView.findViewById(R.id.edit_item_tourist);
            deleteBtntourist = itemView.findViewById(R.id.delete_item_tourist);
            idNumber_tv_itemtourist = itemView.findViewById(R.id.idNumber_itemtourist);
            phototourist = itemView.findViewById(R.id.touristPicItem);
        }
    }


}


