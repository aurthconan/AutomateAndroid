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


package individual.aurthconan.automateandroid.module;

import individual.aurthconan.automateandroid.module.ClassDefinition.MethodDefinition;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import android.util.Log;

public class ModuleBridge extends ScriptableObject {
    public ModuleBridge( ClassDefinition def ) {
        super();
        mDefinition = def;
        for ( int i = 0, max = mDefinition.mMethods.size();
                i < max; ++i ) {
            MethodCallBridge methodBridge = new MethodCallBridge( i );
            ScriptableObject.defineProperty( this, mDefinition.mMethods.get(i).mName, methodBridge, ScriptableObject.DONTENUM );
        }
    }

    @Override
    public String getClassName() {
        return mDefinition.mName;
    }

    private final ClassDefinition mDefinition;
    private class MethodCallBridge extends BaseFunction {
        private final int mMethodIndex;

        public MethodCallBridge( int index ) {
            mMethodIndex = index;
        }

        @Override
        public Object call(Context arg0, Scriptable arg1, Scriptable arg2,
                Object[] arg3) {
            // TODO Auto-generated method stub
            MethodDefinition def = ModuleBridge.this.mDefinition.mMethods.get(mMethodIndex);
            Log.e("ModuleBridge", "callMethod " + def.mName );
            return null;
        }
        
    }
}
