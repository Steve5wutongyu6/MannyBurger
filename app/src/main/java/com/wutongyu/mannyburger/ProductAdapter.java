package com.wutongyu.mannyburger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends BaseAdapter {

    private final Context context;
    private final List<Product> productList;
    private final List<Product> selectedProducts;
    private final TextView bottomCartTextView;
    private double totalPrice;
    private OnTotalPriceChangedListener totalPriceChangedListener;

    public ProductAdapter(Context context, List<Product> productList, List<Product> selectedProducts, TextView bottomCartTextView) {
        this.context = context;
        this.productList = productList;
        this.selectedProducts = selectedProducts;
        this.bottomCartTextView = bottomCartTextView;
    }


    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_list_item, parent, false);
            holder = new ViewHolder();
            holder.productNameTextView = convertView.findViewById(R.id.productNameTextView);
            holder.checkBox = convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = productList.get(position);
        holder.productNameTextView.setText(product.getName() + " - ￥" + product.getPrice());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectedProducts.contains(product));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedProducts.add(product);
            } else {
                selectedProducts.remove(product);
            }
            updateTotalPrice();
        });

        return convertView;
    }

    // 设置监听器setOnTotalPriceChangedListener，用于更新MainActivity购物车总金额
    public void setOnTotalPriceChangedListener(OnTotalPriceChangedListener listener) {
        this.totalPriceChangedListener = listener;
    }

    // 用于更新MainActivity购物车总金额
    private void updateTotalPrice() {
        totalPrice = 0.0;
        for (Product product : selectedProducts) {
            totalPrice += product.getPrice();
        }
        bottomCartTextView.setText("购物车：￥" + totalPrice);
        if (totalPriceChangedListener != null) {
            totalPriceChangedListener.onTotalPriceChanged(totalPrice);
        }
    }

    // 回调接口，用于更新OrderActivity用户订单总金额（这是个补丁，改ProductAdapter出问题了导致用户订单总金额获取不到数据）
    public interface OnTotalPriceChangedListener {
        void onTotalPriceChanged(double totalPrice);
    }

    static class ViewHolder {
        TextView productNameTextView;
        CheckBox checkBox;
    }
}
