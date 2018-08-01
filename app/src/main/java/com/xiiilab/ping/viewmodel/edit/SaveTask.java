package com.xiiilab.ping.viewmodel.edit;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import com.xiiilab.ping.persistance.HostEntity;
import com.xiiilab.ping.repository.Repository;

import java.util.Arrays;
import java.util.List;

/**
 * Created by XIII-th on 31.07.2018
 */
class SaveTask extends AsyncTask<HostEntity, Void, Boolean> {

    private final MutableLiveData<Boolean> mResult = new MutableLiveData<>();
    private final List<Function<HostEntity, Boolean>> mCheckList;
    private final Repository mRepository;

    @SafeVarargs
    public SaveTask(Repository repository, Function<HostEntity, Boolean>... checkList) {
        mRepository = repository;
        mCheckList = Arrays.asList(checkList);
    }

    public void startWith(@Nullable HostEntity entity) {
        if (entity != null)
            execute(entity);
    }

    public LiveData<Boolean> getSaveResult() {
        return mResult;
    }

    @Override
    protected Boolean doInBackground(HostEntity... entities) {
        boolean save = true;
        for (Function<HostEntity, Boolean> checkFunction : mCheckList)
            if (isCancelled())
                return Boolean.FALSE;
            else
                save &= checkFunction.apply(entities[0]);

        save &= !isCancelled();
        if (save)
            mRepository.insert(entities[0]);
        return save;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mResult.setValue(result);
    }
}
