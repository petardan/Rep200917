package com.rpd.irepair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;


class DownloadingProcess extends AsyncTask<String, String, String>{


    //Nov interfejs za citanje na rezultatot od asynctask, zaedno so konstruktor
    private AsyncResponse delegate;
    DownloadingProcess(AsyncResponse delegate){
        this.delegate=delegate;
    }




    @Override
    protected String doInBackground(String... params) {

        try {

            URL url = new URL(params[0]);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();


            String PATH = Environment.getExternalStorageDirectory() + "/download/";
            File file = new File(PATH);
            if(file.mkdirs()){
                File outputFile = new File(file, "irepair.apk");
                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();//till here, it works fine - .apk is download to my sdcard in download file
            }
            else {
                Log.d("DownloadingProccess", "Error downloading apk file");
            }
        } catch (IOException e) {
            Log.d("Downloadingprocess", "Downloading Error!!!!!!!");
        }
        return "";
    }



    @Override
    protected void onPostExecute(String result) {
//isprakanje na rezultatot na streams activity
        delegate.processFinish(result);
    }


    interface AsyncResponse {
        void processFinish(String result);
    }

}
//End of XMLParser


