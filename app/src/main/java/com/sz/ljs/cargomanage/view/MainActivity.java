package com.sz.ljs.cargomanage.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ljs.examinegoods.view.ExamineGoodsActivity;
import com.sz.ljs.base.BaseActivity;
import com.sz.ljs.base.BaseApplication;
import com.sz.ljs.cargomanage.R;
import com.sz.ljs.cargomanage.adapter.HomeMenuAdapter;
import com.sz.ljs.cargomanage.model.HomeMenuModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private GridView gv_homeMenu;
    private List<HomeMenuModel> homeMenuList = new ArrayList<>();
    private HomeMenuAdapter homeMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getHomeMenu(0);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        gv_homeMenu = (GridView) findViewById(R.id.gv_homeMenu);
        homeMenuAdapter = new HomeMenuAdapter(this, homeMenuList);
        gv_homeMenu.setAdapter(homeMenuAdapter);
    }

    private void initData() {

    }

    private void setListener() {
        gv_homeMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != homeMenuList && homeMenuList.size() > 0) {
                    menuJump(homeMenuList.get(position).getId());
                }
            }
        });
    }

    //TODO 点击菜单跳转，根据id来进行跳转
    private void menuJump(int id) {
        switch (id) {
            case 0: {
                //TODO 验货
                BaseApplication.startActivity(ExamineGoodsActivity.class);
            }
            break;
        }
    }

    //TODO 获取首页菜单，根据用户权限来获取
    private void getHomeMenu(int type) {
        homeMenuList.clear();
        switch (type) {
            case 0: {
                homeMenuList.add(new HomeMenuModel(0, getResources().getString(R.string.str_yh), R.mipmap.common_lading_live1));
                homeMenuList.add(new HomeMenuModel(1, getResources().getString(R.string.str_rk), R.mipmap.common_lading_live1));
                homeMenuList.add(new HomeMenuModel(2, getResources().getString(R.string.str_db), R.mipmap.common_lading_live1));
                homeMenuList.add(new HomeMenuModel(3, getResources().getString(R.string.str_ck), R.mipmap.common_lading_live1));
                homeMenuList.add(new HomeMenuModel(4, getResources().getString(R.string.str_pk), R.mipmap.common_lading_live1));
                homeMenuList.add(new HomeMenuModel(5, getResources().getString(R.string.str_djsm), R.mipmap.common_lading_live1));
                homeMenuList.add(new HomeMenuModel(6, getResources().getString(R.string.str_bdbq), R.mipmap.common_lading_live1));
                homeMenuList.add(new HomeMenuModel(7, getResources().getString(R.string.str_sz), R.mipmap.common_lading_live1));
            }
            break;
        }
    }
}