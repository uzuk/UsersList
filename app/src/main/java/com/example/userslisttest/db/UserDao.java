package com.example.userslisttest.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users ORDER BY uid DESC")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM users WHERE uid =:userId")
    LiveData<User> getUser(int userId);

    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);
}
