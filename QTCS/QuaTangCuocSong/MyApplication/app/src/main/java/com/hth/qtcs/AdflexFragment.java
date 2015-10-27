package com.hth.qtcs;

import com.hth.utils.UIUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class AdflexFragment extends Fragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		LinearLayout linearLayout = UIUtils.BuildGetMoreApps(getActivity());
		linearLayout.setBackgroundColor(Color.WHITE);
		return linearLayout;
	}

}
