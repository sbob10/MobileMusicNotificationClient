package com.example.david.musicserversampleapp;

import android.os.AsyncTask;
import android.content.Context;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class UDPSenderManager {

    public String m_IpAddress = "";
    public int m_Port = 0;

    private Context m_Context = null;

    public UDPSenderManager(Context context){
        m_Context = context;
    }

    public void sendTrackData(String artist, String album, String track){
        String udpMessageString = artist + ";" + album + ";" + track;
        SendUDPTask senderTask = new SendUDPTask();
        senderTask.execute(m_IpAddress, m_Port, udpMessageString.getBytes());
    }

    public void sendToast(String message){
        Toast.makeText(m_Context, message, Toast.LENGTH_SHORT).show();
    }

    private class SendUDPTask extends AsyncTask<Object, Void, String> {

        private boolean m_Success;

        @Override
        protected String doInBackground(Object... params) {
            try {
                String ipString = (String)params[0];
                int port = (int)params[1];
                byte[] data = (byte[])params[2];

                InetAddress ip = InetAddress.getByName(ipString);

                DatagramSocket datagramSocket = new DatagramSocket();
                DatagramPacket datagramPacket = new DatagramPacket(data, data.length, ip, port);
                datagramSocket.send(datagramPacket);
            }
            catch(Exception e){
                e.printStackTrace();
                m_Success = false;
                return "";
            }
            m_Success = true;
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            if(m_Success)
                sendToast("Successfully sent UDP-Packet!");
            else
                sendToast("Unsuccessfully sent UDP-Packet!");
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
