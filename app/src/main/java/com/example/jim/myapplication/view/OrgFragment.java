package com.example.jim.myapplication.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jim.myapplication.Adapter.AllOrgAdapter;
import com.example.jim.myapplication.R;
import com.example.jim.myapplication.model.Organization;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by jim on 2015/8/24.
 */
public class OrgFragment extends Fragment implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{

    private static OrgFragment of = null;
    public static OrgFragment getInstance(){
        if (of == null){
            of = new OrgFragment();
        }
        return of;
    }

    private EasyRecyclerView recyclerView;
    private AllOrgAdapter adapter;

    private List<Organization> orgs;

    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多

    private int limit = 10;        // 每页的数据是10条
    private int curPage = 0;        // 当前页的编号，从0开始
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_org, container, false);
        initView(view);
        queryOrg(STATE_MORE);
        return view;
    }

    private void initView(View view) {
        recyclerView = (EasyRecyclerView) view.findViewById(R.id.org_recycler);
        adapter = new AllOrgAdapter(getActivity());
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_nomore);
        recyclerView.setRefreshListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void queryOrg(final int ActionType){
        BmobQuery<Organization> query = new BmobQuery<>();
        query.addWhereEqualTo("passExam", true);
        query.setLimit(limit);
        query.setSkip(curPage * limit);
        query.findObjects(getActivity(), new FindListener<Organization>() {
            @Override
            public void onSuccess(List<Organization> list) {
                orgs = list;
                if (orgs != null && orgs.size() != 0) {
                    if (ActionType == STATE_REFRESH) {
                        adapter.clear();
                    }
                    adapter.addAll(orgs);
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.stopMore();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Override
    public void onLoadMore() {
        curPage ++;
        queryOrg(STATE_MORE);
    }

    @Override
    public void onRefresh() {
        curPage = 0;
        queryOrg(STATE_REFRESH);
    }

    public void onPause() {
        super.onPause();
        curPage = 0;
    }
}
