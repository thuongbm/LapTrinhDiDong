package com.example.onthi1pdf;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    DatabaseHelper dbHelper;
    ListView listView; // Or RecyclerView
    ArrayList<Student> studentList;

    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    private Button btnAdd, btnDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        // Requirement 3: Add sample data (run this once or check if empty)
        dbHelper.addSampleData();

        // Link UI components
        listView = findViewById(R.id.listView);

        // Display the data
        loadData();

        editText = findViewById(R.id.editTextText); // Ensure this ID matches your XML

        editText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Requirement 2: Search by name as the user types
                searchData(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        registerForContextMenu(listView);
    }

    private void loadData() {
        studentList = new ArrayList<>();
        android.database.sqlite.SQLiteDatabase db = dbHelper.getReadableDatabase();
        android.database.Cursor cursor = db.rawQuery("SELECT * FROM Students", null);

        while (cursor.moveToNext()) {
            studentList.add(new Student(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4)
            ));
        }
        cursor.close();

        // THIS IS THE MISSING LINK:
        StudentAdapter adapter = new StudentAdapter(this, studentList);
        listView.setAdapter(adapter);
    }

    private void searchData(String query) {
        studentList.clear(); // Clear the current list to show new results
        android.database.sqlite.SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Requirement 2: Search for names using the SQL LIKE operator
        String sql = "SELECT * FROM Students WHERE name LIKE ?";
        android.database.Cursor cursor = db.rawQuery(sql, new String[]{"%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                studentList.add(new Student(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Requirement 4: Update the UI with the filtered list
        StudentAdapter adapter = new StudentAdapter(this, studentList);
        listView.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Edit");
        menu.add(0, 2, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 2) { // Delete option selected
            showDeleteDialog();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Bạn có muốn xoá tất cả các học sinh có tổng điểm nhỏ hơn 25 không?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteLowScoringStudents();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteLowScoringStudents() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Logic: Delete where (math + chem + phys) < 25
        db.delete("Students", "(math + chemistry + physics) < ?", new String[]{"25"});
        db.close();
        loadData(); // Refresh your list to show the changes
    }

    private void initNetworkCallback() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                Log.d("NetworkStatus", "ONLINE");
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Đã kết nối mạng", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                Log.d("NetworkStatus", "OFFLINE");
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Mất kết nối mạng", Toast.LENGTH_SHORT).show();
                });
            }
        };
    }
}