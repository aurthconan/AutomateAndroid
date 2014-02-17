/*
    AutomateAndroid
    Copyright (C) 2014 JIANG Huai

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package individual.aurthconan.automateandroid;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/*
 * Persistent spell library
 */
public class SpellLibrary extends ContentProvider {
    public static final String AUTHORITY = "individual.aurthconan.automateandroid.spell";
    public static final String SPELLS = "spell";
    public static final String SPELLS_TABLE = "spells";

    public static final String ID_COLUMN = "_id";
    public static final String NAME_COLUMN = "name";
    public static final String ENABLE_COLUMN = "enable";
    public static final String DESC_COLUMN = "desc";
    public static final String SCRIPT_COLUMN = "script";

    private static UriMatcher mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int SPELL_LIST = 1;
    static {
        mMatcher.addURI(AUTHORITY, SPELLS, SPELL_LIST);
    }

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getType(Uri arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri arg0, ContentValues arg1) {
        int match = mMatcher.match(arg0);
        Uri newUri = null;
        switch(match) {
        case SPELL_LIST:
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            long rowId = db.insert(SPELLS_TABLE, null, arg1);
            if (rowId > 0) {
                newUri = ContentUris.withAppendedId(Uri.parse(AUTHORITY), rowId);
            }
            break;
        }
        
        return newUri;
    }

    private static final String SPELLS_FILE = "spells.db";
    @Override
    public boolean onCreate() {
        mDBHelper = new OpenHelper(this.getContext(), SPELLS_FILE);
        return true;
    }

    @Override
    public Cursor query(Uri arg0, String[] projection, String selection, String[] selectionArgs,
            String sort) {
        int match = mMatcher.match(arg0);
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        switch(match) {
        case SPELL_LIST:
            qb.setTables(SPELLS_TABLE);
            break;
        }
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sort);
        return cursor;
    }

    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        // TODO Auto-generated method stub
        return 0;
    }

    static final class OpenHelper extends SQLiteOpenHelper {
        private static final int DB_VERSION = 1;
        public OpenHelper(Context context, String name) {
            super(context, name, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase arg0) {
            onUpgrade(arg0, 1, DB_VERSION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
            db.execSQL("CREATE TABLE IF NOT EXISTS spells " +
                         "(" + ID_COLUMN + " INTEGER PRIMARY KEY," +
                         NAME_COLUMN + " TEXT," +
                         ENABLE_COLUMN + " INTEGER," +
                         DESC_COLUMN + " TEXT," +
                         SCRIPT_COLUMN + " TEXT);");
            db.execSQL("CREATE INDEX spells_query ON spells(_id, name, enable)");
        }
    }

    private OpenHelper mDBHelper;
}
