package com.prajitdas.sprivacy.contentprovider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.prajitdas.sprivacy.ProviderApplication;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;

public class PrivacyAwareContentContracts implements ProviderContstants {
    
    public static Uri getImageContentUri() {
		if(isThisAllowed())
			return REAL_IMAGE_CONTENT_URI;
		return FAKE_IMAGE_CONTENT_URI;
	}
	
	private static boolean isThisAllowed() {
		// TODO Auto-generated method stub
		return true;
	}
	   /**
     * Broadcast Action:  A broadcast to indicate the end of an MTP session with the host.
     * This broadcast is only sent if MTP activity has modified the media database during the
     * most recent MTP session.
     *
     * @hide
     */
    public static final String ACTION_MTP_SESSION_END = "android.provider.action.MTP_SESSION_END";

    /**
     * The method name used by the media scanner and mtp to tell the media provider to
     * rescan and reclassify that have become unhidden because of renaming folders or
     * removing nomedia files
     * @hide
     */
    public static final String UNHIDE_CALL = "unhide";

    /**
     * This is for internal use by the media scanner only.
     * Name of the (optional) Uri parameter that determines whether to skip deleting
     * the file pointed to by the _data column, when deleting the database entry.
     * The only appropriate value for this parameter is "false", in which case the
     * delete will be skipped. Note especially that setting this to true, or omitting
     * the parameter altogether, will perform the default action, which is different
     * for different types of media.
     * @hide
     */
    public static final String PARAM_DELETE_DATA = "deletedata";

    /**
     * The name of the Intent-extra used to define the artist
     */
    public static final String EXTRA_MEDIA_ARTIST = "android.intent.extra.artist";
    /**
     * The name of the Intent-extra used to define the album
     */
    public static final String EXTRA_MEDIA_ALBUM = "android.intent.extra.album";
    /**
     * The name of the Intent-extra used to define the song title
     */
    public static final String EXTRA_MEDIA_TITLE = "android.intent.extra.title";
    /**
     * The name of the Intent-extra used to define the search focus. The search focus
     * indicates whether the search should be for things related to the artist, album
     * or song that is identified by the other extras.
     */
    public static final String EXTRA_MEDIA_FOCUS = "android.intent.extra.focus";

    /**
     * The name of the Intent-extra used to control the orientation of a ViewImage or a MovieView.
     * This is an int property that overrides the activity's requestedOrientation.
     * @see android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
     */
    public static final String EXTRA_SCREEN_ORIENTATION = "android.intent.extra.screenOrientation";

    /**
     * The name of an Intent-extra used to control the UI of a ViewImage.
     * This is a boolean property that overrides the activity's default fullscreen state.
     */
    public static final String EXTRA_FULL_SCREEN = "android.intent.extra.fullScreen";

    /**
     * The name of an Intent-extra used to control the UI of a ViewImage.
     * This is a boolean property that specifies whether or not to show action icons.
     */
    public static final String EXTRA_SHOW_ACTION_ICONS = "android.intent.extra.showActionIcons";

    /**
     * The name of the Intent-extra used to control the onCompletion behavior of a MovieView.
     * This is a boolean property that specifies whether or not to finish the MovieView activity
     * when the movie completes playing. The default value is true, which means to automatically
     * exit the movie player activity when the movie completes playing.
     */
    public static final String EXTRA_FINISH_ON_COMPLETION = "android.intent.extra.finishOnCompletion";

    /**
     * The name of the Intent-extra used to control the quality of a recorded video. This is an
     * integer property. Currently value 0 means low quality, suitable for MMS messages, and
     * value 1 means high quality. In the future other quality levels may be added.
     */
    public final static String EXTRA_VIDEO_QUALITY = "android.intent.extra.videoQuality";

    /**
     * Specify the maximum allowed size.
     */
    public final static String EXTRA_SIZE_LIMIT = "android.intent.extra.sizeLimit";

    /**
     * Specify the maximum allowed recording duration in seconds.
     */
    public final static String EXTRA_DURATION_LIMIT = "android.intent.extra.durationLimit";

    /**
     * The name of the Intent-extra used to indicate a content resolver Uri to be used to
     * store the requested image or video.
     */
    public final static String EXTRA_OUTPUT = "output";

    /**
      * The string that is used when a media attribute is not known. For example,
      * if an audio file does not have any meta data, the artist and album columns
      * will be set to this value.
      */
    public static final String UNKNOWN_STRING = "<unknown>";

    /**
     * Common fields for most MediaProvider tables
     */

    public interface MediaColumns extends BaseColumns {
        /**
         * The data stream for the file
         * <P>Type: DATA STREAM</P>
         */
        public static final String DATA = "_data";

        /**
         * The size of the file in bytes
         * <P>Type: INTEGER (long)</P>
         */
        public static final String SIZE = "_size";

