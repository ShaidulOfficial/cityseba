package com.kawsar.eseba_chandpur.adapters.users_adapter;

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

import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.views.activities.Web_browser;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.List;

public class AdapterBloodUser extends RecyclerView.Adapter<AdapterBloodUser.ViewHolderBloodUser> {
    Context context;
    List<CommonModel> bloodModelList;

    public AdapterBloodUser(Context context, List<CommonModel> bloodModelList) {
        this.context = context;
        this.bloodModelList = bloodModelList;
    }

    public void setFilterList_userblood(List<CommonModel> filterList) {
        this.bloodModelList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderBloodUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewUser = LayoutInflater.from(context).inflate(R.layout.item_lay_blood_user
                , parent, false);

        return new ViewHolderBloodUser(viewUser);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBloodUser holder, int position) {
        CommonModel bloodModel = bloodModelList.get(position);
        String intialText = String.valueOf(bloodModel.getName().charAt(0));
        holder.iconTvBlood.setText(intialText);
        holder.name.setText(bloodModel.getName());
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);
        holder.webBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = bloodModel.getWebUrl();
                Web_browser.WEBSITE_TITLE = "WebSite";
                Web_browser.WEBSITE_LINK = url;
                Intent myIntent = new Intent(context, Web_browser.class);
                context.startActivity(myIntent);
            }
        });
        holder.fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = bloodModel.getFbPageUrl();
                Web_browser.WEBSITE_TITLE = "Facebook";
                Web_browser.WEBSITE_LINK = url;
                Intent myIntent = new Intent(context, Web_browser.class);
                context.startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bloodModelList.size();
    }

    public class ViewHolderBloodUser extends RecyclerView.ViewHolder {

        TextView name, iconTvBlood;
        ImageView fbBtn, webBtn;

        public ViewHolderBloodUser(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.bloodUser_name_tv_item);
            fbBtn = itemView.findViewById(R.id.fb_item_bloodUser);
            webBtn = itemView.findViewById(R.id.web_item_bloodUser);
            iconTvBlood = itemView.findViewById(R.id.iconTvBloodUser);
        }
    }
}
