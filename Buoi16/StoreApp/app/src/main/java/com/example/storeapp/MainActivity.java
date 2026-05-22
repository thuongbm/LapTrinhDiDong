package com.example.storeapp;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ListView lvProducts;
    private FloatingActionButton fabAdd;
    private EditText etSearch; // Added for Search
    private DatabaseHelper dbHelper;
    private ProductAdapter adapter;
    private ArrayList<Product> productList;
    private NetworkChangeReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Initialize Components
        lvProducts = findViewById(R.id.lvProducts);
        fabAdd = findViewById(R.id.fabAdd);
        etSearch = findViewById(R.id.etSearch); // Initialize Search Bar
        dbHelper = new DatabaseHelper(this);

        // 2. Sample Data
        if (dbHelper.getAllProducts().isEmpty()) {
            dbHelper.addProduct(new Product(0, "Iphone 15 Promax", "15000000", "", true));
            dbHelper.addProduct(new Product(0, "TV Sony 75'", "25000000", "", true));
            dbHelper.addProduct(new Product(0, "Iphone 14", "15000000", "Standard Edition", false));
            dbHelper.addProduct(new Product(0, "Samsung S23", "12000000", "", false));
        }

        loadData();

        // 3. Search Logic (Question 2)
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        fabAdd.setOnClickListener(v -> Toast.makeText(this, "Add product feature", Toast.LENGTH_SHORT).show());
        registerForContextMenu(lvProducts);

        networkReceiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
    }

    private void loadData() {
        productList = dbHelper.getAllProducts();
        for (Product p : productList) {
            if (p.isDiscount()) {
                p.setDetails(calculateDiscount(p.getPrice()));
            }
        }
        adapter = new ProductAdapter(this, productList);
        lvProducts.setAdapter(adapter);
    }

    // Logic for Question 2: Filter items by name
    private void filterList(String text) {
        ArrayList<Product> filteredList = new ArrayList<>();
        for (Product item : productList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.updateData(filteredList);
    }

    private String calculateDiscount(String priceStr) {
        try {
            double price = Double.parseDouble(priceStr);
            return "Giam gia con " + (long) (price * 0.9);
        } catch (Exception e) { return "Error"; }
    }

    // --- CONTEXT MENU ---
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        new MenuInflater(this).inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Product selectedProduct = (Product) lvProducts.getItemAtPosition(info.position);

        if (item.getItemId() == R.id.menu_sort) {
            Collections.sort(productList, (p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
            adapter.notifyDataSetChanged();
            return true;
        } else if (item.getItemId() == R.id.menu_delete) {
            handleDeleteLowerPrices(selectedProduct);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void handleDeleteLowerPrices(Product selected) {
        long selectedPrice = Long.parseLong(selected.getPrice());
        for (int i = productList.size() - 1; i >= 0; i--) {
            long currentPrice = Long.parseLong(productList.get(i).getPrice());
            if (currentPrice < selectedPrice) {
                dbHelper.deleteProduct(productList.get(i).getId());
                productList.remove(i);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkReceiver != null) unregisterReceiver(networkReceiver);
    }
}