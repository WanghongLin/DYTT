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

import com.wanghong.dytt.DyttListItem;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by wanghong on 21/06/2017.
 */

public class DyttListViewModel extends AndroidViewModel {

    private LiveData<List<DyttListItem>> dyttListItems;

    @Inject
    DyttListRepository dyttListRepository;

    @Inject
    public DyttListViewModel(Application application) {
        super(application);
    }


    public LiveData<List<DyttListItem>> getDyttListItems(String url, int type) {
        return dyttListRepository.getDyttListItems(url, type);
    }
}
