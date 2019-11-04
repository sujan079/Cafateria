package com.example.cafateriaclientapp.Socket;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class PrintSocket {

    public static String BASE_URL = "https://cafateriaapp.herokuapp.com/";

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
