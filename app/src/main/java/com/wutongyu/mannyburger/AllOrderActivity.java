package com.wutongyu.mannyburger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AllOrderActivity extends AppCompatActivity {

    private RecyclerView orderRecyclerView;
    private Button copyButton;
    private OrderAdapter orderAdapter;
    private OrderDatabaseHelper dbHelper;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_order);

        // Initialize views
        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        copyButton = findViewById(R.id.copyButton);

        // Initialize database helper
        dbHelper = new OrderDatabaseHelper(this);

        // Load orders from database
        loadOrders();

        // Set up RecyclerView
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter(orderList);
        orderRecyclerView.setAdapter(orderAdapter);

        // Copy Button Click Listener
        copyButton.setOnClickListener(v -> {
            if (!orderList.isEmpty()) {
                Order lastOrder = orderList.get(orderList.size() - 1);
                dbHelper.insertOrder(lastOrder.getItems(), lastOrder.getNote(), lastOrder.getTotalPrice());
                Toast.makeText(AllOrderActivity.this, "订单创建成功", Toast.LENGTH_SHORT).show();
                loadOrders(); // Refresh the list
            } else {
                Toast.makeText(AllOrderActivity.this, "没有可复制的订单", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadOrders() {
        orderList = dbHelper.getAllOrders();
        if (orderAdapter != null) {
            orderAdapter.setOrders(orderList);
        }
    }

    static class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
        private List<Order> orderList;

        public OrderAdapter(List<Order> orderList) {
            this.orderList = orderList;
        }

        public void setOrders(List<Order> orderList) {
            this.orderList = orderList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
            return new OrderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
            Order order = orderList.get(position);
            holder.itemsTextView.setText(order.getItems());
            holder.noteTextView.setText("备注: " + order.getNote());
            holder.totalPriceTextView.setText("总价: ￥" + order.getTotalPrice());
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }

        static class OrderViewHolder extends RecyclerView.ViewHolder {
            TextView itemsTextView;
            TextView noteTextView;
            TextView totalPriceTextView;

            public OrderViewHolder(@NonNull View itemView) {
                super(itemView);
                itemsTextView = itemView.findViewById(R.id.itemsTextView);
                noteTextView = itemView.findViewById(R.id.noteTextView);
                totalPriceTextView = itemView.findViewById(R.id.totalPriceTextView);
            }
        }
    }
}