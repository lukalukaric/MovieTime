package com.example.movietime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.data.Movie;
import com.squareup.picasso.Picasso;

public class AdapterMovies extends RecyclerView.Adapter<AdapterMovies.ViewHolder> {

    ApplicationMy app;
    private OnItemClickListener listener;

    public AdapterMovies(ApplicationMy app, OnItemClickListener listener){
        this.app = app;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_rowlayout, parent, false);
        AdapterMovies.ViewHolder viewHolder = new AdapterMovies.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie tmp = app.getMovieAtPos(position);
        holder.txtName.setText(tmp.getName());
        holder.txtGenre.setText(tmp.getGenre());
        holder.txtDuration.setText(tmp.getDurationString() + " minutes");
        Picasso.get().load(tmp.getImageLink()).placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                // To fit image into imageView
                .fit()
                // To prevent fade animation
                .noFade().into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return app.getSeznamSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public TextView txtGenre;
        public TextView txtDuration;
        public ImageView iv;
        public View background;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.firstLine);
            txtGenre = (TextView) itemView.findViewById(R.id.secondLine);
            txtDuration = (TextView) itemView.findViewById(R.id.thirdLine);
            iv = (ImageView) itemView.findViewById(R.id.rowicon);
            background = itemView.findViewById(R.id.mylayoutrow);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        listener.onItemLongClick(itemView, position);
                    }
                    return false;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        listener.onItemClick(itemView, position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
        void onItemLongClick(View itemView, int position);
    }
}
