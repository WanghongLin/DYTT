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

import com.wanghong.dytt.DyttApplication;
import com.wanghong.dytt.model.DyttDetailComponent;
import com.wanghong.dytt.model.DyttListComponent;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by wanghong on 20/06/2017.
 */

@Singleton
@Component(modules = { AndroidInjectionModule.class, DyttApplicationModule.class, ActivityModule.class, FragmentModule.class })
public interface DyttApplicationComponent {

    void inject(DyttApplication dyttApplication);

    DyttListComponent.Builder dyttListComponent();

    DyttDetailComponent.Builder dyttDetailComponent();
}
