package assignment.parsable.yisfibonacci.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import assignment.parsable.yisfibonacci.R;
import assignment.parsable.yisfibonacci.model.Fibonacci;

/**
 * Created by Yee on 6/19/17.
 */

public class LoaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = "LoaderAdapter";
    public static final int TYPE_NORMAL_ITEM = 0;
    public static final int TYPE_BOTTOM_REFRESH_ITEM = 1;

    private ArrayList<Fibonacci> dataSet;
    private Context mContext;

    public LoaderAdapter(ArrayList<Fibonacci> dataSet, Context context) {
        this.dataSet = dataSet;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.loader_item, parent, false);
            return new LoaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.refresh_item, parent, false);
            return new BottomRefreshViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoaderViewHolder) {
            ((LoaderViewHolder) holder).mLoaderMemberText.setText(mContext.getString(R.string.loader_member_text,
                    String.valueOf(position), dataSet.get(position).getCurrent()));
        } else if (holder instanceof BottomRefreshViewHolder) {
            ((BottomRefreshViewHolder) holder).mProgressBar.getIndeterminateDrawable().setColorFilter(ResourcesCompat
                    .getColor(mContext.getResources(), R.color.pureBlack, null), PorterDuff.Mode.MULTIPLY);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size() + 1;
    }

    private class LoaderViewHolder extends RecyclerView.ViewHolder {
        TextView mLoaderMemberText;

        LoaderViewHolder(View itemView) {
            super(itemView);
            mLoaderMemberText = (TextView) itemView.findViewById(R.id.loader_member);
        }
    }

    private class BottomRefreshViewHolder extends RecyclerView.ViewHolder {

        ProgressBar mProgressBar;

        BottomRefreshViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loader_progress_bar);
        }
    }

    public void setDataSet(ArrayList<Fibonacci> fibonaccis) {
        dataSet = fibonaccis;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < dataSet.size()) {
            return TYPE_NORMAL_ITEM;
        } else {
            return TYPE_BOTTOM_REFRESH_ITEM;
        }
    }
}
