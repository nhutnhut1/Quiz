package com.example.englishpractice.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishpractice.R;
import com.example.englishpractice.model.DBQuery;
import com.example.englishpractice.other.MyCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout fullname, email, phone_number, password, comfirmPassword;
    private Button signUpNext, login_button;
    private ImageView signUp_back_button;
    private FirebaseAuth mAuth;
    private String emailStr, passStr,phoneStr,fullnameStr , comfirmPassStr;
    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        initUi();

        mAuth = FirebaseAuth.getInstance();

        dialogText = findViewById(R.id.progressBar_text);
        progressDialog = new Dialog(SignUpActivity.this);
        progressDialog.setContentView(R.layout.progress_bar_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.progressBar_text);
        dialogText.setText("Đang đăng ký.....");

        signUp_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);

                Pair[] pairs = new Pair[1];

                pairs[0] = new Pair<View,String>(findViewById(R.id.signUp_back_button),"Splash");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this,pairs);

                startActivity(intent, options.toBundle());
            }
        });


        signUpNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateData()) {
                    signUp();
                }else {
                    Toast.makeText(getApplicationContext(), "Lỗi!!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    private void signUp() {

        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "Đăng kí tài khoản thành công", Toast.LENGTH_SHORT).show();

                            DBQuery.createUserData(emailStr, fullnameStr, Long.valueOf(phoneStr), new MyCompleteListener() {
                                @Override
                                public void onSuccess() {

                                    DBQuery.loadData(new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {

                                            startActivity(new Intent(getApplicationContext(),TrangChu.class));
                                            progressDialog.dismiss();
                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(getApplicationContext(), "Lỗi đăng kí....", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(getApplicationContext(), "Lỗi đăng kí....", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }


    private boolean validateData() {

        fullnameStr = fullname.getEditText().getText().toString();
        passStr = password.getEditText().getText().toString();
        emailStr = email.getEditText().getText().toString();
        phoneStr = phone_number.getEditText().getText().toString();
        comfirmPassStr = comfirmPassword.getEditText().getText().toString();

        if (fullnameStr.isEmpty()) {
            fullname.setError("Vui lòng nhập Email");
            return false;
        }


        if (emailStr.isEmpty()) {
            email.setError("Vui lòng nhập Email");
            return false;
        }
        if (phoneStr.isEmpty()) {
            phone_number.setError("Vui lòng nhập số điện thoại");
            return false;
        }
        if (passStr.isEmpty()) {
            password.setError("Vui lòng nhập mật khẩu");
            return false;
        }

        if (comfirmPassStr.isEmpty()) {
            comfirmPassword.setError("Vui lòng nhập mật khẩu");
            return false;
        }

        if (passStr.compareTo(comfirmPassStr) != 0) {
            Toast.makeText(getApplicationContext(), "Nhập sai rồi nhá???", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    private void initUi() {
        fullname = findViewById(R.id.signup_fullname);
        email = findViewById(R.id.signup_email);
        phone_number = findViewById(R.id.signup_phone_number);
        password = findViewById(R.id.signup_password);
        comfirmPassword = findViewById(R.id.signup_ConfirmPassword);
        signUpNext = findViewById(R.id.signup_button_next);
        login_button = findViewById(R.id.signup_login_button);
        signUp_back_button = findViewById(R.id.signUp_back_button);

    }
}
