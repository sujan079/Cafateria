package com.example.printerserver.BackgroundServices;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.printerserver.Application.App;
import com.example.printerserver.Database.Models.IpAdders;
import com.example.printerserver.Database.RoomDatabase;
import com.example.printerserver.Executors.AppExecutor;
import com.example.printerserver.MainActivity;
import com.example.printerserver.Model.MenuItem;
import com.example.printerserver.Model.Order;
import com.example.printerserver.Printer.PrinterHelper;
import com.example.printerserver.R;
import com.example.printerserver.Socket.PrintSocket;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import java.util.List;

public class PrintService extends Service {

    private Socket socket;

    private Notification notification;
    private int PRINT_NOTIFICATIN_ID = 1;

    private String TAG = "PrintService";


    private RoomDatabase mDB;


    @Override
    public void onCreate() {
        super.onCreate();


        PrintSocket printSocket = new PrintSocket();

        socket = printSocket.getSocket();
        socket.on("print", onPrintCommand);

        socket.connect();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mDB = RoomDatabase.getInstance(getApplicationContext());

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle("Print Server")
                .setContentText("Running")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(PRINT_NOTIFICATIN_ID, notification);


        return super.onStartCommand(intent, flags, startId);
    }


    public void startService(final Order order) {
        AppExecutor.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<IpAdders> ipAdders = mDB.printerDao().getAllIp();

                for (IpAdders adders : ipAdders) {
                    PrinterHelper printerHelper = new PrinterHelper(adders.getIpAddr());

                    print(printerHelper, order);
                }
            }
        });
    }

    public void print(PrinterHelper printerHelper, Order data) {

        List<MenuItem> menuItems = data.getMenuItems();

        String email = data.getEmail();

        String printData = "\t\t\t" + data.getPrintId() + "\n\n";
        printData += "Email:" + email + "\n\n";
        printData += "Name\t\t\t" + "Quantity\n\n";

        for (MenuItem item : menuItems) {
            printData += item.getItemName() + "\t\t" + item.getQuantity() + "\n\n";
        }

        printData += "\n\n\n";
        printerHelper.PrintfData(printData.getBytes());
        printerHelper.cutPaper();


    }


    public Emitter.Listener onPrintCommand = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "call: " + args[0]);

            Gson gson = new Gson();

            String jsonString = (String) args[0];

            Order order = gson.fromJson(jsonString, Order.class);

            startService(order);

        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();

        socket.disconnect();
        socket.close();

        socket = null;
        startForeground(PRINT_NOTIFICATIN_ID, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
