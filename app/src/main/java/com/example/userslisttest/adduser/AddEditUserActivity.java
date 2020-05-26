package com.example.userslisttest.adduser;


import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.userslisttest.R;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class AddEditUserActivity extends AppCompatActivity {
    ///variables for the avatar
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    private ImageView selectedImage;
    private ImageButton imageGalleryButton;
    private ImageButton imageCameraButton;
    private ImageView userAvatar;
    private Bitmap imageBitmap;


    public static final String EXTRA_ID = "com.example.userslisttest.adduser.EXTRA_ID";
    public static final String EXTRA_NAME = "com.example.userslisttest.adduser.EXTRA_NAME";
    public static final String EXTRA_AGE = "com.example.userslisttest.adduser.EXTRA_AGE";
    public static final String EXTRA_COLOR = "com.example.userslisttest.adduser.EXTRA_COLOR";
    public static final String EXTRA_IMAGE_URI_STRING = "com.example.userslisttest.adduser.EXTRA_IMAGE_URI_STRING";

    private EditText editTextName;
    private EditText editTextAge;
    private Spinner colorSpinner;
    private String imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        editTextName = findViewById(R.id.name_et);
        editTextAge = findViewById(R.id.age_et);
        colorSpinner = findViewById(R.id.color_spinner);
        userAvatar = findViewById(R.id.user_avatar);
        imageUri = null;

        imageGalleryButton = findViewById(R.id.image_from_gallery);
        imageCameraButton = findViewById(R.id.image_from_camera);

        imageGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGallery();
            }
        });


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit User");
            editTextName.setText(intent.getStringExtra(EXTRA_NAME));
            editTextAge.setText(intent.getStringExtra(EXTRA_AGE));
            colorSpinner.setSelection(((ArrayAdapter<String>) colorSpinner.getAdapter()).getPosition(intent.getStringExtra(EXTRA_COLOR)));
            imageUri = intent.getStringExtra(EXTRA_IMAGE_URI_STRING);
            if(imageUri != null){
                Uri uri = Uri.parse(imageUri);
                userAvatar.setImageURI(uri);

            }else {
                userAvatar.setImageResource(R.drawable.ic_person_person);
            }
        } else {
            setTitle("Add User");
            userAvatar.setImageResource(R.drawable.ic_person_person);
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void saveUser() {
        String name = editTextName.getText().toString();
        String ageString = editTextAge.getText().toString();
        String color = colorSpinner.getSelectedItem().toString();

        if (name.trim().isEmpty() || ageString.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a Name and Age", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isInteger(ageString)) {
            Toast.makeText(this, "Please insert only numbers to the Age", Toast.LENGTH_LONG).show();
            return;
        }
        Integer age = Integer.parseInt(ageString);
        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_AGE, age);
        data.putExtra(EXTRA_COLOR, color);
        data.putExtra(EXTRA_IMAGE_URI_STRING, imageUri);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_user:
                saveUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    ////////////image from gallery
    private void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pick an image"), GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageData = data.getData();
            userAvatar.setImageURI(imageData);
            imageUri = imageData.toString();
            Toast.makeText(this, ""+imageUri, Toast.LENGTH_SHORT).show();


        }
    }

}
