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
 * Created by wanghong on 12/7/16.
 */

public class ActivityConstants {

    public static final String THUNDER_PACKAGE_NAME = "com.xunlei.downloadprovider";
    public static final String IMDB_PACKAGE_NAME = "com.imdb.mobile";
    public static final String USER_AGENT =
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";
    /**
     * Use IE user agent, otherwise request body will be empty after the first request in site dytt8.net
     */
    public static final String USER_AGENT_IE =
            "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko";
    public static final String OMDB_ENDPOINT = "http://www.omdbapi.com/?i=%s&plot=full&r=json";
    public static final String BT_SEARCH_ENDPOINT = "http://www.bttiantangs.com/e/search/new.php";
    public static final int HTTP_TIMEOUT_MILLIS = 6000;
}
