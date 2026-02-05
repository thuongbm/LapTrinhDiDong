package com.example.grid_view2;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<SinhVien> arlSV;

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
        lv= (ListView) findViewById(R.id.ListView);
        //tao du lieu
        arlSV = new ArrayList<>();
        arlSV.add(new SinhVien("ThanhTung",2005, "Riot"));
        arlSV.add(new SinhVien("ThanhTung2",2006, "Tencent"));
        arlSV.add(new SinhVien("ThanhTung3",2007, "Riot"));
        arlSV.add(new SinhVien("ThanhTung4",2008, "Tencent"));
        SinhVienAdapter adapterSV= new SinhVienAdapter(MainActivity.this,R.layout.list_item,arlSV);
        lv.setAdapter(adapterSV);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, arlSV.get(position).Name+" "+arlSV.get(position).Age, Toast.LENGTH_SHORT).show();
            }
        });
    }
}