package com.apptech.Networking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.apptech.apptechcomponents.R;

import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NetworkActivity extends AppCompatActivity {

    public int port = 2521;
    private ExecutorService executor;
    private final int PING_TIMEOUT = 600; //150: Can find all ip within 4 seconds but cant send messages to others for ultra concurrent request

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        fullNetworkScan();
    }

    public void fullNetworkScan() {
        ArrayList<String> allIpAddress = new ArrayList<>();
        String myIP = Util.getDeviceIP();
        Log.e("fullNetworkScan: ", myIP);
        String[] tokens = myIP.split("\\.");

        String firstPart = tokens[0];
        String secondPart = tokens[1];
        String thirdPart = tokens[2];
        //String fourthPart = tokens[3];

        for (int i = 0; i <= 255; i++) {
            allIpAddress.add(firstPart + "." + secondPart + "." + thirdPart + "." + i);
        }
        GetInfo(allIpAddress);
    }



    public void GetInfo(ArrayList<String> allIpAddress) {

        try {
            executor = Executors.newFixedThreadPool(10);
            for (String ipAddress : allIpAddress) {
                InetAddress currentPingAddr = InetAddress.getByName(ipAddress);
                Log.e("GetInfo: ", currentPingAddr.toString());
                executor.execute(IPList(currentPingAddr));
            }

            executor.shutdown();
            try {
                if (!executor.awaitTermination(3600, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }


        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }

    }

    private Runnable IPList(final InetAddress inet) {
        return new Runnable() {
            public void run() {
                try {
                    String finalIP = inet.toString().substring(1);

                    boolean reachable = inet.isReachable(PING_TIMEOUT);
                    if (reachable) {
                        SendData(finalIP, "Imran Hosen Nirob");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void SendData(final String ipAddress, final String data) {

        try {
            Socket senderSocket = new Socket();
            InetAddress addr = InetAddress.getByName(ipAddress);
            SocketAddress sockaddr = new InetSocketAddress(addr, port);
            senderSocket.connect(sockaddr, 4000);
            DataOutputStream DOS = new DataOutputStream(senderSocket.getOutputStream());
            DOS.write(data.getBytes());
            senderSocket.close();

        } catch (Exception e) {
        }
    }
}
