package com.ljs.examinegoods.view;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.ljs.examinegoods.R;
import com.ljs.examinegoods.adapter.PhotoGridAdapter;
import com.ljs.examinegoods.contract.ExamineGoodsContract;
import com.ljs.examinegoods.model.DetectionByModel;
import com.ljs.examinegoods.model.ImageType;
import com.ljs.examinegoods.model.ItemTypeModel;
import com.qiloo.ble.AndBleScanner;
import com.qiloo.ble.bean.ScanInfo;
import com.sz.ljs.common.constant.GenApi;
import com.sz.ljs.common.model.ListialogModel;
import com.sz.ljs.common.model.OrderModel;
import com.ljs.examinegoods.model.SaveDeteTionOrderRequestModel;
import com.ljs.examinegoods.model.SaveDetecTionOrderResultModel;
import com.ljs.examinegoods.model.UploadFileResultModel;
import com.ljs.examinegoods.presenter.ExamineGoodsPresenter;
import com.sz.ljs.base.BaseActivity;
import com.sz.ljs.common.model.UserModel;
import com.sz.ljs.common.utils.BluetoothManager;
import com.sz.ljs.common.utils.Utils;
import com.sz.ljs.common.view.AlertDialog;
import com.sz.ljs.common.view.ListDialog;
import com.sz.ljs.common.view.PhotosUtils;
import com.sz.ljs.common.view.ScanView;
import com.sz.ljs.common.view.SelectionPopForBottomView;
import com.sz.ljs.common.view.WaitingDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liujs on 2018/8/13.
 * 验货界面
 */

