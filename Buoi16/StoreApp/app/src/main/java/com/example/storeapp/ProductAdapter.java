package com.example.storeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends BaseAdapter {

    private Context context;
    private List<Product> productList;

    // Constructor to receive the context and the data list
    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        // Tells the ListView how many rows to create
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Inflate the layout for a single row if it doesn't exist yet
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        }

        // 2. Find the UI components in item_product.xml
        TextView tvName = convertView.findViewById(R.id.tvProductName);
        TextView tvPrice = convertView.findViewById(R.id.tvProductPrice);
        TextView tvDiscount = convertView.findViewById(R.id.tvDiscount);

        // 3. Get the product data for the current row
        Product product = productList.get(position);

        // 4. Bind the data to the UI
        tvName.setText(product.getName());
        tvPrice.setText(product.getPrice());

        // 5. Logical Check: Only show the discount TextView if the product is on sale
        if (product.isDiscount()) {
            tvDiscount.setVisibility(View.VISIBLE);
            // Since we calculated the discount in MainActivity and saved it to 'details'
            tvDiscount.setText(product.getDetails());
        } else {
            // Use GONE so the TextView doesn't take up any space
            tvDiscount.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void updateData(ArrayList<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }
}