package com.yangzhou.diplomaproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.module.comment.CommentBodyValue;
import com.zy.sdk.imageloader.ImageLoaderManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zy on 2017/4/14.
 */

public class CommentActivityAdapter extends BaseAdapter {

    private Context mContext;
    private ViewHolder mViewHolder;
    private LayoutInflater mInflate;

    private ArrayList<CommentBodyValue> mData;

    private ImageLoaderManager mImageLoader;

    public CommentActivityAdapter (Context context, ArrayList<CommentBodyValue> data) {
        mContext = context;
        mData = data;
        mInflate = LayoutInflater.from(mContext);
        mImageLoader = ImageLoaderManager.getInstance(mContext);
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

        final CommentBodyValue value = (CommentBodyValue) getItem(position);

        if (convertView == null) {
            convertView = mInflate.inflate(R.layout.item_comment_layout, parent, false);

            mViewHolder = new ViewHolder();

            mViewHolder.mName = (TextView) convertView.findViewById(R.id.comment_user_name_view);
            mViewHolder.mContent = (TextView) convertView.findViewById(R.id.comment_content_view);
            mViewHolder.mDate = (TextView) convertView.findViewById(R.id.comment_date_view);
            mViewHolder.mPhoto = (CircleImageView) convertView.findViewById(R.id.comment_uesr_photo_view);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mName.setText(value.name);
        mViewHolder.mContent.setText(value.content);
        mViewHolder.mDate.setText(value.date);
        mImageLoader.displayImage(mViewHolder.mPhoto, value.photo);

        return convertView;
    }

    private class ViewHolder {
        private TextView mName;
        private TextView mContent;
        private TextView mDate;
        private CircleImageView mPhoto;
    }
}
