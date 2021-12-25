package com.example.myfitmeapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.internal.view.SupportMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitmeapp.FriendsModel.User;
import com.example.myfitmeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private final Context mContext;
    private final List<User> mUsers;
    private boolean isFragment;

    private FirebaseUser firebaseUser;
    private DocumentReference reference , reference1;

    public UserAdapter(Context mContext, List<User> mUsers, boolean isFragment) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item , parent , false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = mUsers.get(position);
        holder.btnFollow.setVisibility(View.VISIBLE);

        holder.userName.setText(user.getUserName());
        holder.fullName.setText(user.getFullName());

        Picasso.get().load(user.getImageUrl()).placeholder(R.drawable.person_45_45).into(holder.imageProfile);

        isFollowed(user.getUserId() , holder.btnFollow);

        if (user.getUserId() != null && user.getUserId().equals(firebaseUser.getUid())){
            holder.btnFollow.setVisibility(View.GONE);
        }

        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference = FirebaseFirestore.getInstance()
                        .collection("users").document(firebaseUser.getUid())
                        .collection("Following").document(user.getUserId());

                reference1 = FirebaseFirestore.getInstance()
                        .collection("users").document(user.getUserId())
                        .collection("Followers").document(firebaseUser.getUid());

                if (holder.btnFollow.getText().toString().equals(("follow"))){

                    Map<String, Object> user1 = new HashMap<>();
                    reference.set(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(mContext,"You started following "+ user.getUserName(),Toast.LENGTH_SHORT).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Failure","Failed");
                                }
                            });

                    Map<String, Object> user2 = new HashMap<>();
                    reference1.set(user2).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("Success", "DocumentSnapshot added with ID: " + reference.getId());
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Failure","Failed");
                                }
                            });

                    //addNotification(user.getUserId());
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Do you want to unfollow");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                    } else {
                                        Log.w("TAG", "Error deleting document");
                                    }
                                }
                            });
                            reference1.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                    } else {
                                        Log.w("TAG", "Error deleting document");
                                    }
                                }
                            });

                            removeItem(holder.getAdapterPosition());
                        }
                    });
                    builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    Button nbutton = dialog.getButton(-2);
                    nbutton.setBackgroundColor(-1);
                    nbutton.setTextColor(-7829368);
                    nbutton.setAllCaps(false);
                    Button pbutton = dialog.getButton(-1);
                    pbutton.setBackgroundColor(-1);
                    pbutton.setTextColor(SupportMenu.CATEGORY_MASK);
                    pbutton.setAllCaps(false);
                }
            }
        });

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFargment) {
                    mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().putString("profileId", user.getUserId()).apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                } else {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("publisherId", user.getUserId());
                    mContext.startActivity(intent);
                }
            }
        });*/
    }

    private void isFollowed(final String userId, final Button btnFollow) {
        DocumentReference reference = FirebaseFirestore.getInstance()
                .collection("users").document(firebaseUser.getUid())
                .collection("Following").document(userId);
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }
                if (value != null && value.exists()) {
                    btnFollow.setText("following");
                } else {
                    btnFollow.setText("follow");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imageProfile;
        public TextView userName;
        public TextView fullName;
        public Button btnFollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            userName = itemView.findViewById(R.id.username);
            fullName = itemView.findViewById(R.id.fullname);
            btnFollow = itemView.findViewById(R.id.btn_follow);
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
