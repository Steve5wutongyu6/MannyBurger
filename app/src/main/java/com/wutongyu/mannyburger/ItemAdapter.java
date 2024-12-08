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
    private final List<Item> itemList;
    private final List<Item> selectedItems;

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

    // 获取商品名称和价格数据
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.productNameTextView.setText(item.getName() + " - " + item.getPrice() + "元");
        holder.checkBox.setChecked(selectedItems.contains(item));
        holder.itemView.setOnClickListener(v -> toggleSelection(item));
    }

    // 用户选定商品时事件，将商品添加到或从列表中删除
    private void toggleSelection(Item item) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
        } else {
            selectedItems.add(item);
        }
    }

    // 商品数量计数器
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // 将选定商品存入列表后返回
    public List<Item> getSelectedItems() {
        return selectedItems;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        CheckBox checkBox;
        // 绑定控件
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(android.R.id.text1);
            checkBox = itemView.findViewById(android.R.id.checkbox);
        }
    }
}
