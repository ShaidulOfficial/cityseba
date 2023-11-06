package com.kawsar.eseba_chandpur.adapters.users_adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.views.activities.Web_browser;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.List;

public class AdapterTouristUser extends RecyclerView.Adapter<AdapterTouristUser.ViewHolderTouristUser> {
    Context context;
    List<CommonModel> resturentUserModelList;

    public AdapterTouristUser(Context context, List<CommonModel> modelList) {
        this.context = context;
        this.resturentUserModelList = modelList;
    }

    public void setFilterList_TouristUser(List<CommonModel> filterList) {
        this.resturentUserModelList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderTouristUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay_tourist_user, parent, false);
        return new ViewHolderTouristUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTouristUser holder, int position) {
        CommonModel resturentModel = resturentUserModelList.get(position);
        String webUrl = resturentModel.getWebUrl();
        String touristSpotName = resturentModel.getName();
        String tourist_descriptionUser = resturentModel.getMessage();
        holder.name_tv_item_touristUser.setText(touristSpotName);
        Glide.with(context).load(resturentModel.getPicUrl()).placeholder(R.drawable.photo_camera_24)
                .into(holder.photoTouristUser);
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);
        holder.photoTouristUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog seeMore_dialog = new Dialog(context);
                seeMore_dialog.setContentView(R.layout.custom_dialog_tourist_seemore);
                seeMore_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                seeMore_dialog.setCanceledOnTouchOutside(true);
                ImageView touristUserDialogPhoto = seeMore_dialog.findViewById(R.id.touristDialogPhoto);
                TextView touristUserWebUrl = seeMore_dialog.findViewById(R.id.touristWebUrl);
                TextView nametouristUser = seeMore_dialog.findViewById(R.id.name_tv_tourist);
                TextView descriptiontouristUser = seeMore_dialog.findViewById(R.id.description_tv_tourist);
                nametouristUser.setText(touristSpotName);
                touristUserWebUrl.setText(webUrl);
                touristUserWebUrl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Web_browser.WEBSITE_TITLE = resturentModel.getName();
                        Web_browser.WEBSITE_LINK = webUrl;
                        context.startActivity(new Intent(context, Web_browser.class));
                    }
                });
                Glide.with(context).load(resturentModel.getPicUrl()).placeholder(R.drawable.chandpur)
                        .into(touristUserDialogPhoto);
                descriptiontouristUser.setText(tourist_descriptionUser);
                seeMore_dialog.create();
                seeMore_dialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return resturentUserModelList.size();
    }

    public class ViewHolderTouristUser extends RecyclerView.ViewHolder {

        TextView name_tv_item_touristUser;
        ImageView photoTouristUser;

        public ViewHolderTouristUser(@NonNull View itemView) {
            super(itemView);
            name_tv_item_touristUser = itemView.findViewById(R.id.name_tv_itemtourist_user);
            photoTouristUser = itemView.findViewById(R.id.touristPicItem_user);
        }
    }


}


