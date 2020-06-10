package com.padangsmartcity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.padangsmartcity.R;
import com.padangsmartcity.activity.Tagihan;
import com.padangsmartcity.model.ModelTagih;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterTagihan extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
private List<ModelTagih> mModel;
    private Context context;


    public AdapterTagihan(List<ModelTagih> mModel, Context context) {
        this.mModel = mModel;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_menu, parent, false);
        return new HolderData(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ModelTagih md = mModel.get(position);
        final AdapterTagihan.HolderData holderData = (AdapterTagihan.HolderData) holder;
        holderData.title_menu_app.setText(md.getTitle_menu_app());
        holderData.subtitle_menu_app.setText(md.getSubtitle_menu_app());
        Glide.with(context)
                .load(md.getImage_menu_app())
                .into(holderData.image_menu_app);
//        holderData.card_menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, Konten.class);
//
        holderData.card_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Tagihan.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mModel.size();
    }


    private class HolderData extends RecyclerView.ViewHolder {
        public TextView title_menu_app, subtitle_menu_app;
        public ImageView image_menu_app;
        public CardView card_menu;
        public HolderData(@NonNull View view) {
            super(view);
            title_menu_app = (TextView) view.findViewById(R.id.title_menu_app);
            subtitle_menu_app = (TextView) view.findViewById(R.id.subtitle_menu_app);
            image_menu_app = (ImageView) view.findViewById(R.id.image_menu_app);
            card_menu = (CardView) view.findViewById(R.id.card_menu);
        }
    }

}
