package com.hth.photopuzzle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MyFragment extends Fragment {

	int mCurrentPage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/** Getting the arguments to the Bundle object */
		Bundle data = getArguments();

		/** Getting integer data of the key current_page from the bundle */
		mCurrentPage = data.getInt("current_page", 0);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.myfragment_layout, container, false);
		RelativeLayout themeback = (RelativeLayout) v
				.findViewById(R.id.themeback);
		ImageView iv = (ImageView) v.findViewById(R.id.ivtheme);
		if (mCurrentPage == 1) {
			iv.setImageResource(R.drawable.theme_1);
			themeback.setBackgroundColor(getActivity().getResources().getColor(
					R.color.grey));
		} else if (mCurrentPage == 2) {
			iv.setImageResource(R.drawable.theme_2);
			themeback.setBackgroundColor(getActivity().getResources().getColor(
					R.color.light_green));
		} else if (mCurrentPage == 3) {
			iv.setImageResource(R.drawable.theme_3);
			themeback.setBackgroundColor(getActivity().getResources().getColor(
					R.color.light_pink));
		} else if (mCurrentPage == 4) {
			iv.setImageResource(R.drawable.theme_4);
			themeback.setBackgroundColor(getActivity().getResources().getColor(
					R.color.background_4));
		} else if (mCurrentPage == 5) {
			iv.setImageResource(R.drawable.theme_5);
			themeback.setBackgroundColor(getActivity().getResources().getColor(
					R.color.background_5));
		}
		return v;
	}
}
