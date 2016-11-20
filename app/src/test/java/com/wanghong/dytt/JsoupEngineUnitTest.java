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

import org.junit.Test;

import java.util.List;

/**
 * Created by wanghong on 11/19/16.
 */

public class JsoupEngineUnitTest {

    @Test
    public void jsoupEngineListItemTest() {
        new JsoupEngine<DyttListItem>()
                .setResultClass(DyttListItem.class)
                .setPageUrl("http://www.ygdy8.net/html/gndy/dyzz/index.html")
                .setJsoupEngineCallback(new JsoupEngine.JsoupEngineCallback<DyttListItem>() {
                    @Override
                    public void onJsoupParsed(List<DyttListItem> results) {
                        System.out.println(results);
                    }
                })
                .parse();
    }

    @Test
    public void jsoupEngineMovieItemTest() {
        new JsoupEngine<DyttMovieItem>()
                .setResultClass(DyttMovieItem.class)
                .setPageUrl("http://www.ygdy8.net/html/gndy/dyzz/20161116/52484.html")
                .setJsoupEngineCallback(new JsoupEngine.JsoupEngineCallback<DyttMovieItem>() {
                    @Override
                    public void onJsoupParsed(List<DyttMovieItem> results) {
                        System.out.println(results);
                    }
                })
                .parse();
    }

    @Test
    public void jsoupEngineTVDramaItemTest() {
        new JsoupEngine<DyttTVDramaItem>()
                .setResultClass(DyttTVDramaItem.class)
                .setPageUrl("http://dytt8.net/html/tv/oumeitv/20161013/52228.html")
                .setJsoupEngineCallback(new JsoupEngine.JsoupEngineCallback<DyttTVDramaItem>() {
                    @Override
                    public void onJsoupParsed(List<DyttTVDramaItem> results) {
                        System.out.println(results);
                    }
                })
                .parse();
    }
}
