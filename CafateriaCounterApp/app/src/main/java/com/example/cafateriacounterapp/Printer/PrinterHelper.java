package com.example.cafateriacounterapp.Printer;



public class PrinterHelper {
    Socketmanager socketmanager;

    byte[] center=new byte[]{0x1b,0x61,0x1};
    byte[] left=new byte[]{0x1b,0x61,0x0};
    byte[] right=new byte[]{0x1b,0x61,0x2};

    byte[] bold=new byte[] {0x1b,0x21,0x08};
    byte[] doubleWithHeight=new byte[] {0x1b,0x21,0x30};
    byte[] reset=new byte[]{0x1b,0x21,0x0};
    byte[] doubleHeight=new byte[]{0x1b,0x21,0x10};
    byte[] doubleWidth=new byte[]{0x1b,0x21,0x20};

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
