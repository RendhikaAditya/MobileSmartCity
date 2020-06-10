package com.padangsmartcity.adapter;

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
import com.padangsmartcity.R;
import com.padangsmartcity.activity.LihatFoto;
import com.padangsmartcity.model.ModelTanggapan;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AdapterTanggapan extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ModelTanggapan> mItems;

    public AdapterTanggapan(List<ModelTanggapan> mItems, Context context) {
        this.mItems = mItems;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tanggapan, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ModelTanggapan md = mItems.get(position);
        final HolderData holderData = (HolderData) holder;
//        if (md.getNama_admin().equals("null")){
            holderData.nama_tanggapan.setText(md.getNama_user());
            holderData.nama_tanggapan.setBackgroundResource(R.color.light);
//        }else{
//            holderData.nama_tanggapan.setText("    ADMINISTRATOR    ");
//            holderData.nama_tanggapan.setBackgroundResource(R.drawable.shape_bg_round_edittext);
//        }

        holderData.tanggal_tanggapan.setText(md.getTanggal_tanggapan());
        holderData.isi_tanggapan.setText(md.getIsi_tanggapan());

        if (md.getNama_admin().equals("null")){
            try {
                Glide.with(context)
                        .load(new URL(md.getThumb_user()))
                        .placeholder(R.drawable.img_profile)
                        .into(holderData.foto_user_tanggapan);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else{
            try {
                Glide.with(context)
                        .load(new URL(md.getThumb_admin()))
                        .placeholder(R.drawable.img_profile)
                        .into(holderData.foto_user_tanggapan);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        if (md.getVerified().equals("0")){
            holderData.verified.setVisibility(View.GONE);
        }else{
            holderData.verified.setVisibility(View.VISIBLE);
        }

        if (md.getFoto_tanggapan() == null){
            holderData.foto_tanggapan.setVisibility(View.GONE);
            holderData.card_foto_tanggapan.setVisibility(View.GONE);
        }else{
            try {
                Glide.with(context)
                        .load(new URL(md.getFoto_tanggapan()))
                        .placeholder(R.drawable.no_image)
                        .into(holderData.foto_tanggapan);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        holderData.card_foto_tanggapan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, LihatFoto.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                i.putExtra("image", md.getFoto_tanggapan());
                i.putExtra("title", "FOTO ADUAN");
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class HolderData extends RecyclerView.ViewHolder {
        public TextView nama_tanggapan, tanggal_tanggapan, isi_tanggapan;
        public ImageView foto_user_tanggapan, foto_tanggapan, verified;
        public CardView card_foto_tanggapan;

        public HolderData(View view) {
            super(view);
            nama_tanggapan = (TextView) view.findViewById(R.id.nama_tanggapan);
            tanggal_tanggapan = (TextView) view.findViewById(R.id.tanggal_tanggapan);
            isi_tanggapan = (TextView) view.findViewById(R.id.isi_tanggapan);
            foto_user_tanggapan = (ImageView) view.findViewById(R.id.foto_user_tanggapan);
            foto_tanggapan = (ImageView) view.findViewById(R.id.foto_tanggapan);
            card_foto_tanggapan = view.findViewById(R.id.card_foto_tanggapan);
            verified = (ImageView) view.findViewById(R.id.verified);
        }
    }
}
