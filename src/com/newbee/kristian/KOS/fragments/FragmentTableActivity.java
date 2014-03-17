package com.newbee.kristian.KOS.fragments;

import com.newbee.kristian.KOS.R;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentTableActivity extends Fragment {
	@SuppressWarnings("unused")
	private TextView text;

	public FragmentTableActivity() {
		// TODO Auto-generated constructor stub

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = LayoutInflater.from(getActivity()).inflate(R.layout.activity_fragment,
				null);
//		text = (TextView) v.findViewById(R.id.text);
//		if (getArguments() != null) {
//			//
//			try {
//				String value = getArguments().getString("key");
//				text.setText("Current Tab is: " + value);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	//
}
