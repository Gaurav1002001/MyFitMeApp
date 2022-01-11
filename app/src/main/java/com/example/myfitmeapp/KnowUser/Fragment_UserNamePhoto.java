package com.example.myfitmeapp.KnowUser;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.myfitmeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_UserNamePhoto extends Fragment {

    private EditText firstName;
    private EditText lastName;
    private Button signIn;
    private CircleImageView profileImage;

    FirebaseUser mUser;
    private Uri imageUri;
    private Bundle args;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_kua_name_photo, container, false);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        args = new Bundle();

        profileImage = view.findViewById(R.id.profile_Image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity().getIntent(getContext());
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        firstName = view.findViewById(R.id.firstName);
        firstName.addTextChangedListener(textWatcher);

        lastName = view.findViewById(R.id.lastName);

        signIn = view.findViewById(R.id.signIn);
        signIn.setOnClickListener(v -> sendData());

        return view;
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!firstName.getText().toString().isEmpty()) {
                signIn.setEnabled(true);
                signIn.setAlpha(1);
            } else {
                signIn.setEnabled(false);
                signIn.setAlpha(0.6f);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            getActivity();
            if (resultCode == Activity.RESULT_OK) {
                imageUri = result.getUri();
                Log.e("resultUri ->", String.valueOf(imageUri));
                profileImage.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("error ->", String.valueOf(error));
            }
        }
    }

    private void sendData() {
        String fname = firstName.getText().toString();
        String lname = lastName.getText().toString();
        Fragment_UserGender fragment = new Fragment_UserGender();
        if (imageUri != null) {
            args.putString("imageUri",imageUri.toString());
        }
        args.putString("fullName",fname+lname);
        fragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment,null).addToBackStack(null).commit();
    }
}
