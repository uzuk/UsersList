package com.example.userslisttest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userslisttest.adduser.AddEditUserActivity;
import com.example.userslisttest.db.User;
import com.example.userslisttest.notes.UserAdapter;
import com.example.userslisttest.notes.UserViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_USER_REQUEST = 1;
    public static final int EDIT_USER_REQUEST = 2;
    private UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final UserAdapter adapter = new UserAdapter();
        recyclerView.setAdapter(adapter);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                adapter.submitList(users);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                userViewModel.delete(adapter.getUserAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User user) {
                Intent intent = new Intent(MainActivity.this, AddEditUserActivity.class);
                intent.putExtra(AddEditUserActivity.EXTRA_ID, user.getUid());
                intent.putExtra(AddEditUserActivity.EXTRA_NAME, user.getName());
                intent.putExtra(AddEditUserActivity.EXTRA_AGE, user.getAge().toString());
                intent.putExtra(AddEditUserActivity.EXTRA_COLOR, user.getFavColor());
                intent.putExtra(AddEditUserActivity.EXTRA_IMAGE_URI_STRING, user.getImageUriString());

                startActivityForResult(intent, EDIT_USER_REQUEST);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/////////////////////ADD USER REQUEST
        if (requestCode == ADD_USER_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(AddEditUserActivity.EXTRA_NAME);
            Integer age = data.getIntExtra(AddEditUserActivity.EXTRA_AGE, 0);
            String color = data.getStringExtra(AddEditUserActivity.EXTRA_COLOR);
            String uriString = data.getStringExtra(AddEditUserActivity.EXTRA_IMAGE_URI_STRING);

            User user = new User(name, age, color, uriString);
            userViewModel.insert(user);

            Toast.makeText(this, "User saved", Toast.LENGTH_SHORT).show();

        } //////////////////////EDIT USER REQUEST
        else if (requestCode == EDIT_USER_REQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddEditUserActivity.EXTRA_ID,-1);

            if (id == -1) {
                Toast.makeText(this, "Note Can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = data.getStringExtra(AddEditUserActivity.EXTRA_NAME);
            Integer age = data.getIntExtra(AddEditUserActivity.EXTRA_AGE, 0);
            String color = data.getStringExtra(AddEditUserActivity.EXTRA_COLOR);
            String uriString = data.getStringExtra(AddEditUserActivity.EXTRA_IMAGE_URI_STRING);

            User user = new User(name, age, color, uriString);
            user.setUid(id);
            userViewModel.update(user);

            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "User not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(MainActivity.this, AddEditUserActivity.class);
                startActivityForResult(intent, ADD_USER_REQUEST);
                return true;
        }

        return false;
    }
    }


