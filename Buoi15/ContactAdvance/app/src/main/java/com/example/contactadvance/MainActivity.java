package com.example.contactadvance;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Important for the menu

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Student Name: Bùi Minh Thương
 * Student ID: 231230922
 * Assignment: ContactAdvance Final Implementation
 */
public class MainActivity extends AppCompatActivity {

    private ListView listViewContacts;
    private Button btnAdd, btnDelete;
    private EditText etSearch;
    private ContactAdapter adapter;
    private Toolbar toolbar;

    private List<User> userList;      // The list currently visible on screen
    private List<User> originalList;  // The master list (Source of Truth) from Database
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Setup Toolbar (CRITICAL: This makes the 3-dot menu show up)
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("ContactAdvance");
        }

        // Initialize Database Helper (Requirement 3)
        db = new DatabaseHelper(this);

        // 2. Link Java variables to XML IDs
        listViewContacts = findViewById(R.id.listViewContacts);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        etSearch = findViewById(R.id.etSearch);

        // 3. Requirement 3: Seed Database with sample data if it's the first run
        if (db.getContactsCount() == 0) {
            db.addContact(new User(0, "Nam", "09893838", "avatar1"));
            db.addContact(new User(0, "Bich", "03393039", "avatar2"));
            db.addContact(new User(0, "Hai", "098789089", "avatar3"));
        }

        // 4. Initial load of data
        refreshData();

        // 5. Requirement 2: Search Bar Logic (Filters as you type)
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 6. Requirement 6: Delete selected contacts via Checkbox status
        btnDelete.setOnClickListener(v -> {
            boolean itemDeleted = false;
            // Iterate backwards to avoid index shifting when deleting
            for (int i = userList.size() - 1; i >= 0; i--) {
                User u = userList.get(i);
                if (u.isChecked()) {
                    db.deleteContact(u.getId()); // Delete from SQL
                    itemDeleted = true;
                }
            }

            if (itemDeleted) {
                refreshData(); // Re-sync master list and UI
                Toast.makeText(this, "Deleted selected contacts", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please select at least one contact", Toast.LENGTH_SHORT).show();
            }
        });

        // 7. Handle bottom ADD button
        btnAdd.setOnClickListener(v -> {
            addNewContact();
        });
    }

    // --- REQUIREMENT 5: MENU LOGIC (Sort & Add) ---

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_sort) {
            sortListAlphabetically();
            return true;
        } else if (id == R.id.menu_add) {
            addNewContact();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // --- HELPER METHODS ---

    private void addNewContact() {
        // Creates a dummy user and persists to SQLite
        String newName = "New User " + (db.getContactsCount() + 1);
        db.addContact(new User(0, newName, "0123456789", ""));

        refreshData();
        etSearch.setText(""); // Clear search so the new entry shows up
        Toast.makeText(this, "Added: " + newName, Toast.LENGTH_SHORT).show();
    }

    private void sortListAlphabetically() {
        // Sort the Master List using a Comparator
        Collections.sort(originalList, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                return u1.getName().compareToIgnoreCase(u2.getName());
            }
        });

        // Re-apply the current search text to update UI while maintaining order
        filter(etSearch.getText().toString());
        Toast.makeText(this, "List Sorted A-Z", Toast.LENGTH_SHORT).show();
    }

    /**
     * Requirement 3: Syncs originalList with SQLite and refreshes the UI.
     */
    private void refreshData() {
        originalList = db.getAllContacts();
        // Maintain search filter state
        filter(etSearch.getText().toString());
    }

    /**
     * Requirement 2: Filters the list and updates the adapter.
     */
    private void filter(String text) {
        List<User> filteredList = new ArrayList<>();

        for (User user : originalList) {
            if (user.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(user);
            }
        }

        // Update the list reference for the adapter
        userList = new ArrayList<>(filteredList);

        if (adapter == null) {
            adapter = new ContactAdapter(this, userList);
            listViewContacts.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(userList);
            adapter.notifyDataSetChanged();
        }
    }
}