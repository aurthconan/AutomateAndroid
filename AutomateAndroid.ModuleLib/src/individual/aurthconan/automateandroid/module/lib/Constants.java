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

public class Constants {
    public final static String ACTION_AUTOMATE_ANDROID_SUBMODULE = "individual.aurthconan.automateandroid.submodule";
    public final static String DESCRIPTOR = "individual.aurthconan.automateandroid.submodule";
    public final static int TRANSACTION_getDefinition = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    public final static int TRANSACTION_registerForEventTrigger = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);

    // reserve 50 slot for possible generic method. Hope that is enough
    public final static int TRANSACTION_MODULE_METHOD_START = android.os.IBinder.FIRST_CALL_TRANSACTION + 50;
}
