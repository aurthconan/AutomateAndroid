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

import individual.aurthconan.automateandroid.AutomateAndroidApplication;
import individual.aurthconan.automateandroid.module.ModuleManager;

import java.util.Vector;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrapFactory;

import android.util.Log;

/*
 * Represent a script that is actively enabled.
 */
public class Spell {
    /*
     * Constant variable exists in js scope containing the name of the script. 
     */
    public static final String SCRIPT_NAME_VARIABLE = "SCRIPT_NAME";
    /*
     * variable exists in js scope containing the android context
     */
    public static final String ANDROID_CONTEXT_VARIABLE = "ANDROID_CONTEXT";

    public Spell(String script, String name) {
        mName = name;
        org.mozilla.javascript.Context context = Context.enter();
        try {
            context.setOptimizationLevel(-1);
            mScope = createScope(context);
            mScript = context.compileString(script, mName, 0, null);
        } finally {
            Context.exit();
        }
    }

    public String getName() {
        return mName;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void enable() {
        if (mEnabled) {
            return;
        }
        org.mozilla.javascript.Context context = Context.enter();
        try {
            context.setWrapFactory(new WrapFactory() {

                @Override
                public Object wrap(Context arg0, Scriptable arg1, Object arg2,
                        Class<?> arg3) {
                    Log.e("wrap", arg3==null?"null":arg3.getName());
                    return super.wrap(arg0, arg1, arg2, arg3);
                }

                @Override
                public Scriptable wrapAsJavaObject(Context arg0,
                        Scriptable arg1, Object arg2, Class<?> arg3) {
                    Log.e("wrapAsJavaObject", arg2==null?"null":arg2.getClass()==null?"null":arg2.getClass().getName());
                    return super.wrapAsJavaObject(arg0, arg1, arg2, arg3);
                }

                @Override
                public Scriptable wrapJavaClass(Context arg0, Scriptable arg1,
                        Class arg2) {
                    Log.e("wrapJavaClass", arg2==null?"null":arg2.getName());
                    return super.wrapJavaClass(arg0, arg1, arg2);
                }

                @Override
                public Scriptable wrapNewObject(Context arg0, Scriptable arg1,
                        Object arg2) {
                    Log.e("wrapNewObject", arg2==null?"null":arg2.getClass()==null?"null":arg2.getClass().getName());
                    if (arg2 instanceof ResourceHandler) {
                        ResourceHandler r = (ResourceHandler) arg2;
                        r.alloc();
                        mResourceHandler.add(r);
                    }
                    return super.wrapNewObject(arg0, arg1, arg2);
                }
            });
            mScript.exec(context, mScope);
        } finally {
            Context.exit();
        }
    }

    public void disable() {
        if (!mEnabled) {
            return;
        }

        // release all resource
        for(ResourceHandler r: mResourceHandler) {
            r.release();
        }
    }

    private static org.mozilla.javascript.ScriptableObject createScope(org.mozilla.javascript.Context context) {
        ScriptableObject scope = context.initStandardObjects();
        Object androidContext = Context.javaToJS(AutomateAndroidApplication.mContext, scope);
        ScriptableObject.putProperty(scope, ANDROID_CONTEXT_VARIABLE, androidContext);
        Vector<ScriptableObject> objects = ModuleManager.getModuleManager( AutomateAndroidApplication.mContext ).getModule();
        for ( ScriptableObject obj : objects) {
            ScriptableObject.putProperty(scope, obj.getClassName(), obj);
        }
        return scope;
    }

    private ScriptableObject mScope;
    private Script mScript;
    private String mName;
    private boolean mEnabled = false;
    private Vector<ResourceHandler> mResourceHandler = new Vector<ResourceHandler>();
}
