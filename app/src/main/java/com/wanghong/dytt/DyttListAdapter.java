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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by wanghong on 11/19/16.
 */

public class DyttListAdapter extends RecyclerView.Adapter<DyttListItemViewHolder> {

    private List<DyttListItem> dyttListItems;
    private Context context;

    public DyttListAdapter(List<DyttListItem> dyttListItems, Context context) {
        this.dyttListItems = dyttListItems;
        this.context = context;
    }

    @Override
    public DyttListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DyttListItemViewHolder(LayoutInflater.from(context).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(DyttListItemViewHolder holder, int position) {
        holder.setDyttListItem(dyttListItems.get(position));
        holder.invalidate();
    }

    @Override
    public int getItemCount() {
        return dyttListItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.dytt_list_item;
    }

    public void handleMoreData(List<DyttListItem> dyttListItems) {
        if (dyttListItems != null && this.dyttListItems != null) {
            this.dyttListItems.addAll(dyttListItems);
            notifyItemRangeInserted(this.dyttListItems.size() - dyttListItems.size(), dyttListItems.size());
        }
    }
}
