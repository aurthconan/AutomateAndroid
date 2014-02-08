package individual.aurthconan.automateandroid.wrap;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.mozilla.javascript.Function;

public class BroadcastReceiver extends android.content.BroadcastReceiver {

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
}
