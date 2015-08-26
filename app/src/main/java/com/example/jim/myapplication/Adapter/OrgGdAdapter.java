package com.example.jim.myapplication.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.jim.myapplication.R;
import com.example.jim.myapplication.model.OrgPic;
import com.example.jim.myapplication.utils.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jim on 2015/8/23.
 */
public class OrgGdAdapter extends BaseAdapter {

    private List<OrgPic> orgPics;
    private Activity activity;

    private Map<Integer, ImageView> extraView = new HashMap<>();

    public OrgGdAdapter(Activity activity, List<OrgPic> orgPics) {
        this.orgPics = orgPics;
        this.activity = activity;
    }

    public void setOrgPics(List<OrgPic> orgPics) {
        this.orgPics = orgPics;
    }

    public Map<Integer, ImageView> getExtraView(){
        return extraView;
    }

    @Override
    public int getCount() {
        return 9;
    }

    @Override
    public Object getItem(int position) {
        return orgPics == null ? null : orgPics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView != null) {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        } else {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(activity).inflate(R.layout.org_choose_pic, parent, false);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image);
            view.setTag(viewHolder);
        }
        if (position == (orgPics == null ? 0 : orgPics.size())) {
            viewHolder.imageView.setImageResource(R.drawable.ic_add);
        } else if (position < (orgPics == null ? 0 : orgPics.size())){
            orgPics.get(position).getOrgPic().loadImage(activity, viewHolder.imageView);
        }
        else if (position > (orgPics == null ? 0 : orgPics.size())){
            viewHolder.imageView.setImageResource(R.drawable.ic_add);
            viewHolder.imageView.setVisibility(View.INVISIBLE);
            extraView.put(position, viewHolder.imageView);
        }
        return view;
    }

    class ViewHolder{
        ImageView imageView;
    }
}
