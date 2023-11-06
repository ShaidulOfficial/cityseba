package com.kawsar.eseba_chandpur;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kawsar.eseba_chandpur.views.activities.AdminDashboardActivity;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public static void openLinkedin(Context context, String appLink, String packageName) {
        try {
            Uri uri = Uri.parse(appLink);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(packageName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException activityNotFoundException) {
            Toast.makeText(context, "no app install", Toast.LENGTH_SHORT).show();
        }
    }

    public static void dialogShowPic(Context context) {
        AlertDialog.Builder showDialog = new AlertDialog.Builder(context);
        showDialog.setIcon(R.drawable.danger);
        showDialog.setTitle("দুঃখিত!");
        showDialog.setMessage("একটি ছবি বাছাই করুন");
        showDialog.setCancelable(true);
        showDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
            }
        });
        AlertDialog alertDialogCommon = showDialog.create();
        alertDialogCommon.show();


    }

    public static void dialogShow(Context context) {
        AlertDialog.Builder showDialog = new AlertDialog.Builder(context);
        showDialog.setIcon(R.drawable.danger);
        showDialog.setTitle("জরুরী নোটিশ");
        showDialog.setMessage("এখনও কোনো তথ্য যুক্ত হয় নি!!!");
        showDialog.setCancelable(false);
        showDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
            }
        });
        AlertDialog alertDialogCommon = showDialog.create();
        alertDialogCommon.show();


    }

    public static void noNetdialogShow(Context context) {
        AlertDialog.Builder showDialog = new AlertDialog.Builder(context);
        showDialog.setIcon(R.drawable.wifi_off_24);
        showDialog.setTitle("OOPS!!! Connection Loss");
        showDialog.setMessage("Please,Connect your Internet & try again");
        showDialog.setCancelable(false);
        showDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
            }
        });
        AlertDialog alertDialogCommon = showDialog.create();
        alertDialogCommon.show();


    }


    public static void updateDialogShow(Context context) {
        AlertDialog.Builder noticeUpdateDialog = new AlertDialog.Builder(context);
        noticeUpdateDialog.setIcon(R.drawable.check_circle_24)
                .setTitle("Congratulations").setMessage("Your Data successfully Updated!");
        noticeUpdateDialog.setCancelable(false);
        noticeUpdateDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                context.startActivity(new Intent(context, AdminDashboardActivity.class));

            }
        });
        noticeUpdateDialog.create();
        noticeUpdateDialog.show();


    }

    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo() != null;

    }
    public static void removeParibahanData(Context context, String dataType, String userId,String name) {
        //remove  to RDB
        DatabaseReference dbrefer = FirebaseDatabase.getInstance().getReference("Admin");

        dbrefer.child(dataType).child(userId)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, name+" Removing Done!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "failed to remove" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public static void removeCommonData(Context context, String dataType, String userId) {
        //remove  to RDB
        DatabaseReference dbrefer = FirebaseDatabase.getInstance().getReference("Admin");

        dbrefer.child(dataType).child(userId)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Remove Done!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "failed to remove" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public static void removeNewsData(Context context, String dataType, String userId) {
        //remove  to RDB
        DatabaseReference dbrefer = FirebaseDatabase.getInstance().getReference("NewsPapers");

        dbrefer.child(dataType).child(userId)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Remove Done!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "failed to remove" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
