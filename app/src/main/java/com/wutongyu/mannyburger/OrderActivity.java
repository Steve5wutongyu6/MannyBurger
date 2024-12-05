package com.wutongyu.mannyburger;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView productRecyclerView;
    private EditText noteEditText;
    private TextView totalPriceTextView;
    private Button submitOrderButton;
    private ProductAdapter productAdapter;
    private List<String> productList;
    private Double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Initialize views
        productRecyclerView = findViewById(R.id.productRecyclerView);
        noteEditText = findViewById(R.id.note);
        totalPriceTextView = findViewById(R.id.totalPrice);
        submitOrderButton = findViewById(R.id.submitOrder);

        // Get data from MainActivity
        Intent intent = getIntent();
        String orderDetails = intent.getStringExtra("order_details");
        totalPrice = intent.getDoubleExtra("total_price", 0.0);

        // Set total price text
        totalPriceTextView.setText("合计 ￥" + totalPrice);

        // Parse order details into a list of products
        productList = parseOrderDetails(orderDetails);

        // Set up RecyclerView
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(productList);
        productRecyclerView.setAdapter(productAdapter);

        // Submit Order Button Click Listener
        submitOrderButton.setOnClickListener(v -> {
            String note = noteEditText.getText().toString();
            saveOrderToDatabase(note);
            Toast.makeText(OrderActivity.this, "支付成功！总价：￥" + totalPrice, Toast.LENGTH_SHORT).show();
        });
    }

    private List<String> parseOrderDetails(String orderDetails) {
        List<String> products = new ArrayList<>();
        if (orderDetails != null && !orderDetails.isEmpty()) {
            String[] lines = orderDetails.split("\n");
            for (String line : lines) {
                products.add(line.trim());
            }
        }
        return products;
    }

    private void saveOrderToDatabase(String note) {
        OrderDatabaseHelper dbHelper = new OrderDatabaseHelper(this);
        StringBuilder itemsStringBuilder = new StringBuilder();
        for (String item : productList) {
            itemsStringBuilder.append(item).append("\n");
        }
        String items = itemsStringBuilder.toString().trim();
        dbHelper.insertOrder(items, note, totalPrice);
    }

    static class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
        private List<String> productList;

        public ProductAdapter(List<String> productList) {
            this.productList = productList;
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            holder.productNameTextView.setText(productList.get(position));
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        static class ProductViewHolder extends RecyclerView.ViewHolder {
            TextView productNameTextView;

            public ProductViewHolder(@NonNull View itemView) {
                super(itemView);
                productNameTextView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}