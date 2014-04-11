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

import individual.aurthconan.automateandroid.module.ModuleDefinition.MethodDefinition;
import individual.aurthconan.automateandroid.module.ModuleDefinition.TYPE;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.mozilla.javascript.ScriptableObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class ModuleManager {

    private class ModuleConnectionHelper {
        public ModuleDefinition mModule;
        public final String mPackageName;
        public final String mServiceName;

        public IBinder mModuleBinder = null;
        private Object mBinderLock = new Object();

        private ServiceConnection mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName arg0, IBinder binder) {
                Log.e("ModuleManager", "onServiceConnected");
                mModuleBinder = binder;
                synchronized(mBinderLock) {
                    mBinderLock.notifyAll();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName binder) {
                mModuleBinder = null;
                synchronized(mBinderLock) {
                    mBinderLock.notifyAll();
                }
            }

        };

        public ModuleConnectionHelper( String pkgName, String serviceName ) {
            mPackageName = pkgName;
            mServiceName = serviceName;
        }

        public void bindService() {
            Intent intent = new Intent(Constants.ACTION_AUTOMATE_ANDROID_SUBMODULE);
            intent.setClassName(mPackageName, mServiceName);

            if (!ModuleManager.this.mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)) {
                throw new RuntimeException("ServiceNotFound");
            }
            try {
                synchronized(mBinderLock) {
                    mBinderLock.wait();
                }
            } catch (InterruptedException e) {
                Log.e("ModuleManager", e.toString());
            }
        }

        public void unbindService() {
            mContext.unbindService( mServiceConnection );
        }

        public void initializeModuleDefinition() {
            if ( mModule != null || mModuleBinder == null ) {
                return;
            }
            ModuleBinderHelper helper = new ModuleBinderHelper();
            try {
                mModule = helper.getModuleDefinition( mModuleBinder );
                Log.e("ModuleManager", "getModuleDefinition " + mModule.mName );
                for ( MethodDefinition method: mModule.mMethods ) {
                    Log.e("ModuleManager", method.mReturnType.toString() + " " + method.mName);
                }
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public Vector<ScriptableObject> getModule() {
        Vector<ScriptableObject> vector = new Vector<ScriptableObject>();
        Log.e("ModuleManager", "mServiceMap.size " + Integer.toString(mServiceMap.size()));

        for ( ModuleConnectionHelper moduleHelper : mServiceMap ) {
            vector.add( new ModuleBridge( moduleHelper.mModule ) );
        }
        return vector;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentServices(new Intent(Constants.ACTION_AUTOMATE_ANDROID_SUBMODULE),
                PackageManager.GET_INTENT_FILTERS);
        for ( ResolveInfo info: list ) {
            Log.i("ModuleManager", info.serviceInfo.packageName + ":" + info.serviceInfo.name);
            mServiceMap.add(new ModuleConnectionHelper(info.serviceInfo.packageName, info.serviceInfo.name));
        }
        for ( ModuleConnectionHelper module : mServiceMap ) {
            module.bindService();
            module.initializeModuleDefinition();
            module.unbindService();
        }
    }

    static private ModuleManager sModuleManager = null;
    static public ModuleManager getModuleManager( Context context ) {
        if ( sModuleManager == null ) {
            sModuleManager = new ModuleManager();
            sModuleManager.init( context );
        }
        return sModuleManager;
    }

    private Context mContext;
    public Vector<ModuleConnectionHelper> mServiceMap = new Vector<ModuleConnectionHelper>();
}
