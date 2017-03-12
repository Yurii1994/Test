package com.muv.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class DetailActivity extends AppCompatActivity
{
    private boolean state_animation;
    private String NEW_DIALOG;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String image = intent.getStringExtra("Image");
        String description = intent.getStringExtra("Description");
        NEW_DIALOG = intent.getStringExtra("Dialog");

        ImageView imageView = (ImageView)findViewById(R.id.image_detail);
        TextView textView = (TextView)findViewById(R.id.text_detail);
        textView.setText(description);
        Picasso.with(imageView.getContext()).load(image).into(imageView);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null)
        {
            if (!state_animation)
            {
                final Animation animation = AnimationUtils.loadAnimation(this,
                        R.anim.animation);
                imageView.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener()
                {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        state_animation = true;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                if (fragments != null)
                {
                    if (fragments.size() > 0)
                    {
                        for (int i = 0; i < fragments.size(); i++)
                        {
                            try
                            {
                                DialogFragment fragment = (DialogFragment) fragments.get(i);
                                fragment.dismiss();
                            }
                            catch (Exception e)
                            {}
                        }
                    }
                }
                String image = intent.getStringExtra("Image");
                String time = intent.getStringExtra("Time");
                DialogFragment dialogFragment = new DialogFragment();
                dialogFragment.setTime(time);
                dialogFragment.setImage(image);
                dialogFragment.show(getSupportFragmentManager(), "dialogFragment");

            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(NEW_DIALOG));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean("Animation", state_animation);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        state_animation = savedInstanceState.getBoolean("Animation");
    }
}
