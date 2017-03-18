package com.adityathakker.wmb.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.adityathakker.wmb.utils.AppConst;
import com.adityathakker.wmb.interfaces.ICopyAsyncCallback;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by adityajthakker on 21/6/16.
 */
public class CopyAsync extends AsyncTask<Void, Void, Void> {
    private static final String TAG = CopyAsync.class.getSimpleName();
    private Context context;
    private ProgressDialog progressDialog;
    private ICopyAsyncCallback copyAsyncCallback;

    public CopyAsync(Context context, ICopyAsyncCallback copyAsyncCallback) {
        this.context = context;
        this.copyAsyncCallback = copyAsyncCallback;
    }


    @Override
    protected Void doInBackground(Void... params) {
        String dbPath = AppConst.DB.DB_PATH + AppConst.DB.DB_NAME;

        try {
            SQLiteDatabase checkDB = context.openOrCreateDatabase(AppConst.DB.DB_NAME, Context.MODE_PRIVATE, null);
            if (checkDB != null) {
                checkDB.close();
            }

            InputStream inputStream = context.getApplicationContext().getAssets().open(AppConst.DB.DB_NAME);
            OutputStream fileOutputStream = new FileOutputStream(dbPath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
            inputStream.close();
            fileOutputStream.close();
            Log.d(TAG, "doInBackground: Dictionary File Has Been Copied");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "doInBackground: Error Occurred While Copying", e);
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        copyAsyncCallback.onCopyComplete();
    }
}
