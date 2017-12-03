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

import com.wanghong.dytt.DyttMovieItem;
import com.wanghong.dytt.DyttTVDramaItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

/**
 * Created by wanghong on 20/06/2017.
 */

@Singleton
public class DyttDetailCache {

    private Map<String, LiveData<List<Object>>> dyttDetailItems = new HashMap<>();

    public LiveData<List<DyttTVDramaItem>> getDyttTVDramaItems(String url) {
        return ((MutableLiveData) dyttDetailItems.get(url));
    }

    public LiveData<List<DyttMovieItem>> getDyttMovieItems(String url) {
        return ((MutableLiveData) dyttDetailItems.get(url));
    }

    public void put(String url, LiveData<List<Object>> data) {
        dyttDetailItems.put(url, data);
    }
}
