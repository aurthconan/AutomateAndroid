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

import individual.aurthconan.automateandroid.core.SpellBook;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

public class ScriptEngineService extends Service {
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Thread init = new Thread() {
            @Override
            public void run() {
                mSpellBook = new SpellBook(AutomateAndroidApplication.mContext);
            }
        };
        init.run();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String action = intent.getAction();
        if ( action.equals("individual.aurthconan.automateandroid.run_script") ) {
            String scriptString = intent.getStringExtra("script");
            String name = intent.getStringExtra("name");
            // Spell spell = new Spell(scriptString, name);
            // spell.enable();
            ContentValues values = new ContentValues();
            values.put(SpellLibrary.NAME_COLUMN, name);
            values.put(SpellLibrary.SCRIPT_COLUMN, scriptString);
            values.put(SpellLibrary.ENABLE_COLUMN, 1);
            getContentResolver().insert(SpellLibrary.SPELL_LIST_URI, values);
        }
        return startId;
    }

    public SpellBook mSpellBook;
}
