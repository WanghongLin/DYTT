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

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.wanghong.dytt.DyttDetailActivity;
import com.wanghong.dytt.DyttMovieItem;
import com.wanghong.dytt.DyttTVDramaItem;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by wanghong on 20/06/2017.
 */

public class DyttDetailViewModel extends AndroidViewModel {

    private LiveData<List<DyttMovieItem>> dyttMovieItems;
    private LiveData<List<DyttTVDramaItem>> dyttTVDramaItems;

    @Inject
    DyttDetailRepository dyttDetailRepository;

    @Inject
    public DyttDetailViewModel(Application application) {
        super(application);
    }

    public void initViewModel(int type, String url) {
        switch (type) {
            case DyttDetailActivity.TYPE_MOVIE:
                if (dyttMovieItems != null) {
                    return;
                }

                dyttMovieItems = dyttDetailRepository.getDyttMovieItems(url);
                break;
            case DyttDetailActivity.TYPE_DRAMA:
                if (dyttTVDramaItems != null) {
                    return;
                }

                dyttTVDramaItems = dyttDetailRepository.getDyttTVDramaItems(url);
                break;
        }
    }

    public LiveData<List<DyttMovieItem>> getDyttMovieItems() {
        return dyttMovieItems;
    }

    public LiveData<List<DyttTVDramaItem>> getDyttTVDramaItems() {
        return dyttTVDramaItems;
    }
}
