package com.wutongyu.mannyburger;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;
    private List<Item> selectedItems;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
        this.selectedItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.productNameTextView.setText(item.getName() + " - " + item.getPrice() + "元");
        holder.checkBox.setChecked(selectedItems.contains(item));
        holder.itemView.setOnClickListener(v -> toggleSelection(item));
    }

    private void toggleSelection(Item item) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
        } else {
            selectedItems.add(item);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public List<Item> getSelectedItems() {
        return selectedItems;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        CheckBox checkBox;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(android.R.id.text1);
            checkBox = itemView.findViewById(android.R.id.checkbox);
        }
    }
}
