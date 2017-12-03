/*
 * Copyright (C) 2017 mutter
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

package com.wanghong.dytt.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.wanghong.dytt.DyttTVDramaItem;

import java.util.List;

/**
 * Created by mutter on 12/3/17.
 */
@Dao
public interface DyttTVDramaItemDao {

    @Query("SELECT * from dytttvdramaitem WHERE url = :url")
    DyttTVDramaItem queryItem(String url);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DyttTVDramaItem> dyttTVDramaItems);
}
