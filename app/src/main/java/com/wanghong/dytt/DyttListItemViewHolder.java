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

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by wanghong on 11/19/16.
 */

public class DyttListItemViewHolder extends RecyclerView.ViewHolder {

    private TextView titleTextView;
    private TextView timeTextView;
    private DyttListItem dyttListItem;

    public DyttListItemViewHolder(View itemView) {
        super(itemView);

        titleTextView = (TextView) itemView.findViewById(R.id.dytt_list_item_title);
        timeTextView = (TextView) itemView.findViewById(R.id.dytt_list_item_time);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(dyttListItem.getTargetUrl());
                DyttDetailActivity.start(v.getContext(), dyttListItem.getTargetUrl(), dyttListItem.getType(), dyttListItem.getTitle());
            }
        });
    }

    public void setDyttListItem(DyttListItem dyttListItem) {
        this.dyttListItem = dyttListItem;
    }

    public void invalidate() {
        if (dyttListItem != null) {
            titleTextView.setText(dyttListItem.getTitle());
            timeTextView.setText(dyttListItem.getDateTime());
        }
    }
}
