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

import android.graphics.Matrix;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;

/**
 * Created by wanghong on 2/19/17.
 */

public class PicassoAutoFitCallback implements Callback {

    private ImageView imageView;

    public static Callback createCallback(ImageView imageView) {
        return new PicassoAutoFitCallback(imageView);
    }

    private PicassoAutoFitCallback(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void onSuccess() {
        int width = imageView.getDrawable().getIntrinsicWidth();
        int height = imageView.getDrawable().getIntrinsicHeight();
        Matrix matrix = new Matrix();
        float scale = (imageView.getResources().getDisplayMetrics().widthPixels
                - imageView.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin) * 2) / (float) width;
        matrix.postScale(scale, scale);

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = (int) (scale * height);
        imageView.setLayoutParams(layoutParams);

        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        imageView.setImageMatrix(matrix);
    }

    @Override
    public void onError() {

    }
}
