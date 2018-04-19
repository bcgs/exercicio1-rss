package br.ufpe.cin.if1001.rss.ui.adapter;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.ufpe.cin.if1001.rss.R;
import br.ufpe.cin.if1001.rss.domain.ItemRSS;


public class ItemRSSAdapter extends RecyclerView.Adapter<CardChangeHolder> {
    private Context context;
    private SortedList<ItemRSS> list;

    public ItemRSSAdapter(Context context, SortedList<ItemRSS> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CardChangeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.itemlista, parent, false);
        return new CardChangeHolder(view);
    }

    @Override
    public void onBindViewHolder(CardChangeHolder holder, int position) {
        holder.bindModel(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Atualizar feed
     * @param data Itens RSS a serem atualizados
     */
    public void swap(ArrayList<ItemRSS> data) {
        if (data != null && data.size() > 0) {
            list.clear();
            list.addAll(data);
            notifyDataSetChanged();
        }
    }
}