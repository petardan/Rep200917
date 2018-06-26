package com.rpd.customViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rpd.irepair.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Petar on 9/28/2017.
 */

public class RatingView extends RelativeLayout{

    ImageView ratingStarImage;
    TextView ratingView;

    public RatingView(Context context, String rating) {
        super(context);

        RelativeLayout.LayoutParams ratingRelativeLayoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ratingRelativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ratingRelativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        setLayoutParams(ratingRelativeLayoutParams);
        setPadding(0,0,0,20);
        setFocusable(false);

        //Rating star image button
        ratingStarImage = new ImageView(context);
        ratingStarImage.setAdjustViewBounds(true);
        RelativeLayout.LayoutParams ratingStarImageParams = new RelativeLayout.LayoutParams(150, 150);
        ratingStarImageParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ratingStarImageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ratingStarImage.setLayoutParams(ratingStarImageParams);
        Picasso.with(context).load(R.drawable.rating_star).into(ratingStarImage);
        ratingStarImage.setFocusable(false);
        ratingStarImage.setClickable(false);

        //Setting rating text view
        ratingView = new TextView(context);
        RelativeLayout.LayoutParams ratingViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ratingViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ratingViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ratingView.setLayoutParams(ratingViewParams);
        ratingView.setText(rating);
        ratingView.setTextSize(16);
        ratingView.setTypeface(null, Typeface.BOLD);
        ratingView.setPadding(0,0,40,40);
        ratingView.setTextColor(Color.WHITE);
        ratingView.setFocusable(false);
        ratingView.setClickable(false);


        addView(ratingStarImage);
        addView(ratingView);


    }
}
