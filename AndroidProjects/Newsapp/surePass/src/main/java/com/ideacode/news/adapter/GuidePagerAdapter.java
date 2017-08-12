package com.ideacode.news.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.ideacode.news.R;
import com.ideacode.news.ui.MainActivity;

public class GuidePagerAdapter extends PagerAdapter {

	// �����㈠��琛�
	private final List<View> views;
	private final Activity activity;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	public GuidePagerAdapter(List<View> views, Activity activity) {
		this.views = views;
		this.activity = activity;
	}

	// ���姣�arg1浣�缃����������
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(views.get(arg1));
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	// ��峰��褰���������㈡��
	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}

	// ���濮����arg1浣�缃����������
	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(views.get(arg1), 0);
		if (arg1 == views.size() - 1) {
			ImageView mStartWeiboImageButton = (ImageView) arg0.findViewById(R.id.iv_start_weibo);
			mStartWeiboImageButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 璁剧疆宸茬��寮�瀵�
					setGuided();
					goHome();
				}

			});
		}
		return views.get(arg1);
	}

	private void goHome() {
		// 璺宠浆
		Intent intent = new Intent(activity, MainActivity.class);
		activity.startActivity(intent);
		activity.finish();
	}

	/**
	 * 
	 * method desc锛�璁剧疆宸茬��寮�瀵艰��浜�锛�涓�娆″����ㄤ����ㄥ��娆″��瀵�
	 */
	private void setGuided() {
		SharedPreferences preferences = activity.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		// 瀛���ユ�版��
		editor.putBoolean("isFirstIn", false);
		// ���浜や慨���
		editor.commit();
	}

	// ��ゆ����������卞�硅薄������������
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

}
