package com.example.khachhang;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView6;
    EditText editTextText;
    EditText editTextText2;
    RadioGroup radioGroup;
    RadioButton radioButton;
    RadioButton radioButton2;
    RadioButton radioButton3;
    Button button;

    ListView listView;
    ArrayList<String> dataList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dataList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);


        initWidget();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processConversion();
            }
        });
    }

    public void initWidget(){
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView6 = findViewById(R.id.textView6);
        editTextText = findViewById(R.id.editTextText);
        editTextText2 = findViewById(R.id.editTextText2);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        button = findViewById(R.id.button3);
    }

    public void processConversion(){
        String text = editTextText.getText().toString();

        Double vnd = Double.parseDouble(editTextText2.getText().toString());

       if (text.isEmpty() || vnd == null) {
           Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
           return;
       }

       double res = 0;
       String currencyName = "";

       int checkedId = radioGroup.getCheckedRadioButtonId();

       if (checkedId == radioButton.getId()) {
           res = vnd / 25000;
           currencyName = "USD";
       }
       else if (checkedId == radioButton2.getId()) {
           res = vnd / 27000;
           currencyName = "EUR";
       }
       else if (checkedId == radioButton3.getId()) {
           res = vnd / 23000;
           currencyName = "NDT";
       }
       else {
           Toast.makeText(this, "Vui lòng chọn đơn vị tiền tệ", Toast.LENGTH_SHORT).show();
           return;
       }

        DecimalFormat df = new DecimalFormat("#,###.##");
        String formattedResult = df.format(res);

        // Hiển thị kết quả lên TextView 6
        textView6.setText("VND sang " + currencyName + " là: " + formattedResult);

        String historyEntry = textView + ": " + df.format(vnd) + " VND -> " + formattedResult + " " + currencyName;
        dataList.add(0, historyEntry); // Thêm vào đầu danh sách
        adapter.notifyDataSetChanged(); // Cập nhật ListView
    }
}