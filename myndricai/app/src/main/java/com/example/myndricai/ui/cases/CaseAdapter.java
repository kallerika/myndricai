package com.example.myndricai.ui.cases;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myndricai.R;
import com.example.myndricai.repo.CaseRepository;

import java.util.ArrayList;
import java.util.List;

public class CaseAdapter extends RecyclerView.Adapter<CaseAdapter.VH> {

    public interface Listener {
        void onClick(CaseUiModel item);
    }

    private final Listener listener;
    private final List<CaseUiModel> items = new ArrayList<>();

    public CaseAdapter(Listener listener) {
        this.listener = listener;
    }

    public void submit(List<CaseUiModel> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_case, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        CaseUiModel it = items.get(position);
        h.tvTitle.setText(it.title);
        h.tvObjective.setText(it.subtitle);
        h.tvLastDate.setText("—");

        String st;
        if (it.status == CaseRepository.STATUS_COMPLETED) st = "Завершен";
        else if (it.status == CaseRepository.STATUS_IN_PROGRESS) st = "В процессе";
        else st = "Не начат";
        h.tvStatus.setText(st);

        h.itemView.setOnClickListener(v -> listener.onClick(it));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvObjective;
        TextView tvStatus;
        TextView tvLastDate;

        VH(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvCaseTitle);
            tvObjective = itemView.findViewById(R.id.tvObjective);
            tvStatus = itemView.findViewById(R.id.tvStatusValue);
            tvLastDate = itemView.findViewById(R.id.tvLastDate);
        }
    }
}
