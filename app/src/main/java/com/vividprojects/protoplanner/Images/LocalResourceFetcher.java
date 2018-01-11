package com.vividprojects.protoplanner.Images;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by Smile on 10.01.2018.
 */

public class LocalResourceFetcher implements DataFetcher<InputStream> {

    private InputStream inputStream;
    private final Context context;
    private final int id;


    LocalResourceFetcher(Context context, int id) {
        this.context = context;
        this.id = id;
    }

    @Override
    public void loadData(Priority priority, DataCallback<? super InputStream> callback) {
       // inputStream = ...
        LoadTask loadTask = new LoadTask(callback);
        loadTask.execute();
/*        inputStream = context.getResources().openRawResource(id);
        callback.onDataReady(inputStream);*/
    }

    @Override
    public void cleanup() {
        try {
            inputStream.close();
        } catch (IOException e) {}
        finally {
            inputStream = null;
        }
    }

    @Override
    public void cancel() {}

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }

    class LoadTask extends AsyncTask<Void,Void,InputStream> {
        private final DataCallback<? super InputStream> callback;

        LoadTask(DataCallback<? super InputStream> callback) {
            this.callback = callback;
        }

        protected InputStream doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            inputStream = context.getResources().openRawResource(id);
            return inputStream;
        }

        protected void onPostExecute(InputStream is) {
            callback.onDataReady(is);
            //callback.onLoadFailed();  //TODO Посмотреть где проверить!!!
        }
    }

}
