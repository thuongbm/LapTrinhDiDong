package com.example.songapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class PlayRingtoneActivity extends AppCompatActivity {

    // Khai báo các biến Button ở mức toàn cục (Class level)
    private Button btnPlay, btnStop, btnBack;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Kết nối với file XML bạn đã tạo ở bước trước
        setContentView(R.layout.activity_play_ringtone);

        // 1. Ánh xạ các View từ XML
        btnPlay = findViewById(R.id.btnPlay);
        btnStop = findViewById(R.id.btnStop);
        btnBack = findViewById(R.id.btnBack);

        // 2. Khởi tạo Intent để gọi Service
        serviceIntent = new Intent(this, MyRingtoneService.class);

        // 3. Xử lý sự kiện nút PLAY: Bật nhạc chuông
        btnPlay.setOnClickListener(v -> {
            startService(serviceIntent);
        });

        // 4. Xử lý sự kiện nút STOP: Dừng nhạc chuông
        btnStop.setOnClickListener(v -> {
            stopService(serviceIntent);
        });

        // 5. Xử lý sự kiện nút TRỞ VỀ: Thoát màn hình này để về trang chủ
        btnBack.setOnClickListener(v -> {
            // Dừng nhạc luôn khi quay về (tùy chọn theo yêu cầu đề bài)
            stopService(serviceIntent);
            finish();
        });
    }

    // (Tùy chọn) Nếu muốn nhạc dừng khi người dùng bấm nút Back cứng của điện thoại
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopService(serviceIntent);
    }
}