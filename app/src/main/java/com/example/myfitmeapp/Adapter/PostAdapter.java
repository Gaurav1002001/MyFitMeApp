package com.example.myfitmeapp.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myfitmeapp.BuildConfig;
import com.example.myfitmeapp.CommentActivity;
import com.example.myfitmeapp.Model.Post;
import com.example.myfitmeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private final Context mContext;
    private final List<Post> mPosts;

    private final FirebaseUser firebaseUser;
    private final ProgressDialog progressDialog;

    public PostAdapter(Context context, List<Post> uploads) {
        mContext = context;
        mPosts = uploads;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(mContext);
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);

        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = mPosts.get(position);

        getPublisherName_Image(post.getPublisher(), holder.profileImage, holder.username);
        holder.postDate.setText(post.getPostDate());
        Glide.with(mContext)
                .load(post.getImageUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.imageView);
        holder.title.setText(post.getTitle());
        holder.time.setText(post.getTime());
        holder.calorie.setText(post.getCalorie());
        holder.postDate.setText(post.getPostDate());
        holder.viewMore.setText(post.getDescription());
        holder.lusername.setText(post.getLusername());
        holder.latestComment.setText(post.getLcomment());

        isLiked(post.getPostid(), holder.like);
        noOfLikes(post.getPostid(), holder.noOfLikes);
        getComments(post.getPostid(), holder.commentList);
        noOfComments(post.getPostid(), holder.noOfComments);

        holder.like.setOnClickListener(v -> {
            if (holder.like.getTag().equals("like")) {
                FirebaseDatabase.getInstance().getReference().child("likes")
                        .child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);

                //addNotification(post.getPostid(), post.getPublisher());
            } else {
                FirebaseDatabase.getInstance().getReference().child("likes")
                        .child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
            }
        });

        holder.comment.setOnClickListener(v -> gotoCommentActivity(post));
        holder.cardView.setOnClickListener(v -> gotoCommentActivity(post));
        holder.addComment.setOnClickListener(v -> gotoCommentActivity(post));

        holder.share.setOnClickListener(v -> {
            share();
        });

        getLatestComment(post.getPostid(), holder.lusername, holder.latestComment);

        holder.removePost.setOnClickListener(v -> removePostDialog(post.getImageUrl(), post.getPublisher(), post.getPostid()));

    }

    private void getPublisherName_Image(String publisher, ImageView profileImage, TextView username) {
        FirebaseDatabase.getInstance().getReference().child("users").child(publisher)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("imageurl")) {
                            Glide.with(mContext).load(snapshot.child("imageurl").getValue().toString()).into(profileImage);
                        } else {
                            profileImage.setImageResource(R.drawable.ic_avatar_recent_login);
                        }
                        username.setText(snapshot.child("username").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void gotoCommentActivity(Post post) {
        Intent intent = new Intent(mContext, CommentActivity.class);
        intent.putExtra("postId", post.getPostid());
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView profileImage;
        public TextView username;
        public ImageView imageView;
        public ProgressBar progressBar;
        public TextView title;
        public TextView time;
        public TextView calorie;
        public TextView postDate;
        public ImageView like;
        public TextView noOfLikes;
        public ImageView comment;
        public TextView noOfComments;
        public CardView cardView;
        public TextView commentList;
        public ImageView share;
        public TextView addComment;
        public ExpandableTextView viewMore;
        public TextView lusername;
        public TextView latestComment;
        public ImageView removePost;

        public PostViewHolder(View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            imageView = itemView.findViewById(R.id.post_image);
            progressBar = itemView.findViewById(R.id.progress_bar);
            title = itemView.findViewById(R.id.postTitle);
            time = itemView.findViewById(R.id.postTime);
            calorie = itemView.findViewById(R.id.postCalorie);
            postDate = itemView.findViewById(R.id.postDate);
            like = itemView.findViewById(R.id.like);
            noOfLikes = itemView.findViewById(R.id.no_of_likes);
            comment = itemView.findViewById(R.id.comment);
            noOfComments = itemView.findViewById(R.id.no_of_comments);
            cardView = itemView.findViewById(R.id.cardView);
            commentList = itemView.findViewById(R.id.commentList);
            share = itemView.findViewById(R.id.share);
            addComment = itemView.findViewById(R.id.addComment);
            viewMore = itemView.findViewById(R.id.description);
            lusername = itemView.findViewById(R.id.lusername);
            latestComment = itemView.findViewById(R.id.latestComment);
            removePost = itemView.findViewById(R.id.removePost);
        }
    }

    private void isLiked(String postId, final ImageView imageView) {
        FirebaseDatabase.getInstance().getReference().child("likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_activity_like_press);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_activity_like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void noOfLikes(String postId, final TextView text) {
        FirebaseDatabase.getInstance().getReference().child("likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText(dataSnapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void noOfComments(String postId, final TextView text) {
        FirebaseDatabase.getInstance().getReference().child("comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText(dataSnapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getComments(String postId, final TextView text) {
        FirebaseDatabase.getInstance().getReference().child("comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    text.setText("View All " + dataSnapshot.getChildrenCount() + " Comments >");
                } else {
                    text.setText("No Comments");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getLatestComment(String postid, TextView lusername, TextView latestComment) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("comments").child(postid).orderByKey().limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                String publisher = child.child("publisher").getValue().toString();
                                databaseReference.child("users").child(publisher).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        lusername.setText(snapshot.child("username").getValue().toString());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                latestComment.setText(child.child("comment").getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void removePostDialog(String imageUrl, String publisher, String postid) {
        CharSequence[] items;
        if (publisher.endsWith(firebaseUser.getUid())) {
            items = new CharSequence[]{"Remove Post", "Save"};
        } else {
            items = new CharSequence[]{"Save"};
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setItems(items, (dialog, which) -> {
            switch (which) {
                case 0:
                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                    if (publisher.endsWith(firebaseUser.getUid())) {
                        progressDialog.setMessage("Removing Post..");
                        progressDialog.setCancelable(false);
                        progressDialog.setIndeterminate(true);
                        progressDialog.show();
                        reference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("posts").child(postid).removeValue();
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    }
                    break;
                case 1:
                    dialog.dismiss();
                    progressDialog.dismiss();
                    break;
            }
        });
        builder.show();
}

    private void share() {
        try {
            Intent shareIntent = new Intent("android.intent.action.SEND");
            shareIntent.setType("text/plain");
            shareIntent.putExtra("android.intent.extra.SUBJECT", "FitMe");
            shareIntent.putExtra("android.intent.extra.TEXT", "\nJoin me in FitMe, a complete Fitness App\n\n" + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n");
            mContext.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
        }
    }
}
