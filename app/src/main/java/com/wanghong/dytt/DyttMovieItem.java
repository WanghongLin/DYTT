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

/**
 * Created by wanghong on 11/19/16.
 */

public class DyttMovieItem {

    @SourceHtmlTag("div[id='Zoom'] > span > p > img[src]:lt(1)")
    @SourceHtmlAttribute("src")
    private String posterUrl;

    @SourceHtmlTag("div[id='Zoom'] > span > p > img[src]:gt(2)")
    @SourceHtmlAttribute("src")
    private String thumbnailUrl;

    @SourceHtmlTag("div[id='Zoom'] > span > table > tbody > tr > td > a[href]:lt(1)")
    @SourceHtmlAttribute("href")
    private String thunderUrl;

    @Override
    public String toString() {
        return "DyttMovieItem{" +
                "posterUrl='" + posterUrl + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", thunderUrl='" + thunderUrl + '\'' +
                '}';
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getThunderUrl() {
        return thunderUrl;
    }
}
