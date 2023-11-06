package com.kawsar.eseba_chandpur.adapters.users_adapter;

import android.app.Dialog;
import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.views.activities.Web_browser;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.List;

public class AdapterResturentUser extends RecyclerView.Adapter<AdapterResturentUser.ViewHolderResturentUser> {
    Context context;
    List<CommonModel> resturentUserModelList;

    public AdapterResturentUser(Context context, List<CommonModel> modelList) {
        this.context = context;
        this.resturentUserModelList = modelList;
    }

    public void setFilterList_ResturentUser(List<CommonModel> filterList) {
        this.resturentUserModelList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderResturentUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay_resturent_user, parent, false);
        return new ViewHolderResturentUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderResturentUser holder, int position) {
        CommonModel resturentModel = resturentUserModelList.get(position);
        String webUrl = resturentModel.getWebUrl();
        String restuName = resturentModel.getName();
        String restu_description = resturentModel.getMessage();
        holder.name_tv_itemResturentUser.setText(restuName);
        Glide.with(context).load(resturentModel.getPicUrl()).placeholder(R.drawable.photo_camera_24)
                .into(holder.photoResturentUser);
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);
        holder.photoResturentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog seeMore_dialogUser = new Dialog(context);
                seeMore_dialogUser.setContentView(R.layout.custom_dialog_resturent_seemore);
                seeMore_dialogUser.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                seeMore_dialogUser.setCanceledOnTouchOutside(true);
                ImageView restuDialogPhotoUser = seeMore_dialogUser.findViewById(R.id.restuDialogPhoto);
                TextView nameRestu = seeMore_dialogUser.findViewById(R.id.name_tv_Resturent);
                TextView descriptionRestu = seeMore_dialogUser.findViewById(R.id.description_tv_Resturent);
                nameRestu.setText(restuName);
                descriptionRestu.setText(restu_description);
                Glide.with(context).load(resturentModel.getPicUrl()).placeholder(R.drawable.chandpur)
                        .into(restuDialogPhotoUser);
                seeMore_dialogUser.create();
                seeMore_dialogUser.show();
            }
        });

        holder.webBtnResturentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_TITLE = resturentModel.getName() + " সাইট";
                Web_browser.WEBSITE_LINK = webUrl;
                context.startActivity(new Intent(context, Web_browser.class));
            }
        });
        holder.call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNo = resturentModel.getMobile();
                String call = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return resturentUserModelList.size();
    }

    public class ViewHolderResturentUser extends RecyclerView.ViewHolder {

        TextView name_tv_itemResturentUser;
        ImageView call_btn;
        ImageView webBtnResturentUser, photoResturentUser;

        public ViewHolderResturentUser(@NonNull View itemView) {
            super(itemView);
            name_tv_itemResturentUser = itemView.findViewById(R.id.name_tv_itemResturent_user);
            call_btn = itemView.findViewById(R.id.resturent_user_call_btn_item);
            webBtnResturentUser = itemView.findViewById(R.id.web_item_resturent_user);
            photoResturentUser = itemView.findViewById(R.id.resturentPicItem_user);
        }
    }


}


