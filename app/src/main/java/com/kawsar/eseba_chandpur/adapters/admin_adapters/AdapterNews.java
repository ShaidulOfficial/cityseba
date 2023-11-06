package com.kawsar.eseba_chandpur.adapters.admin_adapters;


import android.app.AlertDialog;
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
import com.kawsar.eseba_chandpur.views.fragments.edit_fragments.NewsEditFragment;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.List;

public class AdapterNews extends RecyclerView.Adapter<AdapterNews.ViewHolderNews> {
    Context context;
    List<CommonModel> commonModelList;


    public AdapterNews(Context context, List<CommonModel> modelList) {
        this.context = context;
        this.commonModelList = modelList;
    }

    public void setFilterList_news(List<CommonModel> filterList) {
        this.commonModelList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderNews onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay_news, parent, false);
        return new ViewHolderNews(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNews holder, int position) {
        CommonModel newsModel = commonModelList.get(position);

        String userId = newsModel.getUserId();
        String dataType = newsModel.getDataType();
        holder.nameNews.setText(newsModel.getName());
        holder.idNumberTv_itemNews.setText("ID No : " + newsModel.getUserId());
        String imageUrl=newsModel.getPicUrl();
        Glide.with(context).load(imageUrl).placeholder(R.drawable.chandpur)
                .into(holder.newsPaperLogo);
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim1);
        holder.itemView.setAnimation(animation);

        holder.newsPaperLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_TITLE = newsModel.getName();
                Web_browser.WEBSITE_LINK = newsModel.getWebUrl();
                context.startActivity(new Intent(context, Web_browser.class));
            }
        });
        holder.editBtnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundleNews = new Bundle();
                bundleNews.putString("userId", userId);
                bundleNews.putString("data_Type", dataType);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment newsEditFragment = new NewsEditFragment();
                newsEditFragment.setArguments(bundleNews);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.framlayout_admin_dashboard, newsEditFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.deleteBtnNews.setOnClickListener(new View.OnClickListener() {
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
                        MyApplication.removeNewsData(context, dataType, userId);
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
        return commonModelList.size();
    }

    public class ViewHolderNews extends RecyclerView.ViewHolder {

        TextView idNumberTv_itemNews,nameNews;
        ImageView newsPaperLogo, deleteBtnNews, editBtnNews;

        public ViewHolderNews(@NonNull View itemView) {
            super(itemView);
            newsPaperLogo = itemView.findViewById(R.id.newsImageLogoItem);
            deleteBtnNews = itemView.findViewById(R.id.delete_item_news);
            editBtnNews = itemView.findViewById(R.id.edit_item_news);
            nameNews = itemView.findViewById(R.id.name_item_newsPaper);
            idNumberTv_itemNews = itemView.findViewById(R.id.idNumberTv_item_newsPaper);
        }
    }


}


