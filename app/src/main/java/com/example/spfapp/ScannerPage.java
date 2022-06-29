package com.example.spfapp;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Scanner;

public class ScannerPage extends AppCompatActivity implements  CustomAdapter.OnNoteListener{
    Button scanBtn;
    Button save;
    String STRBatchNo,strSaveBatchNo="";

    private int UserId = MainActivity.UserId;
    Connection connection = MainActivity.connection;

    int setError;
    public static String errorMsg;
    ArrayList<String> ProductBatch = new ArrayList<>();
    ArrayList<String> Details = new ArrayList<>();
    ArrayList<String> BatchNo = new ArrayList<>();

    RecyclerView recyclerView;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        save = findViewById(R.id.save);
        scanBtn = findViewById(R.id.scanBtn);

    }


    public void onClick(View v) {

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {

               STRBatchNo = intentResult.getContents();
               procedure();
                customAdapter = new CustomAdapter(ScannerPage.this,ProductBatch, Details,this);

                recyclerView = findViewById(R.id.rv);
                recyclerView.setAdapter(customAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ScannerPage.this));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void procedure(){
        try {
            CallableStatement callableStatement = connection.prepareCall("{call SIA_DeliveryBatchCheck(?,?,?)}");
            callableStatement.setString("in_BatchNo", STRBatchNo);
            callableStatement.registerOutParameter("out_ErrCode", Types.INTEGER);
            callableStatement.registerOutParameter("out_ErrMsg", Types.VARCHAR);
            callableStatement.execute();

            callableStatement.getMoreResults();
            setError = callableStatement.getInt("out_ErrCode");
            errorMsg = callableStatement.getString("out_ErrMsg");
            ResultSet rs = callableStatement.getResultSet();
            while (rs.next()) {
                ProductBatch.add(rs.getString("ProductBatch"));
                Details.add(rs.getString("Details"));
                BatchNo.add(rs.getString("BatchNo"));
           }
//            callableStatement.getMoreResults();
//            setError = callableStatement.getInt("out_ErrCode");
//            errorMsg = callableStatement.getString("out_ErrMsg");
            callableStatement.close();
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //Setting the title manually
                builder
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        {
                                            Toast.makeText(ScannerPage.this, "You pressed OK", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                        );
                AlertDialog alert = builder.create();
                alert.setTitle("AlertDialogExample");
                alert.setMessage(errorMsg);
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void OnSave(View v) {
        for (int i = 0; i < BatchNo.size(); i++) {
            strSaveBatchNo = strSaveBatchNo + BatchNo.get(i) + "!";
        }

        try {
            CallableStatement callableStatement = connection.prepareCall("{call SIA_BatchScanAUD(?,?,?,?)}");
            callableStatement.setString("in_SaveBatchNo", strSaveBatchNo);
            callableStatement.setInt("in_UserId",UserId);
            callableStatement.registerOutParameter("out_ErrCode", Types.INTEGER);
            callableStatement.registerOutParameter("out_ErrMsg", Types.VARCHAR);
            callableStatement.execute();
            callableStatement.getMoreResults();
            setError = callableStatement.getInt("out_ErrCode");
            errorMsg = callableStatement.getString("out_ErrMsg");
            callableStatement.close();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //Setting the title manually
            builder
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (setError==999){
                            finish();
                            Intent intent = new Intent(ScannerPage.this, ScannerPage.class);
                            startActivity(intent);
                        }
                    }
                }
            );
            AlertDialog alert = builder.create();
            alert.setTitle("AlertDialogExample");
            alert.setMessage(errorMsg);
            alert.show();
            strSaveBatchNo = "";
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

        @Override
    public void onNoteClick(int position) {

    }
}