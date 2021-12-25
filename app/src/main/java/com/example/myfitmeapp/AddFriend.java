package com.example.myfitmeapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitmeapp.Adapter.UserAdapter;
import com.example.myfitmeapp.FriendsModel.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class AddFriend extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<User> mUsers;
    private UserAdapter userAdapter;
    private SocialAutoCompleteTextView search_bar;
    private FirebaseUser firebaseUser;
    private boolean isPresent;

    public AddFriend() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.recycler_view_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(this, mUsers ,false);
        recyclerView.setAdapter(userAdapter);

        search_bar = findViewById(R.id.search_bar);

        readUsers();

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUser(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //filter(s.toString());
            }
        });
    }

    /*private void readTags() {

        FirebaseDatabase.getInstance().getReference().child("HashTags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mHashTags.clear();
                mHashTagsCount.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    mHashTags.add(snapshot.getKey());
                    mHashTagsCount.add(snapshot.getChildrenCount() + "");
                }

                tagAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/

    private void readUsers() {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("users");
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && TextUtils.isEmpty(search_bar.getText().toString())) {
                            mUsers.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(firebaseUser.getUid())){
                                    continue;
                                }
                                User user = document.toObject(User.class);
                                mUsers.add(user);
                            }

                            userAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void searchUser(String s) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("users");
        Query query = reference.orderBy("userName").startAt(s).endAt(s + "\uf8ff");

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mUsers.clear();
                            if (!TextUtils.isEmpty(search_bar.getText().toString())){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    User user = document.toObject(User.class);
                                    mUsers.add(user);
                                }
                            } else {
                                readUsers();
                            }

                            userAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /*private void filter (String text) {
        List<String> mSearchTags = new ArrayList<>();
        List<String> mSearchTagsCount = new ArrayList<>();
v
        for (String s : mHashTags) {
            if (s.toLowerCase().contains(text.toLowerCase())){
                mSearchTags.add(s);
                mSearchTagsCount.add(mHashTagsCount.get(mHashTags.indexOf(s)));
            }
        }

        tagAdapter.filter(mSearchTags , mSearchTagsCount);
    }*///
}