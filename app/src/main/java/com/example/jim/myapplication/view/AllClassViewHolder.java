package com.example.jim.myapplication.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jim.myapplication.R;
import com.example.jim.myapplication.model.AllClassMate;
import com.example.jim.myapplication.model.MyInfo;
import com.example.jim.myapplication.utils.LogUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by jim on 2015/8/8.
 */
public class AllClassViewHolder extends BaseViewHolder<AllClassMate> {
    private SimpleDraweeView mImg_face;
    private TextView mTextView;
    private ImageView phoneImage;
    private TextView signature;
    private CardView cardView;
    private ViewGroup parent;

    public AllClassViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_class);
        this.parent = parent;
        mImg_face = (SimpleDraweeView) itemView.findViewById(R.id.head_pic);
        mTextView = (TextView) itemView.findViewById(R.id.classmate_name);
        phoneImage = (ImageView) itemView.findViewById(R.id.show_phone);
        signature = (TextView)itemView.findViewById(R.id.classmate_signature);
        cardView = (CardView) itemView.findViewById(R.id.card);
    }

    @Override
    public void setData(final AllClassMate data) {
        LogUtil.i("thisdata", data.getName());
        MyInfo user = data.getUser();
        String imageUrl = data.getHeadPic();
        String classmateName = data.getName();
        Boolean phoneShow = data.getPhoneShow();
        LogUtil.i(".....", phoneShow.toString());
        if(imageUrl != "") {
            mImg_face.setImageURI(Uri.parse(imageUrl));
        }else {
            mImg_face.setImageResource(R.drawable.head_portrait);
        }
        if (phoneShow){
            LogUtil.e(".....", "set");
            phoneImage.setImageResource(R.drawable.shouji);
        }
        mTextView.setText(classmateName);
        signature.setText(data.getSignature());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), ShowActivity.class);
                intent.putExtra("userId", data.getUser().getObjectId());
                intent.putExtra("name", data.getUser().getUsername());
                parent.getContext().startActivity(intent);
            }
        });
    }
}
