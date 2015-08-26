package com.example.jim.myapplication.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import com.example.jim.myapplication.Adapter.GdAdapter;
import com.example.jim.myapplication.R;
import com.example.jim.myapplication.model.Introduction;
import com.example.jim.myapplication.model.MorePic;
import com.example.jim.myapplication.utils.BmobUtil.BmobTableUtil;
import com.example.jim.myapplication.utils.LogUtil;
import com.jude.library.imageprovider.ImageProvider;
import com.jude.library.imageprovider.OnImageSelectListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.SnackBar;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by jim on 2015/8/9.
 */
public class setInformationActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton backPic;
    private MaterialEditText signature;
    private MaterialEditText introduce;
    private GridView gd;
    private AppCompatCheckBox checkBox;
    private MaterialEditText QQ;
    private MaterialEditText weixin;
    private Button submmit;
    private GdAdapter adapter;
    private ImageProvider provider;
    private BmobFile bmobFile = null;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_information);
        activity = this;
        provider = new ImageProvider(this);
        getInfoPre();
        getMorePicsPre();
        initView();
        setBackPic();
        submmit.setOnClickListener(this);
        gd.setAdapter(adapter);
        deletePicInit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    void initView() {
        backPic = (ImageButton) findViewById(R.id.back_image);
        signature = (MaterialEditText) findViewById(R.id.signature_edit);
        introduce = (MaterialEditText) findViewById(R.id.introduce_edit);
        gd = (GridView) findViewById(R.id.gd);
        checkBox = (AppCompatCheckBox) findViewById(R.id.is_showNo);
        QQ = (MaterialEditText) findViewById(R.id.show_QQ);
        weixin = (MaterialEditText) findViewById(R.id.show_whixin);
        submmit = (Button) findViewById(R.id.submit);
        adapter = new GdAdapter(this, provider);
    }

    private Introduction preIntroduction = null;

    void getInfoPre() {
        final Message message = new Message();
        BmobQuery<Introduction> query = new BmobQuery<>();
        query.addWhereEqualTo("user", MainActivity.user);
        query.findObjects(this, new FindListener<Introduction>() {
            @Override
            public void onSuccess(List<Introduction> list) {
                if (list != null && list.size() != 0) {
                    preIntroduction = list.get(0);
                    message.what = 1;
                    handler.sendMessage(message);
                    LogUtil.i("handler", "find");
                }
            }

            @Override
            public void onError(int i, String s) {
                message.what = 0;
                handler.sendMessage(message);
            }
        });
    }

    private List<MorePic> prePics = null;

    private void getMorePicsPre() {
        final Message message = new Message();
        BmobQuery<MorePic> query = new BmobQuery<>();
        query.addWhereEqualTo("user", MainActivity.user);
        query.findObjects(this, new FindListener<MorePic>() {
            @Override
            public void onSuccess(List<MorePic> list) {
                if (list != null && list.size() != 0) {
                    prePics = list;
                    message.what = 2;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private boolean isSetPreInfo = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            LogUtil.i("handler", "do" + msg.what);
            if (msg.what == 1)
                isSetPreInfo = true;
            if (isSetPreInfo) {
                setWhenGetPre();
            }
            if (msg.what == 2) {
                adapter.setPicsPre(prePics);
                adapter.notifyDataSetChanged();
            }
        }
    };

    private void setWhenGetPre() {
        preIntroduction.getBackImage().loadImage(this, backPic);
        signature.setText(preIntroduction.getSignature());
        introduce.setText(preIntroduction.getIntroduce());
        QQ.setText(preIntroduction.getQQ());
        weixin.setText(preIntroduction.getWeixin());
        checkBox.setChecked(preIntroduction.isPhoneShow());
    }

    private boolean isSetBack = false;

    private Introduction introduction = new Introduction();

    public void onClick(View v) {
        if (signature.getMaxCharacters() < signature.getText().length() || introduce.getMinCharacters() > introduce.getText().length()) {
            View view = findViewById(R.id.relative);
            Snackbar snackbar = Snackbar.make(view, "超出字数限制或未达到", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            introduction.setUser(MainActivity.user);
            introduction.setIntroduce(introduce.getText().toString());
            introduction.setQQ(QQ.getText().toString());
            introduction.setWeixin(weixin.getText().toString());
            introduction.setSignature(signature.getText().toString());
            introduction.setPhoneShow(checkBox.isChecked());
            if (isSetBack) {
                if (isSetPreInfo && preIntroduction.getPicUri() != null && preIntroduction.getPicUri() != "") {
                    BmobFile bmobFile = new BmobFile();
                    bmobFile.setUrl(preIntroduction.getPicUri());
                    bmobFile.delete(this);
                }
                introduction.setBackImage(bmobFile);
                bmobFile.uploadblock(setInformationActivity.this, new UploadFileListener() {
                    @Override
                    public void onSuccess() {
                        introduction.setPicUri(bmobFile.getUrl());
                        if (isSetPreInfo) {
                            introduction.update(activity, preIntroduction.getObjectId(), new UpdateListener() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onFailure(int i, String s) {

                                }
                            });
                        } else
                            BmobTableUtil.insertObj(activity, introduction);
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });

            } else {
                if (isSetPreInfo) {
                    introduction.update(activity, preIntroduction.getObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                } else
                    BmobTableUtil.insertObj(activity, introduction);
            }
            batchUpload();
        }
    }

    private void setBackPic() {
        backPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provider.getImageFromAlbum(new OnImageSelectListener() {
                    @Override
                    public void onImageSelect() {

                    }

                    @Override
                    public void onImageLoaded(Uri uri) {
                        provider.corpImage(uri, 500, 300, new OnImageSelectListener() {
                            @Override
                            public void onImageSelect() {

                            }

                            @Override
                            public void onImageLoaded(Uri uri) {
                                isSetBack = true;
                                File file = new File(uri.getPath());
                                bmobFile = new BmobFile(file);
                                backPic.setImageURI(uri);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        });

    }

    private Map<Integer, Uri> picUris;
    private int i = 0;
    private Boolean isSetMorePic = false;

    void batchUpload() {
        if (adapter.getAllPicUri().size() != 0) {
            picUris = adapter.getAllPicUri();
            final Integer[] indexes = new Integer[picUris.size()];
            String[] filePaths = new String[picUris.size()];
            Iterator iter = picUris.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                filePaths[i] = ((Uri) entry.getValue()).getPath();
                indexes[i] = (Integer) entry.getKey();
                i++;
            }
            Bmob.uploadBatch(this, filePaths, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> list, List<String> list1) {
                    if (list.size() == i) {
                        for (BmobFile file : list) {
                            LogUtil.i("count", "d");
                            BmobTableUtil.insertObj(setInformationActivity.this, new MorePic(file, MainActivity.user, file.getUrl()));
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 500);
                    }
                }

                @Override
                public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                    adapter.setCustomView(indexes[curIndex - 1], totalPercent);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError(int i, String s) {

                }

            });
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 500);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        provider.onActivityResult(requestCode, resultCode, data);
    }

    void deletePicInit(){
        backPic.setOnCreateContextMenuListener(this);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,0,0,"删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {;
        switch (item.getItemId()){
            case 0:
                if (isSetBack == false && preIntroduction.getPicUri() != null && preIntroduction.getPicUri() != ""){
                    BmobFile bmobFile = new BmobFile();
                    bmobFile.setUrl(preIntroduction.getPicUri());
                    bmobFile.delete(this);
                    backPic.setImageResource(R.drawable.without_pic);
                }else if (isSetBack == true){
                    introduction.setBackImage(null);
                }
                break;
        }
        return super.onContextItemSelected(item);
    }
}
