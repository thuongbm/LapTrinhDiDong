package com.example.listview;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> dataList;
    ArrayAdapter<String> adapter;
    int index = 0;
    EditText editTextText2;
    Button button;
    Button button2;
    Button button3;
    Button button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initwidget();

        dataList = new ArrayList<>(Arrays.asList("Java", "Kotlin", "C#", "Python", "C++", "JavaScript"));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String value = dataList.get(position);
            editTextText2.setText(value);
            index = position;
        });

        button.setOnClickListener(v -> {
            String input = editTextText2.getText().toString().trim();
            if (!input.isEmpty()) {
                dataList.add(input);
                adapter.notifyDataSetChanged();
                editTextText2.setText("");
            }
        });

        button2.setOnClickListener(v -> {
            if (index != -1) {
                dataList.set(index, editTextText2.getText().toString());
                adapter.notifyDataSetChanged();
                index = -1;
                editTextText2.setText("");
            } else {
                Toast.makeText(this, "Please select an item first", Toast.LENGTH_SHORT).show();
            }


        });

        button3.setOnClickListener(v -> {
            if (index != -1) {
                DeleteAlert();
//                dataList.remove(index);
//                adapter.notifyDataSetChanged();
//                index = -1;
//                editTextText2.setText("");
            } else {
                Toast.makeText(this, "Please select an item first", Toast.LENGTH_SHORT).show();
            }
        });

        // CLEAR BUTTON
        button4.setOnClickListener(v -> {
            editTextText2.setText("");
            index = -1;
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void DeleteAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataList.remove(index);
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public void initwidget() {
        listView = findViewById(R.id.listView);
        editTextText2 = findViewById(R.id.editTextText2);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
    }
}