        /**
         * The display name of the file
         * <P>Type: TEXT</P>
         */
        public static final String DISPLAY_NAME = "_display_name";

        /**
         * The title of the content
         * <P>Type: TEXT</P>
         */
        public static final String TITLE = "title";

        /**
         * The time the file was added to the media provider
         * Units are seconds since 1970.
         * <P>Type: INTEGER (long)</P>
         */
        public static final String DATE_ADDED = "date_added";

        /**
         * The time the file was last modified
         * Units are seconds since 1970.
         * NOTE: This is for internal use by the media scanner.  Do not modify this field.
         * <P>Type: INTEGER (long)</P>
         */
        public static final String DATE_MODIFIED = "date_modified";

        /**
         * The MIME type of the file
         * <P>Type: TEXT</P>
         */
        public static final String MIME_TYPE = "mime_type";

        /**
         * The MTP object handle of a newly transfered file.
         * Used to pass the new file's object handle through the media scanner
         * from MTP to the media provider
         * For internal use only by MTP, media scanner and media provider.
         * <P>Type: INTEGER</P>
         * @hide
         */
        public static final String MEDIA_SCANNER_NEW_OBJECT_ID = "media_scanner_new_object_id";

        /**
         * Non-zero if the media file is drm-protected
         * <P>Type: INTEGER (boolean)</P>
         * @hide
         */
        public static final String IS_DRM = "is_drm";

        /**
         * The width of the image/video in pixels.
         */
        public static final String WIDTH = "width";

        /**
         * The height of the image/video in pixels.
         */
        public static final String HEIGHT = "height";
     }

    /**
     * Media provider table containing an index of all files in the media storage,
     * including non-media files.  This should be used by applications that work with
     * non-media file types (text, HTML, PDF, etc) as well as applications that need to
     * work with multiple media file types in a single query.
     */
    public static final class Files {

        /**
         * Get the content:// style URI for the files table on the
         * given volume.
         *
         * @param volumeName the name of the volume to get the URI for
         * @return the URI to the files table on the given volume
         */
        public static Uri getContentUri(String volumeName) {
            return Uri.parse(PROVIDER_NAME + volumeName +
                    "/file");
        }

        /**
         * Get the content:// style URI for a single row in the files table on the
         * given volume.
         *
         * @param volumeName the name of the volume to get the URI for
         * @param rowId the file to get the URI for
         * @return the URI to the files table on the given volume
         */
        public static final Uri getContentUri(String volumeName,
                long rowId) {
            return Uri.parse(PROVIDER_NAME + volumeName
                    + "/file/" + rowId);
        }

        /**
         * For use only by the MTP implementation.
         * @hide
         */
        public static Uri getMtpObjectsUri(String volumeName) {
            return Uri.parse(PROVIDER_NAME + volumeName +
                    "/object");
        }

        /**
         * For use only by the MTP implementation.
         * @hide
         */
        public static final Uri getMtpObjectsUri(String volumeName,
                long fileId) {
            return Uri.parse(PROVIDER_NAME + volumeName
                    + "/object/" + fileId);
        }

        /**
         * Used to implement the MTP GetObjectReferences and SetObjectReferences commands.
         * @hide
         */
        public static final Uri getMtpReferencesUri(String volumeName,
                long fileId) {
            return Uri.parse(PROVIDER_NAME + volumeName
                    + "/object/" + fileId + "/references");
        }

        /**
         * Fields for master table for all media files.
         * Table also contains MediaColumns._ID, DATA, SIZE and DATE_MODIFIED.
         */
        public interface FileColumns extends MediaColumns {
            /**
             * The MTP storage ID of the file
             * <P>Type: INTEGER</P>
             * @hide
             */
            public static final String STORAGE_ID = "storage_id";

            /**
             * The MTP format code of the file
             * <P>Type: INTEGER</P>
             * @hide
             */
            public static final String FORMAT = "format";

            /**
             * The index of the parent directory of the file
             * <P>Type: INTEGER</P>
             */
            public static final String PARENT = "parent";

            /**
             * The MIME type of the file
             * <P>Type: TEXT</P>
             */
            public static final String MIME_TYPE = "mime_type";

            /**
             * The title of the content
             * <P>Type: TEXT</P>
             */
            public static final String TITLE = "title";

            /**
             * The media type (audio, video, image or playlist)
             * of the file, or 0 for not a media file
             * <P>Type: TEXT</P>
             */
            public static final String MEDIA_TYPE = "media_type";

            /**
             * Constant for the {@link #MEDIA_TYPE} column indicating that file
             * is not an audio, image, video or playlist file.
             */
            public static final int MEDIA_TYPE_NONE = 0;

            /**
             * Constant for the {@link #MEDIA_TYPE} column indicating that file is an image file.
             */
            public static final int MEDIA_TYPE_IMAGE = 1;