public class ExamineGoodsActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG="ExamineGoodsActivity";
    private EditText et_yundanhao, et_kehucankaodanhao, et_wenti, et_jianshu;
    private ImageView iv_scan, iv_yiyanhuo, iv_scan2;
    private TextView tv_goods_type, tv_goods_shifoudaidian, tv_goods_shifoudaici, tv_goods_shifoudaipai, tv_goods_jianshu, tv_goods_suihuofapiao, tv_goods_fapiaoziliao, tv_goods_baoguanziliao, tv_goods_dandubaoguan, tv_goods_huowusunhuai, tv_goods_baozhuangposun, tv_goods_weijinpin, tv_goods_yisuipin;
    private RadioGroup rg_shifoudaidian, rg_shifoudaici, rg_shifoudaipai, rg_jianshu, rg_suihuofapiao, rg_fapiaoziliao, rg_baoguanziliao, rg_dandubaoguan, rg_huowusunhuai, rg_baozhuangposun, rg_weijinpin, rg_yisuipin;
    private RadioButton yes_shifoudaidian, no_shifoudaidian, yes_shifoudaici, no_shifoudaici, yes_shifoudaipai, no_shifoudaipai, yes_jianshu, no_jianshu, yes_suihuofapiao, no_suihuofapiao, yes_fapiaoziliao, no_fapiaoziliao, yes_baoguanziliao, no_baoguanziliao, yes_dandubaoguan, no_dandubaoguan, yes_huowusunhuai, no_huowusunhuai, yes_baozhuangposun, no_baozhuangposun, yes_weijinpin, no_weijinpin, yes_yisuipin, no_yisuipin;
    private GridView gv_photo;
    private LinearLayout ll_photo, ll_shifoudaidian, ll_shifoudaici, ll_shifoudaipai, ll_jianshu, ll_suihuofapiao, ll_fapiaoziliao, ll_baoguanziliao, ll_dandubaoguan, ll_huowusunhuai, ll_baozhuangposun, ll_weijinpin, ll_yisuipin;
    private Button bt_yes;
    private boolean isYanHuo = false,isWenTiJian=false;
    private List<Bitmap> photoList = new ArrayList<>();
    private PhotoGridAdapter adapter;
    private ExamineGoodsPresenter mPresenter;
    private List<ListialogModel> showList = new ArrayList<>();
    private List<ItemTypeModel.DataBean> typeList = new ArrayList<>();
    private List<DetectionByModel.DataBean> detectionList = new ArrayList<>();
    private WaitingDialog waitingDialog;
    private String isDaiDian, isDaiCi, isDaiPai, isJianShu, isSuiHuoFaPiao, isFaPiaoZiLiao, isBaoGuanZiLiao, isDanDuBaoGuan, isHuoWuSunHuai, isWaiBaoZhuangPoSun, isWeiJinPin, isYiSuiPin;
    private boolean isHongKuang = false, isHongKuang1 = false, isHongKuang2 = false, isHongKuang3 = false, isHongKuang4 = false, isHongKuang5 = false, isHongKuang6 = false, isHongKuang7 = false, isHongKuang8 = false, isHongKuang9 = false, isHongKuang10 = false, isHongKuang11 = false;
    private OrderModel orderModel;
    private List<ImageType> Imagelist = new ArrayList<>();
    private ListDialog dialog;
    private BluetoothManager bluetoothManager;
    private AlertDialog alertDialog;
    private AndBleScanner mScanner;
    private List<ScanInfo> bleList=new ArrayList<ScanInfo>();
    private Handler handl;
    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 默认发音人
    private String voicer = "vixy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examine_goods);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mScanner = new AndBleScanner.Builder()
                .setScanTimeOut(30)
                .build();
        waitingDialog = new WaitingDialog(this);
        mPresenter = new ExamineGoodsPresenter();
        et_yundanhao = (EditText) findViewById(R.id.et_yundanhao);
        et_kehucankaodanhao = (EditText) findViewById(R.id.et_kehucankaodanhao);
        et_wenti = (EditText) findViewById(R.id.et_wenti);
        et_jianshu = (EditText) findViewById(R.id.et_jianshu);
        iv_scan = (ImageView) findViewById(R.id.iv_scan);
        iv_scan2 = (ImageView) findViewById(R.id.iv_scan2);
        iv_yiyanhuo = (ImageView) findViewById(R.id.iv_yiyanhuo);
        tv_goods_type = (TextView) findViewById(R.id.tv_goods_type);
        tv_goods_shifoudaidian = (TextView) findViewById(R.id.tv_goods_shifoudaidian);
        tv_goods_shifoudaici = (TextView) findViewById(R.id.tv_goods_shifoudaici);
        tv_goods_shifoudaipai = (TextView) findViewById(R.id.tv_goods_shifoudaipai);
        tv_goods_jianshu = (TextView) findViewById(R.id.tv_goods_jianshu);
        tv_goods_suihuofapiao = (TextView) findViewById(R.id.tv_goods_suihuofapiao);
        tv_goods_fapiaoziliao = (TextView) findViewById(R.id.tv_goods_fapiaoziliao);
        tv_goods_baoguanziliao = (TextView) findViewById(R.id.tv_goods_baoguanziliao);
        tv_goods_dandubaoguan = (TextView) findViewById(R.id.tv_goods_dandubaoguan);
        tv_goods_huowusunhuai = (TextView) findViewById(R.id.tv_goods_huowusunhuai);
        tv_goods_baozhuangposun = (TextView) findViewById(R.id.tv_goods_baozhuangposun);
        tv_goods_huowusunhuai = (TextView) findViewById(R.id.tv_goods_huowusunhuai);
        tv_goods_baozhuangposun = (TextView) findViewById(R.id.tv_goods_baozhuangposun);
        tv_goods_weijinpin = (TextView) findViewById(R.id.tv_goods_weijinpin);
        tv_goods_yisuipin = (TextView) findViewById(R.id.tv_goods_yisuipin);
        rg_shifoudaidian = (RadioGroup) findViewById(R.id.rg_shifoudaidian);
        rg_shifoudaici = (RadioGroup) findViewById(R.id.rg_shifoudaici);
        rg_shifoudaipai = (RadioGroup) findViewById(R.id.rg_shifoudaipai);
        rg_jianshu = (RadioGroup) findViewById(R.id.rg_jianshu);
        rg_suihuofapiao = (RadioGroup) findViewById(R.id.rg_suihuofapiao);
        rg_fapiaoziliao = (RadioGroup) findViewById(R.id.rg_fapiaoziliao);
        rg_baoguanziliao = (RadioGroup) findViewById(R.id.rg_baoguanziliao);
        rg_dandubaoguan = (RadioGroup) findViewById(R.id.rg_dandubaoguan);
        rg_huowusunhuai = (RadioGroup) findViewById(R.id.rg_huowusunhuai);
        rg_baozhuangposun = (RadioGroup) findViewById(R.id.rg_baozhuangposun);
        rg_weijinpin = (RadioGroup) findViewById(R.id.rg_weijinpin);
        rg_yisuipin = (RadioGroup) findViewById(R.id.rg_yisuipin);
        yes_shifoudaidian = (RadioButton) findViewById(R.id.yes_shifoudaidian);
        no_shifoudaidian = (RadioButton) findViewById(R.id.no_shifoudaidian);
        yes_shifoudaici = (RadioButton) findViewById(R.id.yes_shifoudaici);
        no_shifoudaici = (RadioButton) findViewById(R.id.no_shifoudaici);
        yes_shifoudaipai = (RadioButton) findViewById(R.id.yes_shifoudaipai);
        no_shifoudaipai = (RadioButton) findViewById(R.id.no_shifoudaipai);
        yes_jianshu = (RadioButton) findViewById(R.id.yes_jianshu);
        no_jianshu = (RadioButton) findViewById(R.id.no_jianshu);
        yes_suihuofapiao = (RadioButton) findViewById(R.id.yes_suihuofapiao);
        no_suihuofapiao = (RadioButton) findViewById(R.id.no_suihuofapiao);
        yes_fapiaoziliao = (RadioButton) findViewById(R.id.yes_fapiaoziliao);
        no_fapiaoziliao = (RadioButton) findViewById(R.id.no_fapiaoziliao);
        yes_baoguanziliao = (RadioButton) findViewById(R.id.yes_baoguanziliao);
        no_baoguanziliao = (RadioButton) findViewById(R.id.no_baoguanziliao);
        yes_dandubaoguan = (RadioButton) findViewById(R.id.yes_dandubaoguan);
        no_dandubaoguan = (RadioButton) findViewById(R.id.no_dandubaoguan);
        yes_huowusunhuai = (RadioButton) findViewById(R.id.yes_huowusunhuai);
        no_huowusunhuai = (RadioButton) findViewById(R.id.no_huowusunhuai);
        yes_baozhuangposun = (RadioButton) findViewById(R.id.yes_baozhuangposun);
        no_baozhuangposun = (RadioButton) findViewById(R.id.no_baozhuangposun);
        yes_weijinpin = (RadioButton) findViewById(R.id.yes_weijinpin);
        no_weijinpin = (RadioButton) findViewById(R.id.no_weijinpin);
        yes_yisuipin = (RadioButton) findViewById(R.id.yes_yisuipin);
        no_yisuipin = (RadioButton) findViewById(R.id.no_yisuipin);
        gv_photo = (GridView) findViewById(R.id.gv_photo);
        ll_photo = (LinearLayout) findViewById(R.id.ll_photo);
        bt_yes = (Button) findViewById(R.id.bt_yes);
        ll_shifoudaidian = (LinearLayout) findViewById(R.id.ll_shifoudaidian);
        ll_shifoudaici = (LinearLayout) findViewById(R.id.ll_shifoudaici);
        ll_shifoudaipai = (LinearLayout) findViewById(R.id.ll_shifoudaipai);
        ll_jianshu = (LinearLayout) findViewById(R.id.ll_jianshu);
        ll_suihuofapiao = (LinearLayout) findViewById(R.id.ll_suihuofapiao);
        ll_fapiaoziliao = (LinearLayout) findViewById(R.id.ll_fapiaoziliao);
        ll_baoguanziliao = (LinearLayout) findViewById(R.id.ll_baoguanziliao);
        ll_dandubaoguan = (LinearLayout) findViewById(R.id.ll_dandubaoguan);
        ll_huowusunhuai = (LinearLayout) findViewById(R.id.ll_huowusunhuai);
        ll_baozhuangposun = (LinearLayout) findViewById(R.id.ll_baozhuangposun);
        ll_weijinpin = (LinearLayout) findViewById(R.id.ll_weijinpin);
        ll_yisuipin = (LinearLayout) findViewById(R.id.ll_yisuipin);
    }


    private void initData() {
        adapter = new PhotoGridAdapter(this, photoList);
        gv_photo.setAdapter(adapter);
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(this, new InitListener() {
            @Override
            public void onInit(int i) {
                if (i != ErrorCode.SUCCESS) {
                    Utils.showToast(getBaseActivity(),"初始化失败,错误码："+i);
                } else {
                    // 初始化成功，之后可以调用startSpeaking方法
                    // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                    // 正确的做法是将onCreate中的startSpeaking调用移至这里
                }
            }
        });

        // 设置参数
        setParam();
    }



    private void setListener() {
        et_yundanhao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= GenApi.ScanNumberLeng) {
                    //TODO 当运单号大于8位的时候就开始请求数据
                    getOrderByNumber();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rg_shifoudaidian.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.yes_shifoudaidian) {
                    isHongKuang = inspect(getResources().getString(R.string.str_sfdd), "Y");
                    isDaiDian = "Y";
                } else if (checkedId == R.id.no_shifoudaidian) {
                    isHongKuang = inspect(getResources().getString(R.string.str_sfdd), "N");
                    isDaiDian = "N";
                }
                if (true == isHongKuang) {
                    ll_shifoudaidian.setBackgroundResource(R.drawable.login_edittext2_bg);
                } else {
                    ll_shifoudaidian.setBackgroundResource(R.drawable.login_edittext_bg);
                }
            }
        });
        rg_shifoudaici.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yes_shifoudaici) {
                    isHongKuang1 = inspect(getResources().getString(R.string.str_sfdc), "Y");
                    isDaiCi = "Y";
                } else if (checkedId == R.id.no_shifoudaici) {
                    isHongKuang1 = inspect(getResources().getString(R.string.str_sfdc), "N");
                    isDaiCi = "N";
                }
                if (true == isHongKuang1) {
                    ll_shifoudaici.setBackgroundResource(R.drawable.login_edittext2_bg);
                } else {
                    ll_shifoudaici.setBackgroundResource(R.drawable.login_edittext_bg);
                }
            }
        });
        rg_shifoudaipai.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yes_shifoudaipai) {
                    isHongKuang2 = inspect(getResources().getString(R.string.str_sfdp), "Y");
                    isDaiPai = "Y";
                } else if (checkedId == R.id.no_shifoudaipai) {
                    isHongKuang2 = inspect(getResources().getString(R.string.str_sfdp), "N");
                    isDaiPai = "N";
                }
                if (true == isHongKuang2) {
                    ll_shifoudaipai.setBackgroundResource(R.drawable.login_edittext2_bg);
                } else {
                    ll_shifoudaipai.setBackgroundResource(R.drawable.login_edittext_bg);
                }
            }
        });
        rg_jianshu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yes_jianshu) {
                    isHongKuang3 = inspect(getResources().getString(R.string.str_js), "Y");
                    isJianShu = "Y";
                } else if (checkedId == R.id.no_jianshu) {
                    isHongKuang3 = inspect(getResources().getString(R.string.str_js), "N");
                    isJianShu = "N";
                }
                if (true == isHongKuang3) {
                    ll_jianshu.setBackgroundResource(R.drawable.login_edittext2_bg);
                } else {
                    ll_jianshu.setBackgroundResource(R.drawable.login_edittext_bg);
                }
            }
        });
        rg_suihuofapiao.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yes_suihuofapiao) {
                    isHongKuang4 = inspect(getResources().getString(R.string.str_sfyshfp), "Y");
                    isSuiHuoFaPiao = "Y";
                } else if (checkedId == R.id.no_suihuofapiao) {
                    isHongKuang4 = inspect(getResources().getString(R.string.str_sfyshfp), "N");
                    isSuiHuoFaPiao = "N";
                }
                if (true == isHongKuang4) {
                    ll_suihuofapiao.setBackgroundResource(R.drawable.login_edittext2_bg);
                } else {
                    ll_suihuofapiao.setBackgroundResource(R.drawable.login_edittext_bg);
                }
            }
        });
        rg_fapiaoziliao.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yes_fapiaoziliao) {
                    isHongKuang5 = inspect(getResources().getString(R.string.str_fpzlsfzq), "Y");
                    isFaPiaoZiLiao = "Y";
                } else if (checkedId == R.id.no_fapiaoziliao) {
                    isHongKuang5 = inspect(getResources().getString(R.string.str_fpzlsfzq), "N");
                    isFaPiaoZiLiao = "N";
                }
                if (true == isHongKuang5) {
                    ll_fapiaoziliao.setBackgroundResource(R.drawable.login_edittext2_bg);
                } else {
                    ll_fapiaoziliao.setBackgroundResource(R.drawable.login_edittext_bg);
                }
            }
        });
        rg_baoguanziliao.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yes_baoguanziliao) {
                    isHongKuang6 = inspect(getResources().getString(R.string.str_ywbgzl), "Y");
                    isBaoGuanZiLiao = "Y";
                } else if (checkedId == R.id.no_baoguanziliao) {
                    isHongKuang6 = inspect(getResources().getString(R.string.str_ywbgzl), "N");
                    isBaoGuanZiLiao = "N";
                }
                if (true == isHongKuang6) {
                    ll_baoguanziliao.setBackgroundResource(R.drawable.login_edittext2_bg);
                } else {
                    ll_baoguanziliao.setBackgroundResource(R.drawable.login_edittext_bg);
                }
            }
        });
        rg_dandubaoguan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yes_dandubaoguan) {
                    isHongKuang7 = inspect(getResources().getString(R.string.str_sfddbg), "Y");
                    isDanDuBaoGuan = "Y";
                } else if (checkedId == R.id.no_dandubaoguan) {
                    isHongKuang7 = inspect(getResources().getString(R.string.str_sfddbg), "N");
                    isDanDuBaoGuan = "N";
                }
                if (true == isHongKuang7) {
                    ll_dandubaoguan.setBackgroundResource(R.drawable.login_edittext2_bg);
                } else {
                    ll_dandubaoguan.setBackgroundResource(R.drawable.login_edittext_bg);
                }
            }
        });
        rg_huowusunhuai.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yes_huowusunhuai) {
                    isHongKuang8 = inspect(getResources().getString(R.string.str_hwsfsh), "Y");
                    isHuoWuSunHuai = "Y";
                } else if (checkedId == R.id.no_huowusunhuai) {
                    isHongKuang8 = inspect(getResources().getString(R.string.str_hwsfsh), "N");
                    isHuoWuSunHuai = "N";
                }
                if (true == isHongKuang8) {
                    ll_huowusunhuai.setBackgroundResource(R.drawable.login_edittext2_bg);
                } else {
                    ll_huowusunhuai.setBackgroundResource(R.drawable.login_edittext_bg);
                }
            }
        });
        rg_baozhuangposun.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yes_baozhuangposun) {
                    isHongKuang9 = inspect(getResources().getString(R.string.str_wbzsfps), "Y");
                    isWaiBaoZhuangPoSun = "Y";
                } else if (checkedId == R.id.no_baozhuangposun) {
                    isHongKuang9 = inspect(getResources().getString(R.string.str_wbzsfps), "N");
                    isWaiBaoZhuangPoSun = "N";
                }
                if (true == isHongKuang9) {
                    ll_baozhuangposun.setBackgroundResource(R.drawable.login_edittext2_bg);
                } else {
                    ll_baozhuangposun.setBackgroundResource(R.drawable.login_edittext_bg);
                }
            }
        });
        rg_weijinpin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yes_weijinpin) {
                    isHongKuang10 = inspect(getResources().getString(R.string.str_sfwwjp), "Y");
                    isWeiJinPin = "Y";
                } else if (checkedId == R.id.no_weijinpin) {
                    isHongKuang10 = inspect(getResources().getString(R.string.str_sfwwjp), "N");
                    isWeiJinPin = "N";
                }
                if (true == isHongKuang10) {
                    ll_weijinpin.setBackgroundResource(R.drawable.login_edittext2_bg);
                } else {
                    ll_weijinpin.setBackgroundResource(R.drawable.login_edittext_bg);
                }
            }
        });
        rg_yisuipin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yes_yisuipin) {
                    isHongKuang11 = inspect(getResources().getString(R.string.str_sfwysp), "Y");
                    isYiSuiPin = "Y";
                } else if (checkedId == R.id.no_yisuipin) {
                    isHongKuang11 = inspect(getResources().getString(R.string.str_sfwysp), "N");
                    isYiSuiPin = "N";
                }
                if (true == isHongKuang11) {
                    ll_yisuipin.setBackgroundResource(R.drawable.login_edittext2_bg);
                } else {
                    ll_yisuipin.setBackgroundResource(R.drawable.login_edittext_bg);
                }
            }
        });
        adapter.setDelOnclickCallBack(new PhotoGridAdapter.DelOnclickCallBack() {
            @Override
            public void CallBack(int position) {
                if (null != photoList && photoList.size() > 0) {
                    photoList.remove(position);
                    Imagelist.get(position).setImage_type("D");
                    adapter.notifyDataSetChanged();
                }
            }
        });
        ll_photo.setOnClickListener(this);
        bt_yes.setOnClickListener(this);
        iv_scan.setOnClickListener(this);
        iv_scan2.setOnClickListener(this);
        iv_yiyanhuo.setOnClickListener(this);
