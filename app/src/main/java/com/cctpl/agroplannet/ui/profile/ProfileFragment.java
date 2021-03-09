package com.cctpl.agroplannet.ui.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.cctpl.agroplannet.R;
import com.cctpl.agroplannet.UpdateProfile;
import com.cctpl.agroplannet.Welcome;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    CircleImageView profile ;
    Button logout ;
    TextView Username , Phone_number , Address , Language , location ;
    FirebaseAuth auth ;
    FirebaseFirestore db ;
    ImageView updateProfile ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        profile = root.findViewById(R.id.profile_image);
        Username  = root.findViewById(R.id.username);
        Phone_number = root.findViewById(R.id.phone_number);
        location = root.findViewById(R.id.location);
        Address = root.findViewById(R.id.shopAddres);
        logout = root.findViewById(R.id.logout);
        updateProfile = root.findViewById(R.id.updateProfile);
        auth =  FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance() ;

        ProgressDialog pd = new ProgressDialog(root.getContext());
        pd.show();
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Loading");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(root.getContext() , Welcome.class));
                getActivity().finish();

            }
        });

        db.collection("user").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Picasso.get().load(value.get("imgUrl").toString()).into(profile);
                Username.setText(value.get("name").toString());
                Phone_number.setText(value.get("phone_no").toString());
                location.setText(value.get("address ").toString());
                pd.cancel();
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(root.getContext() , UpdateProfile.class));
            }
        });
        return root;
    }
}