            /**
             * Constant for the {@link #MEDIA_TYPE} column indicating that file is an audio file.
             */
            public static final int MEDIA_TYPE_AUDIO = 2;

            /**
             * Constant for the {@link #MEDIA_TYPE} column indicating that file is a video file.
             */
            public static final int MEDIA_TYPE_VIDEO = 3;

            /**
             * Constant for the {@link #MEDIA_TYPE} column indicating that file is a playlist file.
             */
            public static final int MEDIA_TYPE_PLAYLIST = 4;
        }
    }
    
    /**
     * Contains meta data for all available images.
     */
    public static final class Images {
        public interface ImageColumns extends MediaColumns {
            /**
             * The description of the image
             * <P>Type: TEXT</P>
             */
            public static final String DESCRIPTION = "description";

            /**
             * The picasa id of the image
             * <P>Type: TEXT</P>
             */
            public static final String PICASA_ID = "picasa_id";

            /**
             * Whether the video should be published as public or private
             * <P>Type: INTEGER</P>
             */
            public static final String IS_PRIVATE = "isprivate";

            /**
             * The latitude where the image was captured.
             * <P>Type: DOUBLE</P>
             */
            public static final String LATITUDE = "latitude";

            /**
             * The longitude where the image was captured.
             * <P>Type: DOUBLE</P>
             */
            public static final String LONGITUDE = "longitude";

            /**
             * The date & time that the image was taken in units
             * of milliseconds since jan 1, 1970.
             * <P>Type: INTEGER</P>
             */
            public static final String DATE_TAKEN = "datetaken";

            /**
             * The orientation for the image expressed as degrees.
             * Only degrees 0, 90, 180, 270 will work.
             * <P>Type: INTEGER</P>
             */
            public static final String ORIENTATION = "orientation";

            /**
             * The mini thumb id.
             * <P>Type: INTEGER</P>
             */
            public static final String MINI_THUMB_MAGIC = "mini_thumb_magic";

            /**
             * The bucket id of the image. This is a read-only property that
             * is automatically computed from the DATA column.
             * <P>Type: TEXT</P>
             */
            public static final String BUCKET_ID = "bucket_id";

            /**
             * The bucket display name of the image. This is a read-only property that
             * is automatically computed from the DATA column.
             * <P>Type: TEXT</P>
             */
            public static final String BUCKET_DISPLAY_NAME = "bucket_display_name";
        }

        public static final class Media implements ImageColumns {
            public static final Cursor query(ContentResolver cr, Uri uri, String[] projection) {
            	Log.v(ProviderApplication.getDebugTag(), uri.toString());
                return cr.query(uri, projection, null, null, DEFAULT_SORT_ORDER);
            }

            public static final Cursor query(ContentResolver cr, Uri uri, String[] projection,
                    String where, String orderBy) {
                return cr.query(uri, projection, where,
                                             null, orderBy == null ? DEFAULT_SORT_ORDER : orderBy);
            }

            public static final Cursor query(ContentResolver cr, Uri uri, String[] projection,
                    String selection, String [] selectionArgs, String orderBy) {
                return cr.query(uri, projection, selection,
                        selectionArgs, orderBy == null ? DEFAULT_SORT_ORDER : orderBy);
            }

            /**
             * Retrieves an image for the given url as a {@link Bitmap}.
             *
             * @param cr The content resolver to use
             * @param url The url of the image
             * @throws FileNotFoundException
             * @throws IOException
             */
            public static final Bitmap getBitmap(ContentResolver cr, Uri url)
                    throws FileNotFoundException, IOException {
                InputStream input = cr.openInputStream(url);
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                input.close();
                return bitmap;
            }

            /**
             * Get the content:// style URI for the image media table on the
             * given volume.
             *
             * @param volumeName the name of the volume to get the URI for
             * @return the URI to the image media table on the given volume
             */
            public static Uri getContentUri(String volumeName) {
                return Uri.parse(PROVIDER_NAME + volumeName +
                        "/images/media");
            }

            /**
             * The content:// style URI for the internal storage.
             */
            public static final Uri INTERNAL_CONTENT_URI =
                    getContentUri("internal");

            /**
             * The content:// style URI for the "primary" external storage
             * volume.
             */
            public static final Uri EXTERNAL_CONTENT_URI =
                    getContentUri("external");

            /**
             * The MIME type of of this directory of
             * images.  Note that each entry in this directory will have a standard
             * image MIME type as appropriate -- for example, image/jpeg.
             */
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/image";

            /**
             * The default sort order for this table
             */
            public static final String DEFAULT_SORT_ORDER = ImageColumns.BUCKET_DISPLAY_NAME;
        }

    }

    /**
     * Container for all audio content.
     */
    @SuppressLint("DefaultLocale")
	public static final class Audio {
        /**
         * Columns for audio file that show up in multiple tables.
         */
        public interface AudioColumns extends MediaColumns {

