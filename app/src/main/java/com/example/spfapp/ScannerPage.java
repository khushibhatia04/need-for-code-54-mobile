package com.example.spfapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.sql.Connection;

public class ScannerPage extends AppCompatActivity {

    private int inputParameter = MainActivity.inputParameter;
    Connection connection = MainActivity.connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);


    }


}