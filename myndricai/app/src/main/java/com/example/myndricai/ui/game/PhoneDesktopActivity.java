package com.example.myndricai.ui.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myndricai.R;
import com.example.myndricai.common.IntentKeys;

public class PhoneDesktopActivity extends AppCompatActivity {

    private long caseId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_desktop);

        caseId = getIntent().getLongExtra(IntentKeys.EXTRA_CASE_ID, 1L);

        // Apps
        View appGallery = findViewById(R.id.appGallery);
        View appCalendar = findViewById(R.id.appCalendar);
        View appMessages = findViewById(R.id.appMessages);

        if (appGallery != null) appGallery.setOnClickListener(v -> open(GalleryActivity.class));
        if (appCalendar != null) appCalendar.setOnClickListener(v -> open(CalendarActivity.class));
        if (appMessages != null) appMessages.setOnClickListener(v -> open(MessagesActivity.class));

        // Dock
        ImageButton dockGallery = findViewById(R.id.btnDockGallery);
        ImageButton dockMessages = findViewById(R.id.btnDockMessages);
        if (dockGallery != null) dockGallery.setOnClickListener(v -> open(GalleryActivity.class));
        if (dockMessages != null) dockMessages.setOnClickListener(v -> open(MessagesActivity.class));

        View btnExit = findViewById(R.id.btnExitPhone);
        View btnConclusion = findViewById(R.id.btnConclusion);
        if (btnExit != null) btnExit.setOnClickListener(v -> finish());
        if (btnConclusion != null) {
            btnConclusion.setOnClickListener(v -> {
                Intent i = new Intent(this, ConclusionActivity.class);
                i.putExtra(IntentKeys.EXTRA_CASE_ID, caseId);
                startActivity(i);
            });
        }
    }

    private void open(Class<?> clz) {
        Intent i = new Intent(this, clz);
        i.putExtra(IntentKeys.EXTRA_CASE_ID, caseId);
        startActivity(i);
    }
}
