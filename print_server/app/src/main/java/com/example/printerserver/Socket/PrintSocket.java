package com.example.printerserver.Socket;

import android.app.Application;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class PrintSocket {

    public static String BASE_URL = "http://192.168.0.3:8000";

    private Socket mSocket;

    public PrintSocket() {
        {
            try {
                mSocket = IO.socket(BASE_URL);
            } catch (URISyntaxException e) {
            }
        }
    }





    public Socket getSocket() {
        return mSocket;
    }
}
