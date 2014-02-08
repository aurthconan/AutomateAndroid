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

import org.mozilla.javascript.Context;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.ScriptableObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ScriptEngineService extends Service {
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String action = intent.getAction();
        if ( action.equals("individual.aurthconan.automateandroid.run_script") ) {
            String scriptString = intent.getStringExtra("script");
            String name = intent.getStringExtra("name");
            org.mozilla.javascript.Context context = Context.enter();
            try {
                context.setOptimizationLevel(-1);
                ScriptableObject scriptableObject = context.initStandardObjects();
                Object androidContext = Context.javaToJS(this, scriptableObject);
                ScriptableObject.putProperty(scriptableObject, "android_context", androidContext);
                org.mozilla.javascript.Script script = context.compileString(scriptString, name, 0, null);
                script.exec(context, scriptableObject);
            }
            catch (EvaluatorException compileErr) {
                Log.e("compile err", compileErr.lineNumber() + ":" + compileErr.columnNumber() + "->" + compileErr.details() );
            }
            finally {
                Context.exit();
            }
        }
        return startId;
    }


}
