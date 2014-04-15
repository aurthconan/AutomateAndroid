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


package individual.aurthconan.automateandroid.module.lib;

import java.util.Vector;

import android.os.Parcel;
import android.os.Parcelable;

public class ModuleDefinition implements Parcelable {
    public ModuleDefinition() {
    }

    static private Vector<TYPE> readVectorOfTypeFromParcel( Parcel in ) {
        Vector<TYPE> result = new Vector<TYPE>();
        int max = in.readInt();
        for ( int i = 0; i < max; ++i ) {
            result.add(TYPE.values()[in.readInt()]);
        }
        return result;
    }
    static private void writeVectorOfTypeToParcel( Parcel dest, Vector<TYPE> types ) {
        dest.writeInt(types.size());
        for ( int i = 0, max = types.size(); i < max; ++i ) {
            dest.writeInt(types.get(i).ordinal());
        }
    }

    public enum TYPE{
        UNSUPPORTED_TYPE,
        STRING_TYPE,
        INTEGER_TYPE,
        BOOLEAN_TYPE,
        DOUBLE_TYPE,
        VOID_TYPE,
    }

    static public class MethodDefinition implements Parcelable {
        public MethodDefinition() {
        }
        public TYPE mReturnType;
        public String mName;
        public Vector<TYPE> mArgsType = new Vector<TYPE>();

        public static final Parcelable.Creator<MethodDefinition> CREATOR = new Parcelable.Creator<MethodDefinition>() {
            public MethodDefinition createFromParcel(Parcel in) {
                return new MethodDefinition( in );
            }

            public MethodDefinition[] newArray(int size) {
                return new MethodDefinition[size];
            }
        };
        public MethodDefinition( Parcel in ) {
            mReturnType = TYPE.values()[in.readInt()];
            mName = in.readString();
            mArgsType = readVectorOfTypeFromParcel( in );
        }
        @Override
        public int describeContents() {
            return 0;
        }
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(mReturnType.ordinal());
            dest.writeString(mName);
            writeVectorOfTypeToParcel( dest, mArgsType );
        }

    }

    static public class EventCallbackDefinition implements Parcelable {
        public String mEventId;
        public Vector<TYPE> mArgsType = new Vector<TYPE>();
        public Vector<TYPE> mCallbackArgs = new Vector<TYPE>();
        @Override
        public int describeContents() {
            return 0;
        }
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mEventId);
            writeVectorOfTypeToParcel( dest, mArgsType );
            writeVectorOfTypeToParcel( dest, mCallbackArgs );
        }
        public static final Parcelable.Creator<EventCallbackDefinition> CREATOR = new Parcelable.Creator<EventCallbackDefinition>() {
            public EventCallbackDefinition createFromParcel(Parcel in) {
                return new EventCallbackDefinition( in );
            }

            public EventCallbackDefinition[] newArray(int size) {
                return new EventCallbackDefinition[size];
            }
        };
        public EventCallbackDefinition( Parcel in ) {
            mEventId = in.readString();
            mArgsType = readVectorOfTypeFromParcel( in );
            mCallbackArgs = readVectorOfTypeFromParcel( in );
        }
    }

    public String mName;
    public Vector<MethodDefinition> mMethods = new Vector<MethodDefinition>();
    public Vector<EventCallbackDefinition> mEventCallbacks = new Vector<EventCallbackDefinition>();

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mMethods.size());
        for ( int i = 0, max = mMethods.size(); i < max; ++i ) {
            mMethods.get(i).writeToParcel(dest, flags);
        }
        dest.writeInt(mEventCallbacks.size());
        for ( int i = 0, max = mEventCallbacks.size(); i < max; ++i ) {
            mEventCallbacks.get(i).writeToParcel(dest, flags);
        }
    }
    public ModuleDefinition( Parcel in ) {
        mName = in.readString();
        int max = in.readInt();
        for ( int i = 0; i < max; ++i ) {
            mMethods.add(MethodDefinition.CREATOR.createFromParcel(in));
        }
        max = in.readInt();
        for ( int i = 0; i < max; ++i ) {
            mEventCallbacks.add(EventCallbackDefinition.CREATOR.createFromParcel(in));
        }
    }

    public static final Parcelable.Creator<ModuleDefinition> CREATOR = new Parcelable.Creator<ModuleDefinition>() {
        public ModuleDefinition createFromParcel(Parcel in) {
            return new ModuleDefinition( in );
        }

        public ModuleDefinition[] newArray(int size) {
            return new ModuleDefinition[size];
        }
    };

}
