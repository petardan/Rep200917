package com.rpd.customViews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rpd.irepair.R;
import com.rpd.irepair.Repairman;
import com.squareup.picasso.Picasso;

/**
 * Created by neotv on 9/23/17.
 */

public class SmallRepairmanItem extends LinearLayout{

    TextView nameSurnameView;
    RelativeLayout imageRelativeLayout;
    ImageButton smallProfilePicture;
    RelativeLayout ratingRelativeLayout;
    ImageView ratingStarImage;
    TextView ratingView;

    TextView descriptionView;

    RatingLayout ratingLayout;

    //Default constructor
    public SmallRepairmanItem(Context context, int columnCount, Repairman repairman) {
        super(context);

        String nameSurname = repairman.getFirstName() + " " + repairman.getLastName();
        String repairmanUrl = repairman.getImageUrl();
        double rating = repairman.getAverageRating();
        String professions = repairman.getProfessionsString();


        Point size = new Point();
        Activity currentActivity = (Activity)getContext();
        currentActivity.getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        int layoutSreenWidth = (int)(screenWidth / columnCount);

        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
        linearParams.width = layoutSreenWidth;
        linearParams.gravity = Gravity.CENTER;
        setOrientation(VERTICAL);
        setLayoutParams(linearParams);
        setBackgroundResource(R.drawable.custom_view_border);
        setPadding(20,20,20,20);
        setFocusable(true);

        //Setting layout items

        //Setting name and surname text view
        nameSurnameView = new TextView(context);
        nameSurnameView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        nameSurnameView.setText(nameSurname);
        nameSurnameView.setGravity(Gravity.CENTER);
        nameSurnameView.setTypeface(null, Typeface.BOLD);
        nameSurnameView.setMinLines(2);
        nameSurnameView.setMaxLines(2);
        nameSurnameView.setEllipsize(TextUtils.TruncateAt.END);
        nameSurnameView.setFocusable(false);
        nameSurnameView.setClickable(false);


        //Image Relative Layout start
        imageRelativeLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams imageRelativeLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        imageRelativeLayout.setLayoutParams(imageRelativeLayoutParams);
        imageRelativeLayout.setPadding(20,20,20,20);
        imageRelativeLayout.setFocusable(false);

        //Setting profile image button
        smallProfilePicture = new ImageButton(context);
        smallProfilePicture.setAdjustViewBounds(true);
        smallProfilePicture.setBackgroundColor(Color.TRANSPARENT);
        smallProfilePicture.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Picasso.with(context).load(R.drawable.blankuser1).into(smallProfilePicture);
        smallProfilePicture.setFocusable(false);
        smallProfilePicture.setClickable(false);

        ratingLayout = new RatingLayout(context, rating);

        imageRelativeLayout.addView(smallProfilePicture);
        imageRelativeLayout.addView(ratingLayout);

        //Image Relative Layout end

        //Setting desciption view
        descriptionView = new TextView(context);
        descriptionView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        descriptionView.setText(professions);
        descriptionView.setGravity(Gravity.CENTER);
        descriptionView.setFocusable(false);
        descriptionView.setClickable(false);
        descriptionView.setMinLines(2);
        descriptionView.setMaxLines(2);
        descriptionView.setEllipsize(TextUtils.TruncateAt.END);

        addView(nameSurnameView);
        addView(imageRelativeLayout);
        addView(descriptionView);

    }
}
