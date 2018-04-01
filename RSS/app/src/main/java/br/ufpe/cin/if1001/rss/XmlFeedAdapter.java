package br.ufpe.cin.if1001.rss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class XmlFeedAdapter extends BaseAdapter {
    private Context context;
    private List<ItemRSS> items;

    public XmlFeedAdapter(Context context, List<ItemRSS> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemlista, parent, false);

        TextView tv_titulo = view.findViewById(R.id.item_titulo);
        TextView tv_data = view.findViewById(R.id.item_data);

        String titulo = ((ItemRSS) getItem(position)).getTitle();
        String data = ((ItemRSS) getItem(position)).getPubDate();

        tv_titulo.setText(titulo);
        tv_data.setText(data);

        return view;
    }
}
