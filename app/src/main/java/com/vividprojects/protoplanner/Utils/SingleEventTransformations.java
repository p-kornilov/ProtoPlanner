package com.vividprojects.protoplanner.Utils;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Smile on 20.01.2018.
 */

public class SingleEventTransformations {

    private SingleEventTransformations() {
    }

    /**
     * Applies the given function on the main thread to each value emitted by {@code source}
     * LiveData and returns LiveData, which emits resulting values.
     * <p>
     * The given function {@code func} will be executed on the main thread.
     * <p>
     * Suppose that you have a LiveData, named {@code userLiveData}, that contains user data and you
     * need to display the user name, created by concatenating the first and the last
     * name of the user. You can define a function that handles the name creation, that will be
     * applied to every value emitted by {@code useLiveData}.
     *
     * <pre>
     * LiveData<User> userLiveData = ...;
     * LiveData<String> userName = Transformations.map(userLiveData, user -> {
     *      return user.firstName + " " + user.lastName
     * });
     * </pre>
     *
     * @param source a {@code LiveData} to listen to
     * @param func   a function to apply
     * @param <X>    a type of {@code source} LiveData
     * @param <Y>    a type of resulting LiveData.
     * @return a LiveData which emits resulting values
     */
    @MainThread
    public static <X, Y> LiveData<Y> map(@NonNull LiveData<X> source,
                                         @NonNull final Function<X, Y> func) {
        final MediatorLiveData<Y> result = new MediatorLiveData<>();
        result.addSource(source, new Observer<X>() {
            @Override
            public void onChanged(@Nullable X x) {
                result.setValue(func.apply(x));
            }
        });
        return result;
    }

    /**
     * Creates a LiveData, let's name it {@code swLiveData}, which follows next flow:
     * it reacts on changes of {@code trigger} LiveData, applies the given function to new value of
     * {@code trigger} LiveData and sets resulting LiveData as a "backing" LiveData
     * to {@code swLiveData}.
     * "Backing" LiveData means, that all events emitted by it will retransmitted
     * by {@code swLiveData}.
     * <p>
     * If the given function returns null, then {@code swLiveData} is not "backed" by any other
     * LiveData.
     *
     * <p>
     * The given function {@code func} will be executed on the main thread.
     *
     * <p>
     * Consider the case where you have a LiveData containing a user id. Every time there's a new
     * user id emitted, you want to trigger a request to get the user object corresponding to that
     * id, from a repository that also returns a LiveData.
     * <p>
     * The {@code userIdLiveData} is the trigger and the LiveData returned by the {@code
     * repository.getUserById} is the "backing" LiveData.
     * <p>
     * In a scenario where the repository contains User(1, "Jane") and User(2, "John"), when the
     * userIdLiveData value is set to "1", the {@code switchMap} will call {@code getUser(1)},
     * that will return a LiveData containing the value User(1, "Jane"). So now, the userLiveData
     * will emit User(1, "Jane"). When the user in the repository gets updated to User(1, "Sarah"),
     * the {@code userLiveData} gets automatically notified and will emit User(1, "Sarah").
     * <p>
     * When the {@code setUserId} method is called with userId = "2", the value of the {@code
     * userIdLiveData} changes and automatically triggers a request for getting the user with id
     * "2" from the repository. So, the {@code userLiveData} emits User(2, "John"). The LiveData
     * returned by {@code repository.getUserById(1)} is removed as a source.
     *
     * <pre>
     * MutableLiveData<String> userIdLiveData = ...;
     * LiveData<User> userLiveData = Transformations.switchMap(userIdLiveData, id ->
     *     repository.getUserById(id));
     *
     * void setUserId(String userId) {
     *      this.userIdLiveData.setValue(userId);
     * }
     * </pre>
     *
     * @param trigger a {@code LiveData} to listen to
     * @param func    a function which creates "backing" LiveData
     * @param <X>     a type of {@code source} LiveData
     * @param <Y>     a type of resulting LiveData
     */
    @MainThread
    public static <X, Y> SingleLiveEvent<Y> switchMap(@NonNull LiveData<X> trigger,
                                               @NonNull final Function<X, LiveData<Y>> func) {
        final MediatorLiveData<Y> result = new MediatorLiveData<>();
        SingleLiveEvent<Y> r = new SingleLiveEvent<>();
        result.addSource(trigger, new Observer<X>() {
            LiveData<Y> mSource;

            @Override
            public void onChanged(@Nullable X x) {
                LiveData<Y> newLiveData = func.apply(x);
                if (mSource == newLiveData) {
                    return;
                }
                if (mSource != null) {
                    result.removeSource(mSource);
                }
                mSource = newLiveData;
                if (mSource != null) {
                    result.addSource(mSource, new Observer<Y>() {
                        @Override
                        public void onChanged(@Nullable Y y) {
                            result.setValue(y);
                            r.setValue(y);
                        }
                    });
                }
            }
        });
        return r;
    }
}
