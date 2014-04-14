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

import individual.aurthconan.automateandroid.module.ModuleDefinition.TYPE;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public class ModuleBinderHelper {

    // many methods set as package access on purpose

    // ModuleDefinition getModuleDefinition()
    static ModuleDefinition getModuleDefinition( IBinder binder ) throws RemoteException {
        ModuleDefinition result = null;
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(Constants.DESCRIPTOR);
            binder.transact(Constants.TRANSACTION_getDefinition, data,
                    reply, 0);
            reply.readException();
            if ( reply.readInt() != 0 ) {
                result = ModuleDefinition.CREATOR.createFromParcel(reply);
            }
        } finally {
            data.recycle();
            reply.recycle();
        }
        return result;
    }

    // void registerForEventTrigger(${predefined argument})
    static void registerForEventTrigger( IBinder binder, Object[] args ) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            data.writeInterfaceToken(Constants.DESCRIPTOR);
            writeArguments( data, args );
            binder.transact(Constants.TRANSACTION_registerForEventTrigger, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    static Object invokeMethod( IBinder binder, TYPE returnType, int methodIndex, Object[] args ) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        Object result = null;
        try {
            data.writeInterfaceToken(Constants.DESCRIPTOR);
            writeArguments( data, args );
            binder.transact(Constants.TRANSACTION_MODULE_METHOD_START+methodIndex, data, reply, 0);
            reply.readException();
            if ( returnType == TYPE.STRING_TYPE ) {
                if ( reply.readInt() == 1 ) {
                    result = reply.readString();
                }
            } else if ( returnType == TYPE.INTEGER_TYPE ) {
                result = reply.readInt();
            } else if ( returnType == TYPE.BOOLEAN_TYPE ) {
                result = reply.readInt()==1;
            } else if ( returnType == TYPE.DOUBLE_TYPE ) {
                result = reply.readDouble();
            }
        } finally {
            data.recycle();
            reply.recycle();
        }
        return result;
    }

    static private void writeArguments( Parcel out, Object[] args ) {
        for ( int i = 0, max = args.length; i < max;++i ) {
            Object arg = args[i];
            if ( arg instanceof String ) {
                if ( arg != null ) {
                    out.writeInt(1);
                    out.writeString((String)arg);
                }
            } else if ( arg instanceof Integer ) {
                out.writeInt((Integer)arg);
            } else if ( arg instanceof Boolean ) {
                out.writeInt(((Boolean)arg)?1:0);
            } else if ( arg instanceof Double ) {
                out.writeDouble((Double) arg );
            }
        }
    }
}
