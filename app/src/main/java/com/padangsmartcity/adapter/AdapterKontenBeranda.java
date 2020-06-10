package com.padangsmartcity.adapter;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.padangsmartcity.R;
import com.padangsmartcity.activity.DetailKonten;
import com.padangsmartcity.helper.Helper;
import com.padangsmartcity.model.ModelKonten;
import com.padangsmartcity.util.PrefManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AdapterKontenBeranda extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ModelKonten> mItems;

    public AdapterKontenBeranda(List<ModelKonten> mItems, Context context) {
        this.mItems = mItems;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_konten_beranda, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ModelKonten md = mItems.get(position);
        final HolderData holderData = (HolderData) holder;
        holderData.title_konten_app.setText(md.getTitle_konten_app());
        holderData.subtitle_konten_app.setText(md.getSubtitle_konten_app());
        String jarak = "";
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (md.getLat().equals("null")) {
                    holderData.jarak.setVisibility(View.GONE);
                } else {
                    Helper helper = new Helper();
                    PrefManager prefManager = new PrefManager(context);
                    Double lat1 = Double.parseDouble(prefManager.getLat());
                    Double lng1 = Double.parseDouble(prefManager.getLng());
                    Double lat2 = Double.parseDouble(md.getLat());
                    Double lng2 = Double.parseDouble(md.getLng());
                    holderData.jarak.setText(helper.getDistance(lat1, lng1, lat2, lng2));
                    holderData.jarak.setVisibility(View.VISIBLE);
                    jarak = helper.getDistance(lat1, lng1, lat2, lng2);
                }
            } else {
                holderData.jarak.setVisibility(View.GONE);
            }
        } else {
            holderData.jarak.setVisibility(View.GONE);
        }

        try {
            Glide.with(context)
                    .load(new URL(md.getImage_konten_app()))
                    .placeholder(R.drawable.no_image)
                    .into(holderData.image_konten_app);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        final String finalJarak = jarak;
        holderData.card_konten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailKonten.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                i.putExtra("id_konten_app", md.getId_konten_app());
                i.putExtra("title_konten_app", md.getTitle_konten_app());
                i.putExtra("subtitle_konten_app", md.getSubtitle_konten_app());
                i.putExtra("konten_app", md.getKonten_app());
                i.putExtra("image_konten_app", md.getImage_konten_app());
                i.putExtra("lat", md.getLat());
                i.putExtra("lng", md.getLng());
                i.putExtra("jarak", finalJarak);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class HolderData extends RecyclerView.ViewHolder {
        public TextView title_konten_app, subtitle_konten_app, jarak;
        public ImageView image_konten_app;
        public CardView card_konten;

        public HolderData(View view) {
            super(view);
            title_konten_app = (TextView) view.findViewById(R.id.title_konten_app);
            subtitle_konten_app = (TextView) view.findViewById(R.id.subtitle_konten_app);
            jarak = (TextView) view.findViewById(R.id.jarak);
            image_konten_app = (ImageView) view.findViewById(R.id.image_konten_app);
            card_konten = (CardView) view.findViewById(R.id.card_konten);
        }
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }
}

