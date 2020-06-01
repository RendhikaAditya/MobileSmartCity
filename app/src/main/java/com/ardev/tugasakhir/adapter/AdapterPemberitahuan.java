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
import com.ardev.tugasakhir.model.ModelPemberitahuan;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AdapterPemberitahuan extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private Context context;
    private List<ModelPemberitahuan> mItems;

    public AdapterPemberitahuan(List<ModelPemberitahuan> mItems, Context context) {
        this.mItems = mItems;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pemberitahuan, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ModelPemberitahuan md = mItems.get(position);
        final HolderData holderData = (HolderData) holder;

        holderData.isi_pemberitahuan.setText(md.getIsi_pemberitahuan());
        holderData.tanggal.setText(md.getTanggal());

        holderData.card_pemberitahuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class HolderData extends RecyclerView.ViewHolder {
        public TextView isi_pemberitahuan, tanggal;
        public CardView card_pemberitahuan;

        public HolderData(View view) {
            super(view);
            isi_pemberitahuan = (TextView) view.findViewById(R.id.isi_pemberitahuan);
            tanggal = (TextView) view.findViewById(R.id.tanggal);
            card_pemberitahuan = view.findViewById(R.id.card_pemberitahuan);
        }
    }
}
