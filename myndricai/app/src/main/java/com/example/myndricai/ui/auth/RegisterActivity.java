package com.example.myndricai.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myndricai.MyApp;
import com.example.myndricai.R;
import com.example.myndricai.ui.cases.CasesActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnRegister;
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

        btnRegister.setOnClickListener(v -> doRegister());
        tvLoginLink.setOnClickListener(v -> finish());
    }

    private void doRegister() {
        String name = etName.getText() != null ? etName.getText().toString() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString() : "";
        String pass = etPassword.getText() != null ? etPassword.getText().toString() : "";
        String pass2 = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString() : "";

        if (!pass.equals(pass2)) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            long uid = MyApp.get().users().register(name, email, pass);
            MyApp.get().session().setUserId(uid);
            startActivity(new Intent(this, CasesActivity.class));
            finish();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
