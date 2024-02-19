package com.example.firebasestorageprueba;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        // Ejemplo de subida de imagen
        //Aqui el R.drawable.example_image lo deberiamos de obtener del movil
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.example_image);
        FirebaseManager.uploadImage(bitmap, "nombre_post.jpg");

        // Ejemplo de descarga de imagen
        FirebaseManager.downloadImage("Captura.PNG", bitmapResult -> {
            if (bitmapResult != null) {
                imageView.setImageBitmap(bitmapResult);
            } else {
                Log.e(TAG, "onCreate: Failed to download image");
            }
        });

        // Ejemplo de conexión a Firestore y agregando un documento
        Map<String, Object> post = new HashMap<>();
        post.put("text", "Vacaciones en la playa 2024");
        SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");
        String now = ISO_8601_FORMAT.format(new Date());
        post.put("date", now);
        post.put("user_id_post", "1");

        FirebaseManager.addDocument("posts", post,
                aVoid -> Log.d(TAG, "Document added successfully"),
                e -> Log.e(TAG, "Error adding document", e));
    }
}