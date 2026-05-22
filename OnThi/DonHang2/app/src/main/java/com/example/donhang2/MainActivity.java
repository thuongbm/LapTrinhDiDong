package com.example.donhang2;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // 1. Khai báo các thuộc tính thành viên
    private ListView lvDonHang;
    private ArrayList<DonHang> dsDonHang;
    private DonHangAdapter adapter;
    private EditText edtSearch;
    private DatabaseHelper dbHelper;

    // Khai báo thêm nút Sắp xếp và TextView hiển thị Trung bình cho Câu 3
    private Button btnSort;
    private TextView txtTrungBinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 2. Ánh xạ các View từ Layout XML sang Java
        lvDonHang = findViewById(R.id.lvDonHang);
        edtSearch = findViewById(R.id.edtSearch);
        btnSort = findViewById(R.id.btnSort);
        txtTrungBinh = findViewById(R.id.txtTrungBinh);

        // Xử lý tràn viền an toàn (Padding hệ thống) bằng cách găm vào ListView hoặc Layout chính
        ViewCompat.setOnApplyWindowInsetsListener(lvDonHang, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- CÂU 2: KHỞI TẠO CSDL VÀ HIỂN THỊ TOAST MINH CHỨNG ---
        dbHelper = new DatabaseHelper(this);
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            if (db != null && db.isOpen()) {
                Toast.makeText(this, "Khởi tạo CSDL SQLite và nạp dữ liệu mẫu thành công!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khởi tạo CSDL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // 3. Khởi tạo nguồn dữ liệu: ĐỌC DỮ LIỆU THỰC TẾ TỪ SQLITE
        dsDonHang = docDuLieuTuSQLite();

        // 4. Tạo Adapter và gán vào ListView
        adapter = new DonHangAdapter(this, dsDonHang);
        lvDonHang.setAdapter(adapter);

        // --- CÂU 3.2: TÍNH VÀ HIỂN THỊ GIÁ TRỊ TRUNG BÌNH KHI KHỞI CHẠY ---
        capNhatGiaTriTrungBinh();

        // --- ĐỔI THÀNH CÂU 5.1: BẮT SỰ KIỆN NHẤN GIỮ (LONG CLICK) ĐỂ HIỂN THỊ DIALOG ---
        lvDonHang.setOnItemLongClickListener((adapterView, view, position, id) -> {
            DonHang selectedOrder = dsDonHang.get(position);
            showConfirmDialog(selectedOrder, adapter);

            // Trả về true để hệ thống biết sự kiện Long Click đã xử lý xong, không kích hoạt click thường
            return true;
        });

        // --- CÂU 3.3: CLICK NÚT SẮP XẾP GIẢM DẦN THEO TÊN HÀNG ---
        if (btnSort != null) {
            btnSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Collections.sort(dsDonHang, new Comparator<DonHang>() {
                        @Override
                        public int compare(DonHang dh1, DonHang dh2) {
                            // Đổi dh2 so sánh với dh1 để xếp giảm dần (từ Z về A)
                            return dh2.getTenHang().compareToIgnoreCase(dh1.getTenHang());
                        }
                    });

                    // Cập nhật lại giao diện danh sách
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Đã sắp xếp danh sách giảm dần theo tên!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // --- CÂU HỎI PHỤ: LỌC THEO GIÁ THÀNH TIỀN TRONG Ô SEARCH ---
        if (edtSearch != null) {
            edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String input = s.toString().trim();

                    if (input.isEmpty()) {
                        // Nếu ô trống, tải lại toàn bộ danh sách gốc từ SQLite
                        dsDonHang.clear();
                        dsDonHang.addAll(docDuLieuTuSQLite());
                    } else {
                        try {
                            float giaGioiHan = Float.parseFloat(input);
                            ArrayList<DonHang> danhSachGoc = docDuLieuTuSQLite();
                            ArrayList<DonHang> danhSachLoc = new ArrayList<>();

                            for (DonHang dh : danhSachGoc) {
                                // Điều kiện lọc: Thành tiền phải nhỏ hơn hoặc bằng giá trị nhập vào
                                if (dh.tinhThanhTien() <= giaGioiHan) {
                                    danhSachLoc.add(dh);
                                }
                            }

                            dsDonHang.clear();
                            dsDonHang.addAll(danhSachLoc);

                        } catch (NumberFormatException e) {
                            // Báo lỗi nếu người dùng gõ nhầm chữ thay vì gõ số
                            Toast.makeText(MainActivity.this, "Vui lòng nhập số hợp lệ!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    // Vẽ lại giao diện danh sách và cập nhật lại con số trung bình tương ứng
                    adapter.notifyDataSetChanged();
                    capNhatGiaTriTrungBinh();
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    // Hàm phụ trách đọc dữ liệu từ bảng SQLite đổ vào ArrayList<DonHang>
    private ArrayList<DonHang> docDuLieuTuSQLite() {
        ArrayList<DonHang> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Thực hiện câu lệnh truy vấn toàn bộ bảng DonHang
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst()) {
            int index = 0; // Tạo chỉ số phụ để truyền vào constructor DonHang
            do {
                // Đọc dữ liệu từng cột dựa trên vị trí index của cột
                String ma = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MA));
                String ten = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEN));
                String ngayStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NGAY));
                float gia = cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GIA));
                int giaoNhanhInt = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GIAO_NHANH));

                boolean loaiGiaoHang = (giaoNhanhInt == 1); // Đổi 1 thành true, 0 thành false

                // Chuyển chuỗi String ngày tháng "dd/MM/yyyy" từ DB ngược lại thành kiểu Date
                Date ngayDat;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    ngayDat = sdf.parse(ngayStr);
                } catch (Exception e) {
                    ngayDat = new Date(); // Nếu lỗi thì lấy ngày hiện tại
                }

                // Khởi tạo đối tượng DonHang từ dữ liệu SQLite
                DonHang dh = new DonHang(index, ten, ngayDat, gia, loaiGiaoHang);
                list.add(dh);

                index++;
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close(); // Đóng con trỏ sau khi đọc xong để tránh rò rỉ bộ nhớ
        }
        return list;
    }

    // --- CÂU 3.2: HÀM TÍNH TOÁN VÀ HIỂN THỊ GIÁ TRỊ TRUNG BÌNH THÀNH TIỀN ---
    private void capNhatGiaTriTrungBinh() {
        if (dsDonHang == null || dsDonHang.isEmpty()) {
            if (txtTrungBinh != null) txtTrungBinh.setText("Trung bình: 0");
            return;
        }

        float tongThanhTien = 0;
        for (DonHang dh : dsDonHang) {
            tongThanhTien += dh.tinhThanhTien(); // Gọi hàm tính thành tiền của câu 1.2
        }
        float trungBinh = tongThanhTien / dsDonHang.size();

        // Hiển thị lên giao diện định dạng đẹp mắt (Ví dụ: 1,015,000)
        if (txtTrungBinh != null) {
            txtTrungBinh.setText("Trung bình: " + String.format("%,.0f", trungBinh));
        }
    }

    // --- CÂU 5.2: HÀM HIỂN THỊ DIALOG XÁC NHẬN GIẢM GIÁ VÀ LƯU XUỐNG SQLITE ---
    private void showConfirmDialog(DonHang donHang, DonHangAdapter adapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");

        float giaHienTai = donHang.getGiaHang();
        float giaMoi = giaHienTai * 0.9f; // Tính nhẩm trước mức 10% để hiển thị

        // Định dạng chuỗi thông báo xuống dòng giống đề bài
        String message = "Bạn muốn giảm giá " + donHang.getTenHang() + " 10%?\n\n"
                + "Từ: " + String.format("%,.0f", giaHienTai) + "\n"
                + "Còn: " + String.format("%,.0f", giaMoi);

        builder.setMessage(message);

        // Sự kiện khi nhấn nút OK
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 1. Gọi hàm xử lý giảm giá nội bộ trong Class DonHang để cập nhật List hiện tại
                donHang.giamGiaMuoiPhanTram();

                // 2. THỰC HIỆN CẬP NHẬT TRỰC TIẾP XUỐNG CƠ SỞ DỮ LIỆU SQLITE
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_GIA, donHang.getGiaHang());

                db.update(DatabaseHelper.TABLE_NAME, values, DatabaseHelper.COLUMN_MA + " = ?", new String[]{donHang.getMaDonHang()});

                // 3. Báo cho Adapter biết dữ liệu đã đổi để vẽ lại giao diện
                adapter.notifyDataSetChanged();

                // 4. Cập nhật lại giá trị trung bình sau khi đã giảm giá
                capNhatGiaTriTrungBinh();

                Toast.makeText(MainActivity.this, "Đã cập nhật giảm giá và lưu vào CSDL!", Toast.LENGTH_SHORT).show();
            }
        });

        // Sự kiện khi nhấn nút Cancel
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Hiển thị dialog lên màn hình
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}