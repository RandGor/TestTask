package ru.randgor.testtask;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private ArrayList<SimpleRow> rows;

    private OnItemClickListener onItemClickListener;

    private Context appContext;

    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_NORMAL = 1;
    public static final int VIEW_TYPE_RETRY = 2;
    public boolean isLoaderVisible = false;
    public boolean isRetryVisible = false;

    private int loaderIndex;


    public MyAdapter(ArrayList<SimpleRow> rows, OnItemClickListener onItemClickListener, Context context) {
        this.rows = rows;
        this.onItemClickListener = onItemClickListener;
        this.appContext = context;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new SimpleViewHolder((LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_loading, parent, false));
            case VIEW_TYPE_RETRY:
                return new RetryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_retry, parent, false));
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, int position) {
        viewHolder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        return rows.get(position).getType();
    }

    public void clear() {
        rows.clear();
        notifyDataSetChanged();
    }

    public SimpleRow getItem(int position) {
        return rows.get(position);
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    public void addLoading() {
        if (isLoaderVisible)
            return;
        isLoaderVisible = true;
        rows.add(new SimpleRow());
        loaderIndex = rows.size() - 1;
        notifyItemInserted(loaderIndex);
    }

    public void changeLoadingToRetry() {
        if (!isLoaderVisible || isRetryVisible)
            return;

        isLoaderVisible = false;
        isRetryVisible = true;

        rows.get(loaderIndex).setType(VIEW_TYPE_RETRY);
        notifyItemChanged(loaderIndex);
    }

    public void changeRetryToLoading() {
        if (isLoaderVisible || !isRetryVisible)
            return;

        isLoaderVisible = true;
        isRetryVisible = false;

        rows.get(loaderIndex).setType(VIEW_TYPE_LOADING);
        notifyItemChanged(loaderIndex);
    }

    public void removeLoading() {
        if (!isLoaderVisible)
            return;
        isLoaderVisible = false;
        SimpleRow item = getItem(loaderIndex);
        if (item != null) {
            rows.remove(loaderIndex);
            notifyItemRemoved(loaderIndex);
        }
    }

    class SimpleViewHolder extends BaseViewHolder implements RecyclerView.OnClickListener{
        @BindView(R.id.recycler_photo)
        ImageView imageViewPhoto;
        @BindView(R.id.recycler_header)
        TextView textViewHeader;
        @BindView(R.id.recycler_description)
        TextView textViewDescription;
        @BindView(R.id.recycler_date)
        TextView textViewDate;

        MyAdapter.OnItemClickListener monItemClickListener;

        SimpleViewHolder(LinearLayout itemView) {
            super(itemView);

            monItemClickListener = onItemClickListener;

            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            monItemClickListener.onItemClicked(getAdapterPosition());
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

            SimpleRow row = rows.get(position);
            String date;
            try {
                date = row.getPublishedAt().split("T")[0];
            }catch (Exception e){
                date = null;
            }
            textViewHeader.setText(row.getTitle());
            textViewDescription.setText(row.getDescription());
            textViewDate.setText(date);

            if (row.getUrlToImage().length()<3){
                imageViewPhoto.setVisibility(View.GONE);
                return;
            }
            Picasso.with(appContext).load(row.getUrlToImage()).fit().centerInside().into(imageViewPhoto);
        }
    }

    class ProgressHolder extends BaseViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class RetryHolder extends BaseViewHolder implements RecyclerView.OnClickListener {
        @BindView(R.id.reloadButton)
        Button buttonReload;

        MyAdapter.OnItemClickListener monItemClickListener;

        RetryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            monItemClickListener = onItemClickListener;

            itemView.setOnClickListener(this);
            buttonReload.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            monItemClickListener.onItemClicked(getAdapterPosition());
        }
    }
}