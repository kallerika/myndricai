package com.example.myndricai.repo;

import com.example.myndricai.data.dao.CaseDao;
import com.example.myndricai.data.entity.CaseEntity;

import java.util.ArrayList;
import java.util.List;

public class CaseRepository {

    public static final int STATUS_NEW = 0;
    public static final int STATUS_IN_PROGRESS = 1;
    public static final int STATUS_COMPLETED = 2;

    private final CaseDao dao;

    public CaseRepository(CaseDao dao) {
        this.dao = dao;
    }

    public void seedDefaultsIfNeeded() {
        if (dao.count() > 0) {
            dao.updatePin(1L, "1833");
            return;
        }
        dao.insert(new CaseEntity(
                1L,
                "Дело №1: Исчезновение",
                "Разблокируйте телефон и соберите улики",
                STATUS_NEW,
                "1833",
                "Даша,бариста,19"
        ));
    }


    public List<CaseEntity> getAll() {
        List<CaseEntity> list = dao.getAll();
        return list != null ? list : new ArrayList<>();
    }

    public CaseEntity getById(long id) {
        return dao.getById(id);
    }

    public void setStatus(long id, int status) {
        dao.updateStatus(id, status);
    }

    public boolean allCompleted() {
        int total = dao.count();
        return total > 0 && dao.completedCount() == total;
    }

    public void resetAllProgress() {
        dao.resetAll();
    }
}
