package com.ardev.tugasakhir.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ardev.tugasakhir.R;
import com.ardev.tugasakhir.activity.Konten;
import com.ardev.tugasakhir.activity.SubMenu;
import com.ardev.tugasakhir.model.ModelMenu;
import com.ardev.tugasakhir.model.ModelSubmenu;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AdapterSubmenu   extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<ModelSubmenu> mItems;

    public AdapterSubmenu(List<ModelSubmenu> mItems, Context context) {
        this.mItems = mItems;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_submenu, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ModelSubmenu md = mItems.get(position);
        final HolderData holderData = (HolderData) holder;
        holderData.title_submenu_app.setText(md.getTitle_submenu_app());
        holderData.subtitle_submenu_app.setText(md.getSubtitle_submenu_app());
        try {
            Glide.with(context)
                    .load(new URL(md.getImage_submenu_app()))
                    .placeholder(R.drawable.no_image)
                    .into(holderData.image_submenu_app);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        holderData.card_submenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Konten.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                i.putExtra("id_submenu_app", md.getId_submenu_app());
                i.putExtra("title_submenu_app", md.getTitle_submenu_app());
                i.putExtra("subtitle_submenu_app", md.getSubtitle_submenu_app());
                i.putExtra("image_submenu_app", md.getImage_submenu_app());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class HolderData extends RecyclerView.ViewHolder {
        public TextView title_submenu_app, subtitle_submenu_app;
        public ImageView image_submenu_app;
        public CardView card_submenu;

        public HolderData(View view) {
            super(view);
            title_submenu_app = (TextView) view.findViewById(R.id.title_submenu_app);
            subtitle_submenu_app = (TextView) view.findViewById(R.id.subtitle_submenu_app);
            image_submenu_app = (ImageView) view.findViewById(R.id.image_submenu_app);
            card_submenu = (CardView) view.findViewById(R.id.card_submenu);
        }
    }
}
