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

package individual.aurthconan.automateandroid.core;

import individual.aurthconan.automateandroid.SpellLibrary;

import java.util.HashMap;

import org.mozilla.javascript.WrappedException;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/*
 * Manage spell life cycle
 */
public class SpellBook {
    public SpellBook(Context context) {
        Log.i("SpellBook", "SpellBook::SpellBook");
        mContext = context;
        mContentObserver = new ContentObserver(null) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                if (uri != null) {
                    Log.e("SpellBook", uri.toString());
                    uriStateChanged(uri);
                }
            }

            @Override
            public void onChange(boolean selfChange) {
                onChange( selfChange, null);
            }
            
        };
        context.getContentResolver().registerContentObserver(SpellLibrary.SPELL_LIST_URI,
                                        true, mContentObserver);
    }

    private void uriStateChanged(Uri uri) {
        Cursor cursor = mContext.getContentResolver().query(uri,
                                                new String[]{SpellLibrary.ID_COLUMN,
                                                             SpellLibrary.ENABLE_COLUMN,
                                                             SpellLibrary.NAME_COLUMN,
                                                             SpellLibrary.SCRIPT_COLUMN},
                                                null, null, null);
        if (cursor.getCount() == 0) {
            return;
        } else {
            cursor.moveToFirst();
        }
        Long id = cursor.getLong(0);
        boolean enabled = cursor.getInt(1)==1;
        String name = cursor.getString(2);
        String scriptString = cursor.getString(3);

        if (mSpells.containsKey(id) && !enabled) {
            // disable spell
            Spell spell = mSpells.get(id);
            mSpells.remove(id);
            spell.disable();
        } else if (!mSpells.containsKey(id) && enabled) {
            // enable spell
            Spell spell = new Spell(scriptString, name);
            try {
                spell.enable();
                mSpells.put(id, spell);
            } catch (WrappedException e) {
                Log.e("SpellBook", "got exception " + e.getMessage());
            }
        }
    }

    private Context mContext;
    private HashMap<Long, Spell> mSpells = new HashMap<Long, Spell>();
    private ContentObserver mContentObserver;
}
