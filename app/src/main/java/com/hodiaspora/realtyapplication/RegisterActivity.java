package com.hodiaspora.realtyapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {
    ImageView mTenantImage;
    static int PreqCode = 1;
    static int REQUESCODE = 1;
    Uri selectedImageUri;

    private EditText dname, demail, dphone, dpassword, dconfirm, dkipande;
    String name, email, password, phone, cnf_password, identification;

    FirebaseDatabase database;
    DatabaseReference myRef;

    private FirebaseAuth fAuth;
    private static final String USER = "user";
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dname = findViewById(R.id.Name);
        demail = findViewById(R.id.Email);
        dkipande = findViewById(R.id.Identification);
        dphone = findViewById(R.id.Phone);
        dpassword = findViewById(R.id.Password);
        dconfirm = findViewById(R.id.Cnf_Password);
        Button dRegisterBtn = findViewById(R.id.RegisterBtn);
        Button dLogInP = findViewById(R.id.LoginPage);
        fAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(USER);

        dRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = demail.getText().toString();
                password = dpassword.getText().toString();
                cnf_password = dconfirm.getText().toString();
                phone = dphone.getText().toString();
                name = dname.getText().toString();
                identification = dkipande.getText().toString();
                user = new User(email, name, phone, identification);



                if (email.isEmpty() || name.isEmpty() || phone.isEmpty() || identification.isEmpty()
                        || password.isEmpty() || !password.equals(cnf_password)){

                    displayMessage("confirm all fields are populated");

                }
                else{
                    createUser(email, name, phone, password);

                }



            }
        });


        mTenantImage = findViewById(R.id.TenantImage);
        mTenantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22){

                    requestForPermission();
                }
                else{
                    openGallery();
                }
            }
        });

        dLogInP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
    }

    private void createAccount() {
        String keyId = myRef.push().getKey();
        myRef.child(keyId).setValue(user);
    }

    private void createUser(String email, final String name, final String phone, String password) {
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            createAccount();

                            displayMessage("Successfully registered");
                            updateUserInfo( name, phone, identification, selectedImageUri, fAuth.getCurrentUser());

                        }
                        else{

                            displayMessage("Registration failed. Try again. " + task.getException().getMessage());
                        }

                    }
                });
    }

    private void updateUserInfo(final String name, final String phone, final String identification, Uri selectedImageUri, final FirebaseUser currentUser) {
        StorageReference fStorage = FirebaseStorage.getInstance().getReference().child("Tenant_photos");
        final StorageReference imageFilePath = fStorage.child(selectedImageUri.getLastPathSegment());
        imageFilePath.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate =  new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            displayMessage("Registration completed");
                                            updateUi();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }


    private void updateUi() {
        Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(homeActivity);
        finish();
    }

    private void displayMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            selectedImageUri = data.getData();
            mTenantImage.setImageURI(selectedImageUri);

        }
    }

    private void requestForPermission() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(RegisterActivity.this, "Allow RealtyApp to access gallery?", Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]
                        {Manifest.permission.READ_EXTERNAL_STORAGE}, PreqCode);
            }
        }
        else {
            openGallery();
        }



    }

}
