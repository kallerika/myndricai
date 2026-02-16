package com.example.myndricai.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myndricai.common.result.UiState;
import com.example.myndricai.common.util.SingleLiveEvent;
import com.example.myndricai.common.util.Validation;
import com.example.myndricai.data.repository.AuthRepository;
import com.example.myndricai.data.repository.CaseContentRepository;
import com.example.myndricai.data.repository.CasesRepository;
import com.example.myndricai.data.repository.UserRepository;
import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends ViewModel {

    public static class ProfileUi {
        public String name;
        public String email;
    }

    public static class Navigation {
        public final String destination; // "back" | "main"
        private Navigation(String d) { destination = d; }
        public static Navigation back() { return new Navigation("back"); }
        public static Navigation toMain() { return new Navigation("main"); }
    }

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final CasesRepository casesRepository;
    private final CaseContentRepository caseContentRepository;

    private final MutableLiveData<UiState<ProfileUi>> profileState =
            new MutableLiveData<>(new UiState.Idle<>());

    private final SingleLiveEvent<String> toastEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Navigation> navEvent = new SingleLiveEvent<>();

    public ProfileViewModel(AuthRepository authRepository,
                            UserRepository userRepository,
                            CasesRepository casesRepository,
                            CaseContentRepository caseContentRepository) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.casesRepository = casesRepository;
        this.caseContentRepository = caseContentRepository;
    }

    public LiveData<UiState<ProfileUi>> getProfileState() {
        return profileState;
    }

    public SingleLiveEvent<String> getToastEvent() {
        return toastEvent;
    }

    public SingleLiveEvent<Navigation> getNavEvent() {
        return navEvent;
    }

    public void load() {
        profileState.setValue(new UiState.Loading<>());

        FirebaseUser u = authRepository.currentUser();
        String email = u != null ? u.getEmail() : "";

        userRepository.loadProfile()
                .addOnSuccessListener(p -> {
                    ProfileUi ui = new ProfileUi();
                    ui.email = email != null ? email : "";
                    ui.name = (p != null && p.displayName != null && !p.displayName.trim().isEmpty())
                            ? p.displayName
                            : "Игрок";
                    profileState.setValue(new UiState.Content<>(ui));
                })
                .addOnFailureListener(e -> {
                    ProfileUi ui = new ProfileUi();
                    ui.email = email != null ? email : "";
                    ui.name = "Игрок";
                    profileState.setValue(new UiState.Content<>(ui));
                });
    }

    public void onBack() {
        navEvent.setValue(Navigation.back());
    }

    public void onLogout() {
        authRepository.signOut();
        navEvent.setValue(Navigation.toMain());
    }

    public void onSettingsToggle() {
        toastEvent.setValue("Эта функция недоступна и появится в следующей версии");
    }

    public void onResetProgress() {
        profileState.setValue(new UiState.Loading<>());
        casesRepository.resetAllProgress()
                .addOnSuccessListener(v -> {
                    toastEvent.setValue("Прогресс сброшен");
                    load();
                })
                .addOnFailureListener(e -> {
                    toastEvent.setValue("Не удалось сбросить прогресс");
                    load();
                });
    }

    public void onChangeName(String newName) {
        String err = Validation.validateDisplayName(newName);
        if (err != null) {
            toastEvent.setValue(err);
            return;
        }

        profileState.setValue(new UiState.Loading<>());

        userRepository.updateDisplayName(newName.trim())
                .addOnSuccessListener(v -> {
                    toastEvent.setValue("Имя обновлено");
                    load();
                })
                .addOnFailureListener(e -> {
                    toastEvent.setValue("Не удалось изменить имя");
                    load();
                });
    }

    /**
     * Заполняет Firestore локальными кейсами в коллекцию `cases`,
     * чтобы можно было показать наполненную базу в Firebase Console.
     */
    public void seedCasesToFirestore() {
        profileState.setValue(new UiState.Loading<>());

        caseContentRepository.seedCasesToFirestore()
                .addOnSuccessListener(v -> {
                    toastEvent.setValue("Кейсы записаны в Firestore (коллекция cases)");
                    load();
                })
                .addOnFailureListener(e -> {
                    toastEvent.setValue("Не удалось записать кейсы в Firestore");
                    load();
                });
    }
}
