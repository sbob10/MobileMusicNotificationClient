package com.example.david.musicserversampleapp;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.widget.Toast;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.media.MediaPlayer.TrackInfo;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity  {

    private UDPSenderManager m_SenderManager = null;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String cmd = intent.getStringExtra("command");

            String artist = intent.getStringExtra("artist");
            String album = intent.getStringExtra("album");
            String track = intent.getStringExtra("track");

            m_SenderManager.sendTrackData(artist,track,album);
        }
    };

    private EditText et_IP = null;
    private EditText et_Port = null;

    private boolean m_IsBroadcastReceiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et_IP = findViewById(R.id.et_IP);
        et_Port = findViewById(R.id.et_Port);

        m_SenderManager = new UDPSenderManager(MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRegisterClicked(View v){

        m_SenderManager.m_IpAddress = et_IP.getText().toString();
        m_SenderManager.m_Port = Integer.parseInt(et_Port.getText().toString());

        if(!m_IsBroadcastReceiverRegistered){
            IntentFilter iF = new IntentFilter();
            iF.addAction("com.android.music.metachanged");

            registerReceiver(mReceiver, iF);
            m_IsBroadcastReceiverRegistered = true;
        }
    }


}
