/*
 * Copyright (C) 2016 wanghong
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

package com.wanghong.dytt;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by wanghong on 11/19/16.
 */

@Entity
public class DyttTVDramaItem {

    @PrimaryKey
    @NonNull
    private String url;

    @CreatedFromHtmlTag("div[id='Zoom'] > span > p > img[src]:lt(2)")
    @CreatedFromHtmlAttribute("src")
    private String posterUrl;

    @CreatedFromHtmlTag("div[id='Zoom'] > span > table > tbody > tr > td > a[href]")
    @CreatedFromHtmlAttribute("href")
    @CreatedFromHtmlCollections
    private List<String> thunderUrls;

    @Override
    public String toString() {
        return "DyttTVDramaItem{" +
                "posterUrl='" + posterUrl + '\'' +
                ", thunderUrls=" + thunderUrls +
                '}';
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public List<String> getThunderUrls() {
        return thunderUrls;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setThunderUrls(List<String> thunderUrls) {
        this.thunderUrls = thunderUrls;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }
}
