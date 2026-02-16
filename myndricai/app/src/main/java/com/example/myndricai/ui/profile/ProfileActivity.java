package com.example.myndricai.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Switch;

import com.example.myndricai.MyApp;
import com.example.myndricai.R;
import com.example.myndricai.data.entity.UserEntity;
import com.example.myndricai.ui.main.MainActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUserName;
    private TextView tvUserEmail;
    private ImageButton btnEditProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnReset = findViewById(R.id.btnResetProgress);
        Switch switchSound = findViewById(R.id.switchSound);
        Switch switchVibration = findViewById(R.id.switchVibration);
        Switch switchHints = findViewById(R.id.switchHints);

        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        btnLogout.setOnClickListener(v -> {
            MyApp.get().session().clear();
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });

        btnReset.setOnClickListener(v -> {
            MyApp.get().cases().resetAllProgress();
            Toast.makeText(this, "Прогресс кейсов сброшен", Toast.LENGTH_SHORT).show();
        });

        setupUnavailableSwitch(switchSound);
        setupUnavailableSwitch(switchVibration);
        setupUnavailableSwitch(switchHints);

        btnEditProfile.setOnClickListener(v -> showEditNameDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindUser();
    }

    private void bindUser() {
        long uid = MyApp.get().session().getUserId();
        UserEntity u = MyApp.get().users().getById(uid);
        if (u == null) {
            tvUserName.setText("Гость");
            tvUserEmail.setText("—");
            return;
        }
        tvUserName.setText(u.name);
        tvUserEmail.setText(u.email);
    }

    private void setupUnavailableSwitch(Switch sw) {
        if (sw == null) return;
        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // revert silently, show toast
            buttonView.post(() -> buttonView.setChecked(false));
            Toast.makeText(this, "Эта функция недоступна и появится в следующей версии", Toast.LENGTH_SHORT).show();
        });
    }

    private void showEditNameDialog() {
        long uid = MyApp.get().session().getUserId();
        UserEntity u = MyApp.get().users().getById(uid);
        if (u == null) {
            Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
            return;
        }

        final android.widget.EditText input = new android.widget.EditText(this);
        input.setText(u.name);

        new AlertDialog.Builder(this)
                .setTitle("Изменить имя")
                .setView(input)
                .setPositiveButton("Сохранить", (d, w) -> {
                    try {
                        MyApp.get().users().updateName(uid, input.getText().toString());
                        bindUser();
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }
}
