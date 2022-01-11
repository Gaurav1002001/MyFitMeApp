package com.example.myfitmeapp.MeFragment_Items;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myfitmeapp.Adapter.PostAdapter;
import com.example.myfitmeapp.Friends.FriendsActivity;
import com.example.myfitmeapp.Model.Post;
import com.example.myfitmeapp.Model.User;
import com.example.myfitmeapp.Post_Activity;
import com.example.myfitmeapp.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_PageInfoActivity extends AppCompatActivity {

    private CircleImageView imageProfile;
    private TextView followersCount;
    private TextView followingCount;
    private TextView postsCount;
    private TextView joinDate;
    private TextView textview;

    private Button btnFollow;
    private FirebaseUser fUser;
    String profileId;

    private DatabaseReference reference;
    private PostAdapter postAdapter;
    private RecyclerView recyclerViewPosts;
    private List<Post> myPostList;
    private LinearLayout linearLayout;
    private CollapsingToolbarLayout toolbarLayout;
    private String isUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page_info);

        Intent intent = getIntent();
        profileId = intent.getStringExtra("publisherId");

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        toolbarLayout = findViewById(R.id.toolbarLayout);
        toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        imageProfile = findViewById(R.id.image_profile);
        LinearLayout followers = findViewById(R.id.followers);
        LinearLayout following = findViewById(R.id.following);
        LinearLayout posts = findViewById(R.id.posts);
        followersCount = findViewById(R.id.followersCount);
        followingCount = findViewById(R.id.followingCount);
        postsCount = findViewById(R.id.postsCount);
        joinDate = findViewById(R.id.userJoinDate);
        btnFollow = findViewById(R.id.btn_follow);
        linearLayout = findViewById(R.id.linearLayout);
        ImageView postImage = findViewById(R.id.addPost);

        Button btn_editProfile = findViewById(R.id.btn_editProfile);
        reference = FirebaseDatabase.getInstance().getReference();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        userInfo(profileId);
        getFollowersAndFollowingCount();
        getPostCount();

        checkFollowingStatus(btnFollow);

        textview = findViewById(R.id.textView);
        if (profileId != null && profileId.equals(fUser.getUid())){
            btnFollow.setVisibility(View.GONE);
            btn_editProfile.setVisibility(View.VISIBLE);
        }

        btnFollow.setOnClickListener(v -> {
            if (btnFollow.getText().toString().equals(("Follow"))){
                reference.child("Follow").
                        child((fUser.getUid())).child("following").child(profileId).setValue(true)
                        .addOnSuccessListener(unused -> Toast.makeText(Profile_PageInfoActivity.this,"You started following ",Toast.LENGTH_SHORT).show());

                reference.child("Follow").
                        child(profileId).child("followers").child(fUser.getUid()).setValue(true);

                //addNotification(user.getId());
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure you want to unfollow this user?");
                builder.setPositiveButton("confirm", (dialog, which) -> {
                    reference.child("Follow").
                            child((fUser.getUid())).child("following").child(profileId).removeValue();

                    reference.child("Follow").
                            child(profileId).child("followers").child(fUser.getUid()).removeValue();

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

        posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewPosts.requestFocus();
            }
        });

        btn_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile_PageInfoActivity.this, EditProfile_Activity.class));
            }
        });

        reference.child("users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isUser = snapshot.child("isUser").getValue().toString();

                if (profileId.equals(fUser.getUid()) && isUser.equals("false")) {
                    postImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        postImage.setOnClickListener(v -> startActivity(new Intent(Profile_PageInfoActivity.this, Post_Activity.class)));

        recyclerViewPosts = findViewById(R.id.postRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);
        recyclerViewPosts.setHasFixedSize(true);

        myPostList = new ArrayList<>();
        readMyPosts();
    }

    private void getFollowersAndFollowingCount() {
        reference.child("Follow").child(profileId).child("followers")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followersCount.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("Follow").child(profileId).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingCount.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getPostCount() {
        FirebaseDatabase.getInstance().getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    if (post.getPublisher().equals(profileId)) counter ++;
                }

                postsCount.setText(String.valueOf(counter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void userInfo(String profileId) {
        reference.child("users").child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user.getImageurl() != null) {
                    Glide.with(Profile_PageInfoActivity.this).load(user.getImageurl()).into(imageProfile);
                } else {
                    imageProfile.setImageResource(R.drawable.ic_avatar_recent_login);
                }
                toolbarLayout.setTitle(user.getFullname());
                joinDate.setText(user.getJoindate());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkFollowingStatus(final Button btnFollow) {
        reference.child("Follow").child(fUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
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

    private void readMyPosts() {
        reference.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myPostList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    if (post.getPublisher().equals(profileId)) {
                        myPostList.add(post);
                    }
                }
                if (myPostList.isEmpty()) {
                    linearLayout.setVisibility(View.VISIBLE);
                    if (!profileId.equals(fUser.getUid())) {
                        textview.setText("No posts Available");
                    } else {
                        textview.setText("Start training and share your story");
                    }
                } else {
                    linearLayout.setVisibility(View.GONE);
                }
                postAdapter = new PostAdapter(Profile_PageInfoActivity.this,myPostList);
                recyclerViewPosts.setAdapter(postAdapter);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                linearLayout.setVisibility(View.VISIBLE);
            }
        });

    }
}