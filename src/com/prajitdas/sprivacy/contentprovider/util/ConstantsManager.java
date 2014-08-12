package com.prajitdas.sprivacy.contentprovider.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.util.Log;

import com.prajitdas.sprivacy.SPrivacyApplication;

public class ConstantsManager {
	public static Uri getLookupUri(Context context, long id, String lookupKey) {
		Log.v(SPrivacyApplication.getDebugTag(), "Came here and no error yet!");
		setContext(context);
		if(!foundInFakeTable(id, lookupKey))
			return Contacts.getLookupUri(id, lookupKey);
		return getFakeLookupUri();
	}

	private static Uri fakeLookupUri = null;
	private static Context context;
	
	public static Uri getFakeLookupUri() {
		return fakeLookupUri;
	}

	public static void setFakeLookupUri(Uri fakeLookupUri) {
		ConstantsManager.fakeLookupUri = fakeLookupUri;
	}

	private static boolean foundInFakeTable(long id, String lookupKey) {
//		String URL = "content://com.prajitdas.sprivacy.fakecontentprovider.Content.fakecontacts/contacts/"+lookupKey;
		Uri contact = Uri.parse("content://com.prajitdas.sprivacy.fakecontentprovider.Content.fakecontacts/contacts/"+id);
		Cursor c = getContext().getContentResolver().query(contact, null, null, null, null);
		if (!c.moveToFirst()) {
			Log.v(SPrivacyApplication.getDebugTag(), "Came here and no error?");
			return false;
		}
		else {
			setFakeLookupUri(contact);
			return true;
		}
	}

	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		ConstantsManager.context = context;
	}
}