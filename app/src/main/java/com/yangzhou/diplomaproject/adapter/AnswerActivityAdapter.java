package com.yangzhou.diplomaproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aliquis.yangzhou.diplomaproject.R;
import com.yangzhou.diplomaproject.module.answer.AnswerBodyValue;
import com.zy.sdk.imageloader.ImageLoaderManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zy on 2017/4/10.
 */

public class AnswerActivityAdapter extends BaseAdapter {


    private Context mContext;
    private ViewHolder mViewHolder;
    private LayoutInflater mInflate;

    private ArrayList<AnswerBodyValue> mData;

    private ImageLoaderManager mImageLoader;

    public AnswerActivityAdapter(Context context, ArrayList<AnswerBodyValue> data) {
        mContext = context;
        mData = data;
        mInflate = LayoutInflater.from(mContext);
        mImageLoader = ImageLoaderManager.getInstance(mContext);
    }

    public void onDataChange(ArrayList<AnswerBodyValue> data) {
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

        final AnswerBodyValue value = (AnswerBodyValue) getItem(position);

        if (convertView == null) {

            mViewHolder = new ViewHolder();

            convertView = mInflate.inflate(R.layout.item_answers_list, parent, false);

            mViewHolder.mAuthor = (TextView) convertView.findViewById(R.id.answer_author_view);
            mViewHolder.mPhoto = (CircleImageView) convertView.findViewById(R.id.answer_author_image);
            mViewHolder.mAnswerContent = (TextView) convertView.findViewById(R.id.answer_content_info);
            mViewHolder.mLike = (TextView) convertView.findViewById(R.id.answer_like);
            mViewHolder.mLikeView = (TextView) convertView.findViewById(R.id.answer_like_view);
            mViewHolder.mComment = (TextView) convertView.findViewById(R.id.answer_comment);
            mViewHolder.mCommentView = (TextView) convertView.findViewById(R.id.answer_comment_view);
            mViewHolder.mDate = (TextView) convertView.findViewById(R.id.answer_date);


            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }


        mViewHolder.mAuthor.setText(value.author);
        mViewHolder.mAnswerContent.setText(value.content);
        if (value.like == 0) {
            mViewHolder.mLike.setVisibility(View.GONE);
            mViewHolder.mLikeView.setVisibility(View.GONE);
        } else {
            mViewHolder.mLike.setText(value.like + " ");
        }
        if (value.comments == 0) {
            mViewHolder.mComment.setVisibility(View.GONE);
            mViewHolder.mCommentView.setVisibility(View.GONE);
        } else {
            mViewHolder.mComment.setText(value.comments + " ");
        }
        mViewHolder.mDate.setText(value.date);

        mImageLoader.displayImage(mViewHolder.mPhoto, value.photo);


        return convertView;
    }

    private static class ViewHolder {
        private TextView mAuthor;
        private CircleImageView mPhoto;
        private TextView mAnswerContent;
        private TextView mLike;
        private TextView mComment;
        private TextView mDate;
        private TextView mCommentView;
        private TextView mLikeView;
    }
}
