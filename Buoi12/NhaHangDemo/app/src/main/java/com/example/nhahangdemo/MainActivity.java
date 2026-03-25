package com.example.nhahangdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nhahangdemo.DatabaseHelper;
import com.example.nhahangdemo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    ListView listView;
    FloatingActionButton fab;
    DatabaseHelper db; // You'll need to create this class
    NhaHangAdapter adapter;
    List<NhaHang> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 1. Initialize Views
        editText = findViewById(R.id.editTextText);
        listView = findViewById(R.id.listView);
        fab = findViewById(R.id.floatingActionButton);
        db = new DatabaseHelper(this);

        // 2. Initialize Database
        loadData();

        // 3. Handle Add Button (Requirement 5)
        fab.setOnClickListener(v -> {
            db.addNhaHang(new NhaHang("Sen Tây Hồ", "514 Lạc Long Quân", 8.6));
            db.addNhaHang(new NhaHang("Nón Lá", "Nguyễn Đình Chiểu", 8.8));
            db.addNhaHang(new NhaHang("Quán Ngon Hà Nội", "Phan Bội Châu", 8.9));
            db.addNhaHang(new NhaHang("Lục Thủy", "Lê Thái Tổ", 8.5));
            db.addNhaHang(new NhaHang("Charm Charm", "Phan Văn Chương", 8.2));
            db.addNhaHang(new NhaHang("Ly Club", "Lê Phụng Hiểu", 7.8));

            Toast.makeText(this, "Thêm dữ liệu thành công", Toast.LENGTH_SHORT).show();
            loadData(); // Refresh the list
        });

        // 4. Handle Search (Requirement 2 & 7)
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        // 5. Requirement 6: Long Press to Delete
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            NhaHang selected = restaurantList.get(position);

            new AlertDialog.Builder(this)
                    .setTitle("Bạn chắc chưa?")
                    .setMessage("Confirm to delete restaurants with rating lower than " + selected.getDanhGia() + "?")
                    .setPositiveButton("OK", (dialog, which) -> {
                        db.deleteLowerRated(selected.getDanhGia());
                        loadData(); // Refresh list after delete
                    })
                    .setNegativeButton("CANCEL", null)
                    .show();
            return true;
        });

        // Handle Edge-to-Edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadData() {
        restaurantList = db.getAllNhaHang();
        adapter = new NhaHangAdapter(this, restaurantList);
        listView.setAdapter(adapter);
    }

    private void filterData(String query) {
        List<NhaHang> filteredList = new ArrayList<>();
        for (NhaHang item : restaurantList) {
            if (item.getTen().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter = new NhaHangAdapter(this, filteredList);
        listView.setAdapter(adapter);
    }

    // Requirement 8: Broadcast Receiver for Network
    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
                Toast.makeText(context, "Mất kết nối mạng", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkReceiver);
    }
}