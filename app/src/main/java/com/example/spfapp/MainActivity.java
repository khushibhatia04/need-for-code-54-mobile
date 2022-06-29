package com.example.spfapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;


import com.example.spfapp.R;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView tv;

    public static Connection connection;

    public static int UserId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ConnectionHelper connectionHelper = new ConnectionHelper();
        connection = connectionHelper.connectionClass();
    }

    public void onClick(View view) {

        EditText getUsername = (EditText) findViewById(R.id.username);
        String username = getUsername.getText().toString();

        EditText getPassword = (EditText) findViewById(R.id.password);
        String password = getPassword.getText().toString();

        tv = findViewById(R.id.condition);

        UserId = 0;
        String temp = null;

        try {
            if (connection != null) {
//                String query = "Select * from SysUser where UsrLogin = '" + username + "' and UsrPswd = '" + password + "'";
                String query = "Select * from SysUser where UsrLoginName = '" + username + "' and UsrLoginPassword = '" + password + "'";

                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next()) {
                    temp = rs.getString(1);
                }

                if (temp != null) {
                    tv.setText("Login Successful");
                    UserId = Integer.parseInt(temp);
                    Intent intent = new Intent(MainActivity.this, ScannerPage.class);
                    startActivity(intent);
                } else {
                    tv.setText("Login Failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


