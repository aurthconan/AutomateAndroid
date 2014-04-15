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

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class SpellBookActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Cursor cursor =
                getContentResolver().query(SpellLibrary.SPELL_LIST_URI,
                        new String[]{SpellLibrary.ID_COLUMN, SpellLibrary.ENABLE_COLUMN, SpellLibrary.NAME_COLUMN}, null, null, null);
        CursorAdapter adapter = new CursorAdapter(this.getApplicationContext(), cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
            @Override
            public void bindView(View arg0, Context arg1, Cursor cursor) {
                CheckBox box = (CheckBox) arg0.findViewById(R.id.is_enabled);
                if (box != null) {
                    box.setChecked(cursor.getInt(1)==1);
                }
                TextView view = (TextView) arg0.findViewById(R.id.script_name);
                if (view != null) {
                    view.setText(cursor.getString(2));
                }
            }

            @Override
            public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
                View view = getLayoutInflater().inflate(R.layout.spell_list_item, null);
                return view;
            }
        };
        setContentView(R.layout.spell_book_layout);
        ListView list = (ListView) findViewById(R.id.spell_list);
        list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

}
