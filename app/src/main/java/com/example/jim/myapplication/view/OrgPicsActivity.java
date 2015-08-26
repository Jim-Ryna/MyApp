package com.example.jim.myapplication.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.jim.myapplication.Adapter.OrgGdAdapter;
import com.example.jim.myapplication.R;
import com.example.jim.myapplication.model.OrgPic;
import com.example.jim.myapplication.utils.BmobUtil.BmobTableUtil;
import com.example.jim.myapplication.utils.LogUtil;
import com.jude.library.imageprovider.ImageProvider;
import com.jude.library.imageprovider.OnImageSelectListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

public class OrgPicsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    GridView gd;
    Button submit;
    OrgGdAdapter adapter;
    ImageProvider provider;

    private Activity activity;

    private Map<Integer, File> addFiles = new HashMap<>();
    //private Map<Integer, OrgPic> clearBmobFiles = new HashMap<>();
    private List<OrgPic> orgPics;

    private String orgName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_pics);
        activity = this;
        setOrgName();
        provider = new ImageProvider(this);
        initView();
        queryOrgPics();
    }

    private void setOrgName() {
        Intent intent = getIntent();
        orgName = intent.getStringExtra("orgname");
    }

    private void queryOrgPics() {
        BmobQuery<OrgPic> query = new BmobQuery<>();
        query.findObjects(this, new FindListener<OrgPic>() {
            @Override
            public void onSuccess(List<OrgPic> list) {
                LogUtil.i("list", "" + list.size());
                orgPics = list;
                adapter = new OrgGdAdapter(activity, orgPics);
                gd.setAdapter(adapter);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void initView() {
        gd = (GridView) findViewById(R.id.org_pics);
        submit = (Button) findViewById(R.id.submit);
        gd.setOnCreateContextMenuListener(this);
        gd.setOnItemClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImageView imageView = (ImageView) view;
        setPic(imageView, position);

    }

    private void setPic(final ImageView imageView, final int position) {
        provider.getImageFromAlbum(new OnImageSelectListener() {
            @Override
            public void onImageSelect() {

            }

            @Override
            public void onImageLoaded(Uri uri) {
                LogUtil.i("image", "load");
                provider.corpImage(uri, 500, 300, new OnImageSelectListener() {
                    @Override
                    public void onImageSelect() {

                    }

                    @Override
                    public void onImageLoaded(Uri uri) {
                        imageView.setImageURI(uri);
                        if (adapter.getExtraView().get(position + 1) != null)
                            adapter.getExtraView().get(position + 1).setVisibility(View.VISIBLE);
                        if (addFiles.get(position) != null) {
                            addFiles.remove(position);
                        }
                        File file = new File(uri.getPath());
                        LogUtil.i("file", file.getName());
                        addFiles.put(position, file);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //menu.add(0, 0, 0, "删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        provider.onActivityResult(requestCode, resultCode, data);
    }

    private int i = 0;

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (addFiles.size() != 0) {
                    String filePaths[] = new String[addFiles.size()];
                    Iterator iter = addFiles.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        final int position = (Integer) entry.getKey();
                        File file = (File) entry.getValue();
                        if (position < (orgPics == null ? 0 : orgPics.size())) {
                            BmobFile deleteFile = new BmobFile();
                            deleteFile.setUrl(orgPics.get(position).getUri());
                            deleteFile.delete(this, new DeleteListener() {
                                @Override
                                public void onSuccess() {
                                    OrgPic pic = new OrgPic();
                                    pic.setObjectId(orgPics.get(position).getObjectId());
                                    pic.delete(activity);
                                }

                                @Override
                                public void onFailure(int i, String s) {

                                }
                            });
                        }
                        filePaths[i] = file.getPath();
                        LogUtil.i("len", ""+i);
                        i++;
                    }
                    Bmob.uploadBatch(this, filePaths, new UploadBatchListener() {
                        @Override
                        public void onSuccess(List<BmobFile> list, List<String> list1) {
                            if (list.size() == i) {
                                for (BmobFile file : list) {
                                    BmobTableUtil.insertObj(activity, new OrgPic(orgName, file, file.getUrl()));
                                }
                            }
                        }

                        @Override
                        public void onProgress(int i, int i1, int i2, int i3) {

                        }

                        @Override
                        public void onError(int i, String s) {

                        }
                    });
                }
                break;
        }
    }
}
