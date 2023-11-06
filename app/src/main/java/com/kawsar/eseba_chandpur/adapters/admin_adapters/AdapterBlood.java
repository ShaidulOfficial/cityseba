package com.kawsar.eseba_chandpur.adapters.admin_adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.views.activities.EditBloodActivity;
import com.kawsar.eseba_chandpur.views.activities.Web_browser;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.List;

public class AdapterBlood extends RecyclerView.Adapter<AdapterBlood.ViewHolderBlood> {
    Context context;
    List<CommonModel> bloodModelList;

    public AdapterBlood(Context context, List<CommonModel> bloodModelList) {
        this.context = context;
        this.bloodModelList = bloodModelList;
    }

    public void setFilterList_adminblood(List<CommonModel> filterList) {
        this.bloodModelList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderBlood onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lay_blood, parent, false);

        return new ViewHolderBlood(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBlood holder, int position) {
        CommonModel bloodModel = bloodModelList.get(position);

        String userId = bloodModel.getUserId();
        String dataType = bloodModel.getDataType();
        String intialText = String.valueOf(bloodModel.getName().charAt(0));
        holder.iconTvBlood.setText(intialText);
        holder.name.setText(bloodModel.getName());
        holder.idNumber.setText("ID No: " + bloodModel.getUserId());
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
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditBloodActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("dataType", dataType);
                context.startActivity(intent);
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
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
        return bloodModelList.size();
    }

    public class ViewHolderBlood extends RecyclerView.ViewHolder {

        TextView name, idNumber, iconTvBlood;
        ImageView editBtn, deleteBtn, fbBtn, webBtn;

        public ViewHolderBlood(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.blood_name_tv_item);
            idNumber = itemView.findViewById(R.id.idNumber_tv_item_blood);
            editBtn = itemView.findViewById(R.id.edit_item_blood);
            deleteBtn = itemView.findViewById(R.id.delete_item_blood);
            fbBtn = itemView.findViewById(R.id.fb_item_blood);
            webBtn = itemView.findViewById(R.id.web_item_blood);
            iconTvBlood = itemView.findViewById(R.id.iconTvBlood);
        }
    }
}
