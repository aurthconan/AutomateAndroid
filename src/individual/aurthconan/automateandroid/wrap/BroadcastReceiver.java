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

package individual.aurthconan.automateandroid.wrap;

import individual.aurthconan.automateandroid.AutomateAndroidApplication;
import individual.aurthconan.automateandroid.core.ResourceHandler;

import org.mozilla.javascript.Function;

import android.content.Context;
import android.content.Intent;

public class BroadcastReceiver extends android.content.BroadcastReceiver implements ResourceHandler {

    private final Function mOnReceiveCallback;
    
    public BroadcastReceiver( Function onReceiveCallback ) {
        mOnReceiveCallback = onReceiveCallback;
    }

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        org.mozilla.javascript.Context context = org.mozilla.javascript.Context.enter();
        try {
            mOnReceiveCallback.call(context,
                                mOnReceiveCallback.getParentScope(), null, new Object[] {arg0, arg1});
        } finally {
            org.mozilla.javascript.Context.exit();
        }
    }

    @Override
    public void alloc() {
        // nothing to do
    }

    @Override
    public void release() {
        AutomateAndroidApplication.mContext.unregisterReceiver(this);
    }
}
