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


package individual.aurthconan.automateandroid.wifi;

import individual.aurthconan.automateandroid.module.lib.Constants;
import individual.aurthconan.automateandroid.module.lib.ModuleDefinition;
import individual.aurthconan.automateandroid.module.lib.ModuleDefinition.MethodDefinition;
import individual.aurthconan.automateandroid.module.lib.ModuleDefinition.TYPE;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface WifiModule extends IInterface {
    public boolean isWifiEnabled();
    public void setWifiEnabled( boolean on );

    public static final int TRANSACTION_isWifiEnabled = (Constants.TRANSACTION_MODULE_METHOD_START + 0);
    public static final int TRANSACTION_setWifiEnabled = (Constants.TRANSACTION_MODULE_METHOD_START + 1);

    public static abstract class Stub extends Binder implements WifiModule {
        public Stub() {
            this.attachInterface(this, Constants.DESCRIPTOR);
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply,
                int flags) throws RemoteException {
            switch(code) {
            case INTERFACE_TRANSACTION: {
                reply.writeString(Constants.DESCRIPTOR);
                return true;
            }
            case Constants.TRANSACTION_getDefinition: {
                data.enforceInterface(Constants.DESCRIPTOR);

                ModuleDefinition def = new ModuleDefinition();
                def.mName = "wifi";

                MethodDefinition method = new MethodDefinition();
                method.mName = "isWifiEnabled";
                method.mReturnType = TYPE.BOOLEAN_TYPE;
                def.mMethods.add(method);

                method = new MethodDefinition();
                method.mName = "setWifiEnabled";
                method.mReturnType = TYPE.VOID_TYPE;
                method.mArgsType.add(TYPE.BOOLEAN_TYPE);
                def.mMethods.add(method);

                reply.writeNoException();
                reply.writeInt(1);
                def.writeToParcel(reply, 0);
                return true;
            }
            case Constants.TRANSACTION_registerForEventTrigger: {
                return true;
            }
            case TRANSACTION_isWifiEnabled: {
                data.enforceInterface(Constants.DESCRIPTOR);

                boolean result = this.isWifiEnabled();
                reply.writeNoException();
                reply.writeInt((result)?(1):(0));
                return true;
            }
            case TRANSACTION_setWifiEnabled: {
                data.enforceInterface(Constants.DESCRIPTOR);

                boolean arg0 = data.readInt()==1;
                this.setWifiEnabled(arg0);
                data.writeNoException();
                return true;
            }
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
}
