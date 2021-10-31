package com.example.englishpractice.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishpractice.R;
import com.example.englishpractice.model.DBQuery;
import com.example.englishpractice.other.MyCompleteListener;
import com.google.android.material.textfield.TextInputLayout;

public class MyProfileActivity extends AppCompatActivity {

    private TextInputLayout name, email, phone;
    private Button cancelB, saveB, editB;
    private TextView profileText;
    private LinearLayout button_layout;
    private String nameStr, phoneStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


        Toolbar toolbar = findViewById(R.id.toolbarScore);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Account");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileText = findViewById(R.id.profile_test_myprofile);
        name = findViewById(R.id.name_myProfile);
        email = findViewById(R.id.email_myProfile);
        phone = findViewById(R.id.phone_myProfile);
        cancelB = findViewById(R.id.cancelB_mp);
        saveB = findViewById(R.id.saveB_mp);
        button_layout = findViewById(R.id.button_layout);
        editB = findViewById(R.id.editB);

        disableEditing();

        editB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableEditing();
            }
        });

        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableEditing();
            }
        });

        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {

                    saveData();

                }
            }
        });
    }


    private void disableEditing() {

        // set cho 3 ô text k được sửa chữa
        // và button edit bị ẩn
        name.setEnabled(false);
        email.setEnabled(false);
        phone.setEnabled(false);
        button_layout.setVisibility(View.GONE);

        // Lấy dử liệu từ CSDL về và set về các text
        name.getEditText().setText(DBQuery.myProfile.getName());
        email.getEditText().setText(DBQuery.myProfile.getEmail());

        if (DBQuery.myProfile.getPhone() != null)
        phone.getEditText().setText(DBQuery.myProfile.getPhone().toString());

        String prifileName = DBQuery.myProfile.getName();

        profileText.setText(prifileName.toUpperCase().substring(0,1));
    }

    private void enableEditing() {

        name.setEnabled(true);
//        email.setEnabled(true);
        phone.setEnabled(true);
        button_layout.setVisibility(View.VISIBLE);
        editB.setEnabled(false);

    }

    private boolean validate() {

        nameStr = name.getEditText().getText().toString();
        phoneStr = phone.getEditText().getText().toString();

        // ràng buộc name và phone
        if (nameStr.isEmpty()) {
            name.setError("Vui lòng nhập tên !!!");
            return false;
        }

        if (! phoneStr.isEmpty()) {

            if (!((phoneStr.length() == 10) && (TextUtils.isDigitsOnly(phoneStr)))) {

                phone.setError("Vui lòng nhập số điện thoại");
                return false;
            }
        }
        return true;
    }

    private void saveData() {

        if (phoneStr.isEmpty())
            phoneStr = null;

        DBQuery.saveProfileData(nameStr, Long.valueOf(phoneStr), new MyCompleteListener() {
            @Override
            public void onSuccess() {

                Toast.makeText(getApplicationContext(), "Cập nhật thành công..", Toast.LENGTH_SHORT).show();

                disableEditing();

            }

            @Override
            public void onFailure() {

                Toast.makeText(getApplicationContext(), "Lỗi nhá", Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId() == android.R.id.home) {
            MyProfileActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}