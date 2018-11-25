package com.example.mike.week5weekend;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    List<Contact> contacts;

    public RecyclerViewAdapter( List<Contact> contacts ) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.recycler_view_item, viewGroup, false );
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Contact c = contacts.get(i);
        viewHolder.id.setText(c.getId());
        viewHolder.name.setText(c.getName());
        viewHolder.address.setText(c.getAddress());

        final Context context = viewHolder.daddy.getContext();

        viewHolder.daddy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra( "address", viewHolder.address.getText() );
                context.startActivity( intent );
            }
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView id;
        TextView name;
        TextView address;
        View daddy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            daddy = itemView;
            id = itemView.findViewById( R.id.tvId );
            name = itemView.findViewById( R.id.tvName );
            address = itemView.findViewById( R.id.tvAddress );

        }
    }

}
