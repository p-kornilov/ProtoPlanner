package com.vividprojects.protoplanner.DataManager;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.CoreData.Resource;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.DB.LocalDataDB;
import com.vividprojects.protoplanner.DB.NetworkDataDB;
import com.vividprojects.protoplanner.AppExecutors;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.DB.NetworkResponse;
import com.vividprojects.protoplanner.Images.BitmapUtils;
import com.vividprojects.protoplanner.Network.NetworkLoader;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Smile on 05.12.2017.
 */

@Singleton
public class DataRepository {
    public static final int SAVE_TO_DB_DONE = 300;
    public static final int CONVERT_DONE = 202;
    public static final int LOAD_DONE = 200;
    public static final int LOAD_ERROR = -1;

    public static final String IMAGES_FULL = "/img_f_";
    public static final String IMAGES_SMALL = "/img_s_";

    private Context context;
    private String imagesDirectory;

    private final NetworkDataDB networkDataDB;
    private final LocalDataDB localDataDB;
    private final AppExecutors appExecutors;
    private final NetworkLoader networkLoader;

    @Inject
    public DataRepository(Context context, AppExecutors appExecutors, LocalDataDB ldb, NetworkDataDB ndb, NetworkLoader networkLoader){
        this.localDataDB = ldb;
        this.networkDataDB = ndb;
        this.appExecutors = appExecutors;
        this.networkLoader = networkLoader;
        this.context = context;
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (dir != null)
            imagesDirectory = dir.getAbsolutePath(); //TODO Проверить на Null и подготовить несколько вариантов директорий

/*        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ProtoPlanner");
//        boolean success = true;
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        imagesDirectory = storageDir.getAbsolutePath();
        Log.d("Test", "External Storage - " + imagesDirectory);*/
    }

    public String getImagesDirectory() {
        return imagesDirectory;
    }

