package com.ljs.examinegoods.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.ljs.examinegoods.contract.ExamineGoodsContract;
import com.ljs.examinegoods.model.DetectionByModel;
import com.ljs.examinegoods.model.ItemTypeModel;
import com.sz.ljs.common.model.OrderModel;
import com.ljs.examinegoods.model.SaveDeteTionOrderRequestModel;
import com.ljs.examinegoods.model.SaveDetecTionOrderResultModel;
import com.ljs.examinegoods.model.UploadFileResultModel;
import com.sz.ljs.common.base.HDateGsonAdapter;
import com.sz.ljs.common.constant.GenApi;
import com.sz.ljs.common.model.UserModel;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liujs on 2018/8/20.
 */

public class ExamineGoodsPresenter {

    private ExamineGoodsContract mContract;

    public ExamineGoodsPresenter() {
        mContract = new Retrofit.Builder()
                .baseUrl(GenApi.URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(HDateGsonAdapter.createGson()))
                .build().create(ExamineGoodsContract.class);
    }

    public void release() {
        mContract = null;
    }

    //TODO 根据订单号查询订单信息
    public Flowable<OrderModel> getOrderByNumber(String number) {
        Map<String, String> param = new HashMap<>();
        String token = "";
        if (null != UserModel.getInstance() && null != UserModel.getInstance().getTokenModel()) {
            token = UserModel.getInstance().getTokenModel().getToken();
        } else {
            token = "";
        }
        param.put(ExamineGoodsContract.NUMBER, number);
        param.put(ExamineGoodsContract.SUMMARY, ExamineGoodsContract.summary);
//        String json= new Gson().toJson(param);
//        RequestBody requestBody= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json);
        return mContract.getOrderByNumber(token, param);
    }

    //TODO 查询所有得货物类型
    public Flowable<ItemTypeModel> getItemType() {
        Map<String, String> param = new HashMap<>();
        param.put(ExamineGoodsContract.SUMMARY, ExamineGoodsContract.summary);
        return mContract.getItemType(param);
    }

    //TODO 根据货物类型差检查项  detection_name:货物类型中文名称
    public Flowable<DetectionByModel> getDetectionBy(String detection_name) {
        Map<String, String> param = new HashMap<>();
        param.put(ExamineGoodsContract.SUMMARY, ExamineGoodsContract.summary);
        param.put(ExamineGoodsContract.DETECTIONNAME, detection_name);
        String token = "";
        if (null != UserModel.getInstance() && null != UserModel.getInstance().getTokenModel()) {
            token = UserModel.getInstance().getTokenModel().getToken();
        } else {
            token = "";
        }
        return mContract.getDetectionBy(token, param);
    }

    //TODO 添加问题件或者保存验货结果
    public Flowable<SaveDetecTionOrderResultModel> saveDetecTionOrder(SaveDeteTionOrderRequestModel requestModel) {
//        String json = "";
////        if (null != requestModel) {
////            json = new Gson().toJson(requestModel);
////        } else {
////            json = "";
////        }
//        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Map<String, String> param = new HashMap<>();
        param.put(ExamineGoodsContract.NUMBER,requestModel.getNumber());
        if(TextUtils.isEmpty(requestModel.getReference_number())){
            param.put(ExamineGoodsContract.REFERENCE_NUMBER,"");
        }else {
            param.put(ExamineGoodsContract.REFERENCE_NUMBER,requestModel.getReference_number());
        }
        param.put(ExamineGoodsContract.REQUEST_TYPE,requestModel.getRequest_type());
        if(TextUtils.isEmpty(requestModel.getDetection_note())){
            param.put(ExamineGoodsContract.DETECTION_NOTE,"");
        }else {
            param.put(ExamineGoodsContract.DETECTION_NOTE,requestModel.getDetection_note());
        }
        if(null==requestModel.getImage_url()||requestModel.getImage_url().size()<=0){
            param.put(ExamineGoodsContract.IMAGE_LIST,"");
        }else {
            param.put(ExamineGoodsContract.IMAGE_LIST,new Gson().toJson(requestModel.getImage_url()));
        }
        param.put(ExamineGoodsContract.ORDER_ID,requestModel.getOrder_id());
        param.put(ExamineGoodsContract.USERID,requestModel.getUserId());
        param.put(ExamineGoodsContract.SUMMARY, ExamineGoodsContract.summary);
        if(TextUtils.isEmpty(requestModel.getQuest_note())){
            param.put(ExamineGoodsContract.QUEST_NOTE, "");
        }else {
            param.put(ExamineGoodsContract.QUEST_NOTE, requestModel.getQuest_note());
        }

        String token = "";
        if (null != UserModel.getInstance() && null != UserModel.getInstance().getTokenModel()) {
            token = UserModel.getInstance().getTokenModel().getToken();
        } else {
            token = "";
        }
        return mContract.saveDetecTionOrder(token, param);
    }

    //TODO 图片上传
    public Flowable<UploadFileResultModel> uploadFile(String imageUrl) {
        Map<String, String> param = new HashMap<>();
        param.put(ExamineGoodsContract.HEXADECIMAL_STR, imageUrl);
        param.put(ExamineGoodsContract.SUMMARY, ExamineGoodsContract.summary);
        String token = "";
        if (null != UserModel.getInstance() && null != UserModel.getInstance().getTokenModel()) {
            token = UserModel.getInstance().getTokenModel().getToken();
        } else {
            token = "";
        }
        String json = new Gson().toJson(param);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        return mContract.uploadFile(token, requestBody);
    }
}