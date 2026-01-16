package com.example.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private CheckBox checkBox;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private RadioGroup radioGroup;
    private EditText editTextDate;
    private EditText editTextPhone;
    private EditText editTextText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initWidget();

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String name = editTextText.getText().toString();
                String date = editTextDate.getText().toString();
                String phone = editTextPhone.getText().toString();

                String hobbies = "";

                if (checkBox.isChecked()){
                    hobbies += checkBox.getText().toString() + ", ";
                }
                if (checkBox2.isChecked()){
                    hobbies += checkBox2.getText().toString() + ", ";
                }
                if (checkBox3.isChecked()){
                    hobbies += checkBox3.getText().toString() + ", ";
                }

                String sex = "";
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId != -1){
                    RadioButton radioButton = (RadioButton) findViewById(selectedId);
                    sex = radioButton.getText().toString();
                }

                String res = "Tên: " + name + "\n" +
                        "Ngày sinh: " + date + "\n" +
                        "SĐT: " + phone + "\n" +
                        "Giới tính: " + sex + "\n" +
                        "Sở thích: " + hobbies;



                Toast.makeText(MainActivity.this, res, Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initWidget(){
        button = (Button) findViewById(R.id.button);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox2 =(CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextPhone =(EditText) findViewById(R.id.editTextPhone);
        editTextText = (EditText) findViewById(R.id.editTextText);
    }
}