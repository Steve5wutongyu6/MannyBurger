package com.wutongyu.mannyburger;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView menuItemsListView;
    private RadioGroup categoriesRadioGroup;
    private RadioButton orderingRadioButton;
    private RadioButton shopRadioButton;
    private Button allOrderButton;
    private Button submitOrderButton;
    private TextView bottomCartTextView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<Product> selectedProducts;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        menuItemsListView = findViewById(R.id.menuItems);
        categoriesRadioGroup = findViewById(R.id.categories);
        orderingRadioButton = findViewById(R.id.ordering);
        shopRadioButton = findViewById(R.id.shop);
        allOrderButton = findViewById(R.id.AllOrderButton);
        submitOrderButton = findViewById(R.id.SubmitOrderButton);
        bottomCartTextView = findViewById(R.id.bottomCartTextView);

        // Initialize database and load products
        ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(this);
        productList = loadProductsFromDatabase(dbHelper.getWritableDatabase());
        selectedProducts = new ArrayList<>();

        // Set up adapter for ListView
        productAdapter = new ProductAdapter();
        menuItemsListView.setAdapter(productAdapter);

        // Handle category selection
        categoriesRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.ordering) {
                menuItemsListView.setVisibility(View.VISIBLE);
                bottomCartTextView.setVisibility(View.VISIBLE);
                submitOrderButton.setVisibility(View.VISIBLE);
            } else {
                menuItemsListView.setVisibility(View.GONE);
                bottomCartTextView.setVisibility(View.GONE);
                submitOrderButton.setVisibility(View.GONE);
            }
        });

        // Handle All Orders button click
        allOrderButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AllOrderActivity.class);
            startActivity(intent);
        });

        //跳转到评论
        shopRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动 CommentActivity
                Intent intent = new Intent(MainActivity.this, CommentActivity.class);
                startActivity(intent);
            }
        });


        // Handle Submit Order button click
        submitOrderButton.setOnClickListener(v -> {
            StringBuilder orderDetailsBuilder = new StringBuilder();
            for (Product product : selectedProducts) {
                orderDetailsBuilder.append(product.getName()).append(" - ").append(product.getPrice()).append("元\n");
            }
            String orderDetails = orderDetailsBuilder.toString().trim();

            Intent intent = new Intent(MainActivity.this, OrderActivity.class);
            intent.putExtra("order_details", orderDetails);
            intent.putExtra("total_price", totalPrice);
            startActivity(intent);
        });
    }


    private List<Product> loadProductsFromDatabase(SQLiteDatabase db) {
        List<Product> products = new ArrayList<>();
        String table = ProductDatabaseHelper.getTableProducts(); // 使用公共方法获取表名
        Cursor cursor = db.query(table, new String[]{"pid", "pname", "shop_price"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int pid = cursor.getInt(cursor.getColumnIndexOrThrow("pid"));
            String pname = cursor.getString(cursor.getColumnIndexOrThrow("pname"));
            double shopPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("shop_price"));
            products.add(new Product(pid, pname, shopPrice));
        }
        cursor.close();
        return products;
    }

    private class ProductAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_list_item, parent, false);
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

        private void updateTotalPrice() {
            totalPrice = 0.0;
            for (Product product : selectedProducts) {
                totalPrice += product.getPrice();
            }
            bottomCartTextView.setText("购物车：￥" + totalPrice);
        }

        class ViewHolder {
            TextView productNameTextView;
            CheckBox checkBox;
        }
    }

}