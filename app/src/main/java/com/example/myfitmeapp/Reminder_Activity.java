package com.example.myfitmeapp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Reminder_Activity extends AppCompatActivity  {

    DocumentReference documentReference;
    FirebaseFirestore fStore;
    private TextView textView;
    String userID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        fStore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        documentReference = fStore.collection("users").document(this.userID);

        Toolbar mToolbar = findViewById(R.id.toolbar3);
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        mToolbar.setTitle("Reminder");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

        //findViewById(R.id.setBtn).setOnClickListener(this);
        //findViewById(R.id.cancelBtn).setOnClickListener(this);
        textView = findViewById(R.id.textView);
        FirebaseDatabase.getInstance().getReference("users").child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        textView.setText(snapshot.child("fullname").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void lambda$onCreate$1$Reminder_Activity(DocumentSnapshot value, FirebaseFirestoreException error) {
        this.textView.setText(value.getString("fullName"));
    }

    /*public void onClick(View view) {
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("notificationId", 1);
        intent.putExtra(ShareConstants.WEB_DIALOG_PARAM_MESSAGE, this.textView.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 268435456);
        AlarmManager alarmManager = (AlarmManager) getSystemService(NotificationCompat.CATEGORY_ALARM);
        switch (view.getId()) {
            case R.id.cancelBtn:
                alarmManager.cancel(pendingIntent);
                Toast.makeText(this, "Canceled.", 0).show();
                return;
            case R.id.setBtn:
                int hour = timePicker.getCurrentHour().intValue();
                int minute = timePicker.getCurrentMinute().intValue();
                Calendar startTime = Calendar.getInstance();
                startTime.set(11, hour);
                startTime.set(12, minute);
                startTime.set(13, 0);
                alarmManager.set(0, startTime.getTimeInMillis(), pendingIntent);
                Toast.makeText(this, "Reminder set!", 0).show();
                return;
            default:
                return;
        }*/
}
