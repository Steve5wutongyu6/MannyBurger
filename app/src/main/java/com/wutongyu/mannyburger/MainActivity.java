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

        // 绑定控件
        menuItemsListView = findViewById(R.id.menuItems);
        categoriesRadioGroup = findViewById(R.id.categories);
        orderingRadioButton = findViewById(R.id.ordering);
        shopRadioButton = findViewById(R.id.shop);
        allOrderButton = findViewById(R.id.AllOrderButton);
        submitOrderButton = findViewById(R.id.SubmitOrderButton);
        bottomCartTextView = findViewById(R.id.bottomCartTextView);

        // 初始化数据
        ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(this);
        productList = loadProductsFromDatabase(dbHelper.getWritableDatabase());
        selectedProducts = new ArrayList<>();

        // 绑定适配器productAdapter
        productAdapter = new ProductAdapter(this, productList, selectedProducts, bottomCartTextView);
        menuItemsListView.setAdapter(productAdapter);


        // 监听单选按钮
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

        // 监听All Order button
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


        // 提交按钮相关
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

    // 从数据库加载商品数据
    private List<Product> loadProductsFromDatabase(SQLiteDatabase db) {
        List<Product> products = new ArrayList<>();// 创建一个空列表
        String table = ProductDatabaseHelper.getTableProducts(); // 使用公共方法获取表名

        // 查询数据库中的所有商品数据
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


}