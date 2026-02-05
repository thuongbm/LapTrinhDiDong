package com.example.databasehowto;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editTextId, editTextName, editTextYear;
    Button buttonAdd, buttonUpdate, buttonDelete, buttonView;
    TextView textView;

    // Khai báo helper
    MyDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 1. Ánh xạ View từ XML
        initViews();

        // 2. Khởi tạo Database Helper
        dbHelper = new MyDBHelper(this);

        // 3. Xử lý sự kiện Thêm (ADD)
        buttonAdd.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            int year = Integer.parseInt(editTextYear.getText().toString());

            long result = dbHelper.addData(name, year);
            if (result != -1) {
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        // 4. Xử lý sự kiện Xem (VIEW)
        buttonView.setOnClickListener(v -> {
            Cursor cursor = dbHelper.disPlayData();
            if (cursor.getCount() == 0) {
                textView.setText("Database trống rỗng!");
                return;
            }

            StringBuilder builder = new StringBuilder();
            while (cursor.moveToNext()) {
                builder.append("ID: ").append(cursor.getInt(0)).append("\n");
                builder.append("Tên: ").append(cursor.getString(1)).append("\n");
                builder.append("Năm: ").append(cursor.getInt(2)).append("\n\n");
            }
            textView.setText(builder.toString());
            cursor.close(); // Nhớ đóng cursor sau khi dùng
        });

        // 5. Xử lý sự kiện Sửa (UPDATE)
        buttonUpdate.setOnClickListener(v -> {
            int id = Integer.parseInt(editTextId.getText().toString());
            String name = editTextName.getText().toString();
            int year = Integer.parseInt(editTextYear.getText().toString());

            int rowsAffected = dbHelper.uppdateData(id, name, year);
            if (rowsAffected > 0) {
                Toast.makeText(this, "Đã cập nhật ID " + id, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Không tìm thấy ID để sửa", Toast.LENGTH_SHORT).show();
            }
        });

        // 6. Xử lý sự kiện Xóa (DELETE)
        buttonDelete.setOnClickListener(v -> {
            int id = Integer.parseInt(editTextId.getText().toString());
            int result = dbHelper.deleteData(id);
            if (result > 0) {
                Toast.makeText(this, "Đã xóa ID " + id, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        editTextId = findViewById(R.id.editTextId);
        editTextName = findViewById(R.id.editTextName);
        editTextYear = findViewById(R.id.editTextYear);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonView = findViewById(R.id.buttonView);
        textView = findViewById(R.id.textView);
    }
}