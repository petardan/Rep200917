package com.rpd.customViews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rpd.irepair.R;
import com.squareup.picasso.Picasso;

/**
 * Created by neotv on 9/23/17.
 */

public class smallRepairmanItem extends LinearLayout{

    TextView nameSurnameView;
    ImageButton smallProfilePicture;
    TextView description;

    //Default constructor
    public smallRepairmanItem(Context context, int columnCount, String nameSurname, String repairmanUrl, double rating, String description) {
        super(context);

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
        setPadding(10,10,10,10);
        setFocusable(true);

        //Setting layout items

        //Setting name and surname text view
        nameSurnameView = new TextView(context);
        nameSurnameView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        nameSurnameView.setText(nameSurname);
        nameSurnameView.setGravity(Gravity.CENTER);
        nameSurnameView.setFocusable(false);
        nameSurnameView.setClickable(false);

        //Setting profile image button
        smallProfilePicture = new ImageButton(context);
        smallProfilePicture.setAdjustViewBounds(true);
        smallProfilePicture.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Picasso.with(context).load(R.drawable.blankuser1).into(smallProfilePicture);
        smallProfilePicture.setFocusable(false);
        smallProfilePicture.setClickable(false);

        addView(nameSurnameView);
        addView(smallProfilePicture);

    }
}
