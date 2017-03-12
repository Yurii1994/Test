package com.muv.test;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

public class DialogFragment extends android.support.v4.app.DialogFragment
{
    private String image;
    private String time;

    public void setImage(String image) {
        this.image = image;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Time", time);
        outState.putString("Image", image);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            time = savedInstanceState.getString("Time");
            image = savedInstanceState.getString("Image");
        }
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.dialog, new RelativeLayout(getActivity()), false);
        ImageView imageView = (ImageView)layout.findViewById(R.id.image_dialog);
        TextView timeView = (TextView)layout.findViewById(R.id.time_dialog);
        Picasso.with(imageView.getContext()).load(image).into(imageView);
        timeView.setText(time);

        return new MaterialDialog.Builder(getActivity())
                .customView(layout, false)
                .positiveText(R.string.ok)
                .negativeColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
                .autoDismiss(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .build();
    }
}