package com.example.englishpractice.activity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.englishpractice.R;
import com.example.englishpractice.model.DBQuery;
import com.example.englishpractice.other.MyCompleteListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout email, password;
    private Button btnLogin, forgetPass, btnLogin_new_acount, btnLogin_google;
    private CheckBox rememberUser;
    private TextView Login_logo;
    private ImageView login_back_button;
    private FirebaseAuth mAuth;
    private Dialog progressDialog;
    private TextView dialogText;
    private GoogleSignInClient mGoogleSignInClient;

    private int RC_SIGN_IN = 104;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUi();

        mAuth = FirebaseAuth.getInstance();


        dialogText = findViewById(R.id.progressBar_text);
        progressDialog = new Dialog(LoginActivity.this);
        progressDialog.setContentView(R.layout.progress_bar_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.progressBar_text);
        dialogText.setText("Đang đăng nhập.....");


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnLogin_new_acount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);

                Pair[] pairs = new Pair[3];

                pairs[0] = new Pair<View, String>(findViewById(R.id.btnLogin_new_acount), "transition_next_btn");
                pairs[1] = new Pair<View, String>(findViewById(R.id.login_back_button), "transition_back_arrow_btn");
                pairs[2] = new Pair<View, String>(findViewById(R.id.Login_logo), "transition_back_arrow_btn");


                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);

                startActivity(intent, options.toBundle());
            }
        });

        login_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);

                Pair[] pairs = new Pair[1];

                pairs[0] = new Pair<View, String>(findViewById(R.id.login_back_button), "Splash");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);

                startActivity(intent, options.toBundle());
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (validateData())
                {

                    login();
                }

            }
        });

        btnLogin_google.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                signIn();
            }
        });
    }


    private void login()
    {

        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email.getEditText().getText().toString(), password.getEditText().getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
                            DBQuery.loadData(new MyCompleteListener()
                            {
                                @Override
                                public void onSuccess()
                                {

                                    progressDialog.dismiss();
                                    startActivity(new Intent(getApplicationContext(), TrangChu.class));
                                    LoginActivity.this.finish();
                                }

                                @Override
                                public void onFailure()
                                {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                        }
                        else
                            {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }
                });

    }

    private boolean validateData()
    {
        if (email.getEditText().getText().toString().trim().isEmpty())
        {
            email.setError("Vui lòng nhập Email");
            return false;
        }
        if (password.getEditText().getText().toString().trim().isEmpty())
        {
            password.setError("Vui lòng nhập mật khẩu");
            return false;
        }

        return true;

    }


    private void initUi()
    {
        email = findViewById(R.id.Login_username);
        password = findViewById(R.id.Login_password);
        btnLogin = findViewById(R.id.btnLogin);
        rememberUser = findViewById(R.id.checkbox_remember);
        forgetPass = findViewById(R.id.forgetPass);
        btnLogin_new_acount = findViewById(R.id.btnLogin_new_acount);
        login_back_button = findViewById(R.id.login_back_button);
        Login_logo = findViewById(R.id.Login_logo);
        btnLogin_google = findViewById(R.id.btn_login_google);

    }


    private void signIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e)
            {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {

        progressDialog.show();
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                Toast.makeText(getApplicationContext(), "Đăng ký tài khản thành công", Toast.LENGTH_SHORT).show();

                                FirebaseUser user = mAuth.getCurrentUser();

                                if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                                    DBQuery.createUserData(user.getEmail(),user.getDisplayName(),Long.valueOf(user.getPhoneNumber()), new MyCompleteListener() {
                                        @Override
                                        public void onSuccess()
                                        {
                                            DBQuery.loadData(new MyCompleteListener() {
                                                @Override
                                                public void onSuccess() {
                                                    progressDialog.dismiss();
                                                    startActivity(new Intent(getApplicationContext(), TrangChu.class));
                                                    LoginActivity.this.finish();
                                                }

                                                @Override
                                                public void onFailure() {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }

                                        @Override
                                        public void onFailure() {

                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }else
                                    {

                                    DBQuery.loadData(new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            progressDialog.dismiss();
                                            startActivity(new Intent(getApplicationContext(), TrangChu.class));
                                            LoginActivity.this.finish();
                                        }

                                        @Override
                                        public void onFailure() {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

                            } else {
                                // If sign in fails, display a message to the user.

                            }
                        }
                    });



    }


}