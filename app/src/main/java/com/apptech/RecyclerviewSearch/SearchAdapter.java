package com.apptech.RecyclerviewSearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.apptech.CircularProgressBar.HoloCircularProgressBar;
import com.apptech.apptechcomponents.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 16/11/17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<RecyclerViewSearchModel> contactList;
    private List<RecyclerViewSearchModel> contactListFiltered;
    private ContactsAdapterListener listener;
    private DBHelper dbHelper;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, phone;
        public ImageView thumbnail, downloadIcon;
        public HoloCircularProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            downloadIcon = itemView.findViewById(R.id.download);
            progressBar = itemView.findViewById(R.id.progress_circuls);
            itemView.setOnClickListener(this);
            downloadIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onContactSelected(contactListFiltered.get(getAdapterPosition()), getAdapterPosition(), v);
        }
    }


    public SearchAdapter(Context context, List<RecyclerViewSearchModel> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
        dbHelper = new DBHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.downloadIcon.setTag("" + position);

        final RecyclerViewSearchModel contact = contactListFiltered.get(position);
        holder.name.setText(contact.getName());
        holder.phone.setText(contact.getPhone());

        if (dbHelper.getJsonDataObject(position + 1).getStatus().equalsIgnoreCase("1")) {
            holder.downloadIcon.setImageResource(R.drawable.iphone_icon);
        } else {
            holder.downloadIcon.setImageResource(R.drawable.download_icon);
        }

        Log.e("onBindViewHolder: ", ""+getPosition(contactListFiltered.get(position)));

//        List<RecyclerViewSearchModel> data = dbHelper.getAllJsonData(position + 1);
//
//        if (data != null && data.size() > 0) {
//            holder.downloadIcon.setImageResource(R.drawable.iphone_icon);
//        } else {
//            holder.downloadIcon.setImageResource(R.drawable.download_icon);
//        }

        Glide.with(context)
                .load(contact.getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<RecyclerViewSearchModel> filteredList = new ArrayList<>();
                    for (RecyclerViewSearchModel row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<RecyclerViewSearchModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public RecyclerViewSearchModel getItem(int pos) {
        if (pos < getItemCount()) {
            return contactListFiltered.get(pos);
        }
        return null;
    }

    public interface ContactsAdapterListener {
        void onContactSelected(RecyclerViewSearchModel contact, int pos, View view);
    }


    public int getPosition(RecyclerViewSearchModel item) {

        if (contactListFiltered == null) {
            return -1;
        }

        for (int i = 0; i < contactListFiltered.size(); i++) {
            RecyclerViewSearchModel sItem = contactListFiltered.get(i);
            if (sItem.equals(item)) {
                return i;
            }
        }
        return -1;
    }
}