            /**
             * A non human readable key calculated from the TITLE, used for
             * searching, sorting and grouping
             * <P>Type: TEXT</P>
             */
            public static final String TITLE_KEY = "title_key";

            /**
             * The duration of the audio file, in ms
             * <P>Type: INTEGER (long)</P>
             */
            public static final String DURATION = "duration";

            /**
             * The position, in ms, playback was at when playback for this file
             * was last stopped.
             * <P>Type: INTEGER (long)</P>
             */
            public static final String BOOKMARK = "bookmark";

            /**
             * The id of the artist who created the audio file, if any
             * <P>Type: INTEGER (long)</P>
             */
            public static final String ARTIST_ID = "artist_id";

            /**
             * The artist who created the audio file, if any
             * <P>Type: TEXT</P>
             */
            public static final String ARTIST = "artist";

            /**
             * The artist credited for the album that contains the audio file
             * <P>Type: TEXT</P>
             * @hide
             */
            public static final String ALBUM_ARTIST = "album_artist";

            /**
             * Whether the song is part of a compilation
             * <P>Type: TEXT</P>
             * @hide
             */
            public static final String COMPILATION = "compilation";

            /**
             * A non human readable key calculated from the ARTIST, used for
             * searching, sorting and grouping
             * <P>Type: TEXT</P>
             */
            public static final String ARTIST_KEY = "artist_key";

            /**
             * The composer of the audio file, if any
             * <P>Type: TEXT</P>
             */
            public static final String COMPOSER = "composer";

            /**
             * The id of the album the audio file is from, if any
             * <P>Type: INTEGER (long)</P>
             */
            public static final String ALBUM_ID = "album_id";

            /**
             * The album the audio file is from, if any
             * <P>Type: TEXT</P>
             */
            public static final String ALBUM = "album";

            /**
             * A non human readable key calculated from the ALBUM, used for
             * searching, sorting and grouping
             * <P>Type: TEXT</P>
             */
            public static final String ALBUM_KEY = "album_key";

            /**
             * The track number of this song on the album, if any.
             * This number encodes both the track number and the
             * disc number. For multi-disc sets, this number will
             * be 1xxx for tracks on the first disc, 2xxx for tracks
             * on the second disc, etc.
             * <P>Type: INTEGER</P>
             */
            public static final String TRACK = "track";

            /**
             * The year the audio file was recorded, if any
             * <P>Type: INTEGER</P>
             */
            public static final String YEAR = "year";

            /**
             * Non-zero if the audio file is music
             * <P>Type: INTEGER (boolean)</P>
             */
            public static final String IS_MUSIC = "is_music";

            /**
             * Non-zero if the audio file is a podcast
             * <P>Type: INTEGER (boolean)</P>
             */
            public static final String IS_PODCAST = "is_podcast";

            /**
             * Non-zero if the audio file may be a ringtone
             * <P>Type: INTEGER (boolean)</P>
             */
            public static final String IS_RINGTONE = "is_ringtone";

            /**
             * Non-zero if the audio file may be an alarm
             * <P>Type: INTEGER (boolean)</P>
             */
            public static final String IS_ALARM = "is_alarm";

            /**
             * Non-zero if the audio file may be a notification sound
             * <P>Type: INTEGER (boolean)</P>
             */
            public static final String IS_NOTIFICATION = "is_notification";

            /**
             * The genre of the audio file, if any
             * <P>Type: TEXT</P>
             * Does not exist in the database - only used by the media scanner for inserts.
             * @hide
             */
            public static final String GENRE = "genre";
        }

        /**
         * Converts a name to a "key" that can be used for grouping, sorting
         * and searching.
         * The rules that govern this conversion are:
         * - remove 'special' characters like ()[]'!?.,
         * - remove leading/trailing spaces
         * - convert everything to lowercase
         * - remove leading "the ", "an " and "a "
         * - remove trailing ", the|an|a"
         * - remove accents. This step leaves us with CollationKey data,
         *   which is not human readable
         *
         * @param name The artist or album name to convert
         * @return The "key" for the given name.
         */
        public static String keyFor(String name) {
            if (name != null)  {
                boolean sortfirst = false;
                if (name.equals(UNKNOWN_STRING)) {
                    return "\001";
                }
                // Check if the first character is \001. We use this to
                // force sorting of certain special files, like the silent ringtone.
                if (name.startsWith("\001")) {
                    sortfirst = true;
                }
                name = name.trim().toLowerCase();
                if (name.startsWith("the ")) {
                    name = name.substring(4);
                }
                if (name.startsWith("an ")) {
                    name = name.substring(3);
                }
                if (name.startsWith("a ")) {
                    name = name.substring(2);
                }
                if (name.endsWith(", the") || name.endsWith(",the") ||
                    name.endsWith(", an") || name.endsWith(",an") ||
                    name.endsWith(", a") || name.endsWith(",a")) {
                    name = name.substring(0, name.lastIndexOf(','));
                }
                name = name.replaceAll("[\\[\\]\\(\\)\"'.,?!]", "").trim();
                if (name.length() > 0) {
                    // Insert a separator between the characters to avoid
                    // matches on a partial character. If we ever change
                    // to start-of-word-only matches, this can be removed.
                    StringBuilder b = new StringBuilder();
                    b.append('.');
                    int nl = name.length();
                    for (int i = 0; i < nl; i++) {
                        b.append(name.charAt(i));
                        b.append('.');
                    }
                    name = b.toString();
                    String key = DatabaseUtils.getCollationKey(name);
                    if (sortfirst) {
                        key = "\001" + key;
                    }
                    return key;
               } else {
                    return "";
                }
            }
            return null;
        }

