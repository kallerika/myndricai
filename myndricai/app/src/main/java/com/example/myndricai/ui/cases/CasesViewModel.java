package com.example.myndricai.ui.cases;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myndricai.common.result.UiState;
import com.example.myndricai.common.util.SingleLiveEvent;
import com.example.myndricai.data.repository.CaseContentRepository;
import com.example.myndricai.data.repository.CasesRepository;
import com.example.myndricai.domain.model.CaseDefinition;
import com.example.myndricai.domain.model.CaseProgress;
import com.google.android.gms.tasks.Tasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CasesViewModel extends ViewModel {

    public static class Navigation {
        public final String destination; // "profile" | "lock"
        public final String caseId;

        public Navigation(String destination, String caseId) {
            this.destination = destination;
            this.caseId = caseId;
        }

        public static Navigation toProfile() { return new Navigation("profile", null); }
        public static Navigation toLock(String caseId) { return new Navigation("lock", caseId); }
    }

    private final CaseContentRepository contentRepository;
    private final CasesRepository casesRepository;

    private final MutableLiveData<UiState<List<CaseUiModel>>> casesState =
            new MutableLiveData<>(new UiState.Idle<>());

    private final SingleLiveEvent<String> toastEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Navigation> navEvent = new SingleLiveEvent<>();

    private List<CaseUiModel> cachedList = new ArrayList<>();

    public CasesViewModel(CaseContentRepository contentRepository, CasesRepository casesRepository) {
        this.contentRepository = contentRepository;
        this.casesRepository = casesRepository;
    }

    public LiveData<UiState<List<CaseUiModel>>> getCasesState() {
        return casesState;
    }

    public SingleLiveEvent<String> getToastEvent() {
        return toastEvent;
    }

    public SingleLiveEvent<Navigation> getNavEvent() {
        return navEvent;
    }

    public void load() {
        casesState.setValue(new UiState.Loading<>());

        Tasks.whenAllSuccess(
                contentRepository.loadCasesWithFallback(),
                casesRepository.loadProgress()
        ).addOnSuccessListener(results -> {
            @SuppressWarnings("unchecked")
            List<CaseDefinition> defs = (List<CaseDefinition>) results.get(0);
            @SuppressWarnings("unchecked")
            List<CaseProgress> progress = (List<CaseProgress>) results.get(1);

            Map<String, CaseProgress> progressMap = new HashMap<>();
            if (progress != null) {
                for (CaseProgress p : progress) {
                    if (p != null && p.caseId != null) progressMap.put(p.caseId, p);
                }
            }

            List<CaseUiModel> ui = new ArrayList<>();
            if (defs != null) {
                for (CaseDefinition d : defs) {
                    CaseUiModel m = new CaseUiModel();
                    m.caseId = d.caseId;
                    m.title = d.title;
                    m.objective = d.objective;
                    m.requiredKeywords = d.requiredKeywords;

                    CaseProgress p = progressMap.get(d.caseId);
                    if (p == null || p.status == null) {
                        applyStatus(m, CaseProgress.STATUS_NOT_STARTED, 0L);
                    } else {
                        applyStatus(m, p.status, p.updatedAt);
                    }

                    ui.add(m);
                }
            }

            cachedList = ui;
            casesState.setValue(new UiState.Content<>(ui));
        }).addOnFailureListener(e -> {
            casesState.setValue(new UiState.Error<>("Не удалось загрузить кейсы", e));
        });
    }

    private void applyStatus(CaseUiModel m, String status, long updatedAt) {
        m.rawStatus = status;
        m.statusText = CasesRepository.statusToRu(status);
        m.updatedAtMillis = updatedAt;
        m.lastDateText = updatedAt > 0 ? formatDate(updatedAt) : "-";
    }

    private String formatDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", new Locale("ru"));
        return sdf.format(new Date(millis));
    }

    public void onProfileClick() {
        navEvent.setValue(Navigation.toProfile());
    }

    public void onStartNewClick() {
        // Новый кейс = первый "Не начат"
        CaseUiModel next = null;
        for (CaseUiModel m : cachedList) {
            if (CaseProgress.STATUS_NOT_STARTED.equals(m.rawStatus)) {
                next = m;
                break;
            }
        }

        if (next == null) {
            toastEvent.setValue("Сейчас все доступные сюжеты пройдены");
            return;
        }

        CaseUiModel finalNext = next;
        casesRepository.markInProgress(next.caseId)
                .addOnSuccessListener(v -> navEvent.setValue(Navigation.toLock(finalNext.caseId)))
                .addOnFailureListener(e -> toastEvent.setValue("Не удалось начать расследование"));
    }

    public void onCaseClick(@NonNull CaseUiModel item) {
        // Запрет повторного запуска завершённого: по сценарию сброс прогресса в профиле
        if (CaseProgress.STATUS_COMPLETED.equals(item.rawStatus)) {
            toastEvent.setValue("Кейс завершён. Сбросьте прогресс в профиле, чтобы пройти заново.");
            return;
        }

        // not_started / in_progress -> запуск
        casesRepository.markInProgress(item.caseId)
                .addOnSuccessListener(v -> navEvent.setValue(Navigation.toLock(item.caseId)))
                .addOnFailureListener(e -> toastEvent.setValue("Не удалось открыть кейс"));
    }
}
