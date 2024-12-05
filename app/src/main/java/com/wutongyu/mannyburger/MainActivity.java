package com.wutongyu.mannyburger;

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
}