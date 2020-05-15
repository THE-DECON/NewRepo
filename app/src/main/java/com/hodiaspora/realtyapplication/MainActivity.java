package com.hodiaspora.realtyapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText mEmail1, mPassword1;
    Button mLoginBtn;
    TextView mPwd_reset, mRegisterTv;
    CheckBox mV_Password;
    FirebaseAuth fAuth;
    private ProgressBar loadingProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmail1 = findViewById(R.id.Email1);
        mPassword1 = findViewById(R.id.Password1);
        mLoginBtn = findViewById(R.id.LoginBtn);
        mPwd_reset = findViewById(R.id.Pwd_reset);
        mRegisterTv = findViewById(R.id.RegisterTv);
        mV_Password = findViewById(R.id.V_password);
        loadingProgress = findViewById(R.id.progressBar);

        loadingProgress.setVisibility(View.INVISIBLE);
        mLoginBtn.setVisibility(View.VISIBLE);

        fAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("remember", "");
        if (checkbox.equals("true")){


        }
        else if (checkbox.equals("false")){

        }


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgress.setVisibility(View.VISIBLE);
                mLoginBtn.setVisibility(View.VISIBLE);
                String email = mEmail1.getText().toString().trim();
                String password  = mPassword1.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    mEmail1.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    mPassword1.setError("Enter the correct password");
                    return;
                }

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "you're successfully logged in", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Error! Please check your email and password and try again", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                finish();


            }
        });

        mPwd_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialogue = new AlertDialog.Builder(v.getContext());
                passwordResetDialogue.setTitle("Reset Password");
                passwordResetDialogue.setMessage("Enter email address");
                passwordResetDialogue.setView(resetMail);

                passwordResetDialogue.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "The reset link has been sent to your email address", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error! Email not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });

                passwordResetDialogue.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                passwordResetDialogue.create().show();

            }
        });

        mV_Password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                    Toast.makeText(MainActivity.this, "checked", Toast.LENGTH_SHORT).show();
                } else if (!buttonView.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Toast.makeText(MainActivity.this, "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mRegisterTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();

            }
        });


    }

    public void openRegisterActivity(){

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();

    }

}


