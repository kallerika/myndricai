package com.example.myndricai.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myndricai.R;
import com.example.myndricai.common.result.UiState;
import com.example.myndricai.di.ServiceLocator;
import com.example.myndricai.ui.cases.CasesActivity;
import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {

    private AuthViewModel vm;

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;

    private MaterialButton btnRegister;
    private TextView tvLoginLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        tvLoginLink = findViewById(R.id.tvLoginLink);

        vm = new ViewModelProvider(this, new VmFactory()).get(AuthViewModel.class);

        btnRegister.setOnClickListener(v -> vm.register(
                etName.getText() != null ? etName.getText().toString() : "",
                etEmail.getText() != null ? etEmail.getText().toString() : "",
                etPassword.getText() != null ? etPassword.getText().toString() : "",
                etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString() : ""
        ));

        tvLoginLink.setOnClickListener(v -> vm.openLogin());

        vm.getState().observe(this, st -> {
            boolean loading = st instanceof UiState.Loading;
            btnRegister.setEnabled(!loading);
            tvLoginLink.setEnabled(!loading);

            etName.setEnabled(!loading);
            etEmail.setEnabled(!loading);
            etPassword.setEnabled(!loading);
            etConfirmPassword.setEnabled(!loading);
        });

        vm.getToastEvent().observe(this, msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        );

        vm.getNavEvent().observe(this, nav -> {
            if (nav == null) return;

            if ("cases".equals(nav.destination)) {
                startActivity(new Intent(this, CasesActivity.class));
                finish();
                return;
            }

            if ("login".equals(nav.destination)) {
                // Возврат на авторизацию
                finish();
            }
        });
    }

    private static class VmFactory implements ViewModelProvider.Factory {
        @SuppressWarnings("unchecked")
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(AuthViewModel.class)) {
                return (T) new AuthViewModel(
                        ServiceLocator.get().authRepository(),
                        ServiceLocator.get().userRepository()
                );
            }
            throw new IllegalArgumentException("Unknown ViewModel: " + modelClass);
        }
    }
}
