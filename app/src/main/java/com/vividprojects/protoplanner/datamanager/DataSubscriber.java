package com.vividprojects.protoplanner.datamanager;

import android.arch.lifecycle.MutableLiveData;

import com.vividprojects.protoplanner.coredata.Record;
import com.vividprojects.protoplanner.coredata.Variant;
import com.vividprojects.protoplanner.utils.DataQuery;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class DataSubscriber {
    private Map<String,MutableLiveData> subscribedPlains = new HashMap<>();

    private WeakReference<DataQuery<String,Record.Plain>> findRecord;

    public void init(DataQuery<String,Record.Plain> findRecord) {
        this.findRecord = new WeakReference<>(findRecord);
    }

    public <P> MutableLiveData<P> subscribePlain(Class<P> type, String id) {
        MutableLiveData<P> m = new MutableLiveData<>();
        subscribedPlains.put(id,m);
        return m;
    }

    public void unsubscribePlain(String id) {
        if (subscribedPlains.containsKey(id))
            subscribedPlains.remove(id);
    }

    public void updateSubscribedRecord(Variant.Plain vp) {
        if (vp != null && vp.masterRecord != null)
            for (String id : vp.masterRecord)
                if (subscribedPlains.containsKey(id)) {
                    if (findRecord.get() != null) {
                        Record.Plain r = findRecord.get().find(id);
                        if (r != null)
                            subscribedPlains.get(id).setValue(r);
                    }
                }
    }

    public void updateSubscribedRecord(Record.Plain rp) {
        if (rp != null && subscribedPlains.containsKey(rp.id))
            subscribedPlains.get(rp.id).setValue(rp);
    }

    public void updateSubscribedVariant(Variant.Plain vp) {
        updateSubscribedRecord(vp);
        if (vp != null && subscribedPlains.containsKey(vp.id))
            subscribedPlains.get(vp.id).setValue(vp);
    }

/*    public void unsubscribeRecord(String id) {
        if (subscribedRecords.containsKey(id))
            subscribedRecords.remove(id);
    }*/
}
