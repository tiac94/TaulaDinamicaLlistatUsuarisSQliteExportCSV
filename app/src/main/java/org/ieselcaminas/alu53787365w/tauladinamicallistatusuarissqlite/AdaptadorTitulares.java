package org.ieselcaminas.alu53787365w.tauladinamicallistatusuarissqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class AdaptadorTitulares extends ArrayAdapter<Usuari>{

    public AdaptadorTitulares(Context context, ArrayList<Usuari> datos) {
        super(context, R.layout.listitem_titular, datos);
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View item = convertView;
        final ViewHolder holder;

        if(item == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.listitem_titular, null);

            holder = new ViewHolder();
            holder.titulo = (TextView)item.findViewById(R.id.LblTitulo);
            holder.subtitulo = (TextView)item.findViewById(R.id.LblSubTitulo);

            item.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)item.getTag();
        }

        holder.titulo.setText(getItem(position).getNom());
        holder.subtitulo.setText(""+getItem(position).getDepart());

        return(item);
    }

}