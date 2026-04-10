package com.example.contactadvance;

public class User {
    // Attributes defined in the requirement
    private int id;
    private String name;
    private String phoneNumber;
    private String image; // Typically stores a URL, URI, or resource name

    // Additional attribute to track the checkbox status automatically
    private boolean isChecked;

    // Constructor
    public User(int id, String name, String phoneNumber, String image) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.isChecked = false; // Checkbox is unchecked by default
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getImage() {
        return image;
    }

    // This acts as the function to check the status of the checkbox
    public boolean isChecked() {
        return isChecked;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Used to update the status when the user taps the checkbox in the UI
    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}