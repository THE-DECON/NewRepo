package com.hodiaspora.realtyapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AgentActivity extends AppCompatActivity {

    private EditText C_Name, Reg_Number, Official_Email, C_password, ConfirmP;
    private Button CreateBtn;
    private TextView GoLogin;
    private ImageView ProfilePic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);

        C_Name = findViewById(R.id.C_Name);
        Reg_Number = findViewById(R.id.Reg_Number);
        Official_Email = findViewById(R.id.Official_Email);
        C_password = findViewById(R.id.C_password);
        ConfirmP = findViewById(R.id.ConfirmP);
        ProfilePic = findViewById(R.id.Logo);

        CreateBtn = findViewById(R.id.CreateBtn);
        GoLogin = findViewById(R.id.GoLogin);




    }
}
