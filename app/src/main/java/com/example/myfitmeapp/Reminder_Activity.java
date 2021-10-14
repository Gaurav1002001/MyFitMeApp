package com.example.myfitmeapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import com.facebook.share.internal.ShareConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.Calendar;

public class Reminder_Activity extends AppCompatActivity  {

    DocumentReference documentReference;
    FirebaseFirestore fStore;
    private TextView textView;
    String userID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        Toolbar mToolbar = findViewById(R.id.toolbar3);
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        mToolbar.setTitle("Reminder");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

        /*findViewById(R.id.setBtn).setOnClickListener(this);
        findViewById(R.id.cancelBtn).setOnClickListener(this);
        this.textView = (TextView) findViewById(R.id.textView);
        this.fStore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        this.userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference document = this.fStore.collection("users").document(this.userID);
        this.documentReference = document;
        if (user != null && this.userID != null) {
            document.addSnapshotListener(this, new EventListener() {

                @Override // com.google.firebase.firestore.EventListener
                public final void onEvent(Object obj, FirebaseFirestoreException firebaseFirestoreException) {
                    Reminder_Activity.this.lambda$onCreate$1$Reminder_Activity((DocumentSnapshot) obj, firebaseFirestoreException);
                }
            });
        }
    }

    public void lambda$onCreate$0$Reminder_Activity(View view) {
        onBackPressed();
    }

    public void lambda$onCreate$1$Reminder_Activity(DocumentSnapshot value, FirebaseFirestoreException error) {
        this.textView.setText(value.getString("fullName"));
    }

    public void onClick(View view) {
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
}
