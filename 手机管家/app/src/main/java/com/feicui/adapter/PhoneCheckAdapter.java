package com.feicui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicui.activity.R;
import com.feicui.base.MyBaseAdapter;
import com.feicui.bean.PhoneInfo;

public class PhoneCheckAdapter extends MyBaseAdapter<PhoneInfo> {

	public PhoneCheckAdapter(Context context) {
		super(context);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.item_lv_phonemager, null);
		}
		PhoneInfo phoneInfo = getItem(position);

		ImageView icon = (ImageView) convertView
				.findViewById(R.id.iv_phonemgr_icon);
		TextView title = (TextView) convertView
				.findViewById(R.id.tv_phonemgr_title);
		TextView text = (TextView) convertView
				.findViewById(R.id.tv_phonemgr_text);

		icon.setImageDrawable(phoneInfo.getIcon());
		title.setText(phoneInfo.getTitle());
		text.setText(phoneInfo.getText());

		// 给每个图加不同背�?(无实际作�?)
		switch (position % 3) {
		case 0:
			icon.setBackgroundResource(R.drawable.notification_information_progress_green);
			break;
		case 1:
			icon.setBackgroundResource(R.drawable.notification_information_progress_red);
			break;
		case 2:
		default:
			icon.setBackgroundResource(R.drawable.notification_information_progress_yellow);
			break;
		}
		return convertView;
	}
}
