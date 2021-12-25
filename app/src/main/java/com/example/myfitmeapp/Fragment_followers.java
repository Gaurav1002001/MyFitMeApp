package com.example.myfitmeapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Fragment_followers extends Fragment {

    private RecyclerView recyclerView;
    private List<String> idList;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private LinearLayout linearLayout;

    private FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);

        linearLayout = view.findViewById(R.id.linearLayout);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), mUsers , true);
        recyclerView.setAdapter(userAdapter);

        idList = new ArrayList<>();

        readUsers();

        return view;
    }

    private void readUsers() {
        CollectionReference reference = FirebaseFirestore.getInstance()
                .collection("users").document(firebaseUser.getUid()).collection("Followers");
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            idList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                idList.add(document.getId());
                            }

                            if (idList.size() == 0) {
                                linearLayout.setVisibility(View.VISIBLE);
                            }
                            showUsers();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void showUsers() {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("users");
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mUsers.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);

                                for (String id : idList) {
                                    if (user.getUserId().equals(id)) {
                                        mUsers.add(user);
                                    }
                                }
                            }
                            Log.d("list f", mUsers.toString());
                            userAdapter.notifyDataSetChanged();
                        }
                    }
                });

    }
}
