/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.avcall.app.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.avcall.app.R;
import com.avcall.app.utils.SystemStatusManager;
import com.hyphenate.easeui.ui.EaseBaseActivity;


@SuppressLint("Registered")
public class BaseActivity extends EaseBaseActivity {

    private void setTranslucentStatus(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemStatusManager localSystemStatusManager = new SystemStatusManager(this);
            localSystemStatusManager.setStatusBarTintEnabled(true);
            localSystemStatusManager.setStatusBarTintResource(R.color.top_bar_normal_bg);
            getWindow().getDecorView().setFitsSystemWindows(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
    }


}
