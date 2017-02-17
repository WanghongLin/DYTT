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

public class IMDBListItem implements Parcelable {

    private String posterUrl;
    private String title;
    private String rating;
    private String imdbLink;
    private String imdbID;

    public IMDBListItem() {
    }

    protected IMDBListItem(Parcel in) {
        posterUrl = in.readString();
        title = in.readString();
        rating = in.readString();
        imdbLink = in.readString();
        imdbID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterUrl);
        dest.writeString(title);
        dest.writeString(rating);
        dest.writeString(imdbLink);
        dest.writeString(imdbID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IMDBListItem> CREATOR = new Creator<IMDBListItem>() {
        @Override
        public IMDBListItem createFromParcel(Parcel in) {
            return new IMDBListItem(in);
        }

        @Override
        public IMDBListItem[] newArray(int size) {
            return new IMDBListItem[size];
        }
    };

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImdbLink() {
        return imdbLink;
    }

    public void setImdbLink(String imdbLink) {
        this.imdbLink = imdbLink;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    @Override
    public String toString() {
        return "IMDBListItem{" +
                "posterUrl='" + posterUrl + '\'' +
                ", title='" + title + '\'' +
                ", rating='" + rating + '\'' +
                ", imdbLink='" + imdbLink + '\'' +
                ", imdbID='" + imdbID + '\'' +
                '}';
    }
}
