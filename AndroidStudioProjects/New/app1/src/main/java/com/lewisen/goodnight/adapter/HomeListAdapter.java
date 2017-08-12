package com.lewisen.goodnight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lewisen.goodnight.R;
import com.lewisen.goodnight.controller.DisplayImage;
import com.lewisen.goodnight.dataSrc.MyServer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 主页listview适配器
 * Created by Lewisen on 2015/9/8.
 */
public class HomeListAdapter extends BaseAdapter {
//    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<HashMap<String, String>> list;
    private DisplayImage displayImage;

    public HomeListAdapter(Context mContext) {
//        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
        displayImage = new DisplayImage();
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
            convertView = layoutInflater.inflate(R.layout.home_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.category = (TextView) convertView.findViewById(R.id.category_item);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date_item);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title_item);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text_item);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //先判断是否为空
        if (list.get(position) != null) {
            //开始填充数据
            viewHolder.category.setText(list.get(position).get("category") + " ");
            viewHolder.date.setText(list.get(position).get("date") + "");
            viewHolder.title.setText(list.get(position).get("title") + "");
            viewHolder.text.setText(list.get(position).get("text") + "");
            displayImage.display(viewHolder.image, MyServer.PICTURE_URL + list.get(position).get("image"));
        } else {
            viewHolder.category.setText(" ");
            viewHolder.date.setText(" ");
            viewHolder.title.setText(" ");
            viewHolder.text.setText(" ");
            displayImage.display(viewHolder.image, " ");
        }
        return convertView;
    }

    private class ViewHolder {
        TextView category;
        TextView date;
        TextView title;
        TextView text;
        ImageView image;
    }
}
