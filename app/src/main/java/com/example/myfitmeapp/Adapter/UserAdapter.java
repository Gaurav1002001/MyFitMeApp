package com.example.myfitmeapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myfitmeapp.Model.User;
import com.example.myfitmeapp.MeFragment_Items.Profile_PageInfoActivity;
import com.example.myfitmeapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final Context mContext;
    private final List<User> mUsers;

    private FirebaseUser firebaseUser;

    public UserAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = mUsers.get(position);
        holder.btnFollow.setVisibility(View.VISIBLE);
        holder.fullName.setText(user.getFullname());
        if (user.getImageurl() != null) {
            Glide.with(mContext).load(user.getImageurl()).into(holder.imageProfile);
        } else {
            holder.imageProfile.setImageResource(R.drawable.ic_avatar_recent_login);
        }

        isFollowed(user.getUserid(), holder.btnFollow);

        if (user.getUserid() != null && user.getUserid().equals(firebaseUser.getUid())) {
            holder.btnFollow.setVisibility(View.GONE);
        }

        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.btnFollow.getText().toString().equals(("follow"))) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").
                            child((firebaseUser.getUid())).child("following").child(user.getUserid()).setValue(true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(mContext, "You started following " + user.getUsername(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    FirebaseDatabase.getInstance().getReference().child("Follow").
                            child(user.getUserid()).child("followers").child(firebaseUser.getUid()).setValue(true);

                    //addNotification(user.getId());
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Are you sure you want to unfollow this user?");
                    builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase.getInstance().getReference().child("Follow").
                                    child((firebaseUser.getUid())).child("following").child(user.getUserid()).removeValue();

                            FirebaseDatabase.getInstance().getReference().child("Follow").
                                    child(user.getUserid()).child("followers").child(firebaseUser.getUid()).removeValue();

                            removeItem(holder.getAdapterPosition());
                        }
                    });
                    builder.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    Button nbutton = dialog.getButton(-2);
                    nbutton.setBackgroundColor(-1);
                    nbutton.setTextColor(mContext.getResources().getColor(R.color.yoga_green));
                    nbutton.setAllCaps(false);
                    Button pbutton = dialog.getButton(-1);
                    pbutton.setBackgroundColor(-1);
                    pbutton.setTextColor(mContext.getResources().getColor(R.color.yoga_green));
                    pbutton.setAllCaps(false);
                }
            }
        });

        holder.imageProfile.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, Profile_PageInfoActivity.class);
            intent.putExtra("publisherId", user.getUserid());
            mContext.startActivity(intent);
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Profile_PageInfoActivity.class);
                intent.putExtra("publisherId", user.getUserid());
                mContext.startActivity(intent);
            }
        });

    }

    private void isFollowed(final String userId, final Button btnFollow) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userId).exists())
                    btnFollow.setText("following");
                else
                    btnFollow.setText("follow");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView imageProfile;
        public TextView fullName;
        public Button btnFollow;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            fullName = itemView.findViewById(R.id.fullname);
            btnFollow = itemView.findViewById(R.id.btn_follow);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

    private void removeItem(int actualPosition) {
        mUsers.remove(actualPosition);
        notifyItemRemoved(actualPosition);
        notifyItemRangeChanged(actualPosition, mUsers.size());
    }

    /*private void addNotification(String userId) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("userId", userId);
        map.put("text", "started following you.");
        map.put("postid", "");
        map.put("isPost", false);

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(firebaseUser.getUid()).push().setValue(map);
    }*/

}