        public static final class Media implements AudioColumns {

            private static final String[] EXTERNAL_PATHS;

            static {
                String secondary_storage = System.getenv("SECONDARY_STORAGE");
                if (secondary_storage != null) {
                    EXTERNAL_PATHS = secondary_storage.split(":");
                } else {
                    EXTERNAL_PATHS = new String[0];
                }
            }

            /**
             * Get the content:// style URI for the audio media table on the
             * given volume.
             *
             * @param volumeName the name of the volume to get the URI for
             * @return the URI to the audio media table on the given volume
             */
            public static Uri getContentUri(String volumeName) {
                return Uri.parse(PROVIDER_NAME + volumeName +
                        "/audio/media");
            }

            public static Uri getContentUriForPath(String path) {
                for (String ep : EXTERNAL_PATHS) {
                    if (path.startsWith(ep)) {
                        return EXTERNAL_CONTENT_URI;
                    }
                }

                return (path.startsWith(Environment.getExternalStorageDirectory().getPath()) ?
                        EXTERNAL_CONTENT_URI : INTERNAL_CONTENT_URI);
            }

            /**
             * The content:// style URI for the internal storage.
             */
            public static final Uri INTERNAL_CONTENT_URI =
                    getContentUri("internal");

            /**
             * The content:// style URI for the "primary" external storage
             * volume.
             */
            public static final Uri EXTERNAL_CONTENT_URI =
                    getContentUri("external");

            /**
             * The MIME type for this table.
             */
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/audio";

            /**
             * The default sort order for this table
             */
            public static final String DEFAULT_SORT_ORDER = TITLE_KEY;

            /**
             * Activity Action: Start SoundRecorder application.
             * <p>Input: nothing.
             * <p>Output: An uri to the recorded sound stored in the Media Library
             * if the recording was successful.
             * May also contain the extra EXTRA_MAX_BYTES.
             * @see #EXTRA_MAX_BYTES
             */
            public static final String RECORD_SOUND_ACTION =
                    "android.provider.MediaStore.RECORD_SOUND";

            /**
             * The name of the Intent-extra used to define a maximum file size for
             * a recording made by the SoundRecorder application.
             *
             * @see #RECORD_SOUND_ACTION
             */
             public static final String EXTRA_MAX_BYTES =
                    "android.provider.MediaStore.extra.MAX_BYTES";
        }

        /**
         * Columns representing an audio genre
         */
        public interface GenresColumns {
            /**
             * The name of the genre
             * <P>Type: TEXT</P>
             */
            public static final String NAME = "name";
        }

        /**
         * Contains all genres for audio files
         */
        public static final class Genres implements BaseColumns, GenresColumns {
            /**
             * Get the content:// style URI for the audio genres table on the
             * given volume.
             *
             * @param volumeName the name of the volume to get the URI for
             * @return the URI to the audio genres table on the given volume
             */
            public static Uri getContentUri(String volumeName) {
                return Uri.parse(PROVIDER_NAME + volumeName +
                        "/audio/genres");
            }

            /**
             * Get the content:// style URI for querying the genres of an audio file.
             *
             * @param volumeName the name of the volume to get the URI for
             * @param audioId the ID of the audio file for which to retrieve the genres
             * @return the URI to for querying the genres for the audio file
             * with the given the volume and audioID
             */
            public static Uri getContentUriForAudioId(String volumeName, int audioId) {
                return Uri.parse(PROVIDER_NAME + volumeName +
                        "/audio/media/" + audioId + "/genres");
            }

            /**
             * The content:// style URI for the internal storage.
             */
            public static final Uri INTERNAL_CONTENT_URI =
                    getContentUri("internal");

            /**
             * The content:// style URI for the "primary" external storage
             * volume.
             */
            public static final Uri EXTERNAL_CONTENT_URI =
                    getContentUri("external");

            /**
             * The MIME type for this table.
             */
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/genre";

            /**
             * The MIME type for entries in this table.
             */
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/genre";

            /**
             * The default sort order for this table
             */
            public static final String DEFAULT_SORT_ORDER = NAME;

            /**
             * Sub-directory of each genre containing all members.
             */
            public static final class Members implements AudioColumns {

