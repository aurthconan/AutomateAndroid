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
            }

        };

        public ModuleConnectionHelper( String pkgName, String serviceName ) {
            mPackageName = pkgName;
            mServiceName = serviceName;
        }

        public void bindService() {
            Intent intent = new Intent(Constants.ACTION_AUTOMATE_ANDROID_SUBMODULE);
            intent.setClassName(mPackageName, mServiceName);

            if (!mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)) {
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
            try {
                mModule = ModuleBinderHelper.getModuleDefinition( mModuleBinder );
                Log.e("ModuleManager", "getModuleDefinition " + mModule.mName );
                for ( MethodDefinition method: mModule.mMethods ) {
                    Log.e("ModuleManager", method.mReturnType.toString() + " " + method.mName);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public Vector<ScriptableObject> getModule() {
        Vector<ScriptableObject> vector = new Vector<ScriptableObject>();
        Log.e("ModuleManager", "mServiceMap.size " + Integer.toString(mServiceMap.size()));

        for ( ModuleConnectionHelper moduleHelper : mServiceMap.values() ) {
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
            ModuleConnectionHelper module = new ModuleConnectionHelper(info.serviceInfo.packageName, info.serviceInfo.name);
            module.bindService();
            module.initializeModuleDefinition();
            // module.unbindService();
            if ( mServiceMap.containsKey(module.mModule.mName) ) {
                Log.e("ModuleManager", module.mModule.mName + " is already defined in pkg "
                                        + mServiceMap.get(module.mModule.mName).mPackageName );
            } else {
                mServiceMap.put(module.mModule.mName, module);
            }
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
    public HashMap<String, ModuleConnectionHelper> mServiceMap = new HashMap<String, ModuleConnectionHelper>();

    public synchronized Object invokeModule( String moduleName, int methodIndex, Object[] args ) {
        ModuleConnectionHelper module = mServiceMap.get(moduleName);
        if ( module == null ) {
            Log.e("ModuleManager", moduleName + " not found" );
            return null;
        }
        // module.bindService();
        MethodDefinition def = module.mModule.mMethods.get(methodIndex);
        Object result = null;
        try {
            result = ModuleBinderHelper.invokeMethod(module.mModuleBinder, def.mReturnType, methodIndex, args);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return result;
    }
}
