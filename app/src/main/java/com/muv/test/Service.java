package com.muv.test;


import android.app.IntentService;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.muv.test.MainActivity.animalsParcelable;

public class Service extends IntentService
{
    private final String NEW_DIALOG = "com.muv.action.NEW_DIALOG";

    public Service() {
        super("Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(final Intent intent)
    {
        if (animalsParcelable.size() > 0)
        {
            List<Animal> animals = new ArrayList<>();
            for (int i = 0; i < animalsParcelable.size(); i++)
            {
                Animal animal = new Animal();
                animal.setImage(animalsParcelable.get(i).image);
                animal.setDescription(animalsParcelable.get(i).description);
                animals.add(animal);
            }
            int rand = new Random().nextInt(animals.size());
            String image = animals.get(rand).getImage();
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            String time = getResources().getString(R.string.time) + " - " + hour + ":" + minute;
            Intent intentDialog = new Intent(NEW_DIALOG);
            intentDialog.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intentDialog.putExtra("Image", image);
            intentDialog.putExtra("Time", time);
            sendBroadcast(intentDialog);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

}