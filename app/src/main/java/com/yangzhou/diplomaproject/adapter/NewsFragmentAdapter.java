package com.yangzhou.diplomaproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.module.news.NewsBodyValue;
import com.zy.sdk.imageloader.ImageLoaderManager;

import java.util.ArrayList;

/**
 * Created by zy on 2017/4/13.
 */

public class NewsFragmentAdapter extends BaseAdapter {

    private Context mContext;
    private ViewHolder mViewHolder;
    private LayoutInflater mInflate;

    private ArrayList<NewsBodyValue> mData;

    private ImageLoaderManager mImageLoader;

    public NewsFragmentAdapter(Context context, ArrayList<NewsBodyValue> data) {
        mContext = context;
        mData = data;
        mInflate = LayoutInflater.from(mContext);
        mImageLoader = ImageLoaderManager.getInstance(mContext);
    }

    public void onDataChange(ArrayList<NewsBodyValue> data) {
        mData = data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final NewsBodyValue value = (NewsBodyValue) getItem(position);

        if (convertView == null) {

            mViewHolder = new ViewHolder();
            convertView = mInflate.inflate(R.layout.item_news_layout, parent, false);
            mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.news_title_view);
            mViewHolder.mAuthorView = (TextView) convertView.findViewById(R.id.news_author_view);
            mViewHolder.mDateView = (TextView) convertView.findViewById(R.id.news_date_view);
            mViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.news_listview_imageview);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }


        mViewHolder.mTitleView.setText(value.title);
        mViewHolder.mAuthorView.setText(value.author);
        mViewHolder.mDateView.setText(value.date);

        mImageLoader.displayImage(mViewHolder.mImageView, value.image);

        return convertView;
    }

    private static class ViewHolder {
        private TextView mTitleView;
        private TextView mAuthorView;
        private TextView mDateView;
        private ImageView mImageView;

    }
}
