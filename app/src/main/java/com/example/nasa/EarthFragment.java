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
import android.widget.EditText;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class EarthFragment extends Fragment {

    private static final int REQUEST = 112;
    View myView;
    Bitmap bitmap;
    File f = null;
    EarthImage earthImage = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.earth_layout, container, false);
        Button getData = (Button) myView.findViewById(R.id.btn_show);
        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button saveData = (Button) myView.findViewById(R.id.btn_save);
                saveData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST );

                        java.util.Date date = new java.util.Date();
                        java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("dd-MM-yyyy H:mm:ss");
                        String dat = formater.format(date);
                        Intent i = new Intent();
                        i.setClass(getActivity(), HistoryReceiver.class);
                        i.putExtra("req", "Saving EARTH service image at "+dat);
                        getActivity().sendBroadcast(i);

                        try {
                            String name = "earthImage_"+earthImage.getDate().replace(':','-')+"_"+earthImage.getId().replace('/','_');
                            System.out.println("name = "+name);
                            f = savebitmap(bitmap, name);
                            System.out.println(f.getAbsolutePath());
                            Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                EditText longtitude = (EditText) myView.findViewById(R.id.longtitude);
                EditText latitude = (EditText) myView.findViewById(R.id.latitude);
                EditText dat = (EditText) myView.findViewById(R.id.dat);

                String longt = longtitude.getText().toString();
                String lat = latitude.getText().toString();
                String datt = dat.getText().toString();

                String restUrl = "https://api.nasa.gov/planetary/earth/imagery/?lon="+longt+"&lat="+lat+"&date="+datt+"&cloud_score=True&api_key=DEMO_KEY";
                new RestOperation().execute(restUrl);
            }
        });
        return myView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        String name = "earthImage_"+earthImage.getDate().replace(':','-')+"_"+earthImage.getId().replace('/','_');
                        f = savebitmap(bitmap, name);
                        System.out.println(f.getAbsolutePath());
                        Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "The app was not allowed to read your store.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private class RestOperation extends AsyncTask<String, Void, String> {

        final HttpClient httpclient = new DefaultHttpClient();
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        TextView data = (TextView) myView.findViewById(R.id.titre);
        TextView des = (TextView) myView.findViewById(R.id.contenu);
        ImageView img = (ImageView) myView.findViewById(R.id.img);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Please wait ...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return requestContent(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            try {

                JSONObject json = new JSONObject(result);
                earthImage = new EarthImage(json.getString("cloud_score"),
                        json.getString("date"),
                        json.getString("id"),
                        json.getString("service_version"),
                        json.getString("service_version"),
                        json.getString("url")
                );

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(earthImage !=null) {
                new DownloadImageTask((ImageView) myView.findViewById(R.id.img)).execute(earthImage.getUrl());
            } else
                data.setText("Rien à afficher");

        }

        public String requestContent(String url) {
            HttpClient httpclient = new DefaultHttpClient();
            String result = null;
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = null;
            InputStream instream = null;

            try {
                response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    instream = entity.getContent();
                    result = convertStreamToString(instream);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (instream != null) {
                    try {
                        instream.close();
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            }

            return result;
        }

        public String convertStreamToString(InputStream is) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return sb.toString();
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bitmap = result;
            bmImage.setImageBitmap(result);
        }
    }

    public File savebitmap(Bitmap bmp, String name) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Download/"+name+".jpg");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }

}
