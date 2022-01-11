package com.example.myfitmeapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitmeapp.Adapter.Comment_Adapter;
import com.example.myfitmeapp.Model.Comment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private Comment_Adapter commentAdapter;
    private List<Comment> commentList;

    private EditText addComment;
    private ImageView post;

    private String postId;
    private String comment;

    FirebaseUser fUser;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        linearLayout = findViewById(R.id.linearLayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        postId = getIntent().getStringExtra("postId");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        commentList = new ArrayList<>();
        commentAdapter = new Comment_Adapter(this,commentList,postId);

        recyclerView.setAdapter(commentAdapter);

        addComment = findViewById(R.id.add_comment);
        addComment.addTextChangedListener(textWatcher);
        post = findViewById(R.id.post);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(addComment.getText().toString())) {
                    post.setEnabled(false);
                } else {
                    post.setEnabled(true);
                    post.setImageResource(R.drawable.ic_activity_sent_normal);
                    putComment();

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
                }
            }
        });

        getComment();
    }

    private final TextWatcher textWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String add = addComment.getText().toString();
            if (add.isEmpty()) {
                post.setEnabled(false);
                post.setImageResource(R.drawable.ic_activity_sent_disabled);
            } else {
                post.setEnabled(true);
                post.setImageResource(R.drawable.ic_activity_sent_normal);
            }
        }

        public void afterTextChanged(Editable editable) {
        }
    };

    private void getComment() {
        FirebaseDatabase.getInstance().getReference().child("comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    commentList.add(comment);
                }

                if (commentList.isEmpty()) {
                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    linearLayout.setVisibility(View.INVISIBLE);
                }

                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void putComment() {
        comment = addComment.getText().toString();
        HashMap<String, Object> map = new HashMap<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("comments").child(postId);
        String id = ref.push().getKey();
        map.put("id", id);
        map.put("comment", comment);
        map.put("publisher", fUser.getUid());
        map.put("commentTime", ServerValue.TIMESTAMP);

        addComment.setText("");
        ref.child(id).updateChildren(map);

        updateInPost();
    }

    private void updateInPost() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue().toString();

                HashMap<String, Object> map = new HashMap<>();
                ref.child("posts").child(postId);
                map.put("lcomment", comment);
                map.put("lusername", username);

                ref.child("posts").child(postId).updateChildren(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
