package com.muv.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DialogReceiver extends BroadcastReceiver
{
    public DialogReceiver()
    {}

    @Override
    public void onReceive(Context context, Intent intent)
    {
        context.sendBroadcast(new Intent(intent));
    }
}
