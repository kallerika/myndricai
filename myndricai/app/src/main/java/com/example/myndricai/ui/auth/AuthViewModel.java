package com.example.myndricai.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myndricai.common.result.UiState;
import com.example.myndricai.common.util.SingleLiveEvent;
import com.example.myndricai.common.util.Validation;
import com.example.myndricai.data.repository.AuthRepository;
import com.example.myndricai.data.repository.UserRepository;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {

    public static class Navigation {
        public final String destination; // "cases" | "login" | "register"

        private Navigation(String destination) {
            this.destination = destination;
        }

        public static Navigation toCases() { return new Navigation("cases"); }
        public static Navigation toLogin() { return new Navigation("login"); }
        public static Navigation toRegister() { return new Navigation("register"); }
    }

    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    private final MutableLiveData<UiState<Void>> state = new MutableLiveData<>(new UiState.Idle<>());
    private final SingleLiveEvent<String> toastEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Navigation> navEvent = new SingleLiveEvent<>();

    public AuthViewModel(AuthRepository authRepository, UserRepository userRepository) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
    }

    public LiveData<UiState<Void>> getState() {
        return state;
    }

    public SingleLiveEvent<String> getToastEvent() {
        return toastEvent;
    }

    public SingleLiveEvent<Navigation> getNavEvent() {
        return navEvent;
    }

    public void openRegister() {
        navEvent.setValue(Navigation.toRegister());
    }

    public void openLogin() {
        navEvent.setValue(Navigation.toLogin());
    }

    public void login(String email, String password) {
        String e1 = Validation.validateEmail(email);
        if (e1 != null) { toastEvent.setValue(e1); return; }

        String e2 = Validation.validatePassword(password);
        if (e2 != null) { toastEvent.setValue(e2); return; }

        state.setValue(new UiState.Loading<>());

        authRepository.signIn(email.trim(), password)
                .addOnSuccessListener(r -> {
                    state.setValue(new UiState.Content<>(null));
                    navEvent.setValue(Navigation.toCases());
                })
                .addOnFailureListener(ex -> {
                    state.setValue(new UiState.Error<>("Ошибка входа", ex));
                    toastEvent.setValue("Не удалось войти. Проверьте данные.");
                });
    }

    public void register(String name, String email, String password, String confirmPassword) {
        String e0 = Validation.validateDisplayName(name);
        if (e0 != null) { toastEvent.setValue(e0); return; }

        String e1 = Validation.validateEmail(email);
        if (e1 != null) { toastEvent.setValue(e1); return; }

        String e2 = Validation.validatePassword(password);
        if (e2 != null) { toastEvent.setValue(e2); return; }

        if (confirmPassword == null || !password.equals(confirmPassword)) {
            toastEvent.setValue("Пароли не совпадают");
            return;
        }

        state.setValue(new UiState.Loading<>());

        authRepository.register(email.trim(), password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser u = authRepository.currentUser();
                    if (u == null) {
                        state.setValue(new UiState.Error<>("Ошибка регистрации", null));
                        toastEvent.setValue("Не удалось создать аккаунт");
                        return;
                    }

                    userRepository.createUserIfNeeded(u.getUid(), name.trim())
                            .addOnSuccessListener(v -> {
                                state.setValue(new UiState.Content<>(null));
                                navEvent.setValue(Navigation.toCases());
                            })
                            .addOnFailureListener(ex -> {
                                state.setValue(new UiState.Error<>("Ошибка сохранения профиля", ex));
                                toastEvent.setValue("Аккаунт создан, но не удалось сохранить профиль");
                                navEvent.setValue(Navigation.toCases());
                            });
                })
                .addOnFailureListener(ex -> {
                    state.setValue(new UiState.Error<>("Ошибка регистрации", ex));
                    toastEvent.setValue("Не удалось зарегистрироваться. Проверьте данные.");
                });
    }
}
