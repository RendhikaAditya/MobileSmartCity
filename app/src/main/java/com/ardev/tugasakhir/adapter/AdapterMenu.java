package com.ardev.tugasakhir.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ardev.tugasakhir.activity.Konten;
import com.bumptech.glide.Glide;
import com.ardev.tugasakhir.R;
import com.ardev.tugasakhir.activity.SubMenu;
import com.ardev.tugasakhir.model.ModelMenu;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AdapterMenu  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<ModelMenu> mItems;

    public AdapterMenu(List<ModelMenu> mItems, Context context) {
        this.mItems = mItems;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_menu, parent, false);
        return new HolderData(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ModelMenu md = mItems.get(position);
        final HolderData holderData = (HolderData) holder;
        holderData.title_menu_app.setText(md.getTitle_menu_app());
        holderData.subtitle_menu_app.setText(md.getSubtitle_menu_app());
        Glide.with(context)
                .load(md.getImage_menu_app())
                .into(holderData.image_menu_app);

        holderData.card_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Konten.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//                i.putExtra("id_menu_app", md.getId_menu_app());
//                i.putExtra("title_menu_app", md.getTitle_menu_app());
//                i.putExtra("subtitle_menu_app", md.getSubtitle_menu_app());
//                i.putExtra("image_menu_app", md.getImage_menu_app());
                i.putExtra("id_submenu_app", md.getId_menu_app());
                i.putExtra("title_submenu_app", md.getTitle_menu_app());
                i.putExtra("subtitle_submenu_app", md.getSubtitle_menu_app());
                i.putExtra("image_submenu_app", md.getImage_menu_app());
                context.startActivity(i);
            }
        });

        if (position == 0){
            holderData.card_menu.setCardBackgroundColor(Color.parseColor("#26A69A"));
        }else if(position%2 == 0){
            holderData.card_menu.setCardBackgroundColor(Color.parseColor("#42A5F5"));
        }else if(position%3 == 0){
            holderData.card_menu.setCardBackgroundColor(Color.parseColor("#FF7043"));
        }else{
            holderData.card_menu.setCardBackgroundColor(Color.parseColor("#EF5350"));
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class HolderData extends RecyclerView.ViewHolder {
        public TextView title_menu_app, subtitle_menu_app;
        public ImageView image_menu_app;
        public CardView card_menu;

        public HolderData(View view) {
            super(view);
            title_menu_app = (TextView) view.findViewById(R.id.title_menu_app);
            subtitle_menu_app = (TextView) view.findViewById(R.id.subtitle_menu_app);
            image_menu_app = (ImageView) view.findViewById(R.id.image_menu_app);
            card_menu = (CardView) view.findViewById(R.id.card_menu);

//            title_menu_app = (TextView) view.findViewById(R.id.title_submenu_app);
//            subtitle_menu_app = (TextView) view.findViewById(R.id.subtitle_submenu_app);
//            image_menu_app = (ImageView) view.findViewById(R.id.image_submenu_app);
//            card_menu = (CardView) view.findViewById(R.id.card_submenu);
        }
    }
}
