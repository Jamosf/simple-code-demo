//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.umeng.community.share.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.umeng.comm.core.utils.ResFinder;
import com.umeng.socialize.bean.SnsPlatform;

import java.util.ArrayList;
import java.util.List;

public class PlatformAdapter extends Adapter<PlatformAdapter.PlatformViewHolder> {
    List<SnsPlatform> mPlatforms = new ArrayList();
    OnItemClickListener mClickListener;

    public PlatformAdapter(List<SnsPlatform> platforms) {
        this.mPlatforms = platforms;
    }

    public int getItemCount() {
        return this.mPlatforms.size();
    }

    public SnsPlatform getItem(int position) {
        return (SnsPlatform) this.mPlatforms.get(position);
    }

    public void onBindViewHolder(PlatformViewHolder viewHolder, final int position) {
        SnsPlatform platform = (SnsPlatform) this.mPlatforms.get(position);
        viewHolder.mImageView.setImageResource(platform.mIcon);
        viewHolder.mTextView.setText(platform.mShowWord);
        viewHolder.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PlatformAdapter.this.mClickListener != null) {
                    PlatformAdapter.this.mClickListener.onItemClick((AdapterView) null, v, position, 0L);
                }

            }
        });
    }

    public PlatformViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(ResFinder.getLayout("umeng_socialize_shareboard_item"), parent, false);
        return new PlatformViewHolder(itemView);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }

    static class PlatformViewHolder extends ViewHolder {
        ImageView mImageView;
        TextView mTextView;

        public PlatformViewHolder(View itemView) {
            super(itemView);
            this.mImageView = (ImageView) itemView.findViewById(ResFinder.getId("umeng_socialize_shareboard_image"));
            this.mTextView = (TextView) itemView.findViewById(ResFinder.getId("umeng_socialize_shareboard_pltform_name"));
        }
    }
}
