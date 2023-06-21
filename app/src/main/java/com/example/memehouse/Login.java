package com.example.memehouse;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
@Entity(tableName="Login")
public class Login {
    @ColumnInfo(name="U_Name")
    String U_Name;
    @ColumnInfo(name="Pass")
    String pass;
}
