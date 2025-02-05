package com.example.tabnewlayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;  // Use android.util.Base64

import java.io.ByteArrayOutputStream;

public class ImageEncryption {
    public static String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);  // Use android.util.Base64
    }

    public static Bitmap decodeBase64ToBitmap(String encodedImage) {
        byte[] decodedByteArray = Base64.decode(encodedImage, Base64.DEFAULT);  // Use android.util.Base64
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }
}
