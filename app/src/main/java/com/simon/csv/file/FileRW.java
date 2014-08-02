package com.simon.csv.file;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by simon on 30.06.14.
 */
public class FileRW {
    private static final String LOG_TAG = FileRW.class.getName();
    private static final String DIR = "GpsOIDFiles";
    private FileOutputStream outputStream;

    private File file;

    public FileRW(String date) {
        if (isExternalStorageWritable()) {

            this.file = getGpsDir(date);
            try {
                outputStream = new FileOutputStream(this.file);
            } catch (FileNotFoundException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getGpsDir(String gpsFileName) {
        // Get the directory for the user's public pictures directory.
        File fileDir = new File(Environment.getExternalStorageDirectory(), DIR);

        if (!fileDir.exists()) {
            if (!fileDir.mkdirs()) {
                Log.e(LOG_TAG, "Directory not created");
            }
        }
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + DIR, gpsFileName);

        return file;
    }

    public void writeData(String data) {
        try {
            outputStream.write(data.getBytes());
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    public void closeFile() {
        try {
            outputStream.close();
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }
}
