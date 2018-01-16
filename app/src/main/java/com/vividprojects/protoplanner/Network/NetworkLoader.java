package com.vividprojects.protoplanner.Network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by p.kornilov on 16.01.2018.
 */

@Singleton
public class NetworkLoader {
    private OkHttpClient client;
    private Context context;

    @Inject
    public NetworkLoader(OkHttpClient client, Context context) {
        this.client = client;
        this.context = context;
    }

    public LiveData<Integer> load(String URL, String file_name) {
        final MutableLiveData<Integer> progress = new MutableLiveData<>();
        progress.setValue(0);

        final ProgressListener progressListener = new ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                int p = (int) ((100 * bytesRead) / contentLength);

                // Enable if you want to see the progress with logcat
                 Log.d("Test", "Progress: " + p + "%");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progress.postValue(p);
                if (done) {
                    Log.d("Test", "Done loading");
                }
            }
        };

        client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(),progressListener))
                                .build();
                    }
                })
                .build();


/*        client.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(),progressListener))
                        .build();
            }
        });*/

        LoadTask loadTask = new LoadTask(URL,file_name);
        loadTask.execute();

        return progress;
    }

    private static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source()  {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }

    interface ProgressListener {
        void update(long bytesRead, long contentLength, boolean done);
    }

    class LoadTask extends AsyncTask<Void,Void,Void> {
       // private final DataCallback<? super InputStream> callback;
        private String URL;
        private String file_name;

        LoadTask(String URL, String file_name) {
            this.URL = URL;
            this.file_name = file_name;
        }

        protected Void doInBackground(Void... params) {
            try {
                Request request = new Request.Builder().url(URL).build();
                Response response = client.newCall(request).execute();

                InputStream is = response.body().byteStream();

                BufferedInputStream input = new BufferedInputStream(is);
                OutputStream output = new FileOutputStream(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/"+file_name);

                byte[] data = new byte[1024];

                long total = 0;
                int count = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(InputStream is) {
            //callback.onDataReady(is);
            //callback.onLoadFailed();  //TODO Посмотреть где проверить!!!
        }
    }


}
