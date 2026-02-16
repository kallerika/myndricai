package com.example.myndricai.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myndricai.R;
import com.example.myndricai.app.AppConfig;
import com.example.myndricai.common.result.UiState;
import com.example.myndricai.di.ServiceLocator;
import com.example.myndricai.ui.main.MainActivity;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel vm;

    private ImageButton btnBackToCases;
    private ImageButton btnEditProfile;

    private TextView tvUserName;
    private TextView tvUserEmail;

    private android.widget.Button btnLogout;
    private android.widget.Button btnResetProgress;

    private Switch switchSound;
    private Switch switchVibration;
    private Switch switchHints;

    private boolean lockSwitchCallback = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnBackToCases = findViewById(R.id.btnBack);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);

        btnLogout = findViewById(R.id.btnLogout);
        btnResetProgress = findViewById(R.id.btnResetProgress);

        switchSound = findViewById(R.id.switchSound);
        switchVibration = findViewById(R.id.switchVibration);
        switchHints = findViewById(R.id.switchHints);

        vm = new ViewModelProvider(this, new VmFactory()).get(ProfileViewModel.class);

        // ic_menu_revert -> назад
        btnBackToCases.setOnClickListener(v -> vm.onBack());

        // выход из аккаунта -> activity_main
        btnLogout.setOnClickListener(v -> vm.onLogout());

        // смена имени -> диалог
        btnEditProfile.setOnClickListener(v -> showChangeNameDialog());

        // сброс прогресса
        btnResetProgress.setOnClickListener(v -> vm.onResetProgress());

        // настройки недоступны
        bindSettingsSwitch(switchSound);
        bindSettingsSwitch(switchVibration);
        bindSettingsSwitch(switchHints);

        // Скрытая кнопка "заполнить Firestore кейсами":
        // долгий тап по email (ничего в XML менять не нужно)
        if (AppConfig.ENABLE_SEED_CASES) {
            tvUserEmail.setOnLongClickListener(v -> {
                vm.seedCasesToFirestore();
                return true;
            });
        }

        vm.getProfileState().observe(this, st -> {
            boolean loading = st instanceof UiState.Loading;

            btnLogout.setEnabled(!loading);
            btnResetProgress.setEnabled(!loading);
            btnEditProfile.setEnabled(!loading);
            btnBackToCases.setEnabled(!loading);

            if (st instanceof UiState.Content) {
                ProfileViewModel.ProfileUi ui = ((UiState.Content<ProfileViewModel.ProfileUi>) st).data;
                tvUserName.setText(ui != null && ui.name != null ? ui.name : "Игрок");
                tvUserEmail.setText(ui != null && ui.email != null ? ui.email : "");
            }
        });

        vm.getToastEvent().observe(this, msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        );

        vm.getNavEvent().observe(this, nav -> {
            if (nav == null) return;

            if ("back".equals(nav.destination)) {
                finish();
                return;
            }

            if ("main".equals(nav.destination)) {
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

        vm.load();
    }

    private void bindSettingsSwitch(Switch sw) {
        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (lockSwitchCallback) return;
            vm.onSettingsToggle();
            lockSwitchCallback = true;
            sw.setChecked(!isChecked);
            lockSwitchCallback = false;
        });
    }

    private void showChangeNameDialog() {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        input.setHint("Введите имя");

        new AlertDialog.Builder(this)
                .setTitle("Изменить имя")
                .setView(input)
                .setNegativeButton("Отмена", (d, w) -> d.dismiss())
                .setPositiveButton("Сохранить", (d, w) -> {
                    String name = input.getText() != null ? input.getText().toString() : "";
                    vm.onChangeName(name);
                })
                .show();
    }

    private static class VmFactory implements ViewModelProvider.Factory {
        @SuppressWarnings("unchecked")
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
                return (T) new ProfileViewModel(
                        ServiceLocator.get().authRepository(),
                        ServiceLocator.get().userRepository(),
                        ServiceLocator.get().casesRepository(),
                        ServiceLocator.get().caseContentRepository()
                );
            }
            throw new IllegalArgumentException("Unknown ViewModel: " + modelClass);
        }
    }
}
