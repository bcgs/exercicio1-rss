package br.ufpe.cin.if1001.rss.ui.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.ufpe.cin.if1001.rss.R;
import br.ufpe.cin.if1001.rss.db.SQLiteRSSHelper;
import br.ufpe.cin.if1001.rss.domain.ItemRSS;
import br.ufpe.cin.if1001.rss.ui.MainActivity;

public class CardChangeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView title, date;
    private SQLiteRSSHelper db;

    public CardChangeHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.item_titulo);
        date = itemView.findViewById(R.id.item_data);
        db = SQLiteRSSHelper.getInstance(itemView.getContext());

        title.setOnClickListener(this);
    }

    void bindModel(ItemRSS item) {
        title.setText(item.getTitle());
        date.setText(item.getPubDate());
    }

    @Override
    public void onClick(View v) {
        int pos = getAdapterPosition();
        ItemRSS item = MainActivity.itemList.get(pos);
        String link = item.getLink();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        v.getContext().startActivity(intent);
        db.markAsRead(link);
    }
}