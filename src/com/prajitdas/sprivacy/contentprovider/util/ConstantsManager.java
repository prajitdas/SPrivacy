package com.prajitdas.sprivacy.contentprovider.util;

import android.net.Uri;
import android.provider.ContactsContract.Contacts;

public class ConstantsManager {
	public static Uri getLookupUri(long id, String lookupKey) {
		if(!foundInFakeTable())
			return Contacts.getLookupUri(id, lookupKey);
		return fakeGetLookupUri(id, lookupKey);
	}
	
	private static boolean foundInFakeTable() {
		// TODO Auto-generated method stub
		return false;
	}

	private static Uri fakeGetLookupUri(long id, String lookupKey) {
		// TODO Auto-generated method stub
		return null;
	}
}