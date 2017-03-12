package com.muv.test;


import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdapterRecycler  extends RecyclerView.Adapter<AdapterRecycler.BindingHolder>
{
    private List<Animal> animal = new ArrayList<>();
    private Context context;
    private MainActivity activity;


    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    public static class BindingHolder extends RecyclerView.ViewHolder
    {
        private ViewDataBinding binding;

        public BindingHolder(View rowView)
        {
            super(rowView);
            binding = DataBindingUtil.bind(rowView);
        }

        public ViewDataBinding getBinding()
        {
            return binding;
        }
    }

    public AdapterRecycler(List<Animal> animal) {
        this.animal = animal;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler, parent, false);
        BindingHolder holder = new BindingHolder(v);
        return holder;
    }

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView imageView, String v) {
        Picasso.with(imageView.getContext()).load(v).into(imageView);
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, final int position)
    {
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < activity.getColorParcelable().size(); i++)
        {
            colors.add(activity.getColorParcelable().get(i).color);
        }
        CardView cardView = (CardView) holder.getBinding().getRoot();
        cardView.setCardBackgroundColor(colors.get(position));

        final Animal animal = this.animal.get(position);
        holder.getBinding().setVariable(BR.animal, animal);
        holder.getBinding().executePendingBindings();
        holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String[] colorsTxt = context.getResources().getStringArray(R.array.colors);
                final List<Integer> colors = new ArrayList<>();
                for (int i = 0; i < colorsTxt.length; i++) {
                    int newColor = Color.parseColor(colorsTxt[i]);
                    colors.add(newColor);
                }
                int rand = new Random().nextInt(colors.size());
                Integer color = colors.get(rand);
                CardView cardView = (CardView) holder.getBinding().getRoot();
                cardView.setCardBackgroundColor(color);
                activity.setColorCard(position, color);
            }
        });
        if (position == this.animal.size() - 1)
        {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = pxFromDp(10);
            layoutParams.rightMargin = pxFromDp(10);
            layoutParams.topMargin = pxFromDp(10);
            layoutParams.bottomMargin = pxFromDp(10);
            holder.getBinding().getRoot().setLayoutParams(layoutParams);
        }
        Button button = (Button)holder.getBinding().getRoot().findViewById(R.id.detail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("Image", animal.getImage());
                intent.putExtra("Description", animal.getDescription());
                intent.putExtra("Dialog", activity.NEW_DIALOG);
                context.startActivity(intent);
            }
        });
    }

    private int pxFromDp(float dp)
    {
        return (int) Math.ceil(dp * context.getApplicationContext().getResources().getDisplayMetrics().density);
    }

    public void removeCard(int position, RecyclerView recyclerView)
    {
        animal.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        recyclerView.getRecycledViewPool().clear();
    }

    public List<Animal> getAnimal() {
        return animal;
    }

    @Override
    public int getItemCount() {
        return animal.size();
    }
}