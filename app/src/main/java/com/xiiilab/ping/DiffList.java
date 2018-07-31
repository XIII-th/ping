package com.xiiilab.ping;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

/**
 * Created by XIII-th on 23.07.2018
 */
public class DiffList<T> {

    private List<T> mData;
    private List<T> mNewData;

    public DiffList() {
        mData = Collections.emptyList();
    }

    public T get(int index) {
        return mData.get(index);
    }

    public int size() {
        return mData.size();
    }

    public DiffList<T> beginMigration(@NonNull List<T> newData) {
        if (mNewData != null)
            throw new IllegalStateException("Previous migration is not completed");
        mNewData = newData;
        return this;
    }

    public DiffList<T> onInsert(@NonNull DiffConsumer<T> consumer) {
        searchDiff(mNewData, mData, consumer);
        return this;
    }

    public DiffList<T> onRemove(@NonNull DiffConsumer<T> consumer) {
        searchDiff(mData, mNewData, consumer);
        return this;
    }

    public void completeMigration() {
        mData = mNewData;
        mNewData = null;
    }

    private void searchDiff(List<T> outer, List<T> inner, DiffConsumer<T> diffConsumer) {
        outer:
        for (int i = 0; i < outer.size(); i++) {
            T oData = outer.get(i);
            for (T iData : inner)
                if (oData.equals(iData))
                    continue outer;
            diffConsumer.consume(i, oData);
        }
    }

    public interface DiffConsumer<T> {
        void consume(int position, T data);
    }
}