//        tv_goods_type.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_photo) {
            if (photoList.size() >= 3) {
                Utils.showToast(getBaseActivity(), R.string.str_zpzds);
                return;
            }
            //TODO 拍照
            PhotosUtils.selectUserPhotos(new PhotosUtils.IUserPhotosCallBack() {
                @Override
                public void onResult(Bitmap bitmap) {
                    if (null != bitmap) {
                        String str = PhotosUtils.bitmapToHexString(bitmap);
                        Log.i("图片转成16进制之后的字符串", "str=" + PhotosUtils.bitmapToHexString(bitmap));
                        if (!TextUtils.isEmpty(str)) {
                            uploadFile(bitmap, str);
                        }
                    }
                }
            });
        } else if (id == R.id.bt_yes) {
            //TODO 确认
            if (isHongKuang || isHongKuang1 || isHongKuang2 || isHongKuang3 || isHongKuang4 || isHongKuang5 || isHongKuang6
                    || isHongKuang7 || isHongKuang8 || isHongKuang9 || isHongKuang10 || isHongKuang11) {
                //TODO 表示为问题件，不给点击已验货按钮
                isWenTiJian = true;
            }else {
                isWenTiJian=false;
            }
            saveDetecTionOrder();
        } else if (id == R.id.iv_scan) {
            //TODO 运单号扫描
            ScanView.ScanView(new ScanView.SacnCallBack() {
                @Override
                public void onResult(String result) {
                    if (!TextUtils.isEmpty(result)) {
                        et_yundanhao.setText(result);
                        int code = mTts.startSpeaking(result, mTtsListener);
                        Log.i("语音播报","code="+code);
                    }
                }
            });
        } else if (id == R.id.iv_scan2) {
            //TODO 客户参考单号扫描
            ScanView.ScanView(new ScanView.SacnCallBack() {
                @Override
                public void onResult(String result) {
                    if (!TextUtils.isEmpty(result)) {
                        et_kehucankaodanhao.setText(result);
                    }
                }
            });
        } else if (id == R.id.iv_yiyanhuo) {
            if (isHongKuang || isHongKuang1 || isHongKuang2 || isHongKuang3 || isHongKuang4 || isHongKuang5 || isHongKuang6
                    || isHongKuang7 || isHongKuang8 || isHongKuang9 || isHongKuang10 || isHongKuang11) {
                //TODO 表示为问题件，不给点击已验货按钮
                return;
            }
            //TODO 已验货按钮
            if (false == isYanHuo) {
                isYanHuo = true;
                iv_yiyanhuo.setImageResource(R.mipmap.fb_g);
            } else {
                isYanHuo = false;
                iv_yiyanhuo.setImageResource(R.mipmap.fb_b);
            }
        }
