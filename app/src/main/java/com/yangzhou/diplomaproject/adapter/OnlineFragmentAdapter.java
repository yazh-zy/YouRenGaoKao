package com.yangzhou.diplomaproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.module.online.OnlineBodyValue;
import com.zy.sdk.imageloader.ImageLoaderManager;

import java.util.ArrayList;

/**
 * @autor Yang
 * @function adapter
 * @date 2017/3/29.
 */

public class OnlineFragmentAdapter extends BaseAdapter {


    private Context mContext;
    private ViewHolder mViewHolder;
    private LayoutInflater mInflate;

    private ArrayList<OnlineBodyValue> mData;

    private ImageLoaderManager mImageLoader;

    public OnlineFragmentAdapter(Context context, ArrayList<OnlineBodyValue> data) {
        mContext = context;
        mData = data;
        mInflate = LayoutInflater.from(mContext);
        mImageLoader = ImageLoaderManager.getInstance(mContext);
    }

    public void onDataChange(ArrayList<OnlineBodyValue> data) {
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
        //获取type类型
        int type = getItemViewType(position);

        final OnlineBodyValue value = (OnlineBodyValue) getItem(position);

        if (convertView == null) {

            mViewHolder = new ViewHolder();
            convertView = mInflate.inflate(R.layout.item_online_layout, parent, false);
            mViewHolder.mSubjectView = (TextView) convertView.findViewById(R.id.online_subject_view);
            mViewHolder.mAuthorView = (TextView) convertView.findViewById(R.id.online_author_view);
            mViewHolder.mNumberView = (TextView) convertView.findViewById(R.id.online_number_view);
            mViewHolder.mContentView = (TextView) convertView.findViewById(R.id.online_content_view);
            mViewHolder.mLengthView = (TextView) convertView.findViewById(R.id.online_length_view);
            mViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.online_listview_imageview);


            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }


        mViewHolder.mSubjectView.setText(value.subject);
        mViewHolder.mAuthorView.setText(value.author);
        mViewHolder.mNumberView.setText(value.number);
        mViewHolder.mContentView.setText(value.content);
        mViewHolder.mLengthView.setText(value.length);

        mImageLoader.displayImage(mViewHolder.mImageView, value.image);


        return convertView;
    }

    private static class ViewHolder {
        private TextView mSubjectView;
        private TextView mAuthorView;
        private TextView mNumberView;
        private TextView mContentView;
        private TextView mLengthView;
        private ImageView mImageView;

    }
}
