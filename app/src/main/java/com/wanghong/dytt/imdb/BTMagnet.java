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

package com.wanghong.dytt.imdb;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wanghong on 2/17/17.
 */

public class BTMagnet implements Parcelable {

    private String title;
    private String magnet;

    public BTMagnet(String title, String magnet) {
        this.title = title;
        this.magnet = magnet;
    }

    protected BTMagnet(Parcel in) {
        title = in.readString();
        magnet = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(magnet);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BTMagnet> CREATOR = new Creator<BTMagnet>() {
        @Override
        public BTMagnet createFromParcel(Parcel in) {
            return new BTMagnet(in);
        }

        @Override
        public BTMagnet[] newArray(int size) {
            return new BTMagnet[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getMagnet() {
        return magnet;
    }
}
