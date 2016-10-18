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
package com.avcall.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.easemob.redpacketsdk.RedPacket;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AvcallApplication extends Application {

	public static Context applicationContext;
	public static String currentUserNick = "";
	private static AvcallApplication instance;

	/**
	 * nickname for current user, the nickname instead of ID be shown when user receive notification from APNs
	 */

	@Override
	public void onCreate() {
		MultiDex.install(this);
		super.onCreate();
        applicationContext = this;
        instance = this;
        
        //init demo helper
        AvcallHelper.getInstance().init(applicationContext);
		//red packet code : 初始化红包上下文，开启日志输出开关
		RedPacket.getInstance().initContext(applicationContext);
		RedPacket.getInstance().setDebugMode(true);
		//end of red packet code
	}

	public static AvcallApplication getInstance() {
		return instance;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	public static String md5(String input)
	{
		try {
			MessageDigest messageDigest =MessageDigest.getInstance("MD5");
			byte[] inputByteArray = input.getBytes();
			messageDigest.update(inputByteArray);
			byte[] resultByteArray = messageDigest.digest();
			StringBuffer output = new StringBuffer("");
			int i = 0;
			while(i<resultByteArray.length){
				int k = resultByteArray[i];
				int j = k;
				if(k<0){
					j = k + 256;
				}
				if(j<16){
					output.append("0");
				}
				output.append(Integer.toHexString(j));
				i += 1;
			}
			return output.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
}
