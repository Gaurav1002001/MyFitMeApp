package com.example.myfitmeapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.internal.view.SupportMenu;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myfitmeapp.FriendsModel.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_PageInfoActivity extends AppCompatActivity {

    private CircleImageView imageProfile;
    private TextView followers;
    private TextView following;
    private TextView fullname;
    private TextView joinDate;

    private Button btnFollow,btn_editProfile;
    private FirebaseUser fUser;
    String profileId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page_info_activity);

        Intent intent = getIntent();
        profileId = intent.getStringExtra("publisherId");

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        imageProfile = findViewById(R.id.image_profile);
        followers = findViewById(R.id.followers);
        following = findViewById(R.id.following);
        fullname = findViewById(R.id.fullname);
        joinDate = findViewById(R.id.userJoinDate);
        btnFollow = findViewById(R.id.btn_follow);

        btn_editProfile = findViewById(R.id.btn_editProfile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        userInfo();
        getFollowersAndFollowingCount();

        checkFollowingStatus(btnFollow);

        if (profileId != null && profileId.equals(fUser.getUid())){
            btnFollow.setVisibility(View.GONE);
            btn_editProfile.setVisibility(View.VISIBLE);
        }

        btnFollow.setOnClickListener(v -> {
            if (btnFollow.getText().toString().equals(("Follow"))){
                FirebaseDatabase.getInstance().getReference().child("Follow").
                        child((fUser.getUid())).child("following").child(profileId).setValue(true)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Profile_PageInfoActivity.this,"You started following ",Toast.LENGTH_SHORT).show();
                            }
                        });

                FirebaseDatabase.getInstance().getReference().child("Follow").
                        child(profileId).child("followers").child(fUser.getUid()).setValue(true);

                //addNotification(user.getId());
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure you want to unfollow this user?");
                builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("Follow").
                                child((fUser.getUid())).child("following").child(profileId).removeValue();

                        FirebaseDatabase.getInstance().getReference().child("Follow").
                                child(profileId).child("followers").child(fUser.getUid()).removeValue();

                    }
                });
                builder.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
                Button nbutton = dialog.getButton(-2);
                nbutton.setBackgroundColor(-1);
                nbutton.setTextColor(getResources().getColor(R.color.yoga_green));
                nbutton.setAllCaps(false);
                Button pbutton = dialog.getButton(-1);
                pbutton.setBackgroundColor(-1);
                pbutton.setTextColor(getResources().getColor(R.color.yoga_green));
                pbutton.setAllCaps(false);
            }
        });

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                intent.putExtra("id", profileId);
                intent.putExtra("position", 0);
                startActivity(intent);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                intent.putExtra("id", profileId);
                intent.putExtra("position", 1);
                startActivity(intent);
            }
        });

        btn_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile_PageInfoActivity.this, EditProfile_Activity.class));
            }
        });

        ImageView postImage = findViewById(R.id.addPost);
        if (profileId.equals("Kgom6JB67RNyAw9gYN7DN40E81q1")) {
            postImage.setVisibility(View.VISIBLE);
        } else {
            postImage.setVisibility(View.GONE);
        }
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile_PageInfoActivity.this,Post_Activity.class));
            }
        });
    }

    private void getFollowersAndFollowingCount() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId);

        ref.child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followers.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void userInfo() {
        FirebaseDatabase.getInstance().getReference().child("users").child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user.getImageurl() != null) {
                    Glide.with(Profile_PageInfoActivity.this)
                            .load(user.getImageurl())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    imageProfile.setImageResource(R.drawable.ic_avatar_recent_login);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(imageProfile);
                } else {
                    imageProfile.setImageResource(R.drawable.ic_avatar_recent_login);
                }
                fullname.setText(user.getFullname());
                joinDate.setText(user.getJoindate());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkFollowingStatus(final Button btnFollow) {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profileId).exists()) {
                    btnFollow.setText("Following");
                } else {
                    btnFollow.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}