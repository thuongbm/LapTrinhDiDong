package com.example.ungdungapi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView; // Đổi thành TextView
import android.widget.Toast;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText editWeight, editHeight;
    Button button;
    TextView res;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initwidgets();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra nếu người dùng chưa nhập dữ liệu để tránh crash
                if (editWeight.getText().toString().isEmpty() || editHeight.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ cân nặng và chiều cao", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 1. Lấy dữ liệu và chuyển sang kiểu double
                double w = Double.parseDouble(editWeight.getText().toString());
                double h = Double.parseDouble(editHeight.getText().toString()) / 100; // Đổi cm sang m

                // 2. Tính BMI
                double bmi = w / (h * h);

                // 3. Phân loại chuẩn theo WHO
                String classification = "";
                if (bmi < 18.5) {
                    classification = "Gầy (Underweight)";
                    imageView.setImageResource(R.drawable.bmi_skeleton);
                } else if (bmi < 24.9) {
                    classification = "Bình thường (Normal)";
                    imageView.setImageResource(R.drawable.bmi_nomal);
                } else if (bmi < 29.9) {
                    classification = "Tiền béo phì (Overweight)";
                    imageView.setImageResource(R.drawable.bmi_nomal);
                } else {
                    classification = "Béo phì (Obese)";
                    imageView.setImageResource(R.drawable.bmi_chubby);
                }

                // 4. Hiển thị kết quả (làm tròn 1 chữ số thập phân)
                res.setText(String.format("BMI: %.1f\nKết quả: %s", bmi, classification));


            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initwidgets(){
        // Đảm bảo các ID này trùng với ID trong file activity_main.xml của bạn
        editWeight = (EditText) findViewById(R.id.editTextText);
        editHeight = (EditText) findViewById(R.id.editTextText2);
        res = (TextView) findViewById(R.id.res);
        button = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.imageView);
    }
}