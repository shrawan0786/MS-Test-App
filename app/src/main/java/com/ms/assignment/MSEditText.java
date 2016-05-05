package com.ms.assignment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Created by shrawangupta on 05/05/16.
 */
public class MSEditText extends RelativeLayout {

    private LayoutInflater  mInflater;
    private EditText        mEditText;
    private ProgressBar     mProgressBar;
    private boolean         mIsRefreshing;



    public MSEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public MSEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(attrs);
    }

    private void initViews(AttributeSet attrs) {
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.custom_edit_text, this, true);
        mEditText = (EditText) findViewById(R.id.custom_edit_text_view);
        mProgressBar = (ProgressBar) findViewById(R.id.custom_edit_text_progress_bar);

        mProgressBar.getLayoutParams().height = mEditText.getLayoutParams().height - 30;
        mProgressBar.getLayoutParams().width = mEditText.getLayoutParams().height - 30;
        mProgressBar.requestLayout();

    }

    public void setRefreshing(boolean isVisible) {
        if (isVisible) {
            if (!mIsRefreshing) {
                mIsRefreshing = true;
                mProgressBar.setVisibility(VISIBLE);
            }
        } else {
            if (mIsRefreshing) {
                mIsRefreshing = false;
                mProgressBar.setVisibility(GONE);
            }
        }
    }

    public EditText getEditText() {
        return mEditText;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }
}