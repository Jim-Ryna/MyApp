package com.example.jim.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.jim.myapplication.Config.App;
import com.example.jim.myapplication.utils.LogUtil;
import com.prolificinteractive.parallaxpager.ParallaxContainer;
import com.prolificinteractive.parallaxpager.ParallaxContextWrapper;

import cn.bmob.v3.Bmob;

/**
 * Created by jim on 2015/6/18.
 */
public class GuideActivity extends AppCompatActivity{
    private ImageView imageView;
    private ImageView[] imageViews;
    @Override
    protected void attachBaseContext(Context newBase) {
        //ParallaxPager and Calligraphy don't seem to play nicely together
        //The solution was to add a listener for view creation events so that we can hook up
        // Calligraphy to our view creation calls instead.
        super.attachBaseContext(
                new ParallaxContextWrapper(new ParallaxContextWrapper(newBase))
        );
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        Bmob.initialize(this, App.APPLICATION_ID);
        ParallaxContainer parallaxContainer = (ParallaxContainer) findViewById(R.id.parallax_container_1);
        parallaxContainer.setLooping(false);
        parallaxContainer.setupChildren(getLayoutInflater(),
                R.layout.test2,
                R.layout.test3,
                R.layout.test4);
        Button button = (Button) findViewById(R.id.change);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("guide", "exist");
                Intent mIntent = new Intent(GuideActivity.this, LoginActivity.class);
                startActivity(mIntent);
            }
        });
        LogUtil.d("guide", "ccc" + parallaxContainer.getScrollBarSize());
        imageViews = new ImageView[3];
        for (int i = 0; i < 3; i++) {
            imageView = new ImageView(GuideActivity.this);
            //设置小圆点imageview的参数
            imageView.setLayoutParams(new LayoutParams(20, 20));//创建一个宽高均为20 的布局
            imageView.setPadding(20, 0, 20, 0);
            //将小圆点layout添加到数组中
            imageViews[i] = imageView;

            //默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
            if (i == 0) {
                imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.page_indicator);
            }
            parallaxContainer.setOnPageChangeListener(new GuidePageChangeListener());
        }
    }
        private static final String SHAREDPREFERENCES_NAME = "my_pref";
        private static final String KEY_GUIDE_ACTIVITY = "guide_activity";

    private void setGuided() {
        SharedPreferences settings = getSharedPreferences(SHAREDPREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(KEY_GUIDE_ACTIVITY, "false");
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[position].setBackgroundResource(R.drawable.page_indicator_focused);
                //不是当前选中的page，其小圆点设置为未选中的状态
                if (position != i) {
                    imageViews[i].setBackgroundResource(R.drawable.page_indicator);
                }
            }

        }
    }
}
