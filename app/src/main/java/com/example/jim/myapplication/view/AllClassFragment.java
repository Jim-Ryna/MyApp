package com.example.jim.myapplication.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bmob.BmobProFile;
import com.example.jim.myapplication.Adapter.AllClassAdapter;
import com.example.jim.myapplication.ClassFlowButton;
import com.example.jim.myapplication.Config.App;
import com.example.jim.myapplication.R;
import com.example.jim.myapplication.model.AllClassMate;
import com.example.jim.myapplication.model.Introduction;
import com.example.jim.myapplication.model.MyInfo;
import com.example.jim.myapplication.utils.LogUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.rey.material.widget.Spinner;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by jim on 2015/8/8.
 */
public class AllClassFragment extends Fragment implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private static AllClassFragment acf;
    private Spinner academiesChoosed;
    private Spinner classesChoosed;
    private EasyRecyclerView recyclerView;
    private AllClassAdapter adapter;
    private List<AllClassMate> allClassMates = new ArrayList<>();
    private String academy = "";
    private List<String> classes = new ArrayList<>();
    private String classId = "";
    private ArrayAdapter<String> classesAdapter;

    private static final int FIRST_LOAD = 2;
    private int count = 2;

    public static AllClassFragment getInstance() {
        if (acf == null) {
            acf = new AllClassFragment();
        }
        return acf;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        final View view = inflater.inflate(R.layout.fragment_myclass, container, false);
        ClassFlowButton.initButton(view, (MainActivity) getActivity());
        academiesChoosed = (Spinner) view.findViewById(R.id.spinner_academies);
        academiesChoosed.setVisibility(View.VISIBLE);
        ArrayAdapter<String> academiesAdapter = new ArrayAdapter<>(getActivity(), R.layout.row_spn, getResources().getStringArray(R.array.academy));
        academiesAdapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        academiesChoosed.setAdapter(academiesAdapter);
        academiesChoosed.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner spinner, View view, int i, long l) {
                TextView itemView = (TextView) view;
                String selectedItem = (String) itemView.getText();
                if (!selectedItem.equals(academy)) {
                    academy = selectedItem;
                    LogUtil.i("onItemClick", academy);
                    adapter.clear();
                    classes.clear();
                    queryByAcademy(0);
                }
                return true;
            }
        });

        classesChoosed = (Spinner) view.findViewById(R.id.spinner_classes);
        classesChoosed.setVisibility(View.VISIBLE);
        classesAdapter = new ArrayAdapter<>(getActivity(), R.layout.row_spn, classes);
        academiesAdapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        classesChoosed.setAdapter(classesAdapter);
        classesChoosed.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner spinner, View view, int i, long l) {
                TextView itemView = (TextView) view;
                String selectedItem = (String) itemView.getText();
                if (!selectedItem.equals(classId)){
                    classId = selectedItem;
                    adapter.clear();
                    queryByAcademyAndClass(0, STATE_REFRESH);
                }
                return true;
            }
        });
        recyclerView = (EasyRecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapterWithProgress(adapter = new AllClassAdapter(getActivity()));
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);
        recyclerView.setRefreshListener(this);
        if (count == FIRST_LOAD)
            firstLoad();
        else
            loadAgain();
        count++;
        return view;
    }

    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多

    private int limit = 10;        // 每页的数据是10条
    private int curPage = 0;        // 当前页的编号，从0开始
    private int i = 0;            //控制每次查找返回集合的下标

    void firstLoad() {
        academy = (String) academiesChoosed.getSelectedItem();
        LogUtil.i("firstLoad", academy);
        queryByAcademy(0);
    }

    void loadAgain() {
        queryByAcademyAndClass(0, STATE_REFRESH);
    }

    void queryByAcademy(final int page) {
        BmobQuery<MyInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("myAcademy", academy);
        query.addQueryKeys("myClass");
        query.order("-myClass");
        query.findObjects(getActivity(), new FindListener<MyInfo>() {
            @Override
            public void onSuccess(List<MyInfo> list) {
                for (MyInfo info : list) {
                    Boolean flag = false;
                    for (String thisClass : classes){
                        if (thisClass.equals(info.getMyClass()))
                            flag = true;
                    }
                    if (!flag)
                    classes.add(info.getMyClass());
                }
                if (classes.size() != 0) {
                    classesChoosed.setVisibility(View.VISIBLE);
                    classesAdapter.notifyDataSetChanged();
                    classId = classesChoosed.getSelectedItem().toString();
                    LogUtil.i("classfind", "selected" + classId + classes);
                    queryByAcademyAndClass(page, STATE_REFRESH);
                } else {
                    classesChoosed.setVisibility(View.GONE);
                }

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    void queryByAcademyAndClass(int page, final int actionType) {
        BmobQuery<MyInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("myAcademy", academy);
        query.addWhereEqualTo("myClass", classId);
        query.setLimit(limit);            // 设置每页多少条数据
        query.setSkip(page * limit);        // 从第几条数据开始，
        query.findObjects(getActivity(), new FindListener<MyInfo>() {
            @Override
            public void onSuccess(List<MyInfo> list) {
                LogUtil.i("find", "onsuccess" + list.size());
                allClassMates.clear();
                if (list.size() > 0) {
                    if (actionType == STATE_REFRESH) {
                        adapter.clear();
                    }
                } else {
                    adapter.stopMore();
                }
                for (MyInfo user : list) {
                    queryClassMateSig(user, list.size());
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    void queryClassMateSig(final MyInfo thisUser, final int total) {
        BmobQuery<Introduction> query = new BmobQuery<>();
        query.addWhereEqualTo("user", thisUser);
        query.findObjects(getActivity(), new FindListener<Introduction>() {
            @Override
            public void onSuccess(List<Introduction> list) {
                i++;
                String finalURL = "";
                String imageUrl = "";
                imageUrl = thisUser.getPicUri();
                if (imageUrl != "" && imageUrl != null) {
                    finalURL = BmobProFile.getInstance(getActivity()).signURL(thisUser.getPicName(), imageUrl, App.ACCESS_KEY, 0, null);
                }
                if (list != null && list.size() != 0)
                allClassMates.add(new AllClassMate(finalURL, thisUser.getUsername(), list.get(0).isPhoneShow(), list.get(0).getSignature(), thisUser));
                else
                allClassMates.add(new AllClassMate(finalURL, thisUser.getUsername(), false, "", thisUser));
                LogUtil.i("bmobfile", "allmate" + allClassMates.size());
                if (i == total) {
                    adapter.addAll(allClassMates);
                    adapter.notifyDataSetChanged();
                    i = 0;
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.i("bmobfile", "onError" + s);
            }
        });
    }

    @Override
    public void onLoadMore() {
        LogUtil.i("find", "onLoadMore");
        curPage++;
        queryByAcademyAndClass(curPage, STATE_MORE);
    }

    @Override
    public void onRefresh() {
        LogUtil.i("find", "onrefre");
        curPage = 0;
        queryByAcademyAndClass(curPage, STATE_REFRESH);
    }

    @Override
    public void onPause() {
        super.onPause();
        curPage = 0;
    }
}