//        else if (id == R.id.tv_goods_type) {
//            //TODO 货物类型
//            getItemType();
//        }
    }

    //TODO 图片上传
    private void uploadFile(final Bitmap bitmap, String str) {
        showWaiting(true);
        mPresenter.uploadFile(str)
                .compose(this.<UploadFileResultModel>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UploadFileResultModel>() {
                    @Override
                    public void accept(UploadFileResultModel result) throws Exception {
                        if (0 == result.getCode()) {
                            showWaiting(false);
                            Utils.showToast(ExamineGoodsActivity.this, result.getMsg());
                        } else if (1 == result.getCode()) {
                            showWaiting(false);
                            Imagelist.add(new ImageType(result.getData(),"A"));
                            photoList.add(bitmap);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showWaiting(false);
                        //获取失败，提示
                        Utils.showToast(getBaseActivity(), R.string.str_qqsb);
                    }
                });
    }

    //TODO 提交问题件或者保存验货单
    private void saveDetecTionOrder() {
        if (TextUtils.isEmpty(et_yundanhao.getText().toString().trim())) {
            Utils.showToast(getBaseActivity(), getResources().getString(R.string.str_ydhbnwk));
            return;
        }
//        if (TextUtils.isEmpty(et_kehucankaodanhao.getText().toString().trim())) {
//            Utils.showToast(getBaseActivity(), getResources().getString(R.string.str_khckdhbnwk));
//            return;
//        }
        if (TextUtils.isEmpty(et_jianshu.getText().toString().trim())) {
            Utils.showToast(getBaseActivity(), getResources().getString(R.string.str_jsbnwk));
            return;
        }
        if (true == isWenTiJian) {
            if (TextUtils.isEmpty(et_wenti.getText().toString().trim())) {
                Utils.showToast(getBaseActivity(), getResources().getString(R.string.str_wtmsbnwk));
                return;
            }
            if(null==Imagelist&&Imagelist.size()<=0){
                Utils.showToast(getBaseActivity(), getResources().getString(R.string.str_zpbnwk));
                return;
            }
        }else {
            if(false==isYanHuo){
                Utils.showToast(getBaseActivity(), getResources().getString(R.string.str_qgxyyh));
                return;
            }
        }
        showWaiting(true);
        SaveDeteTionOrderRequestModel requestModel = new SaveDeteTionOrderRequestModel();
        requestModel.setNumber(et_yundanhao.getText().toString().trim());
        requestModel.setReference_number(et_kehucankaodanhao.getText().toString());
        if (true == isWenTiJian) {
            //TODO 问题件，需要上传问题描述与图片集合，不需要上传检验结果
            requestModel.setRequest_type("Y");
            if (null != Imagelist && Imagelist.size() > 0) {
                requestModel.setImage_url(Imagelist);
            }
            requestModel.setQuest_note(et_wenti.getText().toString().trim());
        } else {
            //TODO 不是问题件，不需要上传问题描述与图片集合，需要上传检验结果
            requestModel.setRequest_type("N");
//            + "," + getResources().getString(R.string.str_js) + ":" + isJianShu
            String detection_note = getResources().getString(R.string.str_sfdd) + ":" + isDaiDian + "," + getResources().getString(R.string.str_sfdc) + ":" + isDaiCi
                    + "," + getResources().getString(R.string.str_sfdp) + ":" + isDaiPai
                    + "," + getResources().getString(R.string.str_sfyshfp) + ":" + isSuiHuoFaPiao + "," + getResources().getString(R.string.str_fpzlsfzq) + ":" + isFaPiaoZiLiao
                    + "," + getResources().getString(R.string.str_ywbgzl) + ":" + isBaoGuanZiLiao + "," + getResources().getString(R.string.str_sfddbg) + ":" + isDanDuBaoGuan
                    + "," + getResources().getString(R.string.str_hwsfsh) + ":" + isHuoWuSunHuai + "," + getResources().getString(R.string.str_wbzsfps) + ":" + isWaiBaoZhuangPoSun
                    + "," + getResources().getString(R.string.str_sfwwjp) + ":" + isWeiJinPin + "," + getResources().getString(R.string.str_sfwysp) + ":" + isYiSuiPin
                    + "," + getResources().getString(R.string.str_zjs) + et_jianshu.getText().toString().trim();
            requestModel.setDetection_note(detection_note);
        }
        if (null != orderModel && null != orderModel.getData() && !TextUtils.isEmpty(orderModel.getData().getOrder_id())) {
            requestModel.setOrder_id(orderModel.getData().getOrder_id());
        }
        if (null != UserModel.getInstance()) {
            requestModel.setUserId(String.valueOf(UserModel.getInstance().getSt_id()));
        }
        requestModel.setSummary(ExamineGoodsContract.summary);
        mPresenter.saveDetecTionOrder(requestModel)
                .compose(this.<SaveDetecTionOrderResultModel>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SaveDetecTionOrderResultModel>() {

                    @Override
                    public void accept(SaveDetecTionOrderResultModel result) throws Exception {
                        if (0 == result.getCode()) {
                            showWaiting(false);
                            Utils.showToast(ExamineGoodsActivity.this, result.getMsg());
                        } else if (1 == result.getCode()) {
                            showWaiting(false);
                            Utils.showToast(ExamineGoodsActivity.this, result.getMsg());
                            finish();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showWaiting(false);
                        //获取失败，提示
                        Utils.showToast(getBaseActivity(), R.string.str_qqsb);
                    }
                });
    }

    //TODO 查询所有得货物类型
    private void getItemType() {
        showWaiting(true);
        mPresenter.getItemType()
                .compose(this.<ItemTypeModel>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ItemTypeModel>() {
                    @Override
                    public void accept(ItemTypeModel result) throws Exception {
                        if (0 == result.getCode()) {
                            showWaiting(false);
                            Utils.showToast(ExamineGoodsActivity.this, result.getMsg());
                        } else if (1 == result.getCode()) {
                            showWaiting(false);
                            if (null != result && null != result.getData() && result.getData().size() > 0) {
                                typeList.clear();
                                typeList.addAll(result.getData());
                                showList.clear();
                                for (ItemTypeModel.DataBean bean : result.getData()) {
                                    showList.add(new ListialogModel("",bean.getItem_cn_name(),bean.getItem_en_name(),false));
                                }
                                dialog=new ListDialog(ExamineGoodsActivity.this)
                                        .creatDialog()
                                        .setTitle("请选择货物类型")
                                        .setListData(showList)
                                        .setCallBackListener(new ListDialog.CallBackListener() {
                                            @Override
                                            public void Result(int position, String name) {

                                            }
                                        });
                                dialog.show();

                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showWaiting(false);
                        //获取失败，提示
                        Utils.showToast(getBaseActivity(), R.string.str_qqsb);
                    }
                });
    }

    //TODO 根据货物类型差检查项  detection_name:货物类型中文名称
    private void getDetectionBy(String detection_name) {
        showWaiting(true);
        mPresenter.getDetectionBy(detection_name)
                .compose(this.<DetectionByModel>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DetectionByModel>() {
                    @Override
                    public void accept(DetectionByModel result) throws Exception {
                        if (0 == result.getCode()) {
                            showWaiting(false);
                            Utils.showToast(ExamineGoodsActivity.this, result.getMsg());
                        } else if (1 == result.getCode()) {
                            showWaiting(false);
                            if (null != result.getData() && result.getData().size() > 0) {
                                detectionList.clear();
                                detectionList.addAll(result.getData());
                                setItemDefaultValues();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showWaiting(false);
                        //获取失败，提示
                        Utils.showToast(getBaseActivity(), R.string.str_qqsb);
                    }
                });
    }

    //TODO 根据运单号请求运单数据
    private void getOrderByNumber() {

        mPresenter.getOrderByNumber(et_yundanhao.getText().toString().trim())
                .compose(this.<OrderModel>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OrderModel>() {
                    @Override
                    public void accept(OrderModel result) throws Exception {
                        if (0 == result.getCode()) {
                            Utils.showToast(ExamineGoodsActivity.this, result.getMsg());
                        } else if (1 == result.getCode()) {
                            handelOrderResult(result);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //获取失败，提示
                        Utils.showToast(getBaseActivity(), R.string.str_qqsb);
                    }
                });
    }

    //TODO 处理运单数据
    private void handelOrderResult(OrderModel result) {
        if (null != result && null != result.getData()) {
            if (null == orderModel) {
                orderModel = new OrderModel();
            }
            orderModel = result;
            if (!TextUtils.isEmpty(result.getData().getOrder_info())) {
                tv_goods_type.setText(result.getData().getOrder_info());
                getDetectionBy(result.getData().getOrder_info());
            }
            if (!TextUtils.isEmpty(result.getData().getOrder_pieces())) {
                tv_goods_jianshu.setText(result.getData().getOrder_pieces());
            }

        }
    }

    //TODO 设置检查项默认值
    private void setItemDefaultValues() {
        if (null != detectionList && detectionList.size() > 0) {
            for (DetectionByModel.DataBean bean : detectionList) {
                if (bean.getDetection_cn_name().equals(getResources().getString(R.string.str_sfdd))) {
                    if ("Y".equals(bean.getDefult_value())) {
                        yes_shifoudaidian.setChecked(true);
                        isDaiDian = "Y";
                    } else if ("N".equals(bean.getDefult_value())) {
                        no_shifoudaidian.setChecked(true);
                        isDaiDian = "N";
                    }
                } else if (bean.getDetection_cn_name().equals(getResources().getString(R.string.str_sfdc))) {
                    if ("Y".equals(bean.getDefult_value())) {
                        yes_shifoudaici.setChecked(true);
                        isDaiCi = "Y";
                    } else if ("N".equals(bean.getDefult_value())) {
                        no_shifoudaici.setChecked(true);
                        isDaiCi = "N";
                    }
                } else if (bean.getDetection_cn_name().equals(getResources().getString(R.string.str_sfdp))) {
                    if ("Y".equals(bean.getDefult_value())) {
                        yes_shifoudaipai.setChecked(true);
                        isDaiPai = "Y";
                    } else if ("N".equals(bean.getDefult_value())) {
                        no_shifoudaipai.setChecked(true);
                        isDaiPai = "N";
                    }
                } else if (bean.getDetection_cn_name().equals(getResources().getString(R.string.str_js))) {
                    if ("Y".equals(bean.getDefult_value())) {
                        yes_jianshu.setChecked(true);
                        isJianShu = "Y";
                    } else if ("N".equals(bean.getDefult_value())) {
                        no_jianshu.setChecked(true);
                        isJianShu = "N";
                    }
                } else if (bean.getDetection_cn_name().equals(getResources().getString(R.string.str_sfyshfp))) {
                    if ("Y".equals(bean.getDefult_value())) {
                        yes_suihuofapiao.setChecked(true);
                        isSuiHuoFaPiao = "Y";
                    } else if ("N".equals(bean.getDefult_value())) {
                        no_suihuofapiao.setChecked(true);
                        isSuiHuoFaPiao = "N";
                    }
                } else if (bean.getDetection_cn_name().equals(getResources().getString(R.string.str_fpzlsfzq))) {
                    if ("Y".equals(bean.getDefult_value())) {
                        yes_fapiaoziliao.setChecked(true);
                        isFaPiaoZiLiao = "Y";
                    } else if ("N".equals(bean.getDefult_value())) {
                        no_fapiaoziliao.setChecked(true);
                        isFaPiaoZiLiao = "N";
                    }
                } else if (bean.getDetection_cn_name().equals(getResources().getString(R.string.str_ywbgzl))) {
                    if ("Y".equals(bean.getDefult_value())) {
                        yes_baoguanziliao.setChecked(true);
                        isBaoGuanZiLiao = "Y";
                    } else if ("N".equals(bean.getDefult_value())) {
                        no_baoguanziliao.setChecked(true);
                        isBaoGuanZiLiao = "N";
                    }
                } else if (bean.getDetection_cn_name().equals(getResources().getString(R.string.str_sfddbg))) {
                    if ("Y".equals(bean.getDefult_value())) {
                        yes_dandubaoguan.setChecked(true);
                        isDanDuBaoGuan = "Y";
                    } else if ("N".equals(bean.getDefult_value())) {
                        no_dandubaoguan.setChecked(true);
                        isDanDuBaoGuan = "N";
                    }
                } else if (bean.getDetection_cn_name().equals(getResources().getString(R.string.str_hwsfsh))) {
                    if ("Y".equals(bean.getDefult_value())) {
                        yes_huowusunhuai.setChecked(true);
                        isHuoWuSunHuai = "Y";
                    } else if ("N".equals(bean.getDefult_value())) {
                        no_huowusunhuai.setChecked(true);
                        isHuoWuSunHuai = "N";
                    }
                } else if (bean.getDetection_cn_name().equals(getResources().getString(R.string.str_wbzsfps))) {
                    if ("Y".equals(bean.getDefult_value())) {
                        yes_baozhuangposun.setChecked(true);
                        isWaiBaoZhuangPoSun = "Y";
                    } else if ("N".equals(bean.getDefult_value())) {
                        no_baozhuangposun.setChecked(true);
                        isWaiBaoZhuangPoSun = "N";
                    }
                } else if (bean.getDetection_cn_name().equals(getResources().getString(R.string.str_sfwwjp))) {
                    if ("Y".equals(bean.getDefult_value())) {
                        yes_weijinpin.setChecked(true);
                        isWeiJinPin = "Y";
                    } else if ("N".equals(bean.getDefult_value())) {
                        no_weijinpin.setChecked(true);
                        isWeiJinPin = "N";
                    }
                } else if (bean.getDetection_cn_name().equals(getResources().getString(R.string.str_sfwysp))) {
                    if ("Y".equals(bean.getDefult_value())) {
                        yes_yisuipin.setChecked(true);
                        isYiSuiPin = "Y";
                    } else if ("N".equals(bean.getDefult_value())) {
                        no_yisuipin.setChecked(true);
                        isYiSuiPin = "N";
                    }
                }
            }
        }
    }

    //TODO 通过对应的检测项名称以及所点击的来检测是否需要显示红框
    private boolean inspect(String inspectName, String inspectType) {
        boolean isChaYi = false;
        if (null != detectionList && detectionList.size() > 0) {
            for (int i = 0; i < detectionList.size(); i++) {
                if (inspectName.equals(detectionList.get(i).getDetection_cn_name())) {
                    //如果有这一项检查
                    if ("D".equals(detectionList.get(i).getValue())) {
                        //表示不做限制的，这时候随便点哪项都没事
                        isChaYi = false;
                        break;
                    } else if (inspectType.equals(detectionList.get(i).getValue())) {
                        isChaYi = false;
                        break;
                    } else {
                        isChaYi = true;
                        break;
                    }
                }
            }
        }
        return isChaYi;
    }


    //TODO 打印
    private void Printing(){
        bluetoothManager=new BluetoothManager(ExamineGoodsActivity.this)
                .setStateChangCallBack(new BluetoothManager.IBluetoothStateChangCallBack() {
                    @Override
                    public void onStateChang(int state) {
                        switch (state) {
                            case BluetoothAdapter.STATE_ON: {
                                Scan();
                            }
                            break;
                            case BluetoothAdapter.STATE_OFF: {
                                stopScan();
                            }
                            break;
                        }
                    }
                });
        if (!BluetoothManager.BluetoothState()) { //表示未开启
            alertDialog = new AlertDialog(ExamineGoodsActivity.this).builder()
                    .setMsg(getString(R.string.str_bluetooth_is_close))
                    .setPositiveButton(getString(R.string.str_yes), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BluetoothManager.openBluetooth();
                            alertDialog.dissmiss();
                        }
                    })
                    .setNegativeButton(getString(R.string.str_cancel), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dissmiss();
                        }
                    });
            alertDialog.show();
        } else {
            Scan();
        }
    }

    //TODO 蓝牙搜索
    private void Scan(){
        mScanner.searchBle()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ScanInfo>() {

                    @Override
                    public void accept(ScanInfo scanInfo) throws Exception {
                        String tmp = "Addr=" +scanInfo.getScanDevice().getAddress()
                                + " Rssi=" + scanInfo.getRssi()
                                + " UUID=" + scanInfo.getAdvData().getAdvData()
                                + " LocalName=" + scanInfo.getAdvData().getLocalName();
                        Log.i("设备信息","tmp="+tmp);
                        for (final ScanInfo mdevice : bleList) { // 将设备储存进扫描列表
                            if (mdevice.getScanDevice().getAddress().equals(scanInfo.getScanDevice().getAddress())) {
                                handl.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            bleList.remove(mdevice);
                                        } catch (Exception e) {
                                            Log.e(TAG
                                                    , e.toString());
                                        }
                                    }
                                });
                            }
                        }
                        bleList.add(scanInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {//出错的信息
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {//扫描完成的
                        handl.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Scan();
                                } catch (Exception e) {

                                    Log.e(TAG
                                            , e.toString());
                                }
                            }
                        });
                    }
                });
    }

    //TODO 停止搜索
    private void stopScan() {
        mScanner.stopScan();
    }
    private void showWaiting(boolean isShow) {
        if (null != waitingDialog) {
            waitingDialog.showDialog(isShow);
        }
    }

    /**
     * 参数设置
     * @return
     */
    private void setParam(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //onevent回调接口实时返回音频流数据
            //mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "100");
        }else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.pcm");
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度

            if(!"henry".equals(voicer)||!"xiaoyan".equals(voicer)||
                    !"xiaoyu".equals(voicer)||!"catherine".equals(voicer))
                endPos++;
            Log.e(TAG,"beginPos = "+beginPos +"  endPos = "+endPos);
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip("播放完成");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            Log.e(TAG,"TTS Demo onEvent >>>"+eventType);
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                Log.d(TAG, "session id =" + sid);
            }
        }
    };
    private void showTip(final String str) {
        Utils.showToast(getBaseActivity(),str);
    }
}
