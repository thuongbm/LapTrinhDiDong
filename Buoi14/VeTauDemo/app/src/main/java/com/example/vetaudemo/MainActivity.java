package com.example.vetaudemo;

import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    // 1. Khai báo biến phải nằm bên trong Class này
    private ListView lvVeTau;
    private VeTauAdapter adapter;
    private List<VeTau> dataList;
    private MyDatabaseHelper dbHelper;
    private SearchView searchView;
    private WifiReceiver wifiReceiver; // Đã di chuyển vào đúng chỗ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo receiver
        wifiReceiver = new WifiReceiver();

        // Ánh xạ View
        lvVeTau = findViewById(R.id.lvVeTau);
        searchView = findViewById(R.id.searchView);
        dbHelper = new MyDatabaseHelper(this);

        // Chuẩn bị dữ liệu và hiển thị
        prepareData();
        dataList = dbHelper.getAllVeTau();
        adapter = new VeTauAdapter(this, dataList);
        lvVeTau.setAdapter(adapter);

        // Xử lý tìm kiếm
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        // Đăng ký Context Menu
        registerForContextMenu(lvVeTau);

        // Thống kê ban đầu
        hienThiThongKe();
    }

    // --- CÂU 6: ĐĂNG KÝ BROADCAST RECEIVER ---
    @Override
    protected void onStart() {
        super.onStart();
        // Lắng nghe sự kiện Wifi thay đổi
        IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(wifiReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Hủy đăng ký khi app đóng để tiết kiệm pin
        unregisterReceiver(wifiReceiver);
    }

    // --- XỬ LÝ CONTEXT MENU (SỬA/XÓA) ---
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Chọn thao tác");
        menu.add(0, 1, 0, "Sửa");
        menu.add(0, 2, 1, "Xóa");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        VeTau veSelected = (VeTau) adapter.getItem(position);

        if (item.getItemId() == 1) {
            showEditDialog(veSelected);
        } else if (item.getItemId() == 2) {
            dbHelper.deleteVeTau(veSelected.getMaVe());
            refreshData();
            Toast.makeText(this, "Đã xóa vé", Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }

    // --- HÀM DIALOG SỬA ---
    private void showEditDialog(final VeTau ve) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_edit, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();

        final EditText edtGaDi = view.findViewById(R.id.edtGaDi);
        final EditText edtGaDen = view.findViewById(R.id.edtGaDen);
        final EditText edtGia = view.findViewById(R.id.edtGia);
        final RadioButton rbKhuHoi = view.findViewById(R.id.rbKhuHoi);
        Button btnSua = view.findViewById(R.id.btnSua);
        Button btnQuayVe = view.findViewById(R.id.btnQuayVe);

        edtGaDi.setText(ve.getGaDi());
        edtGaDen.setText(ve.getGaDen());
        edtGia.setText(String.valueOf(ve.getDonGia()));
        if (ve.isLoaiVe()) rbKhuHoi.setChecked(true);

        btnSua.setOnClickListener(v -> {
            try {
                ve.setGaDi(edtGaDi.getText().toString());
                ve.setGaDen(edtGaDen.getText().toString());
                ve.setDonGia(Float.parseFloat(edtGia.getText().toString()));
                ve.setLoaiVe(rbKhuHoi.isChecked());

                dbHelper.updateVeTau(ve);
                refreshData();
                dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi định dạng giá!", Toast.LENGTH_SHORT).show();
            }
        });

        btnQuayVe.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    // --- CÁC HÀM HỖ TRỢ ---
    private void refreshData() {
        dataList.clear();
        dataList.addAll(dbHelper.getAllVeTau());
        adapter.notifyDataSetChanged();
        hienThiThongKe();
    }

    private void hienThiThongKe() {
        String thongKe = dbHelper.getThongKe();
        Toast.makeText(this, thongKe, Toast.LENGTH_LONG).show();
    }

    private void prepareData() {
        if (dbHelper.getCount() == 0) {
            dbHelper.addVeTau(new VeTau(1, "Vinh", "Nam Định", 1268.915f, true));
            dbHelper.addVeTau(new VeTau(2, "Nam Định", "Thanh Hóa", 857.375f, true));
            dbHelper.addVeTau(new VeTau(3, "Hà Nội", "Nam Định", 473.271f, true));
            dbHelper.addVeTau(new VeTau(4, "Thanh Hóa", "Hà Nội", 170.000f, false));
            dbHelper.addVeTau(new VeTau(5, "Hà Nội", "Thanh Hóa", 170.000f, false));
        }
    }
}