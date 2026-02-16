package com.example.myndricai.ui.cases;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myndricai.R;

import java.util.ArrayList;
import java.util.List;

public class CaseAdapter extends RecyclerView.Adapter<CaseAdapter.CaseVH> {

    public interface OnCaseClickListener {
        void onCaseClick(CaseUiModel item);
    }

    private final List<CaseUiModel> items = new ArrayList<>();
    private final OnCaseClickListener listener;

    public CaseAdapter(OnCaseClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<CaseUiModel> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CaseVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_case, parent, false);
        return new CaseVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CaseVH holder, int position) {
        CaseUiModel item = items.get(position);

        holder.tvCaseTitle.setText(item.title != null ? item.title : "");
        holder.tvObjective.setText(item.objective != null ? item.objective : "");
        holder.tvStatusValue.setText(item.statusText != null ? item.statusText : "Не начат");
        holder.tvLastDate.setText(item.lastDateText != null ? item.lastDateText : "-");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onCaseClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CaseVH extends RecyclerView.ViewHolder {

        TextView tvCaseTitle;
        TextView tvObjective;
        TextView tvStatusValue;
        TextView tvLastDate;

        CaseVH(@NonNull View itemView) {
            super(itemView);
            tvCaseTitle = itemView.findViewById(R.id.tvCaseTitle);
            tvObjective = itemView.findViewById(R.id.tvObjective);
            tvStatusValue = itemView.findViewById(R.id.tvStatusValue);
            tvLastDate = itemView.findViewById(R.id.tvLastDate);
        }
    }
}
