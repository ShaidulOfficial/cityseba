package com.kawsar.eseba_chandpur.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kawsar.eseba_chandpur.R;

public class AdminLoginActivity extends AppCompatActivity {
    EditText username_edt, pass_edt;
    ImageView back_btn_login;
    AppCompatButton loginbtn;
    CheckBox remember;
    String username_st, pass_st;
    ProgressDialog progressDialog;
    private static final String File_Email = "rememberMe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("progressing");
        progressDialog.setCancelable(true);
        username_edt = findViewById(R.id.username_edt);
        pass_edt = findViewById(R.id.pass_edt);
        loginbtn = findViewById(R.id.login_btn);
        back_btn_login = findViewById(R.id.back_btn_login);
        remember = findViewById(R.id.checkRemember);

        SharedPreferences sharedPreferences = getSharedPreferences(File_Email, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String userName = sharedPreferences.getString("uname", "");
        String password = sharedPreferences.getString("password", "");
        if (sharedPreferences.contains("checked") && sharedPreferences.getBoolean("checked", false) == true) {
            remember.setChecked(true);
        } else {
            remember.setChecked(false);
        }
        username_edt.setText(userName);
        pass_edt.setText(password);

        back_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_st = username_edt.getText().toString();
                pass_st = pass_edt.getText().toString();
                if (remember.isChecked()) {
                    editor.putBoolean("checked", true);
                    editor.apply();
                    storeDatausingSharedPref(username_st, pass_st);
                    if (username_st.isEmpty()) {
                        username_edt.setError("Username field can no be empty");

                    } else if (pass_st.isEmpty()) {
                        pass_edt.setError("Password field is can no be empty");
                    } else {
                        loginUser(username_st, pass_st);
                        Toast.makeText(getApplicationContext(), "loged in", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    if (username_st.isEmpty()) {
                        username_edt.setError("Username field can no be empty");
                    } else if (pass_st.isEmpty()) {
                        pass_edt.setError("Password field is can no be empty");
                    } else {
                        getSharedPreferences(File_Email, MODE_PRIVATE).edit().clear().commit();
                        loginUser(username_st, pass_st);
                        Toast.makeText(AdminLoginActivity.this, "logging error!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void loginUser(String username_st, String pass_st) {
        progressDialog.show();
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference dbref = fdb.getReference("Admin");

        Query check_username = dbref.orderByChild("username").equalTo(username_st);
        check_username.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                if (snapshot.exists()) {
                    String password_check = (String) snapshot.child("login").child("password").getValue();
                    if (password_check.equals(pass_st)) {
                        progressDialog.dismiss();
                        Toast.makeText(AdminLoginActivity.this, "Login Sucessfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), AdminDashboardActivity.class));
                        finish();
                    } else {
                        progressDialog.dismiss();
                        pass_edt.setError("Wrong password");
                    }
                } else {
                    progressDialog.dismiss();
                    username_edt.setError("Username does not exist");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void storeDatausingSharedPref(String username_st, String pass_st) {
        SharedPreferences.Editor editor = getSharedPreferences(File_Email, MODE_PRIVATE).edit();
        editor.putString("uname", username_st);
        editor.putString("password", pass_st);
        editor.apply();
    }

}