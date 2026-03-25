package com.example.songapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // 1. Khai báo các thành phần
    DatabaseHandler db;
    SearchView searchView;
    ListView listViewSongs;
    ArrayList<Song> fullList;      // Luôn chứa toàn bộ dữ liệu từ DB
    ArrayList<Song> filteredList;  // Chứa dữ liệu sau khi lọc để hiển thị lên màn hình
    // Sửa lại dòng này ở trên cùng của class
    SongAdapter adapter;

    EditText edtTenBaiHat, edtTenCaSi, edtDiem;
    Button btnThem, btnSua, btnGoToRingtone;
    int selectedSongId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 2. Ánh xạ View (CHỈ LÀM 1 LẦN DUY NHẤT)
        db = new DatabaseHandler(this);
        searchView = findViewById(R.id.searchView);
        listViewSongs = findViewById(R.id.listViewSongs);
        edtTenBaiHat = findViewById(R.id.edtTenBaiHat);
        edtTenCaSi = findViewById(R.id.edtTenCaSi);
        edtDiem = findViewById(R.id.edtDiem);
        btnThem = findViewById(R.id.btnThem);
        btnSua = findViewById(R.id.btnSua);
        btnGoToRingtone = findViewById(R.id.btnGoToRingtone);

        // 3. Khởi tạo dữ liệu
        loadDataFromDatabase();

        // 4. Thiết lập Adapter
        filteredList = new ArrayList<>(fullList);
        adapter = new SongAdapter(this, R.layout.item_song, filteredList);
        listViewSongs.setAdapter(adapter);

        // 5. Cài đặt các sự kiện (Listeners)
        setupEventListeners();
    }

    private void loadDataFromDatabase() {
        fullList = db.getAllSongs();
        // Nếu DB trống, thêm dữ liệu mẫu
        if (fullList.isEmpty()) {
            db.addSong(new Song(0, "Kiếp đỏ đen", 4.5f, "Duy Mạnh"));
            db.addSong(new Song(0, "Xuân này con không về", 5.0f, "Quang Lê"));
            db.addSong(new Song(0, "Lạc trôi", 4.8f, "Sơn Tùng M-TP"));
            fullList = db.getAllSongs();
        }
    }

    private void setupEventListeners() {
        // --- TÌM KIẾM ---
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterSongs(newText);
                return true;
            }
        });

        // --- CHỌN DÒNG ĐỂ SỬA ---
        listViewSongs.setOnItemClickListener((parent, view, position, id) -> {
            Song selectedSong = filteredList.get(position);
            edtTenBaiHat.setText(selectedSong.getTen());
            edtTenCaSi.setText(selectedSong.getTenCaSi());
            edtDiem.setText(String.valueOf(selectedSong.getDiemDanhGia()));
            selectedSongId = selectedSong.getMaBaiHat();
        });

        // --- THÊM ---
        btnThem.setOnClickListener(v -> {
            String ten = edtTenBaiHat.getText().toString().trim();
            String casi = edtTenCaSi.getText().toString().trim();
            String diemStr = edtDiem.getText().toString().trim();

            if (ten.isEmpty() || casi.isEmpty() || diemStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                db.addSong(new Song(0, ten, Float.parseFloat(diemStr), casi));
                refreshData();
                clearInputs();
                Toast.makeText(this, "Đã thêm mới!", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Điểm phải là số!", Toast.LENGTH_SHORT).show();
            }
        });

        // --- SỬA ---
        btnSua.setOnClickListener(v -> {
            if (selectedSongId == -1) {
                Toast.makeText(this, "Chọn một bài hát để sửa", Toast.LENGTH_SHORT).show();
                return;
            }

            String ten = edtTenBaiHat.getText().toString();
            String casi = edtTenCaSi.getText().toString();
            float diem = Float.parseFloat(edtDiem.getText().toString());

            db.updateSong(new Song(selectedSongId, ten, diem, casi));
            refreshData();
            clearInputs();
            selectedSongId = -1;
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
        });

        // --- CHUYỂN MÀN HÌNH (INTENT) ---
        btnGoToRingtone.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlayRingtoneActivity.class);
            startActivity(intent);
        });
    }

    private void filterSongs(String text) {
        filteredList.clear();
        for (Song item : fullList) {
            if (item.getTen().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void refreshData() {
        fullList.clear();
        fullList.addAll(db.getAllSongs());
        filterSongs(searchView.getQuery().toString());
    }

    private void clearInputs() {
        edtTenBaiHat.setText("");
        edtTenCaSi.setText("");
        edtDiem.setText("");
        selectedSongId = -1;
        edtTenBaiHat.requestFocus();
    }
}