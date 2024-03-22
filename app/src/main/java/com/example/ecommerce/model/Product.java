package com.example.ecommerce.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class Product implements Parcelable {
    private int id;
    private String title;
    private String description;
    private byte[] image;
    private double price;

    public Product(int id, String title, String description, byte[] image, double price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.price = price;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImageAsByte() {
        return image;
    }
    public Bitmap getImage() {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public void setImage(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.image = stream.toByteArray();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    protected Product(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        int imageLength = in.readInt();
        if (imageLength > 0) {
            image = new byte[imageLength];
            in.readByteArray(image);
        }
        price = in.readDouble();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        if (image != null) {
            dest.writeInt(image.length);
            dest.writeByteArray(image);
        } else {
            dest.writeInt(0);
        }
        dest.writeDouble(price);
    }
}

