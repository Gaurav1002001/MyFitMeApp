package com.example.myfitmeapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitmeapp.FriendsModel.User;
import com.example.myfitmeapp.Model.Comment;
import com.example.myfitmeapp.Profile_PageInfoActivity;
import com.example.myfitmeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comment_Adapter extends RecyclerView.Adapter<Comment_Adapter.ViewHolder> {

    private Context mContext;
    private List<Comment> mComments;

    String postId;

    private FirebaseUser fUser;

    public Comment_Adapter(Context mContext, List<Comment> mComments, String postId) {
        this.mContext = mContext;
        this.mComments = mComments;
        this.postId = postId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        final Comment comment = mComments.get(position);

        holder.comment.setText(comment.getComment());

        FirebaseDatabase.getInstance().getReference().child("users").child(comment.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                holder.username.setText(user.getUsername());
                if (user.getImageurl() != null) {
                    Picasso.get().load(user.getImageurl()).into(holder.imageProfile);
                } else {
                    holder.imageProfile.setImageResource(R.drawable.ic_avatar_recent_login);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.imageProfile.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, Profile_PageInfoActivity.class);
            intent.putExtra("publisherId", comment.getPublisher());
            mContext.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (comment.getPublisher().endsWith(fUser.getUid())) {
                CharSequence[] items = {"Delete"};
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setItems(items, (dialog, which) -> {
                    if (which == 0) {
                        FirebaseDatabase.getInstance().getReference().child("comments")
                                .child(postId).child(comment.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    dialog.dismiss();

                                }
                            }
                        });
                    } else {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }

            return true;
        });

    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("yyyy/MM/dd", cal).toString();
        return date;
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imageProfile;
        public TextView username;
        public TextView comment;
        public TextView commentTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
            commentTime = itemView.findViewById(R.id.commentTime);
        }
    }
}
