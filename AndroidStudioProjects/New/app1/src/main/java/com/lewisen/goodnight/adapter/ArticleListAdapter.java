package com.lewisen.goodnight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lewisen.goodnight.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 文章列表填充
 * Created by Lewisen on 2015/9/14.
 */
public class ArticleListAdapter extends BaseAdapter {
    //    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<HashMap<String, String>> list;

    public ArticleListAdapter(Context mContext) {
//        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public ArrayList<HashMap<String, String>> getArrayList() {
        return list;
    }

    public void setArrayList(ArrayList<HashMap<String, String>> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        Util.printInfo("HomeListAdapter  getView " + position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.article_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.date = (TextView) convertView.findViewById(R.id.date_item);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title_item);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //先判断是否为空
        if (list.get(position) != null) {
            //开始填充数据
            viewHolder.date.setText(list.get(position).get("date") + "");
            viewHolder.title.setText(list.get(position).get("title") + "");
            viewHolder.text.setText(list.get(position).get("text") + "");
        } else {
            viewHolder.date.setText(" ");
            viewHolder.title.setText(" ");
            viewHolder.text.setText(" ");
        }
        return convertView;
    }

    private class ViewHolder {
        TextView date;
        TextView title;
        TextView text;
    }
}
