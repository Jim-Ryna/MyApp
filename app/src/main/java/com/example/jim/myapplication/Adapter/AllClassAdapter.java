package com.example.jim.myapplication.Adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.example.jim.myapplication.model.AllClassMate;
import com.example.jim.myapplication.model.MyClassMate;
import com.example.jim.myapplication.view.AllClassViewHolder;
import com.example.jim.myapplication.view.MyClassViewHolder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by jim on 2015/8/8.
 */
public class AllClassAdapter extends RecyclerArrayAdapter<AllClassMate> {
    public AllClassAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AllClassViewHolder(viewGroup);
    }
}