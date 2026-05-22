package com.example.donhang;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CONTACTS_PERMISSION = 100;

    private ListView lvDonHang;
    private Button btnSapXep;
    private TextView tvTrungBinh;
    private EditText edtSearch;

    private ArrayList<DonHang> listGoc;
    private ArrayList<DonHang> listHienThi;
    private DonHangAdapter adapter;
    private DatabaseHelper dbHelper;

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

        // Ánh xạ các View và đọc CSDL (Các phần trước giữ nguyên)
        lvDonHang = findViewById(R.id.lvDonHang);
        btnSapXep = findViewById(R.id.btnSapXep);
        tvTrungBinh = findViewById(R.id.tvTrungBinh);
        edtSearch = findViewById(R.id.edtSearch);

        dbHelper = new DatabaseHelper(this);
        listGoc = layDanhSachTuDatabase();
        listHienThi = new ArrayList<>(listGoc);

        adapter = new DonHangAdapter(this, R.layout.item_don_hang, listHienThi);
        lvDonHang.setAdapter(adapter);

        // --- KIỂM TRA VÀ XIN QUYỀN TRUY CẬP DANH BẠ RUNTIME ---
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACTS_PERMISSION);
        }

        // --- CÂU 5: XỬ LÝ SỰ KIỆN NHẤN GIỮ LÂU (LONG CLICK) TRÊN LISTVIEW ---
        lvDonHang.setOnItemLongClickListener((parent, view, position, id) -> {
            // Lấy ra đơn hàng vừa được chọn
            DonHang selectedDonHang = listHienThi.get(position);
            String tenCanTim = selectedDonHang.getTenHang(); // Ví dụ: "Bình hoa", "Quạt"...

            // Kiểm tra lại xem đã được cấp quyền đọc danh bạ chưa
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED) {

                // Gọi hàm tìm kiếm từ ContentProvider của hệ thống Android
                String ketQuaContact = timKiemContactTheoTen(tenCanTim);

                // Toast kết quả hiển thị lên màn hình
                Toast.makeText(MainActivity.this, ketQuaContact, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Chưa cấp quyền truy cập danh bạ!", Toast.LENGTH_SHORT).show();
            }

            return true; // Trả về true để xác nhận sự kiện hoàn tất
        });
    }

    // Câu 5: Hàm truy vấn danh bạ bằng ContentResolver
    private String timKiemContactTheoTen(String tenHang) {
        ContentResolver resolver = getContentResolver();

        // Địa chỉ bảng danh bạ trong hệ thống Android
        Uri uriContacts = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        // Điều kiện lọc: Tên hiển thị trong danh bạ trùng với tên mặt hàng (không phân biệt hoa thường)
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{ tenHang };

        Cursor cursor = resolver.query(uriContacts, null, selection, selectionArgs, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Lấy chỉ mục (index) của cột Tên và cột Số điện thoại
            int idxName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int idxPhone = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            if (idxName != -1 && idxPhone != -1) {
                String name = cursor.getString(idxName);
                String phone = cursor.getString(idxPhone);

                cursor.close();
                // Trả về chuỗi thông báo nếu tìm thấy
                return "Trong danh bạ, Số điện thoại của " + name + " là " + phone;
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        // Trả về chuỗi thông báo nếu không tìm thấy bất kỳ contact nào trùng tên
        return "Không tìm thấy";
    }

    // Hàm callback khi người dùng phản hồi hộp thoại xin quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CONTACTS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Đã được cấp quyền danh bạ!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Từ chối quyền danh bạ sẽ không dùng được tính năng click giữ!", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Các hàm layDanhSachTuDatabase() và capNhatDongTrungBinh() giữ nguyên như trước...
    private ArrayList<DonHang> layDanhSachTuDatabase() {
        ArrayList<DonHang> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new DonHang(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getFloat(3), cursor.getInt(4) == 1));
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();
        return list;
    }
}