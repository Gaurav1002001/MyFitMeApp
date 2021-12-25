package com.example.myfitmeapp.Adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myfitmeapp.Model.Post;
import com.example.myfitmeapp.R;
import com.google.firebase.auth.FirebaseUser;
import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context mContext;
    private List<Post> mPosts;

    public PostAdapter(Context context, List<Post> uploads) {
        mContext = context;
        mPosts = uploads;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = mPosts.get(position);

        holder.textViewName.setText(post.getDescription());
        Glide.with(mContext)
                .load(post.getImageUrl())
                .into(holder.imageView);
        holder.title.setText(post.getTitle());
        holder.time.setText(post.getTime());
        holder.calorie.setText(post.getCalorie());
        holder.postDate.setText(post.getPostDate());
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        SocialTextView textViewName;
        public ImageView imageView;
        public TextView title;
        public TextView time;
        public TextView calorie;
        public TextView postDate;

        public PostViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.post_image);
            title = itemView.findViewById(R.id.postTitle);
            time = itemView.findViewById(R.id.postTime);
            calorie = itemView.findViewById(R.id.postCalorie);
            postDate = itemView.findViewById(R.id.postDate);
        }
    }

}
