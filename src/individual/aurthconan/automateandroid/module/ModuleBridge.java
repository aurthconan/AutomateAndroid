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

import individual.aurthconan.automateandroid.module.lib.ModuleDefinition;
import individual.aurthconan.automateandroid.module.lib.ModuleDefinition.MethodDefinition;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import android.util.Log;

public class ModuleBridge extends ScriptableObject {

    public ModuleBridge( ModuleDefinition def ) {
        super();
        if ( def == null ) {
            Log.e("ModuleBridge", "def is null");
            throw new Error();
        }
        mDefinition = def;
        Log.e("ModuleBridge", "mDefinition.mMethods.size " + Integer.toString(mDefinition.mMethods.size()));
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

    private final ModuleDefinition mDefinition;
    private class MethodCallBridge extends BaseFunction {
        private final int mMethodIndex;

        public MethodCallBridge( int index ) {
            mMethodIndex = index;
        }

        @Override
        public Object call(Context arg0, Scriptable arg1, Scriptable arg2,
                Object[] arg3) {
            MethodDefinition def = mDefinition.mMethods.get(mMethodIndex);
            Log.e("ModuleBridge", "callMethod " + def.mName );
            if ( def.mArgsType.size() != arg3.length ) {
                Log.e("ModuleBridge", "Method " + mDefinition.mName + "." + def.mName 
                                      + " requires " + def.mArgsType.size() + "arguments but got"
                                      + arg3.length );
            }
            Object[] args = new Object[def.mArgsType.size()];
            for ( int i = 0, max = def.mArgsType.size(); i < max; ++i ) {
                args[i] = Context.jsToJava(arg3[i], ModuleDefinition.getClazz(def.mArgsType.get(i)));
            }
            ModuleManager moduleManager = ModuleManager.getModuleManager(null);
            Object result = moduleManager.invokeModule(mDefinition.mName, mMethodIndex, args);
            if ( result != null ) {
                Log.e("ModuleBridge", "result " + result.toString() );
            }
            return result;
        }
    }
}
