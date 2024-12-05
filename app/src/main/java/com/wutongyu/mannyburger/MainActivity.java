package com.wutongyu.mannyburger;

/*
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RadioGroup categories;
    private ListView menuItems;
    private LinearLayout bottomCart;
    private Button allOrderButton, submitOrderButton;
    private TextView cartPriceTextView;
    private List<Item> itemList;
    private List<Item> selectedItems;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        categories = findViewById(R.id.categories);
        menuItems = findViewById(R.id.menuItems);
        bottomCart = findViewById(R.id.bottomCart);
        allOrderButton = findViewById(R.id.AllOrderButton);
        submitOrderButton = findViewById(R.id.SubmitOrderButton);
        cartPriceTextView = findViewById(R.id.cartPriceTextView);

        // Sample data for menu items
        itemList = new ArrayList<>();
        itemList.add(new Item("老北京炸酱面", 15));
        itemList.add(new Item("牛肉汉堡", 20));
        itemList.add(new Item("薯条", 10));

        // Adapter for ListView
        itemAdapter = new ItemAdapter(this, itemList);
        menuItems.setAdapter(itemAdapter);

        // Selected items list
        selectedItems = new ArrayList<>();

        // Set up radio button listener
        categories.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.ordering) {
                // Show ordering tab
                menuItems.setVisibility(View.VISIBLE);
                bottomCart.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.shop) {
                // Show shop tab
                menuItems.setVisibility(View.GONE);
                bottomCart.setVisibility(View.GONE);
            }
        });

        // Set up list item click listener
        menuItems.setOnItemClickListener((parent, view, position, id) -> {
            Item item = itemList.get(position);
            if (!selectedItems.contains(item)) {
                selectedItems.add(item);
            } else {
                selectedItems.remove(item);
            }
            updateCartPrice();
        });

        // All Order Button Click Listener
        allOrderButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AllOrderActivity.class);
            startActivity(intent);
        });

        // Submit Order Button Click Listener
        submitOrderButton.setOnClickListener(v -> {
            double totalPrice = 0;
            StringBuilder orderDetails = new StringBuilder();

            for (Item item : selectedItems) {
                totalPrice += item.getPrice();
                orderDetails.append(item.getName()).append(" - ").append(item.getPrice()).append("元\n");
            }

            Intent intent = new Intent(MainActivity.this, OrderActivity.class);
            intent.putExtra("order_details", orderDetails.toString());
            intent.putExtra("total_price", totalPrice);
            startActivity(intent);
        });
    }

    private void updateCartPrice() {
        double totalPrice = 0;
        for (Item item : selectedItems) {
            totalPrice += item.getPrice();
        }
        cartPriceTextView.setText("购物车：" + totalPrice + "元");
    }

    static class Item {
        private String name;
        private double price;

        public Item(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }
    }

    static class ItemAdapter extends ArrayAdapter<Item> {
        public ItemAdapter(MainActivity context, List<Item> items) {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
            }

            CheckBox checkBox = convertView.findViewById(android.R.id.text1);
            Item item = getItem(position);
            if (item != null) {
                checkBox.setText(item.getName() + " - " + item.getPrice() + "元");
            }

            return convertView;
        }
    }
}*/

/*
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example data to pass to OrderActivity
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item("汉堡", 20.0));
        itemList.add(new Item("薯条", 5.0));

        double totalPrice = 25.0;
        StringBuilder orderDetailsBuilder = new StringBuilder();
        for (Item item : itemList) {
            orderDetailsBuilder.append(item.getName()).append(" - ").append(item.getPrice()).append("元\n");
        }
        String orderDetails = orderDetailsBuilder.toString().trim();

        Intent intent = new Intent(MainActivity.this, OrderActivity.class);
        intent.putExtra("order_details", orderDetails);
        intent.putExtra("total_price", totalPrice);
        startActivity(intent);
    }
}*/


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
import android.widget.CompoundButton;
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

/*    private List<Product> loadProductsFromDatabase(SQLiteDatabase db) {
        List<Product> products = new ArrayList<>();
        Cursor cursor = db.query(ProductDatabaseHelper.TABLE_PRODUCTS, new String[]{"pid", "pname", "shop_price"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int pid = cursor.getInt(cursor.getColumnIndexOrThrow("pid"));
            String pname = cursor.getString(cursor.getColumnIndexOrThrow("pname"));
            double shopPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("shop_price"));
            products.add(new Product(pid, pname, shopPrice));
        }
        cursor.close();
        return products;
    }*/

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


    /*private class ProductAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(MainActivity.this).inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
                holder = new ViewHolder();
                holder.productNameTextView = convertView.findViewById(android.R.id.text1);
                holder.checkBox = convertView.findViewById(android.R.id.checkbox);
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

        static class ViewHolder {
            TextView productNameTextView;
            CheckBox checkBox;
        }
    }*/

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