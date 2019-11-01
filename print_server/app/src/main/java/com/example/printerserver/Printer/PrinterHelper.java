package com.example.printerserver.Printer;



public class PrinterHelper {
    Socketmanager socketmanager;

    public PrinterHelper(String printerIp) {
        this.socketmanager = new Socketmanager(printerIp,9100);
    }

    public boolean conTest() {
        socketmanager.threadconnect();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (socketmanager.getIstate()) {
            return true;
        } else {
            return false;
        }
    }

    public  boolean PrintfData(byte[] data) {
        socketmanager.threadconnectwrite(data);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return (socketmanager.getIstate());
    }




    public   boolean cutPaper(){
        byte SendCut[]={0x0a,0x0a,0x1d,0x56,0x31};

        socketmanager.threadconnectwrite(SendCut);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return socketmanager.getIstate();
    }
}
