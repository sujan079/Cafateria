package com.example.printerserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.printerserver.Adapter.IpAddersAdapter;
import com.example.printerserver.BackgroundServices.PrintService;
import com.example.printerserver.Database.Models.IpAdders;
import com.example.printerserver.Database.RoomDatabase;
import com.example.printerserver.Executors.AppExecutor;
import com.example.printerserver.Printer.PrinterHelper;
import com.example.printerserver.Socket.PrintSocket;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class MainActivity extends AppCompatActivity {

    private EditText etIpAddr;
    private Button btnAddIp, btnStartServer, btnStopServer;

    private RecyclerView rvPrintList;
    private RecyclerView.LayoutManager layoutManager;
    private IpAddersAdapter ipAddersAdapter;


    private String TAG = "MainActivity";


    private RoomDatabase mDB;


    //hanlder
    private Handler loadIpAddress = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            List<IpAdders> ipAdders = (List<IpAdders>) msg.obj;
            ipAddersAdapter.setIpAddersList(ipAdders);
            ipAddersAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etIpAddr = findViewById(R.id.et_printer_ip);
        btnAddIp = findViewById(R.id.btn_addPrinter);


        rvPrintList = findViewById(R.id.rv_ipAdd);
        layoutManager = new LinearLayoutManager(this);
        ipAddersAdapter = new IpAddersAdapter();

        ipAddersAdapter.setOnItemClicked(new IpAddersAdapter.OnItemClicked() {
            @Override
            public void delIp(IpAdders ipAdders) {
                deleteIp(ipAdders);
            }
        });

        rvPrintList.setAdapter(ipAddersAdapter);
        rvPrintList.setLayoutManager(layoutManager);

        mDB = RoomDatabase.getInstance(this);

        btnAddIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipAddress = etIpAddr.getText().toString();
                saveIp(ipAddress);
            }
        });

        btnStartServer = findViewById(R.id.btn_start_server);
        btnStopServer = findViewById(R.id.btn_stop_server);

        btnStartServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent printeIntent = new Intent(getApplicationContext(), PrintService.class);
                ContextCompat.startForegroundService(getApplicationContext(), printeIntent);
            }
        });

        btnStopServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent printIntent = new Intent(getApplicationContext(), PrintService.class);
                stopService(printIntent);
            }
        });


        loadAllIp();




    }


    private void saveIp(final String ip) {
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                IpAdders ipAdders = new IpAdders(ip, "false");
                mDB.printerDao().insert(ipAdders);
                loadAllIp();
            }
        });
    }

    private void deleteIp(final IpAdders ipAdders) {
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDB.printerDao().delete(ipAdders);
                loadAllIp();
            }
        });
    }

    private void loadAllIp() {
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<IpAdders> ipAdders = mDB.printerDao().getAllIp();
                Message msg = new Message();
                msg.obj = ipAdders;
                loadIpAddress.sendMessage(msg);

            }
        });
    }




}
