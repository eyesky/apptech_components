package com.apptech.ComplexRecyclerView.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.apptech.ComplexRecyclerView.Model.Model;
import com.apptech.ComplexRecyclerView.Utils.Util;
import com.apptech.ComplexRecyclerView.Utils.MovieApp;
import com.apptech.apptechcomponents.R;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.List;

/**
 * Created by nirob on 6/14/17.
 */

public class ComplexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TITLE_ITEM = 0;
    private static final int CONTENT_ITEM = 1;
    private Context mContext;
    private List<Object> data;
    private ClickListener itemOnClickListener;

    public ComplexAdapter(Context mContext, List<Object> data, ClickListener clickListener) {
        this.mContext = mContext;
        this.data = data;
        this.itemOnClickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return (data.get(position).getClass() == Model.class) ? TITLE_ITEM : CONTENT_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TITLE_ITEM) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.json_data_item, parent, false);
            return new MyViewHolder(itemView);
        } else {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_download_row_title, parent, false);
            itemView.setOnClickListener(null);
            itemView.setClickable(false);
            return new SectionViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder2, int position) {

        if (data.get(position).getClass() == Model.class) {

            final Model model = (Model) data.get(position);

            MyViewHolder holder = (MyViewHolder) holder2;

/*            if (!TextUtils.isEmpty(model.getImage())) {

                Glide.with(mContext).load(model.getImage())
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imgProfile);


            } else {
                // load somethings before load data in img view like default background
            }*/

            MovieApp.getInstance().setMovieImage(model.getId(), holder.imgProfile);

            holder.imgProfile.setColorFilter(null);
            holder.txtTitle.setText(model.getTitle());

            String imageName = FilenameUtils.getName(model.getImage());
            String sdCardLink = Util.generateSDCardLink(Util.PATH, imageName);

            if (!new File(sdCardLink).exists()) {
                holder.deleteOrDownload.setBackgroundResource(R.drawable.download_selector);
            } else {
                holder.deleteOrDownload.setBackgroundResource(R.drawable.delete);
            }

        } else {

            SectionViewHolder sectionHolder = (SectionViewHolder) holder2;

            if ("Downloaded Car".equalsIgnoreCase(data.get(position).toString())) {
                sectionHolder.tv.setBackgroundResource(R.drawable.downloaded_car);
            } else if ("Available for Download".equalsIgnoreCase(data.get(position).toString())) {
                sectionHolder.tv.setBackgroundResource(R.drawable.available_for_downlaod);
            } else {
                sectionHolder.tv.setBackgroundResource(R.drawable.previous_car);
            }

        }

    }

    public Model removeItem(int position, String path) {
        Model model = (Model) data.remove(position);
        notifyItemRemoved(position);
        File f = new File(path);
        if (f != null && f.exists()) {
            f.delete();
        }
        return model;
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public interface ClickListener {
        public void clickListener(View view, int pos, Object o);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imgProfile;
        public TextView txtTitle;
        public ImageButton deleteOrDownload;

        public MyViewHolder(View view) {
            super(view);
            imgProfile = (ImageView) view.findViewById(R.id.icon_profile);
            txtTitle = (TextView) view.findViewById(R.id.txt_title);
            deleteOrDownload = (ImageButton) view.findViewById(R.id.img_btn_delete_or_download);
            deleteOrDownload.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemOnClickListener.clickListener(v, getAdapterPosition(), data.get(getAdapterPosition()));
        }
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public SectionViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.txt_titles);
        }
    }
}
