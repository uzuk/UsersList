package com.example.userslisttest.notes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.userslisttest.db.User;
import com.example.userslisttest.db.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;
    private LiveData<List<User>> allUsers;
    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        allUsers = repository.getAllUsers();
    }
    public void insert(User user) {
        repository.insert(user);
    }
    public void update(User user) {
        repository.update(user);
    }
    public void delete(User user) {
        repository.delete(user);
    }
    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
}
