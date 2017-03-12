package com.muv.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class StartServiceReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        int minute = 60000;
        Timer timer = new Timer();
        TimerTask task = new TaskStartService(context);
        timer.schedule(task, 0, minute * 2);
    }

    public class TaskStartService extends TimerTask {

        public TaskStartService(Context context) {
            this.context = context;
        }

        Context context;

        @Override
        public void run()
        {
            Internet internet = new Internet();
            if (internet.isOnline(context))
            {
                context.startService(new Intent(context, Service.class));
            }
        }
    }
}
