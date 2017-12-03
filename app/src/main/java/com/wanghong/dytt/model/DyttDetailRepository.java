/*
 * Copyright (C) 2017 wanghong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wanghong.dytt.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.wanghong.dytt.DyttMovieItem;
import com.wanghong.dytt.DyttTVDramaItem;
import com.wanghong.dytt.JsoupEngine;
import com.wanghong.dytt.dao.DyttTVDramaItemDao;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by wanghong on 20/06/2017.
 */

@Singleton
public class DyttDetailRepository {

    private static final String TAG = "DyttDetailRepository";
    private final DyttDetailCache dyttDetailCache;
    private final DyttTVDramaItemDao dyttTVDramaItemDao;
    private final Executor executor;

    @Inject
    public DyttDetailRepository(DyttDetailCache dyttDetailCache, DyttTVDramaItemDao dyttTVDramaItemDao, Executor executor) {
        this.dyttDetailCache = dyttDetailCache;
        this.dyttTVDramaItemDao = dyttTVDramaItemDao;
        this.executor = executor;
    }

    public LiveData<List<DyttTVDramaItem>> getDyttTVDramaItems(final String url) {
        if (dyttDetailCache.getDyttTVDramaItems(url) != null) {
            Log.d(TAG, "getDyttTVDramaItems: fetch data from memory");
            return dyttDetailCache.getDyttTVDramaItems(url);
        }

        final MutableLiveData<List<DyttTVDramaItem>> data = new MutableLiveData<>();
        dyttDetailCache.put(url, ((LiveData) data));

        executor.execute(new Runnable() {
            @Override
            public void run() {
                DyttTVDramaItem dyttTVDramaItem = dyttTVDramaItemDao.queryItem(url);
                if (dyttTVDramaItem != null) {
                    Log.d(TAG, "getDyttTVDramaItems: fetch data from disk cache");
                    data.postValue(Collections.singletonList(dyttTVDramaItem));
                    return;
                }
                fetchDataFromNetwork(data, url);
            }
        });

        return data;
    }

    private void fetchDataFromNetwork(final MutableLiveData<List<DyttTVDramaItem>> data, final String url) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                new JsoupEngine<DyttTVDramaItem>().setResultClass(DyttTVDramaItem.class)
                        .setPageUrl(url)
                        .setJsoupEngineCallback(new JsoupEngine.JsoupEngineCallback<DyttTVDramaItem>() {
                            @Override
                            public void onJsoupParsed(final List<DyttTVDramaItem> results) {
                                if (results != null) {
                                    for (DyttTVDramaItem result : results) {
                                        result.setUrl(url);
                                    }

                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            dyttTVDramaItemDao.insertAll(results);
                                        }
                                    });
                                    data.setValue(results);
                                }
                            }
                        }).parseAsync();
                Log.d(TAG, "getDyttTVDramaItems: fetch data from network");
            }
        });
    }

    public LiveData<List<DyttMovieItem>> getDyttMovieItems(String url) {

        if (dyttDetailCache.getDyttMovieItems(url) != null) {
            return dyttDetailCache.getDyttMovieItems(url);
        }

        final MutableLiveData<List<DyttMovieItem>> data = new MutableLiveData<>();
        dyttDetailCache.put(url, ((LiveData) data));

        new JsoupEngine<DyttMovieItem>().setResultClass(DyttMovieItem.class)
                .setPageUrl(url)
                .setJsoupEngineCallback(new JsoupEngine.JsoupEngineCallback<DyttMovieItem>() {
                    @Override
                    public void onJsoupParsed(List<DyttMovieItem> results) {
                        data.setValue(results);
                    }
                }).parseAsync();

        return data;
    }
}
