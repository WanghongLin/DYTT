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

import java.util.List;

/**
 * Created by wanghong on 11/19/16.
 */

public class DyttTVDramaItem {


    @SourceHtmlTag("div[id='Zoom'] > span > p > img[src]:lt(2)")
    @SourceHtmlAttribute("src")
    private String posterUrl;

    @SourceHtmlTag("div[id='Zoom'] > span > table > tbody > tr > td > a[href]")
    @SourceHtmlAttribute("href")
    @SourceHtmlCollections
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
}
