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

package com.wanghong.dytt.injection;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.wanghong.dytt.dao.DyttAppDatabase;
import com.wanghong.dytt.dao.DyttListItemDao;
import com.wanghong.dytt.dao.DyttTVDramaItemDao;
import com.wanghong.dytt.model.DyttDetailCache;
import com.wanghong.dytt.model.DyttDetailComponent;
import com.wanghong.dytt.model.DyttListComponent;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wanghong on 20/06/2017.
 */

@Module(subcomponents = { DyttListComponent.class, DyttDetailComponent.class})
public class DyttApplicationModule {

    Application application;

    public DyttApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Executor providesExecutor() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Provides
    @Singleton
    public Application providesApplication() {
        return application;
    }

    @Provides
    @Singleton
    public DyttAppDatabase providesDyttAppDatabase(Application application) {
        return Room.databaseBuilder(application, DyttAppDatabase.class,
                "dytt.db").build();
    }

    @Provides
    @Singleton
    public DyttListItemDao providesDyttListItemDao(DyttAppDatabase dyttAppDatabase) {
        return dyttAppDatabase.dyttListItemDao();
    }

    @Provides
    @Singleton
    public DyttTVDramaItemDao providesDyttTVDramaItemDao(DyttAppDatabase dyttAppDatabase) {
        return dyttAppDatabase.dyttTVDramaItemDao();
    }

    @Provides
    @Singleton
    DyttDetailCache providesDyttDetailCache() {
        return new DyttDetailCache();
    }
}
