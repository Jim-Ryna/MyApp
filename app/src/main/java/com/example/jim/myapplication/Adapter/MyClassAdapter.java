package com.example.jim.myapplication.Adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.example.jim.myapplication.view.MyClassViewHolder;
import com.example.jim.myapplication.model.MyClassMate;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by jim on 2015/8/7.
 */
public class MyClassAdapter extends RecyclerArrayAdapter<MyClassMate>{
    public MyClassAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyClassViewHolder(viewGroup);
    }
}
