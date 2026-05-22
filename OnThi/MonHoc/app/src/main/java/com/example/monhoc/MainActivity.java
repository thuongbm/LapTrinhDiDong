package com.example.monhoc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<MonThi> listGoc = new ArrayList<>();
    private List<MonThi> listHienThi = new ArrayList<>();
    private MonThiAdapter adapter;
    private ListView listView;
    private TextView tvDiemTrungBinhChung;
    private EditText edtSearch;
    private Button btnSort;
    private FloatingActionButton fabAdd;

    // Khai báo thêm đối tượng quản lý database
    private SqliteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ
        listView = findViewById(R.id.listView);
        tvDiemTrungBinhChung = findViewById(R.id.tvDiemTrungBinhChung);
        edtSearch = findViewById(R.id.edtSearch);
        btnSort = findViewById(R.id.btnSort);
        fabAdd = findViewById(R.id.fabAdd);

        // Khởi tạo Database Helper
        dbHelper = new SqliteHelper(MainActivity.this);

        // Đọc dữ liệu mẫu đã được lưu trong SQLite từ trước đưa vào listGoc
        listGoc = dbHelper.getAllMonThi();

        // Đồng bộ dữ liệu lên giao diện hiển thị
        listHienThi.addAll(listGoc);
        adapter = new MonThiAdapter(this, listHienThi);
        listView.setAdapter(adapter);

        // Tính điểm trung bình ban đầu từ dữ liệu SQLite
        capNhatDiemTrungBinh();

        // 2 & 3.3. Chức năng Sắp xếp TĂNG DẦN theo ĐIỂM TỔNG KẾT
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(listHienThi, new Comparator<MonThi>() {
                    @Override
                    public int compare(MonThi m1, MonThi m2) {
                        // So sánh tăng dần theo điểm tổng kết của môn học
                        return Float.compare(m1.getDiemTongKet(), m2.getDiemTongKet());
                    }
                });
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Đã sắp xếp tăng dần theo điểm TK", Toast.LENGTH_SHORT).show();
            }
        });

        // 3 & 4. Chức năng Tìm kiếm / Lọc theo giá trị điểm nhập vào ô Search
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                listHienThi.clear();

                if (query.isEmpty()) {
                    // Nếu ô tìm kiếm trống, hiển thị lại toàn bộ danh sách môn học ban đầu
                    listHienThi.addAll(listGoc);
                } else {
                    try {
                        // Câu 4: Ép kiểu dữ liệu nhập vào sang số float để làm mốc so sánh điểm
                        float diemX = Float.parseFloat(query);

                        // Lọc danh sách: chỉ giữ lại những môn có điểm tổng kết < diemX
                        for (MonThi m : listGoc) {
                            if (m.getDiemTongKet() < diemX) {
                                listHienThi.add(m);
                            }
                        }
                    } catch (NumberFormatException e) {
                        // Cơ chế phòng ngừa: Nếu người dùng nhập chữ hoặc gõ lỗi dấu phẩy,
                        // ứng dụng sẽ tự động chuyển sang lọc theo Tên Môn học để tránh bị crash app.
                        for (MonThi m : listGoc) {
                            if (m.getTenMon().toLowerCase().contains(query.toLowerCase())) {
                                listHienThi.add(m);
                            }
                        }
                    }
                }

                // Cập nhật lại giao diện danh sách sau khi áp dụng bộ lọc
                adapter.notifyDataSetChanged();

                // Tính toán lại điểm trung bình dựa trên những môn đang hiển thị thực tế
                capNhatDiemTrungBinh();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 4 & 5. Sự kiện Nhấn giữ Item (Long Click) để quét danh bạ hệ thống qua Content Provider
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                // ĐÃ CẬP NHẬT: Gọi hàm check hệ thống thực tế thay vì hiện text cứng
                checkContactFromSystem();
                return true; // Trả về true để không kích hoạt nhầm sự kiện click thường
            }
        });

        // 5. Nút thêm mới (Đồng thời lưu thẳng vào SQLite)
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một đối tượng mới (tự tăng mã theo cấu trúc static cũ)
                MonThi newMon = new MonThi("Môn học mới", 3, 7.0f, 8.0f);

                // Lưu đối tượng này vào database SQLite
                boolean success = dbHelper.addMonThi(newMon);

                if (success) {
                    // Cập nhật lại list ứng dụng từ database
                    listGoc = dbHelper.getAllMonThi();
                    listHienThi.clear();

                    // Chú ý: Cần kiểm tra lại ô search hiện tại xem có đang lọc không
                    String currentQuery = edtSearch.getText().toString().trim();
                    if (currentQuery.isEmpty()) {
                        listHienThi.addAll(listGoc);
                    } else {
                        try {
                            float diemX = Float.parseFloat(currentQuery);
                            for (MonThi m : listGoc) {
                                if (m.getDiemTongKet() < diemX) {
                                    listHienThi.add(m);
                                }
                            }
                        } catch (NumberFormatException e) {
                            for (MonThi m : listGoc) {
                                if (m.getTenMon().toLowerCase().contains(currentQuery.toLowerCase())) {
                                    listHienThi.add(m);
                                }
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                    capNhatDiemTrungBinh();
                    Toast.makeText(MainActivity.this, "Đã lưu vào SQLite và sinh mã: " + newMon.getMa(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 3.2. Tính toán Điểm trung bình có hệ số (Weighted Average) dựa trên số tín chỉ
    private void capNhatDiemTrungBinh() {
        if (listHienThi.isEmpty()) {
            tvDiemTrungBinhChung.setText("0.0");
            return;
        }

        float tongDiemTichLuy = 0f;
        int tongSoTinChi = 0;

        for (MonThi m : listHienThi) {
            tongDiemTichLuy += (m.getDiemTongKet() * m.getSoTinChi());
            tongSoTinChi += m.getSoTinChi();
        }

        if (tongSoTinChi == 0) {
            tvDiemTrungBinhChung.setText("0.0");
            return;
        }

        float dtb = tongDiemTichLuy / tongSoTinChi;
        // Làm tròn lấy 1 chữ số thập phân sau dấu phẩy
        dtb = Math.round(dtb * 10.0f) / 10.0f;

        tvDiemTrungBinhChung.setText(String.valueOf(dtb));
    }

    // Hàm kiểm tra và lấy tên từ danh bạ bằng Content Provider
    private void checkContactFromSystem() {
        // Kiểm tra xem ứng dụng đã được cấp quyền đọc danh bạ chưa
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Nếu chưa có quyền, yêu cầu người dùng cấp quyền runtime
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 100);
            return;
        }

        String foundName = null;

        // Truy vấn danh bạ điện thoại sử dụng Content Provider công khai của hệ thống
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER},
                null, null, null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Lấy tên và số điện thoại từ cột tương ứng
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                // Làm sạch chuỗi số điện thoại (bỏ khoảng trắng, dấu gạch ngang nếu có)
                if (phone != null) {
                    phone = phone.replaceAll("\\s+", "").replaceAll("-", "");
                    // Kiểm tra xem số điện thoại có kết thúc bằng hai số cuối mã sinh viên "04" không
                    if (phone.endsWith("04")) {
                        foundName = name;
                        break; // Tìm thấy thì dừng vòng lặp luôn
                    }
                }
            }
            cursor.close();
        }

        // Hiển thị thông báo Toast theo đúng barem yêu cầu của đề bài
        if (foundName != null) {
            Toast.makeText(this, "Tìm thấy: " + foundName, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Số điện thoại không có trong danh bạ", Toast.LENGTH_LONG).show();
        }
    }

    // Xử lý khi người dùng nhấn đồng ý hoặc từ chối cấp quyền ở lần đầu tiên
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkContactFromSystem();
        } else if (requestCode == 100) {
            Toast.makeText(this, "Ứng dụng cần quyền truy cập danh bạ để thực hiện chức năng này!", Toast.LENGTH_SHORT).show();
        }
    }
}