package com.example.firebasestorageprueba;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class FirebaseManager {

    private static final String TAG = "FirebaseManager";

    // Subir una imagen a Firebase Storage
    public static void uploadImage(Bitmap imageBitmap, String imageName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("imagenes").child(imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            Log.e(TAG, "uploadImage: Failed to upload image", exception);
        }).addOnSuccessListener(taskSnapshot -> {
            Log.d(TAG, "uploadImage: Image uploaded successfully");
        });
    }

    // Descargar una imagen de Firebase Storage
    public static void downloadImage(String imageName, final OnImageDownloadListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("imagenes").child(imageName);

        final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            listener.onImageDownload(bitmap);
        }).addOnFailureListener(exception -> {
            Log.e(TAG, "downloadImage: Failed to download image", exception);
            listener.onImageDownload(null);
        });
    }

    // Interfaz para manejar la descarga de imágenes
    public interface OnImageDownloadListener {
        void onImageDownload(Bitmap bitmap);
    }

    // Conexión a Firebase Firestore
    public static FirebaseFirestore getFirestoreInstance() {
        return FirebaseFirestore.getInstance();
    }

    // Método para agregar un documento a Firestore
    public static void addDocument(String collectionName, Map<String, Object> documentData,
                                   OnSuccessListener<Void> successListener,
                                   OnFailureListener failureListener) {
        FirebaseFirestore db = getFirestoreInstance();
        db.collection(collectionName).document()
                .set(documentData)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    // Ejemplo de uso:
    // FirebaseManager.addDocument("posts", post,
    //     aVoid -> Log.d(TAG, "Document added successfully"),
    //     e -> Log.e(TAG, "Error adding document", e));
}