                public static final Uri getContentUri(String volumeName,
                        long genreId) {
                    return Uri.parse(PROVIDER_NAME + volumeName
                            + "/audio/genres/" + genreId + "/members");
                }

                /**
                 * A subdirectory of each genre containing all member audio files.
                 */
                public static final String CONTENT_DIRECTORY = "members";

                /**
                 * The default sort order for this table
                 */
                public static final String DEFAULT_SORT_ORDER = TITLE_KEY;

                /**
                 * The ID of the audio file
                 * <P>Type: INTEGER (long)</P>
                 */
                public static final String AUDIO_ID = "audio_id";

                /**
                 * The ID of the genre
                 * <P>Type: INTEGER (long)</P>
                 */
                public static final String GENRE_ID = "genre_id";
            }
        }

        /**
         * Columns representing a playlist
         */
        public interface PlaylistsColumns {
            /**
             * The name of the playlist
             * <P>Type: TEXT</P>
             */
            public static final String NAME = "name";

            /**
             * The data stream for the playlist file
             * <P>Type: DATA STREAM</P>
             */
            public static final String DATA = "_data";

            /**
             * The time the file was added to the media provider
             * Units are seconds since 1970.
             * <P>Type: INTEGER (long)</P>
             */
            public static final String DATE_ADDED = "date_added";

            /**
             * The time the file was last modified
             * Units are seconds since 1970.
             * NOTE: This is for internal use by the media scanner.  Do not modify this field.
             * <P>Type: INTEGER (long)</P>
             */
            public static final String DATE_MODIFIED = "date_modified";
        }

        /**
         * Contains playlists for audio files
         */
        public static final class Playlists implements BaseColumns,
                PlaylistsColumns {
            /**
             * Get the content:// style URI for the audio playlists table on the
             * given volume.
             *
             * @param volumeName the name of the volume to get the URI for
             * @return the URI to the audio playlists table on the given volume
             */
            public static Uri getContentUri(String volumeName) {
                return Uri.parse(PROVIDER_NAME + volumeName +
                        "/audio/playlists");
            }

            /**
             * The content:// style URI for the internal storage.
             */
            public static final Uri INTERNAL_CONTENT_URI =
                    getContentUri("internal");

            /**
             * The content:// style URI for the "primary" external storage
             * volume.
             */
            public static final Uri EXTERNAL_CONTENT_URI =
                    getContentUri("external");

            /**
             * The MIME type for this table.
             */
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/playlist";

            /**
             * The MIME type for entries in this table.
             */
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/playlist";

            /**
             * The default sort order for this table
             */
            public static final String DEFAULT_SORT_ORDER = NAME;

            /**
             * Sub-directory of each playlist containing all members.
             */
            public static final class Members implements AudioColumns {
                public static final Uri getContentUri(String volumeName,
                        long playlistId) {
                    return Uri.parse(PROVIDER_NAME + volumeName
                            + "/audio/playlists/" + playlistId + "/members");
                }

                /**
                 * Convenience method to move a playlist item to a new location
                 * @param res The content resolver to use
                 * @param playlistId The numeric id of the playlist
                 * @param from The position of the item to move
                 * @param to The position to move the item to
                 * @return true on success
                 */
                public static final boolean moveItem(ContentResolver res,
                        long playlistId, int from, int to) {
                    Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external",
                            playlistId)
                            .buildUpon()
                            .appendEncodedPath(String.valueOf(from))
                            .appendQueryParameter("move", "true")
                            .build();
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, to);
                    return res.update(uri, values, null, null) != 0;
                }

                /**
                 * The ID within the playlist.
                 */
                public static final String _ID = "_id";

                /**
                 * A subdirectory of each playlist containing all member audio
                 * files.
                 */
                public static final String CONTENT_DIRECTORY = "members";

                /**
                 * The ID of the audio file
                 * <P>Type: INTEGER (long)</P>
                 */
                public static final String AUDIO_ID = "audio_id";

                /**
                 * The ID of the playlist
                 * <P>Type: INTEGER (long)</P>
                 */
                public static final String PLAYLIST_ID = "playlist_id";

                /**
                 * The order of the songs in the playlist
                 * <P>Type: INTEGER (long)></P>
                 */
                public static final String PLAY_ORDER = "play_order";

