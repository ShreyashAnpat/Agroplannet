package com.cctpl.agroplannet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfile extends AppCompatActivity {

    FirebaseAuth auth ;
    FirebaseFirestore db ;
    EditText UserName , Address , PhoneNo ;
    CircleImageView Profile ;
    TextView change_Profile ;
    Uri imageUri ;
    ProgressDialog pd ;
    StorageReference Imagename,Folder ;
    Button Update;
    ImageView back ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance() ;
        UserName = findViewById(R.id.username);
        Address = findViewById(R.id.Address);
        PhoneNo = findViewById(R.id.phone_number);
        Profile  = findViewById(R.id.Profile);
        change_Profile = findViewById(R.id.Change_profile);
        Update = findViewById(R.id.Update);
        pd = new ProgressDialog(UpdateProfile.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Update Profile");
        back = findViewById(R.id.back);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile.super.onBackPressed();
            }
        });

        change_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                Map<String ,Object> update = new HashMap<>();
                update.put("name",UserName.getText().toString());
                update.put("address ", Address.getText().toString());

                db.collection("user").document(auth.getCurrentUser().getUid()).update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                             pd.cancel();
                             UpdateProfile.super.onBackPressed();
                    }
                });
            }
        });

        db.collection("user").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Picasso.get().load(value.get("imgUrl").toString()).into(Profile);
                UserName.setText(value.get("name").toString());
                PhoneNo.setText(value.get("phone_no").toString());
                Address.setText(value.get("address ").toString());
            }
        });




    }

    private void selectImage() {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(UpdateProfile.this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        pd = new ProgressDialog(UpdateProfile.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Loading");
        pd.show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri= result.getUri();
                Profile.setImageURI(imageUri);

                uploadImage( imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void uploadImage(Uri imageUri) {
        Folder = FirebaseStorage.getInstance().getReference().child("UserProfile");
        Imagename = Folder.child(auth.getCurrentUser().getUid() + imageUri.getLastPathSegment());
        Imagename.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap<String , Object> update = new HashMap<>();
                        update.put("Profile",uri.toString());
                        FirebaseFirestore postss = FirebaseFirestore.getInstance() ;
                        HashMap<String , Object> postInfo = new HashMap<>();
                        postInfo.put("imgUrl", uri.toString());
                        postss.collection("user").document(auth.getCurrentUser().getUid()).update(postInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                pd.cancel();
                                UpdateProfile.super.onBackPressed();
                            }
                        });
                    }
                });

            }
        });
    }

}