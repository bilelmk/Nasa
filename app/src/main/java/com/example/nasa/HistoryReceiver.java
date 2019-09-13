package com.example.nasa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

public class HistoryReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        File fichier = new File(Environment.getExternalStorageDirectory() + File.separator + "Podcasts/log.txt");
        try {
            if(!fichier.exists()) { fichier.createNewFile(); }

            FileWriter writer = new FileWriter(fichier,true);
            String requete = intent.getStringExtra("req");
            try {
                writer.write("- "+requete+"\n");
            } finally {
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
