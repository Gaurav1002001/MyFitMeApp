package com.example.myfitmeapp.ui.friends;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myfitmeapp.Fragment_followers;
import com.example.myfitmeapp.Fragment_following;
import com.example.myfitmeapp.FriendsModel.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    CollectionReference reference;
    FirebaseUser firebaseUser;
    private List<String> idList;
    int followersCount = 0;
    int followingCount = 0;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Fragment_followers();
                break;
            case 1:
                fragment = new Fragment_following();
                break;
        }
        return fragment;
    }

    @Override
    public String getPageTitle(int position) {
        idList = new ArrayList<>();
        switch (position) {
            case 0 :
                return "followers "+getFollowersCount();
            case 1 :
                return "following "+getFollowingCount();
        }
        return null;
    }

    private int getFollowersCount() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseFirestore.getInstance()
                .collection("users").document(firebaseUser.getUid())
                .collection("Followers");
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            followersCount = task.getResult().size();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return followersCount;
    }

    private int getFollowingCount() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseFirestore.getInstance()
                .collection("users").document(firebaseUser.getUid())
                .collection("Following");
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            idList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                idList.add(document.getId());
                            }
                            followingCount = idList.size();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return followingCount;
    }

    @Override
    public int getCount() {
        return 2;
    }

}