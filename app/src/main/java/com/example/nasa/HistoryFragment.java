package com.example.nasa;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HistoryFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.history_layout, container, false);

        TextView contenu = (TextView) myView.findViewById(R.id.contenu);

        String ligne, lignes = "";
        try{
            InputStream flux = new FileInputStream(Environment.getExternalStorageDirectory() + File.separator + "Podcasts/log.txt");
            InputStreamReader lecture = new InputStreamReader(flux);
            BufferedReader buff = new BufferedReader(lecture);

            while ((ligne=buff.readLine())!=null){
                lignes = lignes +  ligne + "\n";
            }
            buff.close();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

        contenu.setText(lignes);

        return myView;
    }

    private class RestOperation extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        TextView contenu = (TextView) myView.findViewById(R.id.contenu);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Please wait ...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Intent intent = new Intent();
            intent.setClass(getContext(), HistoryReceiver.class);
            return "hi";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
        }
    }

}
