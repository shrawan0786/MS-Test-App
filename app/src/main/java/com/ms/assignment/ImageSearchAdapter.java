package com.ms.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shrawangupta on 05/05/16.
 */
public class ImageSearchAdapter  extends RecyclerView.Adapter<ImageSearchAdapter.ViewHolder> {

    private int             mLastPosition;
    private List<DataModel> mDataList;
    private Animation       mAnimation;
    private ImageLoader     mImageLoader;
    private Context         mContext;

    public ImageSearchAdapter(Context context, List<DataModel> dataList) {
        mContext     = context;
        mDataList    = new ArrayList<>();
        mDataList.addAll(dataList);

    }

    @Override
    public ImageSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item, parent, false);

        // set the view's size, margins, paddings and layout parameters
        final ViewHolder vh = new ViewHolder((LinearLayout) v);

        return vh;
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        ((ViewHolder) holder).clearAnimation();
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onBindViewHolder(final ImageSearchAdapter.ViewHolder holder, final int position) {

        holder.mTextView_title.setText(mDataList.get(position).getTitle());

        if (mImageLoader == null) mImageLoader = MSNetworkHandler.getInstance(mContext).getImageLoader();

        try {
            holder.mImage_logo.setImageUrl(mDataList.get(position).getLogo(), mImageLoader);
            holder.mImage_logo.setDefaultImageResId(R.mipmap.default_logo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAnimation = AnimationUtils.loadAnimation(mContext, mLastPosition < position ? R.anim.up_from_bottom : R.anim.down_from_top);
        holder.itemView.startAnimation(mAnimation);
        mLastPosition = position;
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView_title;
        NetworkImageView mImage_logo;

        public ViewHolder(LinearLayout layout) {
            super(layout);
            mTextView_title = (TextView) layout.findViewById(R.id.title);
            mImage_logo = (NetworkImageView) layout.findViewById(R.id.logo);

        }

        public void clearAnimation() {
            itemView.clearAnimation();
        }

    }


}
