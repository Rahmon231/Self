package com.lemzeeyyy.self;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lemzeeyyy.self.model.Journal;
import com.squareup.picasso.Picasso;

import java.util.List;

public class JornalListRecyclerAdapter extends RecyclerView.Adapter<JornalListRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Journal> journalList;

    public JornalListRecyclerAdapter() {
    }

    public JornalListRecyclerAdapter(Context context, List<Journal> journalList) {
        this.context = context;
        this.journalList = journalList;
    }

    @NonNull
    @Override
    public JornalListRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.journal_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull JornalListRecyclerAdapter.ViewHolder holder, int position) {
        Journal journal = journalList.get(position);
        String imageUrl;
        holder.title.setText(journal.getTitle());
        holder.thought.setText(journal.getDescription());
        imageUrl = journal.getImageUrl();
        //use picasso to download the image from the image link and show image
        Picasso.get()
                .load(imageUrl)
                .fit()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.image);
        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(journal
                .getTimeAdded()
                .getSeconds()*1000);
        holder.dateCreated.setText(timeAgo);



    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title,thought,dateCreated, name;
        public ImageView image;
        public String userId, userName;
        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            title = itemView.findViewById(R.id.journal_title_list);
            thought = itemView.findViewById(R.id.thought_list);
            image = itemView.findViewById(R.id.journal_image_list);
            dateCreated = itemView.findViewById(R.id.journal_timestamp_list);
            //name = itemView.findViewById(R.id.list_no_thought);
        }
    }
}
