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
import com.padangsmartcity.activity.DetailAduan;
import com.padangsmartcity.model.ModelAduan;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AdapterAduan extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ModelAduan> mItems;

    public AdapterAduan(List<ModelAduan> mItems, Context context) {
        this.mItems = mItems;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_aduan, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ModelAduan md = mItems.get(position);
        final HolderData holderData = (HolderData) holder;
        holderData.nama_user.setText(md.getNama_user());
        holderData.tanggal_aduan.setText(md.getTanggal_aduan());
        holderData.tanggapan.setText(md.getTanggapan());
        holderData.status_aduan.setText(md.getStatus_aduan());

        try {
            Glide.with(context)
                    .load(new URL(md.getThumb_user()))
                    .placeholder(R.drawable.img_profile)
                    .into(holderData.thumb_user);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (md.getFoto_aduan().equals("null")){
            holderData.foto_aduan.setVisibility(View.GONE);
            if (md.getIsi_aduan().length() >= 300){
                holderData.isi_aduan.setText(md.getIsi_aduan().substring(0, 300)+" ...");
            }else{
                holderData.isi_aduan.setText(md.getIsi_aduan());
            }
        }else{
            holderData.foto_aduan.setVisibility(View.VISIBLE);
            if (md.getIsi_aduan().length() >= 150){
                holderData.isi_aduan.setText(md.getIsi_aduan().substring(0, 150)+" ...");
            }else{
                holderData.isi_aduan.setText(md.getIsi_aduan());
            }
            try {
                Glide.with(context)
                        .load(new URL(md.getFoto_aduan()))
                        .placeholder(R.drawable.no_image)
                        .into(holderData.foto_aduan);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        if (md.getVerified().equals("0")){
            holderData.verified.setVisibility(View.GONE);
        }else{
            holderData.verified.setVisibility(View.VISIBLE);
        }

        holderData.card_aduan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailAduan.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                i.putExtra("id_user", md.getId_user());
                i.putExtra("id_aduan", md.getId_aduan());
                i.putExtra("nama_user", md.getNama_user());
                i.putExtra("tanggal_aduan", md.getTanggal_aduan());
                i.putExtra("isi_aduan", md.getIsi_aduan());
                i.putExtra("tanggapan", md.getTanggapan());
                i.putExtra("status_aduan", md.getStatus_aduan());
                i.putExtra("thumb_user", md.getThumb_user());
                i.putExtra("foto_aduan", md.getFoto_aduan());
                i.putExtra("verified", md.getVerified());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class HolderData extends RecyclerView.ViewHolder {
        public TextView nama_user, tanggal_aduan, isi_aduan, tanggapan, status_aduan;
        public ImageView thumb_user, foto_aduan, verified;
        public CardView card_aduan;

        public HolderData(View view) {
            super(view);
            nama_user = (TextView) view.findViewById(R.id.nama_user);
            tanggal_aduan = (TextView) view.findViewById(R.id.tanggal_aduan);
            isi_aduan = (TextView) view.findViewById(R.id.isi_aduan);
            tanggapan = (TextView) view.findViewById(R.id.tanggapan);
            status_aduan = (TextView) view.findViewById(R.id.status_aduan);
            thumb_user = (ImageView) view.findViewById(R.id.thumb_user);
            foto_aduan = (ImageView) view.findViewById(R.id.foto_aduan);
            card_aduan = (CardView) view.findViewById(R.id.card_aduan);
            verified = (ImageView) view.findViewById(R.id.verified);
        }
    }
}
