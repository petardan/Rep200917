package com.rpd.irepair;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;


public class AddNewJobActivity extends AppCompatActivity {


    ImageView addJobJobTitleHelp;
    TextView addJobJobTitle;
    LinearLayout addJobJobTitleHintLayout;
    EditText addJobJobTitleEdit;

    private TabLayout tabLayout;
    private LinearLayout container;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_job);

        context = this;

        //Example of how to add hint baloons
        addJobJobTitleHelp = (ImageView)findViewById(R.id.addJobJobTitleHelp);
        addJobJobTitle = (TextView)findViewById(R.id.addJobJobTitle);
        addJobJobTitleHintLayout = (LinearLayout)findViewById(R.id.addJobJobTitleHintLayout);
        addJobJobTitleEdit = (EditText)findViewById(R.id.addJobJobTitleEdit);

        addJobJobTitleHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleTooltip.Builder(context)
                        .anchorView(addJobJobTitleEdit)
                        .text(R.string.addJobJobTitleHint)
                        .gravity(Gravity.END)
                        .animated(true)
                        .transparentOverlay(false)
                        .build()
                        .show();
            }
        });


    }


}