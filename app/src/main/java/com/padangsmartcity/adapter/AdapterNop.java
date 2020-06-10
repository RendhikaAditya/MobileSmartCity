package com.padangsmartcity.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.padangsmartcity.R;
import com.padangsmartcity.activity.DetailTagihan;
import com.padangsmartcity.model.ModelNop;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterNop extends RecyclerView.Adapter<AdapterNop.ListViewHolder> {
    private List<ModelNop> nop;
    private Context context;

    public AdapterNop(List<ModelNop> nop) {
        this.nop = nop;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_nop, parent, false);
        ListViewHolder holder = new ListViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Locale locale = new Locale("in", "ID");
        final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(locale);
        final ModelNop model = nop.get(position);

        holder.nama.setText(model.getNama_wb());
        holder.tanggal.setText(model.getTanggal());
        holder.harga.setText(formatRupiah.format((double)model.getJumlah()));
        holder.status.setText(model.getStatus());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailTagihan.class);
                intent.putExtra("IDDD", model.getId_tagihan());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return nop.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView nama, tanggal, harga, status;
        private Context context;
        private CardView cardView;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            cardView = itemView.findViewById(R.id.card_nop);

            nama = itemView.findViewById(R.id.namanop);
            tanggal = itemView.findViewById(R.id.tglnop);
            harga = itemView.findViewById(R.id.harganop);
            status = itemView.findViewById(R.id.statusnop);
        }
    }
}
