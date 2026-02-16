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

public class LoginActivity extends AppCompatActivity {

    private AuthViewModel vm;

    private EditText etEmail;
    private EditText etPassword;
    private MaterialButton btnLogin;
    private TextView tvRegisterLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);

        vm = new ViewModelProvider(this, new VmFactory()).get(AuthViewModel.class);

        btnLogin.setOnClickListener(v ->
                vm.login(
                        etEmail.getText() != null ? etEmail.getText().toString() : "",
                        etPassword.getText() != null ? etPassword.getText().toString() : ""
                )
        );

        tvRegisterLink.setOnClickListener(v -> vm.openRegister());

        vm.getState().observe(this, st -> {
            boolean loading = st instanceof UiState.Loading;
            btnLogin.setEnabled(!loading);
            etEmail.setEnabled(!loading);
            etPassword.setEnabled(!loading);
            tvRegisterLink.setEnabled(!loading);
        });

        vm.getToastEvent().observe(this, msg ->
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        );

        vm.getNavEvent().observe(this, nav -> {
            if (nav == null) return;

            if ("cases".equals(nav.destination)) {
                Intent i = new Intent(this, CasesActivity.class);
                startActivity(i);
                finish();
                return;
            }

            if ("register".equals(nav.destination)) {
                startActivity(new Intent(this, RegisterActivity.class));
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
