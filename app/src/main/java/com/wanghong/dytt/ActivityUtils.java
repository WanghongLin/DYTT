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

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by wanghong on 12/8/16.
 */

public class ActivityUtils {

    private ActivityUtils() {
        //no instance
    }

    public static void navigateToThunder(final Activity activity, String downloadUrl) {
        if (activity == null) {
            return;
        }

        Intent intent;
        if (downloadUrl != null) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(downloadUrl));
            intent.setPackage(ActivityConstants.THUNDER_PACKAGE_NAME);
        } else {
            intent = activity.getPackageManager().getLaunchIntentForPackage(ActivityConstants.THUNDER_PACKAGE_NAME);
        }

        if (intent != null && intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        } else {
            final View anchorView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            Snackbar.make(anchorView, R.string.prompt_thunder_not_installed, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.action_install, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + ActivityConstants.THUNDER_PACKAGE_NAME));
                            if (i.resolveActivity(activity.getPackageManager()) != null) {
                                activity.startActivity(i);
                            } else {
                                Snackbar.make(anchorView, R.string.app_store_not_installed, Snackbar.LENGTH_SHORT)
                                        .setAction(R.string.install_thunder_from_browser, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                                        Uri.parse("http://mobile.xunlei.com"));
                                                if (browserIntent.resolveActivity(activity.getPackageManager()) != null) {
                                                    activity.startActivity(browserIntent);
                                                }
                                            }
                                        }).show();
                            }
                        }
                    }).show();
        }
    }

    public static Activity getActivity(Context context) {
        if (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            } else {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }

    private static final String TAG = "ActivityUtils";
    public static void performShare(Activity activity, String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        intent.setType("text/plain");
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.prompt_send_to)));
        }
    }
}
