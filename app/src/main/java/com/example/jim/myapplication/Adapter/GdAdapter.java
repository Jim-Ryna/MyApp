package com.example.jim.myapplication.Adapter;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.example.jim.myapplication.R;
import com.example.jim.myapplication.model.MorePic;
import com.example.jim.myapplication.utils.LogUtil;
import com.example.jim.myapplication.view.CustomView6;
import com.jude.library.imageprovider.ImageProvider;
import com.jude.library.imageprovider.OnImageSelectListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by jim on 2015/8/10.
 */
public class GdAdapter extends BaseAdapter {
    private Map<Integer, Uri> picUris = new HashMap<>();
    private ImageProvider provider;
    private Activity activity;
    private CustomView6 customView6 = null;
    private int curIndex = -1;
    private int progress = -1;

    public Map<Integer, Uri> getAllPicUri() {
        return picUris;
    }

    public void setCustomView(int curIndex, int progress) {
        this.curIndex = curIndex;
        this.progress = progress;
    }

    private List<MorePic> prePics = null;

    public void setPicsPre(List<MorePic> prePics){
        this.prePics = prePics;
    }

    public GdAdapter(Activity activity, ImageProvider provider) {
        this.activity = activity;
        this.provider = provider;
    }

    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        final ViewHolder holder;
        if (convertView != null) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {
            holder = new ViewHolder();
            view = LayoutInflater.from(activity).inflate(R.layout.activity_slidingmenu_albums_item_item, parent, false);
            LogUtil.i("view", view.toString());
            holder.button = (ImageButton) view.findViewById(R.id.image);
            holder.customView6 = (CustomView6) view.findViewById(R.id.customView);
            view.setTag(holder);
        }
        if (prePics != null && position <= (prePics.size()-1)) {
            prePics.get(position).getPicFile().loadImage(activity, holder.button);
        }
        if (curIndex == position) {
            holder.customView6.setVisibility(View.VISIBLE);
            if (progress < 100 && progress > 0)
                holder.customView6.setProgress(progress);
        } else
            holder.customView6.setVisibility(View.INVISIBLE);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provider.getImageFromAlbum(new OnImageSelectListener() {
                    @Override
                    public void onImageSelect() {
                        Log.i("crop", "selected");
                    }

                    @Override
                    public void onImageLoaded(Uri uri) {
                        Log.i("crop", "do");
                        corpImage(uri, position, holder);
                    }

                    @Override
                    public void onError() {
                        Log.i("crop", "error");

                    }
                });
            }

        });
        return view;
    }

    void corpImage(Uri uri, final int position, final ViewHolder holder) {
        provider.corpImage(uri, 300, 300, new OnImageSelectListener() {
            @Override
            public void onImageSelect() {

            }

            @Override
            public void onImageLoaded(Uri uri) {
                LogUtil.i("crop", "load");
                holder.button.setImageURI(uri);
                if (prePics != null && position <= (prePics.size() - 1)){
                    deletePrePic(position);
                    deleteTableItem(position);
                }
                if (picUris.get(position) != null) {
                    picUris.remove(position);
                }
                picUris.put(position, uri);
            }

            @Override
            public void onError() {
                LogUtil.i("crop", "onError");
            }
        });
    }

    private void deletePrePic(int position){
        BmobFile bmobFile = new BmobFile();
        bmobFile.setUrl(prePics.get(position).getPicUri());
        bmobFile.delete(activity);
    }

    private void deleteTableItem(int position){
        MorePic thePic = new MorePic();
        thePic.setObjectId(prePics.get(position).getObjectId());
        thePic.delete(activity);
    }

    class ViewHolder {
        ImageButton button;
        CustomView6 customView6;
    }
}