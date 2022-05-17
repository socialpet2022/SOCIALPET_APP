package com.example.socialpetapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class fotoperfil extends AppCompatActivity {
    ImageView foto_perfil;
    Button subir;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    String cameraPermission[];
    String storagePermission[];
    ProgressDialog pd;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    Uri imageUri = null;

    FirebaseStorage carpeta;
    StorageReference storagereference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotoperfil);
        foto_perfil = findViewById(R.id.perfil);
        subir = findViewById(R.id.btnf);


     carpeta=FirebaseStorage.getInstance();
     storagereference= carpeta.getReference();


        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        foto_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImagePicDialog();
            }
        });
        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri == null) {
                    Toast.makeText(fotoperfil.this, "Select an Image", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    uploadata();
                }

            }
        });

    }



    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // check for the camera and storage permission if
                // not given the request for permission
                if (which == 0) {
                    if (checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    if (checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }
    private Boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(result) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(fotoperfil.this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            });


    // request for permission to write data into storage
    private void requestStoragePermission() {
        mPermissionResult.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    // check camera permission to click picture using camera
    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    // request for permission to click photo using camera in app
    private void requestCameraPermission() {
        mPermissionResult.launch(Manifest.permission.CAMERA);
    }
    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        imageUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent camerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camerIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(camerIntent, IMAGE_PICKCAMERA_REQUEST);
    }

    // if access is given then pick image from gallery
    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGEPICK_GALLERY_REQUEST);
    }



        // initialising the storage reference for updating the data

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == this.RESULT_OK) {
            if (requestCode == IMAGEPICK_GALLERY_REQUEST ) {
                imageUri = data.getData();
                try {
                    getImageInviewImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == IMAGE_PICKCAMERA_REQUEST) {
                foto_perfil.setImageURI(imageUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getImageInviewImage() throws IOException {
        Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
        foto_perfil.setImageBitmap(bitmap);

    }
    private void uploadata(){
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Subiendo");

        progressDialog.show();
        FirebaseStorage.getInstance().getReference("image/"+ UUID.randomUUID().toString()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){

                task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            updatePorfilePicture(task.getResult().toString());
                            Toast.makeText(fotoperfil.this, "Se ha subido correctamente", Toast.LENGTH_LONG).show();
                            Intent intent= new Intent(fotoperfil.this,loginUser.class);

                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(fotoperfil.this, "Error en la subida", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                }
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress=100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount();
                progressDialog.setMessage("Subiendo"+(int)progress+"%");
            }
        });

    }

    private void updatePorfilePicture(String url) {
        FirebaseDatabase.getInstance().getReference("Usuarios/"+FirebaseAuth.getInstance().getCurrentUser().getUid()
        +"/image").setValue(url);
    }


}
