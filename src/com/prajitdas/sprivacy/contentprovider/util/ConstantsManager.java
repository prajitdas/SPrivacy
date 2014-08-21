package com.prajitdas.sprivacy.contentprovider.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.util.Log;

import com.prajitdas.sprivacy.SPrivacyApplication;
/**
 * @author prajit.das
 * #unusedclass
 */
public class ConstantsManager {
	public static Uri getLookupUri(Context context, long id, String lookupKey) {
		Log.v(SPrivacyApplication.getDebugTag(), "Came here and no error yet!");
		setContext(context);
		if(!foundInFakeTable(id, lookupKey))
			return Contacts.getLookupUri(id, lookupKey);
		return getFakeLookupUri();
	}
	
	public static Uri lookupContact(Context context, Uri lookupuri) {
		setContext(context);
		if(!foundInFakeTable(lookupuri))
			return Contacts.lookupContact(context.getContentResolver(), lookupuri);
		return getFakeContactURI();
	}

//	public static Uri getExtraContactUri(Context context, Uri lookupuri) {
//		setContext(context);
//		if(!foundInFakeTable(lookupuri))
//			return Contacts.lookupContact(context.getContentResolver(), lookupuri);
//		return getFakeLookupUri();
//	}
//	private static Uri fakeEXTRA_CONTACT_URI = null;

	private static Uri fakeLookupUri = null;
	private static Uri fakeContactURI = null;
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

	private static boolean foundInFakeTable(Uri lookupuri) {
//		String URL = "content://com.prajitdas.sprivacy.fakecontentprovider.Content.fakecontacts/contacts/"+lookupKey;
		Uri contact = Uri.parse("content://com.prajitdas.sprivacy.fakecontentprovider.Content.fakecontacts/contacts/"+lookupuri.getLastPathSegment());
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

	public static Uri getFakeContactURI() {
		return fakeContactURI;
	}

	public static void setFakeContactURI(Uri fakeContactURI) {
		ConstantsManager.fakeContactURI = fakeContactURI;
	}
//
//	public static Uri getFakeEXTRA_CONTACT_URI() {
//		return fakeEXTRA_CONTACT_URI;
//	}
//
//	public static void setFakeEXTRA_CONTACT_URI(Uri fakeEXTRA_CONTACT_URI) {
//		ConstantsManager.fakeEXTRA_CONTACT_URI = fakeEXTRA_CONTACT_URI;
//	}
}