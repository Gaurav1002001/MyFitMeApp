package com.example.myfitmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.Timer;
import java.util.TimerTask;

public class Main_PageActivity extends AppCompatActivity {

    DotsIndicator dotsIndicator;
    boolean doubleBackToExitPressedOnce = false;
    private EditText editTextMobile;
    private Spinner spinner;
    ViewPager viewPager;

    int[] images = {R.drawable.pass, R.drawable.bodybuilder, R.drawable.preview1, R.drawable.preview2, R.drawable.preview3};

    ViewPagerAdapter mViewPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        viewPager = findViewById(R.id.viewPager1);
        mViewPagerAdapter = new ViewPagerAdapter(Main_PageActivity.this, images);
        viewPager.setAdapter(mViewPagerAdapter);

        dotsIndicator = findViewById(R.id.dots_indicator);
        spinner = findViewById(R.id.spinner2);
        spinner.setAdapter((SpinnerAdapter) new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Country_data.countryNames));

        editTextMobile = (EditText) findViewById(R.id.phoneNumber);

        Button btnSessions = findViewById(R.id.buttonSessions);
        btnSessions.setOnClickListener(v -> {
            String code = Country_data.countryAreaCodes[spinner.getSelectedItemPosition()];
            String mobile = this.editTextMobile.getText().toString().trim();

            if (mobile.length() != 10) {
                editTextMobile.setError("Valid number is required");
                editTextMobile.requestFocus();
                return;
            }

            Intent intent = new Intent(Main_PageActivity.this, VerifyPageActivity.class);
            intent.putExtra("mobile", mobile);
            startActivity(intent);

        });

        Button btnAccount = findViewById(R.id.buttonAccount);
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main_PageActivity.this,Login_pageActivity.class));
            }
        });

        dotsIndicator.setViewPager(viewPager);
        new Timer().scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);
        getWindow().setFlags(512, 512);
        ((TextView) findViewById(R.id.textView13)).setText(Html.fromHtml("By Continuing you agree to the " +
                "<font color='#FF4444'>Terms of services</font> and <font color='#FF4444'>Privacy policy. </font>"));
    }

    public class MyTimerTask extends TimerTask {
        public MyTimerTask() {
        }

        public void run() {
            runOnUiThread(() -> {
                if (viewPager.getCurrentItem() == 0) {
                    viewPager.setCurrentItem(1);
                } else if (viewPager.getCurrentItem() == 1) {
                    viewPager.setCurrentItem(2);
                } else if (viewPager.getCurrentItem() == 2) {
                    viewPager.setCurrentItem(3);
                } else if (viewPager.getCurrentItem() == 3) {
                    viewPager.setCurrentItem(4);
                } else {
                    viewPager.setCurrentItem(0);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
