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
 * 图片栏目
 * Created by Lewisen on 2015/9/14.
 */
public class PictureListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<HashMap<String, String>> list;
    private DisplayImage displayImage;

    public PictureListAdapter(Context mContext) {
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
            convertView = layoutInflater.inflate(R.layout.picture_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.date = (TextView) convertView.findViewById(R.id.date_item);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image_item);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //先判断是否为空
        if (list.get(position) != null) {
            //开始填充数据
            viewHolder.date.setText(list.get(position).get("date") + "");
            viewHolder.text.setText(list.get(position).get("text") + "");
            displayImage.display(viewHolder.image, MyServer.PICTURE_URL + list.get(position).get("image"),180);
        } else {
            viewHolder.date.setText(" ");
            displayImage.display(viewHolder.image, " ");
        }
        return convertView;
    }

    private class ViewHolder {
        TextView date;
        ImageView image;
        TextView text;
    }
}

