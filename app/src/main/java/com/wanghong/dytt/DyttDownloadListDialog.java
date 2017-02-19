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

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wanghong on 2/19/17.
 */

public class DyttDownloadListDialog {

    private AlertDialog alertDialog;

    private DyttDownloadListDialog(Context context, List<String> thunderUrls) {
        View view = LayoutInflater.from(context).inflate(R.layout.dytt_download_list, null, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dytt_download_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new DyttDownloadAdapter(thunderUrls, context));
        TextView titleTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.dialog_title, null, false);
        titleTextView.setText(R.string.download_list);
        alertDialog = new AlertDialog.Builder(context)
                .setCustomTitle(titleTextView)
                .setView(view)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                })
                .setCancelable(true).create();
    }

    public static void showDialog(Context context, List<String> thunderUrls) {
        new DyttDownloadListDialog(context, thunderUrls).show();
    }

    private void show() {
        alertDialog.show();
    }
}
