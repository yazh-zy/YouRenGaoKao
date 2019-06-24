package com.yangzhou.diplomaproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.module.question.QuestionBodyValue;
import com.zy.sdk.imageloader.ImageLoaderManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Yang
 * @fnction
 * @date 2017/3/18.
 */

public class QuestionFragmentAdapter extends BaseAdapter {


    private Context mContext;
    private ViewHolder mViewHolder;
    private LayoutInflater mInflate;

    private ArrayList<QuestionBodyValue> mData;

    private ImageLoaderManager mImageLoader;

    public QuestionFragmentAdapter(Context context, ArrayList<QuestionBodyValue> data) {
        mContext = context;
        mData = data;
        mInflate = LayoutInflater.from(mContext);
        mImageLoader = ImageLoaderManager.getInstance(mContext);
    }

    public void onDataChange(ArrayList<QuestionBodyValue> data) {
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

        final QuestionBodyValue value = (QuestionBodyValue) getItem(position);

        //为空表示没有可用的缓存view
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflate.inflate(R.layout.item_question_listview_layout, parent, false);
            mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_title_view);
            mViewHolder.mContentView = (TextView) convertView.findViewById(R.id.item_content_view);
            mViewHolder.mAuthorView = (TextView) convertView.findViewById(R.id.item_author_view);
            mViewHolder.mDateView = (TextView) convertView.findViewById(R.id.item_date_view);
            mViewHolder.mPhoto = (CircleImageView) convertView.findViewById(R.id.item_question_photo_view);

            convertView.setTag(mViewHolder);
        }
        //有可用的convertView
        else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mTitleView.setText(value.title);
        mViewHolder.mContentView.setText(value.content);
        mViewHolder.mAuthorView.setText(value.author);
        mViewHolder.mDateView.setText(value.date);

        mImageLoader.displayImage(mViewHolder.mPhoto, value.photo);

        return convertView;
    }

    /**
     * 缓存已经创建好的Item
     */
    private static class ViewHolder {
        private TextView mTitleView;
        private TextView mContentView;
        private TextView mAuthorView;
        private TextView mDateView;
        private CircleImageView mPhoto;
    }
}
