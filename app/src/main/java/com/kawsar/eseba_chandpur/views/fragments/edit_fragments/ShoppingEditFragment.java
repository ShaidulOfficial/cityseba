package com.kawsar.eseba_chandpur.views.fragments.edit_fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.databinding.FragmentShoppingEditBinding;
import com.kawsar.eseba_chandpur.views.fragments.admin_fragments.ShoppingFragment;
import com.kawsar.eseba_chandpur.views.fragments.user_fragments.ServicesUserFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class ShoppingEditFragment extends Fragment {

    FragmentShoppingEditBinding shoppingEditBinding;
    String userId = "", dataType = "";
    DatabaseReference dbRefEdit_shoppingEdit;
    String name, webLink;
    final int SHOPPING_PIC = 101;
    String shopping_pic_Url;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference_shopping;

    public ShoppingEditFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shoppingEditBinding = FragmentShoppingEditBinding.inflate(inflater, container, false);
        Bundle bundle = this.getArguments();
        userId = bundle.getString("uIdShop");
        dataType = bundle.getString("dataTypeShop");
        shoppingEditBinding.headTitleShoppingEdit.setText(dataType);
        dbRefEdit_shoppingEdit = FirebaseDatabase.getInstance().getReference("Admin").child(dataType)
                .child(userId);
        storage = FirebaseStorage.getInstance();
        storageReference_shopping = storage.getReference("Shopping_Photo");
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Updating...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);

        shoppingEditBinding.backBtnShoppingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        shoppingEditBinding.cancelBtnshoppingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        shoppingEditBinding.shoppingPhotoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    MyApplication.noNetdialogShow(getContext());
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, SHOPPING_PIC);
                }
            }
        });
        dbRefEdit_shoppingEdit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    shoppingEditBinding.nameEdtShoppingEdit.setText(snapshot.child("name").getValue().toString().trim());
                    shoppingEditBinding.webUrlShoppingEdit.setText(snapshot.child("webUrl").getValue().toString().trim());
                    shopping_pic_Url = snapshot.child("picUrl").getValue().toString().trim();
                    Glide.with(getContext()).load(shopping_pic_Url).placeholder(R.drawable.chandpur)
                            .into(shoppingEditBinding.shoppingPhotoEdit);
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),
                        "failed due to" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        shoppingEditBinding.submitBtnshoppingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                dbRefEdit_shoppingEdit.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        progressDialog.dismiss();
                        name = shoppingEditBinding.nameEdtShoppingEdit.getText().toString().trim();
                        webLink = shoppingEditBinding.webUrlShoppingEdit.getText().toString().trim();
                        if (name.isEmpty()) {
                            shoppingEditBinding.nameEdtShoppingEdit.setError("name required");
                        } else if (webLink.isEmpty()) {
                            shoppingEditBinding.webUrlShoppingEdit.setError("mobile required");
                        } else if (shopping_pic_Url == null) {
                            MyApplication.dialogShowPic(getContext());
                        } else {
                            HashMap<String, Object> updateMap = new HashMap<>();
                            updateMap.put("name", name);
                            updateMap.put("webUrl", webLink);
                            updateMap.put("picUrl", shopping_pic_Url);

                            dbRefEdit_shoppingEdit.updateChildren(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                      //  MyApplication.updateDialogShow(getContext());
                                        Fragment shoppingFragment = new ShoppingFragment();
                                        FragmentTransaction fragmentTransaction_services = getActivity()
                                                .getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction_services.replace(R.id.framlayout_admin_dashboard, shoppingFragment)
                                                .addToBackStack(null).commit();

                                    } else {
                                        progressDialog.show();
                                        Toast.makeText(getContext(), "no update", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
        return shoppingEditBinding.getRoot();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SHOPPING_PIC && data != null && data.getData() != null) {
            imageUri = data.getData();
            shoppingEditBinding.shoppingPhotoEdit.setImageURI(imageUri);
            sendImagetoStorage(imageUri);
        } else {
            Toast.makeText(getContext(), "not pic get", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendImagetoStorage(Uri imageUri) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Please wait");
        pd.setIcon(R.drawable.danger);
        pd.show();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM-dd-yyyy_hh:mm-aa", Locale.US);
        Date now = new Date();
        String fileName = formatter.format(now);
        StorageReference imagename = storageReference_shopping.child("shopping_logo-" + fileName);
        imagename.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                pd.dismiss();
                Toast.makeText(getContext(), "img uploaded", Toast.LENGTH_SHORT).show();


                imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        shopping_pic_Url = String.valueOf(uri);

                        Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                int count = (int) progressPercent;
                pd.setMessage("Progress: " + (count++) + " %");
            }
        });

    }
}