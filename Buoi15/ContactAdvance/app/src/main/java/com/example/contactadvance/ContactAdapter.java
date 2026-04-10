package com.example.contactadvance;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<User> {

    private Context context;
    private List<User> userList;

    public ContactAdapter(@NonNull Context context, @NonNull List<User> objects) {
        super(context, 0, objects);
        this.context = context;
        this.userList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // 1. Recycle the view if possible for better performance
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        }

        // 2. Get the current User data
        User currentUser = userList.get(position);

        // 3. Link Java variables to the item_contact.xml IDs
        ImageView imgAvatar = listItem.findViewById(R.id.imgAvatar);
        TextView tvName = listItem.findViewById(R.id.tvName);
        TextView tvPhone = listItem.findViewById(R.id.tvPhone);
        ImageView imgCall = listItem.findViewById(R.id.imgCall);
        ImageView imgMessage = listItem.findViewById(R.id.imgMessage);
        CheckBox checkBox = listItem.findViewById(R.id.checkBox);

        // 4. Bind the data to the UI
        tvName.setText(currentUser.getName());
        tvPhone.setText(currentUser.getPhoneNumber());

        // 5. Handle the Avatar Image dynamically
        int imageId = context.getResources().getIdentifier(
                currentUser.getImage(), "drawable", context.getPackageName());

        if (imageId != 0) {
            imgAvatar.setImageResource(imageId);
        } else {
            imgAvatar.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // 6. Set static action icons
        imgCall.setImageResource(android.R.drawable.stat_sys_phone_call);
        imgMessage.setImageResource(android.R.drawable.sym_action_email);

        // --- NEW: CLICK LISTENERS FOR ICONS ---

        // 7. Handle Phone Call Icon Click
        imgCall.setOnClickListener(v -> {
            String phoneNum = currentUser.getPhoneNumber();
            if (phoneNum != null && !phoneNum.isEmpty()) {
                // ACTION_DIAL opens the dialer without needing immediate permissions
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Phone number is missing!", Toast.LENGTH_SHORT).show();
            }
        });

        // 8. Handle SMS Icon Click
        imgMessage.setOnClickListener(v -> {
            String phoneNum = currentUser.getPhoneNumber();
            if (phoneNum != null && !phoneNum.isEmpty()) {
                // ACTION_SENDTO opens the default messaging app
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNum));
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Phone number is missing!", Toast.LENGTH_SHORT).show();
            }
        });

        // 9. REQUIREMENT #1: Checkbox state logic
        // Remove existing listener to prevent "re-firing" while scrolling
        checkBox.setOnCheckedChangeListener(null);

        // Sync visual state with the data model
        checkBox.setChecked(currentUser.isChecked());

        // Save new state back to the model when clicked
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentUser.setChecked(isChecked);
        });

        return listItem;
    }
}