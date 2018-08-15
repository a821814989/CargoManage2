package com.sz.ljs.common.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sz.ljs.common.R;
import com.sz.ljs.common.model.ExpressPackageModel;

import java.util.ArrayList;
import java.util.List;


//TODO 左右滑动list数据适配器
public class FourSidesSlidListAdapter extends BaseAdapter {
    private Context mContext;
    private List<ExpressPackageModel> listData = new ArrayList<ExpressPackageModel>();
    private LayoutInflater inflater;
    private ExpressAdapter adapter;

    public FourSidesSlidListAdapter(Context context, List<ExpressPackageModel> list) {
        mContext = context;
        listData = list;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<ExpressPackageModel> list){
        if(null!=this.listData){
            this.listData.clear();
        }
        this.listData=list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler hodler = null;
        if (null == convertView) {
            hodler = new ViewHodler();
            convertView = inflater.inflate(R.layout.item_four_sides_slid_list, null);
            hodler.ll_isOpen = (LinearLayout) convertView.findViewById(R.id.ll_isOpen);
            hodler.ll_ischecked = (LinearLayout) convertView.findViewById(R.id.ll_ischecked);
            hodler.iv_isOpen = (ImageView) convertView.findViewById(R.id.iv_isOpen);
            hodler.iv_ischecked = (ImageView) convertView.findViewById(R.id.iv_ischecked);
            hodler.tv_kongbai = (TextView) convertView.findViewById(R.id.tv_kongbai);
            hodler.tv_kongbai.setVisibility(View.GONE);
            hodler.tv_gx = (TextView) convertView.findViewById(R.id.tv_gx);
            hodler.tv_gx.setVisibility(View.GONE);
            hodler.tv_packageNum = (TextView) convertView.findViewById(R.id.tv_packageNum);
            hodler.tv_zhongzhuanzhuangtai = (TextView) convertView.findViewById(R.id.tv_zhongzhuanzhuangtai);
            hodler.tv_yundanhao = (TextView) convertView.findViewById(R.id.tv_yundanhao);
            hodler.tv_zidantiaoma = (TextView) convertView.findViewById(R.id.tv_zidantiaoma);
            hodler.tv_jianshu = (TextView) convertView.findViewById(R.id.tv_jianshu);
            hodler.tv_shizhong = (TextView) convertView.findViewById(R.id.tv_shizhong);
            hodler.tv_changkuaigao = (TextView) convertView.findViewById(R.id.tv_changkuaigao);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }
        hodler.ll_isOpen.setVisibility(View.VISIBLE);
        hodler.ll_ischecked.setVisibility(View.VISIBLE);
        if (null != listData && listData.size() > 0) {
            hodler.tv_packageNum.setText(""+listData.get(position).getPackageNumber());
            if(String.valueOf(listData.get(position).getPackageNumber()).startsWith("PPNO")){ //表示为包单数据
                hodler.tv_zhongzhuanzhuangtai.setVisibility(View.GONE);
                hodler.tv_yundanhao.setText("");
                hodler.tv_zidantiaoma.setText("");
            }
            hodler.tv_jianshu.setText(""+listData.get(position).getNumber());
            hodler.tv_shizhong.setText(listData.get(position).getWeight()+"KG");
            hodler.tv_changkuaigao.setText(listData.get(position).getLength()+"*"
                                            +listData.get(position).getWidth()+"*"
                                            +listData.get(position).getHeight());
        }

        return convertView;
    }

    class ViewHodler {
        LinearLayout ll_isOpen, ll_ischecked;
        ImageView iv_isOpen, iv_ischecked;
        TextView tv_kongbai, tv_gx, tv_packageNum, tv_zhongzhuanzhuangtai, tv_yundanhao, tv_zidantiaoma, tv_jianshu, tv_shizhong, tv_changkuaigao;
    }
}