    public LiveData<Resource<List<Record>>> loadRecords(List<String> filter) {
        return new NetworkBoundResource<List<Record>, List<Record>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<Record> item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable List<Record> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Record>> loadFromLocalDB() {
                MutableLiveData<List<Record>> ld = new MutableLiveData<>();
                if (filter != null && filter.size() > 0)
                    ld.setValue(localDataDB.queryRecords().labels_equalTo(filter).findAll());
                else
                    ld.setValue(localDataDB.queryRecords().findAll());
                return ld;
            }

            @NonNull
            @Override
            protected LiveData<NetworkResponse<List<Record>>> loadFromNetworkDB() {
                return new MutableLiveData<NetworkResponse<List<Record>>>();
            }
        }.asLiveData();
    }

    public LiveData<Resource<Record.Plain>> loadRecord(String id) {
        return new NetworkBoundResource<Record.Plain, Record.Plain>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Record.Plain item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Record.Plain data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Record.Plain> loadFromLocalDB() {
                MutableLiveData<Record.Plain> ld = new MutableLiveData<>();
                Record.Plain record = localDataDB
                        .queryRecords()
                        .id_equalTo(id)
                        .findFirst()
                        .getPlain();
                ld.setValue(record);
                return ld;
            }

            @NonNull
            @Override
            protected LiveData<NetworkResponse<Record.Plain>> loadFromNetworkDB() {
                return new MutableLiveData<NetworkResponse<Record.Plain>>();
            }
        }.asLiveData();
    }

    public LiveData<Resource<Variant.Plain>> loadVariant(String id) {
        return new NetworkBoundResource<Variant.Plain, Variant.Plain>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Variant.Plain item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Variant.Plain data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Variant.Plain> loadFromLocalDB() {
                MutableLiveData<Variant.Plain> ld = new MutableLiveData<>();
                Variant.Plain variant = localDataDB
                        .queryVariants()
                        .title_equalTo(id)
                        .findFirst()
                        .getPlain();
                for (int i = 0;i<variant.small_images.size();i++) variant.small_images.set(i, imagesDirectory + "/img_s_" + variant.small_images.get(i) + ".jpg");
                for (int i = 0;i<variant.full_images.size();i++) variant.full_images.set(i, imagesDirectory + "/img_f_" + variant.full_images.get(i) + ".jpg");
                ld.setValue(variant);
                return ld;
            }

            @NonNull
            @Override
            protected LiveData<NetworkResponse<Variant.Plain>> loadFromNetworkDB() {
                return new MutableLiveData<NetworkResponse<Variant.Plain>>();
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<String>>> loadImagesForVariant(String id) {
        return new NetworkBoundResource<List<String>, List<String>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<String> item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable List<String> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<String>> loadFromLocalDB() {
                MutableLiveData<List<String>> ld = new MutableLiveData<>();
                Variant.Plain variant = localDataDB
                        .queryVariants()
                        .title_equalTo(id)
                        .findFirst()
                        .getPlain();
                for (int i = 0;i<variant.full_images.size();i++) variant.full_images.set(i, imagesDirectory + "/img_f_" + variant.full_images.get(i) + ".jpg");
                ld.setValue(variant.full_images);
                return ld;
            }

            @NonNull
            @Override
            protected LiveData<NetworkResponse<List<String>>> loadFromNetworkDB() {
                return new MutableLiveData<NetworkResponse<List<String>>>();
            }
        }.asLiveData();
    }

    public LiveData<List<String>> loadImagesForVariant2(String id) {
                MutableLiveData<List<String>> ld = new MutableLiveData<>();
                Variant.Plain variant = localDataDB
                        .queryVariants()
                        .title_equalTo(id)
                        .findFirst()
                        .getPlain();
                for (int i = 0;i<variant.full_images.size();i++) variant.full_images.set(i, imagesDirectory + "/img_f_" + variant.full_images.get(i) + ".jpg");
                ld.setValue(variant.full_images);
                return ld;
    }

    public LiveData<Label.Plain> createLabel(Label.Plain label) {
        MutableLiveData<Label.Plain> labelId = new MutableLiveData<>();
        labelId.setValue(localDataDB.createLabel(label));
        return labelId;
    }

    public LiveData<Label.Plain> editLabel(Label.Plain label) {
        MutableLiveData<Label.Plain> labelId = new MutableLiveData<>();
        labelId.setValue(localDataDB.editLabel(label));
        return labelId;
    }

    public LiveData<String> setRecordName(String id, String name) {
        MutableLiveData<String> recordName = new MutableLiveData<>();
        recordName.setValue(localDataDB.setRecordName(id,name));
        return recordName;
    }

    public LiveData<List<Label.Plain>> getLabels(MutableLiveData<List<Label.Plain>> labels) {
        List<Label> labelsL = localDataDB
                .queryLabels()
                .findAll();
        ArrayList<Label.Plain> al = new ArrayList<>();
        for (Label label:labelsL) {
            al.add(label.getPlain());
        }
        labels.setValue(al);
        return labels;
    }

    public LiveData<List<Label.Plain>> getLabels() {
        MutableLiveData<List<Label.Plain>> labels = new MutableLiveData<>();
        List<Label> labelsL = localDataDB
                .queryLabels()
                .findAll();
        if (labelsL!=null) {
            ArrayList<Label.Plain> al = new ArrayList<>();
            for (Label label : labelsL) {
                al.add(label.getPlain());
            }
            labels.setValue(al);
        }
        return labels;
    }

    public LiveData<List<Label.Plain>> getRecordLabels(String id) {
        MutableLiveData<List<Label.Plain>> labels = new MutableLiveData<>();
        Record record = localDataDB
                .queryRecords()
                .id_equalTo(id)
                .findFirst();

        if (record!=null) {
            labels.setValue(record.getPlain().labels);
        }
        return labels;
    }

    //---------------- Currency --------------------------------------------------------------
    public LiveData<List<Currency.Plain>> getCurrencies() {
        MutableLiveData<List<Currency.Plain>> currencies = new MutableLiveData<>();
        List<Currency> curL = localDataDB
                .queryCurrency()
                .findAll();
        if (curL!=null) {
            ArrayList<Currency.Plain> curs = new ArrayList<>();
            for (Currency cur : curL) {
                curs.add(cur.getPlain());
            }
            currencies.setValue(curs);
        }
        return currencies;
    }

    public LiveData<Currency.Plain> getCurrency(int iso_code) {
        MutableLiveData<Currency.Plain> currency = new MutableLiveData<>();
        Currency currencyF = localDataDB
                .queryCurrency()
                .iso_code_equalTo(iso_code)
                .findFirst();
        if (currencyF!=null) {
            currency.setValue(currencyF.getPlain());
        }
        return currency;
    }

    public LiveData<Currency.Plain> getCurrencyBase() {
        MutableLiveData<Currency.Plain> currency = new MutableLiveData<>();
        Currency currencyF = localDataDB
                .queryCurrency()
                .getBase();
        if (currencyF!=null) {
            currency.setValue(currencyF.getPlain());
        }
        return currency;
    }

    public int saveCurrency(Currency.Plain currency) {
        return localDataDB.saveCurrency(currency);
    }


    public void deleteCurrency(int iso_code_int) {
        localDataDB.deleteCurrency(iso_code_int);
    }

    public void setDefaultCurrency(int iso_code_int) {
        localDataDB.setDefaultCurrency(iso_code_int);
    }
    //---------------------------------------------------------------------------------------

    //---------------- Measure --------------------------------------------------------------
    public LiveData<List<Measure.Plain>> getMeasures(int system) {
        MutableLiveData<List<Measure.Plain>> measures = new MutableLiveData<>();
        List<Measure> mL = localDataDB
                .queryMeasure()
                .systemEqualTo(system)
                .findAll();
        if (mL!=null) {
            ArrayList<Measure.Plain> ms = new ArrayList<>();
            for (Measure m : mL) {
                ms.add(m.getPlain());
            }
            measures.setValue(ms);
        }
        return measures;
    }

    public LiveData<List<Measure.Plain>> getMeasures() {
        return getMeasures(Settings.getMeasureSystem(context));
    }

    public LiveData<Measure.Plain> getMeasure(int hash) {
        MutableLiveData<Measure.Plain> measure = new MutableLiveData<>();
        Measure mF = localDataDB
                .queryMeasure()
                .hashEqualTo(hash)
                .findFirst();
        if (mF!=null) {
            measure.setValue(mF.getPlain());
        }
        return measure;
    }

    public int saveMeasure(Measure.Plain measure) {
        return localDataDB.saveMeasure(measure);
    }

    public void deleteMeasure(int hash) {
        localDataDB.deleteMeasure(hash);
    }

    public void setDefaultMeasure(int hash) {
        localDataDB.setDefaultMeasure(hash);
    }
    //---------------------------------------------------------------------------------------

    public void saveLabelsForRecord(String recordItemId,String[] ids) {
        localDataDB.saveLabelsForRecord(recordItemId,ids);
    };

    public void deleteLabel(String id) {
        localDataDB.deleteLabel(id);
    }

/*    public LiveData<Resource<String>> loadVariantImages(String title) {
        return new NetworkBoundResource<String, String>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull String item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable String data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<String> loadFromLocalDB() {
                MutableLiveData<String> ld = new MutableLiveData<>();
                ld.setValue(localDataDB.queryRecords().id_equalTo(id).findFirst());
                return ld;
            }

            @NonNull
            @Override
            protected LiveData<NetworkResponse<Record>> loadFromNetworkDB() {
                return new MutableLiveData<NetworkResponse<Record>>();
            }
        }.asLiveData();
    }*/

    public int getHeight() {return context.getResources().getConfiguration().screenHeightDp;}

    public Context getContext() {return context;}

    public void setContext(Context context) {
        this.context = context;
    }

    public String getType() {
        boolean isTablet = context.getResources().getBoolean(R.bool.isTablet);
        if (isTablet) return "This is tablet";
        else return "This is phone";
    }

    public void initDB(){
        localDataDB.initDB();
    }

    public void showDB(){
        localDataDB.showDB();
    }

    public void initImages() {
        Log.d("Test", "External Storage - " + getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        //fileManager.test();
   /*     BaseTarget target = new FullTarget("test.jpg");
        GlideApp.with(context)
                //  .load("http://anub.ru/uploads/07.2015/976_podborka_34.jpg")
                // .load(R.raw.testpicture)
                // .load("img_testpicture.jpg")
                .load(new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),"img_testpicture.jpg"))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                //.into(iv);
                .into(target);*/
    }

    public String saveImageFromURLtoVariant(String url, String variant, MutableLiveData<Integer> progress, Runnable onDone) {

        String file_name = UUID.nameUUIDFromBytes(url.getBytes()).toString();
        String full_name = imagesDirectory + "/img_f_" + file_name + ".jpg";
        String thumb_name = imagesDirectory + "/img_s_" + file_name + ".jpg";
        String temp_name = imagesDirectory + "/img_t_" + file_name;

        networkLoader.loadImage(url, temp_name, progress, ()->{
            Log.d("Test", "Done loading in Repository!!!");

            boolean success = BitmapUtils.saveImage(context, BitmapFactory.decodeFile(temp_name),full_name,true);
            if (success)
                success = BitmapUtils.saveImage(context,BitmapUtils.resamplePic(context,temp_name,256,256),thumb_name,false);

            if (success)
            appExecutors.mainThread().execute(()-> {
                progress.setValue(CONVERT_DONE);

                localDataDB.addImageToVariant(variant, file_name); // сделать проверку
                BitmapUtils.deleteImageFile(context, temp_name);
                progress.setValue(SAVE_TO_DB_DONE);
                onDone.run();
            });
            else appExecutors.mainThread().execute(()-> {
                progress.setValue(LOAD_ERROR);
                onDone.run();
            });
        });

        return thumb_name;
    }

/*    public String getImageName(String url) {
        return imagesDirectory + "/img_f_" + UUID.nameUUIDFromBytes(url.getBytes()).toString() + ".jpg";
    }

    public String getImageName() {
        return UUID.randomUUID().toString();
    }*/

    public String saveImageFromCameratoVariant(String temp_name, String variant, MutableLiveData<Integer> progress, Runnable onDone) {

        progress.setValue(LOAD_DONE);

        String file_name = UUID.randomUUID().toString();
        String full_name = imagesDirectory + "/img_f_" + file_name + ".jpg";
        String thumb_name = imagesDirectory + "/img_s_" + file_name + ".jpg";

/*        RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progress.setValue(LOAD_ERROR);
                return false;
            }
            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        };*/

        appExecutors.diskIO().execute(()-> {

/*            appExecutors.mainThread().execute(()-> {
                BaseTarget target = new FullTarget(full_name,
                        () -> {
                            progress.setValue(LOAD_ERROR);
                        },
                        () -> {
                            if (progress.createValue() != LOAD_ERROR)
                                progress.setValue(progress.createValue() + 1);
                        });

                GlideApp.with(context)
                        .load(bitmap)
                        .listener(requestListener)
                        .into(target);

                target = new ThumbnailTarget(thumb_name,
                        () -> {
                            progress.setValue(LOAD_ERROR);
                        },
                        () -> {
                            if (progress.createValue() != LOAD_ERROR)
                                progress.setValue(progress.createValue() + 1);
                        });

                GlideApp.with(context)
                        .load(bitmap)
                        .listener(requestListener)
                        .into(target);
            });*/

            boolean success = BitmapUtils.saveImage(context,BitmapUtils.resamplePic(context,temp_name),full_name,true);
            if (success)
                success = BitmapUtils.saveImage(context,BitmapUtils.resamplePic(context,temp_name,256,256),thumb_name,false);
            if (success)
                BitmapUtils.deleteImageFile(context,temp_name);

/*            while (progress.createValue() != CONVERT_DONE) { // Wait while images is saved
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };*/

            if (success)
                appExecutors.mainThread().execute(()-> {
                    localDataDB.addImageToVariant(variant, file_name); // сделать проверку
                    progress.setValue(SAVE_TO_DB_DONE);
                    onDone.run();
                });
            else appExecutors.mainThread().execute(()-> {
                progress.setValue(LOAD_ERROR);
                onDone.run();
            });
        });

        return thumb_name;
    }

    public String saveImageFromGallerytoVariant(Uri temp_name_uri, String variant, MutableLiveData<Integer> progress, Runnable onDone) {

        progress.setValue(LOAD_DONE);

        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(temp_name_uri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String temp_name = cursor.getString(columnIndex);
        cursor.close();

        String file_name = UUID.randomUUID().toString();
        String full_name = imagesDirectory + "/img_f_" + file_name + ".jpg";
        String thumb_name = imagesDirectory + "/img_s_" + file_name + ".jpg";

        appExecutors.diskIO().execute(()-> {

            boolean success = BitmapUtils.saveImage(context,BitmapFactory.decodeFile(temp_name),full_name,false);
            if (success)
                success = BitmapUtils.saveImage(context,BitmapUtils.resamplePic(context,temp_name,256,256),thumb_name,false);

            if (success)
                appExecutors.mainThread().execute(()-> {
                    localDataDB.addImageToVariant(variant, file_name); // сделать проверку
                    progress.setValue(SAVE_TO_DB_DONE);
                    onDone.run();
                });
            else appExecutors.mainThread().execute(()-> {
                progress.setValue(LOAD_ERROR);
                onDone.run();
            });
        });

        return thumb_name;
    }

    public LiveData<String> saveImageFromGallery(Uri temp_name_uri) {
        MutableLiveData<String> imageName = new MutableLiveData<>();

        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(temp_name_uri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String temp_name = cursor.getString(columnIndex);
        cursor.close();

        String file_name = UUID.randomUUID().toString();
        String full_name = imagesDirectory + "/" + file_name + ".jpg";

        appExecutors.diskIO().execute(()-> {

            boolean success = BitmapUtils.saveImage(context,BitmapFactory.decodeFile(temp_name),full_name,false);

            if (success)
                imageName.postValue(file_name);
        });

        return imageName;
    }

/*
    public String saveImageFromURL(String URL, ProgressBar bar) {

        final ProgressBar progressBar = bar;

        String file_name = UUID.nameUUIDFromBytes(URL.getBytes()).toString() + ".jpg";
        Log.d("Test", "UUID - " + file_name);
        Target target = new ThumbnailTarget("img_s_"+file_name,context);

        Glide.with(context).
                .register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
        Glide.with(context)
                .load(URL)
                // Disabling cache to see download progress with every app load
                // You may want to enable caching again in production
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }*/

}

