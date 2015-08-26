package com.example.jim.myapplication;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.example.jim.myapplication.view.AllClassFragment;
import com.example.jim.myapplication.view.MainActivity;
import com.example.jim.myapplication.view.MyClassFragment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * Created by jim on 2015/8/8.
 */
public class ClassFlowButton {
    public static void initButton(View view, final MainActivity activity){
        final FloatingActionButton actionButton1 = (FloatingActionButton) view.findViewById(R.id.choose_myClass);
        //actionButton1.setTitle("我的班级");
        actionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.initFragmentManager(MyClassFragment.getInstance());
            }
        });
        FloatingActionButton actionButton2 = (FloatingActionButton) view.findViewById(R.id.choose_allClass);
        //actionButton1.setTitle("所有班级");
        actionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.initFragmentManager(AllClassFragment.getInstance());
            }
        });
    }
}
