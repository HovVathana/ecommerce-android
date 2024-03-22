package com.example.ecommerce.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.db.ProductManager;
import com.example.ecommerce.model.Product;

import java.io.ByteArrayOutputStream;

public class AdminProductAddActivity extends Activity {

    private static final int REQUEST_IMAGE_PICK = 1;

    private EditText editTextTitle, editTextDescription, editTextPrice;
    private ImageView imageViewProduct;
    private Button buttonSelectImage, buttonAddProduct;

    private byte[] imageBytes;
    Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_add);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            product = extras.getParcelable("product");
            TextView title = findViewById(R.id.appBarTitle);
            title.setText("Edit Product");
        }


        // Initialize views
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPrice = findViewById(R.id.editTextPrice);
        imageViewProduct = findViewById(R.id.imageViewProduct);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);

        if (product != null) {
            editTextTitle.setText(product.getTitle());
            editTextDescription.setText(product.getDescription());
            editTextPrice.setText(String.valueOf(product.getPrice()));
            imageViewProduct.setImageBitmap(product.getImage());
            imageBytes = product.getImageAsByte();
        }

        // Set click listener for selecting an image
        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGallery();
            }
        });

        // Set click listener for adding product
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if all fields are filled
                String title = editTextTitle.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                String priceStr = editTextPrice.getText().toString().trim();

                if (title.isEmpty() || description.isEmpty() || priceStr.isEmpty() || imageBytes == null) {
                    Toast.makeText(AdminProductAddActivity.this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Convert price to double
                double price = Double.parseDouble(priceStr);

                if (product != null) {
                    long result = updateProduct(product.getId(), title, description, imageBytes, price);
                    if (result != -1) {
                        Toast.makeText(AdminProductAddActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Finish activity
                    } else {
                        // Failed to add product
                        Toast.makeText(AdminProductAddActivity.this, "Failed to updated product", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Add product to database
                    long result = addProduct(title, description, imageBytes, price);

                    if (result != -1) {
                        // Product added successfully
                        Toast.makeText(AdminProductAddActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Finish activity
                    } else {
                        // Failed to add product
                        Toast.makeText(AdminProductAddActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    // Method to select image from gallery
    private void selectImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }

    // Method to handle result of image selection from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                if (imageBitmap != null) {
                    imageViewProduct.setImageBitmap(imageBitmap);
                    imageBytes = getBytesFromBitmap(imageBitmap);
                } else {
                    Log.e("AddProductActivity", "Error: Image selection failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("AddProductActivity", "Error: " + e.getMessage());
            }
        }
    }

    // Method to convert Bitmap to byte array
    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    // Method to add product to database
    private long addProduct(String title, String description, byte[] image, double price) {
        ProductManager databaseManager = new ProductManager(this);
        databaseManager.open();
        long result = databaseManager.addProduct(title, description, image, price);
        databaseManager.close();
        return result;
    }

    private long updateProduct(int id, String title, String description, byte[] image, double price) {
        ProductManager databaseManager = new ProductManager(this);
        databaseManager.open();
        long result = databaseManager.updateProduct(id, title, description, image, price);
        databaseManager.close();
        return result;
    }
}
