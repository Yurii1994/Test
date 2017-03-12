package com.muv.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity
{
    private List<GetModel> data = new ArrayList<>();
    public static ArrayList<AnimalParcelable> animalsParcelable = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressWheel progress;
    private TextView hint;
    private Bundle savedInstanceState;
    private AdapterRecycler adapterRecycler;
    private ArrayList<ColorParcelable> colorParcelable = new ArrayList<>();
    public final String NEW_DIALOG = "com.muv.action.NEW_DIALOG";
    private final String START_SERVICE = "com.muv.action.StartService";
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progress = (ProgressWheel) findViewById(R.id.progress);
        hint = (TextView)findViewById(R.id.hint);
        this.savedInstanceState = savedInstanceState;
        createActivity(savedInstanceState);
        if (savedInstanceState == null)
        {
            Intent intent = new Intent(START_SERVICE);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            sendBroadcast(intent);
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

    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("Animals", animalsParcelable);
        outState.putParcelableArrayList("Colors", colorParcelable);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        animalsParcelable = savedInstanceState.getParcelableArrayList("Animals");
        colorParcelable = savedInstanceState.getParcelableArrayList("Colors");
    }

    private void createActivity(Bundle savedInstanceState)
    {
        if(savedInstanceState == null || !savedInstanceState.containsKey("Animals"))
        {
            progress.setVisibility(View.VISIBLE);
            App.getApi().getData("select").enqueue(new Callback<List<GetModel>>() {
                @Override
                public void onResponse(Call<List<GetModel>> call, Response<List<GetModel>> response)
                {
                    if (response.body() != null)
                    {
                        data = new ArrayList<>();
                        data.addAll(response.body());
                        List<Animal> animals = setAnimal(data);
                        animalsParcelable = new ArrayList<>();
                        for (int i = 0; i < animals.size(); i++)
                        {
                            String image = animals.get(i).getImage();
                            String description = animals.get(i).getDescription();
                            animalsParcelable.add(new AnimalParcelable(image, description));
                        }
                        setAdapter(animals);
                    }
                }

                @Override
                public void onFailure(Call<List<GetModel>> call, Throwable t)
                {
                    progress.setVisibility(View.GONE);
                    hint.setVisibility(View.VISIBLE);
                }
            });
        }
        else
        {
            animalsParcelable = savedInstanceState.getParcelableArrayList("Animals");
            List<Animal> animals = new ArrayList<>();
            for (int i = 0; i < animalsParcelable.size(); i++)
            {
                Animal animal = new Animal();
                animal.setImage(animalsParcelable.get(i).image);
                animal.setDescription(animalsParcelable.get(i).description);
                animals.add(animal);
            }
            setAdapter(animals);
        }
    }


    public ArrayList<ColorParcelable> getColorParcelable() {
        return colorParcelable;
    }

    private ArrayList<ColorParcelable> setColorCardDefault()
    {
        ArrayList<ColorParcelable> colorParcelable = new ArrayList<>();
        for (int i = 0; i < animalsParcelable.size(); i++)
        {
            colorParcelable.add(new ColorParcelable(getResources().getColor(R.color.colorWhite)));
        }
        return colorParcelable;
    }

    public void setColorCard(int position, int color)
    {
        colorParcelable.remove(position);
        colorParcelable.add(position, new ColorParcelable(color));
    }

    private void setAdapter(List<Animal> animals)
    {
        adapterRecycler = new AdapterRecycler(animals);
        adapterRecycler.setContext(this);
        adapterRecycler.setActivity(this);
        recyclerView.setAdapter(adapterRecycler);
        progress.setVisibility(View.GONE);
        colorParcelable = new ArrayList<>();
        colorParcelable = setColorCardDefault();
                SwipeableRecyclerViewTouchListener swipeTouchListener = new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    colorParcelable.remove(position);
                                    adapterRecycler.removeCard(position, recyclerView);
                                    adapterRecycler.notifyItemRemoved(position);
                                    animalsParcelable = new ArrayList<>();
                                    List<Animal> animals = adapterRecycler.getAnimal();
                                    for (int i = 0; i < animals.size(); i++) {
                                        String image = animals.get(i).getImage();
                                        String description = animals.get(i).getDescription();
                                        animalsParcelable.add(new AnimalParcelable(image, description));
                                    }
                                }
                                adapterRecycler.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    adapterRecycler.removeCard(position, recyclerView);
                                    adapterRecycler.notifyItemRemoved(position);
                                    animalsParcelable = new ArrayList<>();
                                    List<Animal> animals = adapterRecycler.getAnimal();
                                    for (int i = 0; i < animals.size(); i++) {
                                        String image = animals.get(i).getImage();
                                        String description = animals.get(i).getDescription();
                                        animalsParcelable.add(new AnimalParcelable(image, description));
                                    }
                                }
                                adapterRecycler.notifyDataSetChanged();
                            }
                        });
        recyclerView.addOnItemTouchListener(swipeTouchListener);
    }

    private List<Animal> setAnimal(List<GetModel> data)
    {
        List<Animal> animalList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++)
        {
            Animal animal = new Animal();
            animal.setImage(data.get(i).getUrlImage());
            animal.setDescription(data.get(i).getText());
            animalList.add(animal);
        }
        return animalList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_manu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_update:
                recyclerView.setAdapter(null);
                adapterRecycler.notifyDataSetChanged();
                createActivity(null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
