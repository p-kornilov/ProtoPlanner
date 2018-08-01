package com.vividprojects.protoplanner.datamanager;

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

import com.vividprojects.protoplanner.coredata.Block;
import com.vividprojects.protoplanner.coredata.Currency;
import com.vividprojects.protoplanner.coredata.Label;
import com.vividprojects.protoplanner.coredata.Measure;
import com.vividprojects.protoplanner.coredata.Resource;
import com.vividprojects.protoplanner.coredata.Variant;
import com.vividprojects.protoplanner.coredata.VariantInShop;
import com.vividprojects.protoplanner.db.LocalDataDB;
import com.vividprojects.protoplanner.db.NetworkDataDB;
import com.vividprojects.protoplanner.AppExecutors;
import com.vividprojects.protoplanner.coredata.Record;
import com.vividprojects.protoplanner.db.NetworkResponse;
import com.vividprojects.protoplanner.images.BitmapUtils;
import com.vividprojects.protoplanner.network.NetworkLoader;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.utils.Bundle2;
import com.vividprojects.protoplanner.utils.DataQuery;
import com.vividprojects.protoplanner.utils.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.ObjectChangeSet;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmObjectChangeListener;

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
    public static final String IMAGES_EXT = ".jpg";
    public static final String IMAGE_DEFAULT_ALTERNATIVE = "00000000-def1-0000-0000-alternative0";

    private Context context;
    private String imagesDirectory;

    private final NetworkDataDB networkDataDB;
    private LocalDataDB localDataDB = null;
    private final AppExecutors appExecutors;
    private final NetworkLoader networkLoader;
    private final DataSubscriber dataSubscriber;

    private Map<String,MutableLiveData<Record.Plain>> subscribedRecords = new HashMap<>();

    @Inject
    public DataRepository(Context context, AppExecutors appExecutors, LocalDataDB ldb, NetworkDataDB ndb, NetworkLoader networkLoader, DataSubscriber dataSubscriber){
        this.localDataDB = ldb;
        this.networkDataDB = ndb;
        this.appExecutors = appExecutors;
        this.networkLoader = networkLoader;
        this.context = context;
        this.dataSubscriber = dataSubscriber;
        this.dataSubscriber.init(functionLoadRecord);
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

    public static String toFullImage(String smallImage) {
        return smallImage.replace(IMAGES_SMALL, IMAGES_FULL);
    }

    public String getImagePath(String image) {
        return imagesDirectory + image + ".jpg";
    }

    public String getImagesDirectory() {
        return imagesDirectory;
    }

/*    public LiveData<Record.Plain> subscribeOnChangeRecord(String recordId) {
        final MutableLiveData<Record.Plain> result = new MutableLiveData<>();
        Record record = localDataDB
                .queryRecords()
                .id_equalTo(recordId)
                .findFirst();
        record.addChangeListener(new RealmObjectChangeListener<Record>() {
            @Override
            public void onChange(Record r, ObjectChangeSet changeSet) {
                result.setValue(r.getPlain());
            }
        });
        return result;
    }*/

    public LiveData<Resource<List<Record.Plain>>> loadRecords(List<String> filter) {
        return new NetworkBoundResource<List<Record.Plain>, List<Record.Plain>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<Record.Plain> item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable List<Record.Plain> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Record.Plain>> loadFromLocalDB() {
                MutableLiveData<List<Record.Plain>> ld = new MutableLiveData<>();
                List<Record> records;
                if (filter != null && filter.size() > 0)
                    records = localDataDB.queryRecords().labels_equalTo(filter).findAll();
                else
                    records = localDataDB.queryRecords().findAll();
                List<Record.Plain> recordsPlain = new ArrayList<>();
                if (records != null) {
                    for (Record r : records) {
                        Record.Plain rp = r.getPlain();
                        setFullImagePath(rp.mainVariant);
                        recordsPlain.add(rp);
                    }
                }
                ld.setValue(recordsPlain);
                return ld;
            }

            @NonNull
            @Override
            protected LiveData<NetworkResponse<List<Record.Plain>>> loadFromNetworkDB() {
                return new MutableLiveData<NetworkResponse<List<Record.Plain>>>();
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<Block.Plain>>> loadBlocks(List<String> filter) {
        return new NetworkBoundResource<List<Block.Plain>, List<Block.Plain>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<Block.Plain> item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable List<Block.Plain> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Block.Plain>> loadFromLocalDB() {
                MutableLiveData<List<Block.Plain>> ld = new MutableLiveData<>();
                List<Block> blocks;
/*                if (filter != null && filter.size() > 0)
                    blocks = localDataDB.queryBlocks().labels_equalTo(filter).findAll();
                else*/
                    blocks = localDataDB.queryBlocks().findAll();
                List<Block.Plain> blocksPlain = new ArrayList<>();
                if (blocks != null) {
                    for (Block b : blocks) {
                        Block.Plain bp = b.getPlain();
                        blocksPlain.add(bp);
                    }
                }
                ld.setValue(blocksPlain);
                return ld;
            }

            @NonNull
            @Override
            protected LiveData<NetworkResponse<List<Block.Plain>>> loadFromNetworkDB() {
                return new MutableLiveData<NetworkResponse<List<Block.Plain>>>();
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
                for (Variant.Plain v : record.variants) {
                    for (int i = 0; i < v.small_images.size(); i++)
                        v.small_images.set(i, imagesDirectory + IMAGES_FULL + v.small_images.get(i) + ".jpg");
                    for (int i = 0; i < v.full_images.size(); i++)
                        v.full_images.set(i, imagesDirectory + IMAGES_SMALL + v.full_images.get(i) + ".jpg");
                }
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

    private DataQuery<String, Record.Plain> functionLoadRecord = (id) -> {
        Record record = localDataDB.queryRecords().id_equalTo(id).findFirst();
        if (record != null) {
            Record.Plain rp = record.getPlain();
            setFullImagePath(rp.mainVariant);
            return rp;
        } else
            return null;
    };

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
                Variant.Plain vPlain;
                Variant variant = localDataDB
                        .queryVariants()
                        .id_equalTo(id)
                        .findFirst();
                if (variant != null)
                    vPlain = variant.getPlain();
                else {
                    vPlain = Variant.Plain.createPlain(
                            localDataDB.queryCurrency().getBase().getPlain(),
                            localDataDB.queryMeasure()
                                    .systemEqualTo(Settings.getMeasureSystem(context))
                                    .measureEqualTo(Measure.MEASURE_UNIT)
                                    .isDefault()
                                    .findFirst()
                                    .getPlain());
                }
                for (int i = 0;i<vPlain.small_images.size();i++) vPlain.small_images.set(i, imagesDirectory + "/img_s_" + vPlain.small_images.get(i) + ".jpg");
                for (int i = 0;i<vPlain.full_images.size();i++) vPlain.full_images.set(i, imagesDirectory + "/img_f_" + vPlain.full_images.get(i) + ".jpg");
                ld.setValue(vPlain);
                return ld;
            }

            @NonNull
            @Override
            protected LiveData<NetworkResponse<Variant.Plain>> loadFromNetworkDB() {
                return new MutableLiveData<NetworkResponse<Variant.Plain>>();
            }
        }.asLiveData();
    }

    public LiveData<Resource<VariantInShop.Plain>> loadShop(String id) {
        return new NetworkBoundResource<VariantInShop.Plain, VariantInShop.Plain>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull VariantInShop.Plain item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable VariantInShop.Plain data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<VariantInShop.Plain> loadFromLocalDB() {
                MutableLiveData<VariantInShop.Plain> ld = new MutableLiveData<>();
                VariantInShop.Plain sPlain;
                VariantInShop shop = localDataDB
                        .queryShop()
                        .id_equalTo(id)
                        .findFirst();
                if (shop != null)
                    sPlain = shop.getPlain();
                else
                    sPlain = VariantInShop.Plain.createPlain(localDataDB.queryCurrency().getBase().getPlain());
                ld.setValue(sPlain);
                return ld;
            }

            @NonNull
            @Override
            protected LiveData<NetworkResponse<VariantInShop.Plain>> loadFromNetworkDB() {
                return new MutableLiveData<NetworkResponse<VariantInShop.Plain>>();
            }
        }.asLiveData();
    }

    public void deleteShop(String id) {
        localDataDB.deleteShop(id);
    }

    public void deleteVariant(String id) {
        localDataDB.deleteVariant(id);
    }

    public void deleteRecord(String id) {
        localDataDB.deleteRecord(id);
    }

    public String saveVariant(String id, String name, double price, double count, int currency, int measure) {
        Variant.Plain vp = localDataDB.saveVariant(id, name, price, count, currency, measure);
        dataSubscriber.updateSubscribedVariant(vp);
        return vp.id;
    }

    public LiveData<Record.Plain> newRecord(String recordName) {
        MutableLiveData<Record.Plain> rId = new MutableLiveData<>();
        Record.Plain r = localDataDB.newRecord(recordName);
        if (r != null) {
            rId.setValue(r);
            return rId;
        } else
            return null;

    }

    public void setDefaultImage(String variantId , int image) {
        dataSubscriber.updateSubscribedVariant(localDataDB.setDefaultImage(variantId, image));
    }

    public String saveShop(VariantInShop.Plain shop, String variantId, boolean asPrimary) {
        return localDataDB.saveShop(shop, variantId, asPrimary);
    }

    public void setShopPrimary(String shopId, String variantId) {
        dataSubscriber.updateSubscribedVariant(localDataDB.setShopPrimary(shopId, variantId));
    }

    public LiveData<String> setBasicVariant(String recordId, String variantId) {
        Record.Plain rp = localDataDB.setBasicVariant(recordId, variantId);
        setFullImagePath(rp.mainVariant);
        dataSubscriber.updateSubscribedRecord(rp);
        MutableLiveData<String> vId = new MutableLiveData<>();
        vId.setValue(variantId);
        return vId;
    }

    public void createBasicVariant(String recordId, String variantId) {
        Record.Plain rp = localDataDB.createBasicVariant(recordId, variantId);
        setFullImagePath(rp.mainVariant);
        dataSubscriber.updateSubscribedRecord(rp);
    }

    public void saveMainVariantToRecord(String variantId, String recordId) {
        dataSubscriber.updateSubscribedRecord(localDataDB.saveMainVariantToRecord(variantId, recordId));
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
                        .id_equalTo(id)
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

    public LiveData<Bundle2<List<String>,Integer>> loadImagesForVariant2(String id) {
                MutableLiveData<Bundle2<List<String>,Integer>> ld = new MutableLiveData<>();
                Bundle2<List<String>,Integer> bundle = new Bundle2<>();
                Variant.Plain variant = localDataDB
                        .queryVariants()
                        .id_equalTo(id)
                        .findFirst()
                        .getPlain();
                for (int i = 0;i<variant.full_images.size();i++) variant.full_images.set(i, imagesDirectory + "/img_f_" + variant.full_images.get(i) + ".jpg");
                bundle.first = variant.full_images;
                bundle.second = variant.defaultImage;
                ld.setValue(bundle);
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

    private void setFullImagePath(Variant.Plain variant) {
        if (variant != null) {
            for (int i = 0; i < variant.full_images.size(); i++)
                variant.full_images.set(i, imagesDirectory + IMAGES_FULL + variant.full_images.get(i) + IMAGES_EXT);
            for (int i = 0; i < variant.small_images.size(); i++)
                variant.small_images.set(i, imagesDirectory + IMAGES_SMALL + variant.small_images.get(i) + IMAGES_EXT);
        }
    }

    public LiveData<Record.Plain> setRecordName(String id, String name) {
        MutableLiveData<Record.Plain> record = new MutableLiveData<>();
        Record.Plain rp = localDataDB.setRecordName(id,name);
        setFullImagePath(rp.mainVariant);
        record.setValue(rp);

        dataSubscriber.updateSubscribedRecord(rp);

        return record;
    }

/*    public LiveData<Record.Plain> subscribeRecordPlain(String id) {
        MutableLiveData<Record.Plain> ld = new MutableLiveData<>();
        Record record = localDataDB
                .queryRecords()
                .id_equalTo(id)
                .findFirst();
        if (record != null) {
            Record.Plain rp = record.getPlain();
            setFullImagePath(rp.mainVariant);
            for (Variant.Plain v : rp.variants)
                setFullImagePath(v);
            ld.setValue(rp);
            subscribedRecords.put(id,ld);
            return ld;
        } else
            return null;
    }*/

    public LiveData<String> setRecordComment(String id, String name) {
        MutableLiveData<String> recordComment = new MutableLiveData<>();
        recordComment.setValue(localDataDB.setRecordComment(id,name));
        return recordComment;
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
        Record.Plain rp = localDataDB.saveLabelsForRecord(recordItemId,ids);
        setFullImagePath(rp.mainVariant);

        dataSubscriber.updateSubscribedRecord(rp);
    };

    public void deleteLabel(String id) {
        localDataDB.deleteLabel(id);
    }

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
        localDataDB.initDB(imagesDirectory);
    }

    public void showDB(){
        localDataDB.showDB();
    }

    public void initImages() {
        Log.d("Test", "External Storage - " + getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
    }

    public String saveImageFromURLtoVariant(String url, String variant, MutableLiveData<Integer> progress) {

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

                Variant.Plain vp = localDataDB.addImageToVariant(variant, file_name); // сделать проверку
                if (vp != null && vp.small_images.size() == 1)
                    dataSubscriber.updateSubscribedVariant(vp);
                BitmapUtils.deleteImageFile(context, temp_name);
                progress.setValue(SAVE_TO_DB_DONE);
            });
            else appExecutors.mainThread().execute(()-> {
                progress.setValue(LOAD_ERROR);
            });
        });

        return thumb_name;
    }

    public String saveImageFromCameratoVariant(String temp_name, String variant, MutableLiveData<Integer> progress) {

        progress.setValue(LOAD_DONE);

        String file_name = UUID.randomUUID().toString();
        String full_name = imagesDirectory + "/img_f_" + file_name + ".jpg";
        String thumb_name = imagesDirectory + "/img_s_" + file_name + ".jpg";

        appExecutors.diskIO().execute(()-> {
            boolean success = BitmapUtils.saveImage(context,BitmapUtils.resamplePic(context,temp_name),full_name,true);
            if (success)
                success = BitmapUtils.saveImage(context,BitmapUtils.resamplePic(context,temp_name,256,256),thumb_name,false);
            if (success)
                BitmapUtils.deleteImageFile(context,temp_name);

            if (success)
                appExecutors.mainThread().execute(()-> {
                    Variant.Plain vp = localDataDB.addImageToVariant(variant, file_name); // сделать проверку
                    if (vp != null && vp.small_images.size() == 1)
                        dataSubscriber.updateSubscribedVariant(vp);
                    progress.setValue(SAVE_TO_DB_DONE);
                });
            else appExecutors.mainThread().execute(()-> {
                progress.setValue(LOAD_ERROR);
            });
        });

        return thumb_name;
    }

    public String saveImageFromGallerytoVariant(Uri temp_name_uri, String variant, MutableLiveData<Integer> progress) {

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
                    Variant.Plain vp = localDataDB.addImageToVariant(variant, file_name); // сделать проверку
                    if (vp != null && vp.small_images.size() == 1)
                        dataSubscriber.updateSubscribedVariant(vp);
                    progress.setValue(SAVE_TO_DB_DONE);
                });
            else appExecutors.mainThread().execute(()-> {
                progress.setValue(LOAD_ERROR);
            });
        });

        return thumb_name;
    }

    public String getDefaultVariantImage() {
        return imagesDirectory + IMAGES_SMALL + IMAGE_DEFAULT_ALTERNATIVE + ".jpg";
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

    public boolean attachVariantToRecord(String variantId, String recordId) {
        return localDataDB.attachVariantToRecord(variantId, recordId);
    }

}

