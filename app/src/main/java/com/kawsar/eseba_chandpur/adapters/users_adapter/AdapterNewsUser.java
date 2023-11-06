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

import com.bumptech.glide.Glide;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.views.activities.Web_browser;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.List;

public class AdapterNewsUser extends RecyclerView.Adapter<AdapterNewsUser.ViewHolderNewsUser> {
    Context context;
    List<CommonModel> newsUserModelList;


    public AdapterNewsUser(Context context, List<CommonModel> modelList) {
        this.context = context;
        this.newsUserModelList = modelList;
    }

    public void setFilterList_newsUser(List<CommonModel> filterList) {
        this.newsUserModelList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderNewsUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay_news_user, parent, false);
        return new ViewHolderNewsUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNewsUser holder, int position) {
        CommonModel newsModelUser = newsUserModelList.get(position);
        holder.nameNewsUser.setText(newsModelUser.getName());
        String imageUrl = newsModelUser.getPicUrl();
        Glide.with(context).load(imageUrl).placeholder(R.drawable.chandpur)
                .into(holder.newsPaperLogoUser);
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);

        holder.newsPaperLogoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_TITLE = newsModelUser.getName();
                Web_browser.WEBSITE_LINK = newsModelUser.getWebUrl();
                context.startActivity(new Intent(context, Web_browser.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsUserModelList.size();
    }

    public class ViewHolderNewsUser extends RecyclerView.ViewHolder {

        TextView nameNewsUser;
        ImageView newsPaperLogoUser;

        public ViewHolderNewsUser(@NonNull View itemView) {
            super(itemView);
            newsPaperLogoUser = itemView.findViewById(R.id.newsImageLogoItemUser);
            nameNewsUser = itemView.findViewById(R.id.name_item_newsPaperUser);
        }
    }


}