                /**
                 * The default sort order for this table
                 */
                public static final String DEFAULT_SORT_ORDER = PLAY_ORDER;
            }
        }

        /**
         * Columns representing an artist
         */
        public interface ArtistColumns {
            /**
             * The artist who created the audio file, if any
             * <P>Type: TEXT</P>
             */
            public static final String ARTIST = "artist";

            /**
             * A non human readable key calculated from the ARTIST, used for
             * searching, sorting and grouping
             * <P>Type: TEXT</P>
             */
            public static final String ARTIST_KEY = "artist_key";

            /**
             * The number of albums in the database for this artist
             */
            public static final String NUMBER_OF_ALBUMS = "number_of_albums";

            /**
             * The number of albums in the database for this artist
             */
            public static final String NUMBER_OF_TRACKS = "number_of_tracks";
        }

        /**
         * Contains artists for audio files
         */
        public static final class Artists implements BaseColumns, ArtistColumns {
            /**
             * Get the content:// style URI for the artists table on the
             * given volume.
             *
             * @param volumeName the name of the volume to get the URI for
             * @return the URI to the audio artists table on the given volume
             */
            public static Uri getContentUri(String volumeName) {
                return Uri.parse(PROVIDER_NAME + volumeName +
                        "/audio/artists");
            }

            /**
             * The content:// style URI for the internal storage.
             */
            public static final Uri INTERNAL_CONTENT_URI =
                    getContentUri("internal");

            /**
             * The content:// style URI for the "primary" external storage
             * volume.
             */
            public static final Uri EXTERNAL_CONTENT_URI =
                    getContentUri("external");

            /**
             * The MIME type for this table.
             */
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/artists";

            /**
             * The MIME type for entries in this table.
             */
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/artist";

            /**
             * The default sort order for this table
             */
            public static final String DEFAULT_SORT_ORDER = ARTIST_KEY;

            /**
             * Sub-directory of each artist containing all albums on which
             * a song by the artist appears.
             */
            public static final class Albums implements AlbumColumns {
                public static final Uri getContentUri(String volumeName,
                        long artistId) {
                    return Uri.parse(PROVIDER_NAME + volumeName
                            + "/audio/artists/" + artistId + "/albums");
                }
            }
        }

        /**
         * Columns representing an album
         */
        public interface AlbumColumns {

            /**
             * The id for the album
             * <P>Type: INTEGER</P>
             */
            public static final String ALBUM_ID = "album_id";

            /**
             * The album on which the audio file appears, if any
             * <P>Type: TEXT</P>
             */
            public static final String ALBUM = "album";

            /**
             * The artist whose songs appear on this album
             * <P>Type: TEXT</P>
             */
            public static final String ARTIST = "artist";

            /**
             * The number of songs on this album
             * <P>Type: INTEGER</P>
             */
            public static final String NUMBER_OF_SONGS = "numsongs";

            /**
             * This column is available when getting album info via artist,
             * and indicates the number of songs on the album by the given
             * artist.
             * <P>Type: INTEGER</P>
             */
            public static final String NUMBER_OF_SONGS_FOR_ARTIST = "numsongs_by_artist";

            /**
             * The year in which the earliest songs
             * on this album were released. This will often
             * be the same as {@link #LAST_YEAR}, but for compilation albums
             * they might differ.
             * <P>Type: INTEGER</P>
             */
            public static final String FIRST_YEAR = "minyear";

            /**
             * The year in which the latest songs
             * on this album were released. This will often
             * be the same as {@link #FIRST_YEAR}, but for compilation albums
             * they might differ.
             * <P>Type: INTEGER</P>
             */
            public static final String LAST_YEAR = "maxyear";

            /**
             * A non human readable key calculated from the ALBUM, used for
             * searching, sorting and grouping
             * <P>Type: TEXT</P>
             */
            public static final String ALBUM_KEY = "album_key";

            /**
             * Cached album art.
             * <P>Type: TEXT</P>
             */
            public static final String ALBUM_ART = "album_art";
        }

        /**
         * Contains artists for audio files
         */
        public static final class Albums implements BaseColumns, AlbumColumns {
            /**
             * Get the content:// style URI for the albums table on the
             * given volume.
             *
             * @param volumeName the name of the volume to get the URI for
             * @return the URI to the audio albums table on the given volume
             */
            public static Uri getContentUri(String volumeName) {
                return Uri.parse(PROVIDER_NAME + volumeName +
                        "/audio/albums");
            }

            /**
             * The content:// style URI for the internal storage.
             */
            public static final Uri INTERNAL_CONTENT_URI =
                    getContentUri("internal");

            /**
             * The content:// style URI for the "primary" external storage
             * volume.
             */
            public static final Uri EXTERNAL_CONTENT_URI =
                    getContentUri("external");

            /**
             * The MIME type for this table.
             */
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/albums";

            /**
             * The MIME type for entries in this table.
             */
            public static final String ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/album";

            /**
             * The default sort order for this table
             */
            public static final String DEFAULT_SORT_ORDER = ALBUM_KEY;
        }
    }

    public static final class Video {

        /**
         * The default sort order for this table.
         */
        public static final String DEFAULT_SORT_ORDER = MediaColumns.DISPLAY_NAME;

        public static final Cursor query(ContentResolver cr, Uri uri, String[] projection) {
            return cr.query(uri, projection, null, null, DEFAULT_SORT_ORDER);
        }

        public interface VideoColumns extends MediaColumns {

            /**
             * The duration of the video file, in ms
             * <P>Type: INTEGER (long)</P>
             */
            public static final String DURATION = "duration";

            /**
             * The artist who created the video file, if any
             * <P>Type: TEXT</P>
             */
            public static final String ARTIST = "artist";

            /**
             * The album the video file is from, if any
             * <P>Type: TEXT</P>
             */
            public static final String ALBUM = "album";

            /**
             * The resolution of the video file, formatted as "XxY"
             * <P>Type: TEXT</P>
             */
            public static final String RESOLUTION = "resolution";

            /**
             * The description of the video recording
             * <P>Type: TEXT</P>
             */
            public static final String DESCRIPTION = "description";

            /**
             * Whether the video should be published as public or private
             * <P>Type: INTEGER</P>
             */
            public static final String IS_PRIVATE = "isprivate";

            /**
             * The user-added tags associated with a video
             * <P>Type: TEXT</P>
             */
            public static final String TAGS = "tags";

            /**
             * The YouTube category of the video
             * <P>Type: TEXT</P>
             */
            public static final String CATEGORY = "category";

            /**
             * The language of the video
             * <P>Type: TEXT</P>
             */
            public static final String LANGUAGE = "language";

            /**
             * The latitude where the video was captured.
             * <P>Type: DOUBLE</P>
             */
            public static final String LATITUDE = "latitude";

            /**
             * The longitude where the video was captured.
             * <P>Type: DOUBLE</P>
             */
            public static final String LONGITUDE = "longitude";

            /**
             * The date & time that the video was taken in units
             * of milliseconds since jan 1, 1970.
             * <P>Type: INTEGER</P>
             */
            public static final String DATE_TAKEN = "datetaken";

            /**
             * The mini thumb id.
             * <P>Type: INTEGER</P>
             */
            public static final String MINI_THUMB_MAGIC = "mini_thumb_magic";

            /**
             * The bucket id of the video. This is a read-only property that
             * is automatically computed from the DATA column.
             * <P>Type: TEXT</P>
             */
            public static final String BUCKET_ID = "bucket_id";

            /**
             * The bucket display name of the video. This is a read-only property that
             * is automatically computed from the DATA column.
             * <P>Type: TEXT</P>
             */
            public static final String BUCKET_DISPLAY_NAME = "bucket_display_name";

            /**
             * The bookmark for the video. Time in ms. Represents the location in the video that the
             * video should start playing at the next time it is opened. If the value is null or
             * out of the range 0..DURATION-1 then the video should start playing from the
             * beginning.
             * <P>Type: INTEGER</P>
             */
            public static final String BOOKMARK = "bookmark";
        }

        public static final class Media implements VideoColumns {
            /**
             * Get the content:// style URI for the video media table on the
             * given volume.
             *
             * @param volumeName the name of the volume to get the URI for
             * @return the URI to the video media table on the given volume
             */
            public static Uri getContentUri(String volumeName) {
                return Uri.parse(PROVIDER_NAME + volumeName +
                        "/video/media");
            }

            /**
             * The content:// style URI for the internal storage.
             */
            public static final Uri INTERNAL_CONTENT_URI =
                    getContentUri("internal");

            /**
             * The content:// style URI for the "primary" external storage
             * volume.
             */
            public static final Uri EXTERNAL_CONTENT_URI =
                    getContentUri("external");

            /**
             * The MIME type for this table.
             */
            public static final String CONTENT_TYPE = "vnd.android.cursor.dir/video";

            /**
             * The default sort order for this table
             */
            public static final String DEFAULT_SORT_ORDER = TITLE;
        }
    }

    /**
     * Uri for querying the state of the media scanner.
     */
    public static Uri getMediaScannerUri() {
        return Uri.parse(PROVIDER_NAME + "none/media_scanner");
    }

    /**
     * Name of current volume being scanned by the media scanner.
     */
    public static final String MEDIA_SCANNER_VOLUME = "volume";

    /**
     * Name of the file signaling the media scanner to ignore media in the containing directory
     * and its subdirectories. Developers should use this to avoid application graphics showing
     * up in the Gallery and likewise prevent application sounds and music from showing up in
     * the Music app.
     */
    public static final String MEDIA_IGNORE_FILENAME = ".nomedia";

    /**
     * Get the media provider's version.
     * Applications that import data from the media provider into their own caches
     * can use this to detect that the media provider changed, and reimport data
     * as needed. No other assumptions should be made about the meaning of the version.
     * @param context Context to use for performing the query.
     * @return A version string, or null if the version could not be determined.
     */
    public static String getVersion(Context context) {
        Cursor c = context.getContentResolver().query(
                Uri.parse(PROVIDER_NAME + "none/version"),
                null, null, null, null);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getString(0);
                }
            } finally {
                c.close();
            }
        }
        return null;
    }
}