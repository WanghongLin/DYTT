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

package com.wanghong.dytt;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by wanghong on 2/18/17.
 */

public class PicassoAutoFitImageTarget implements Target {

    private ImageView imageView;
    private int width;

    private PicassoAutoFitImageTarget(ImageView imageView) {
        this.imageView = imageView;
        this.width = imageView.getResources().getDisplayMetrics().widthPixels
                - imageView.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin) * 2;
    }

    public static Target from(ImageView imageView) {
        return new PicassoAutoFitImageTarget(imageView);
    }

    @Override
    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
        if (bitmap != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                    layoutParams.width = width;
                    layoutParams.height = (int) (width * ((double) bitmap.getHeight() / (double) bitmap.getWidth()));
                    imageView.setLayoutParams(layoutParams);

                    imageView.setImageBitmap(bitmap);
                }
            });
        }
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
