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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanghong.dytt.R;

/**
 * Created by wanghong on 2/17/17.
 */

public class IMDBLabelTextView extends LinearLayout {

    private TextView labelTextView;
    private TextView textTextView;

    public IMDBLabelTextView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public IMDBLabelTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public IMDBLabelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        setOrientation(HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.imdb_label_text_view, this, true);

        labelTextView = (TextView) findViewById(R.id.imdb_label_text_view_label);
        textTextView = (TextView) findViewById(R.id.imdb_label_text_view_text);
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.IMDBLabelTextView);

        labelTextView.setText(typedArray.getString(R.styleable.IMDBLabelTextView_label));

        typedArray.recycle();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IMDBLabelTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr);
    }

    public void setLabel(String label) {
        labelTextView.setText(label);
    }

    public void setText(String text) {
        textTextView.setText(text);
    }
}
