package com.prajitdas.sprivacy.contentprovider.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author prajit.das
 * #unusedclass
 */

public class MediaScannerBroadcastReceiver extends BroadcastReceiver {
	public static boolean mMediaScanning = true;
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(Intent.ACTION_MEDIA_SCANNER_STARTED)){
			mMediaScanning = true;
		}
		if(intent.getAction().equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)){
			mMediaScanning = false;
		}
	}
}