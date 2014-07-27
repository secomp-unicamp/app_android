/*
 * Secomp Mobile. This software is intended for attendants at the Semana de Computação that takes place at UNICAMP, Brazil.
 * Copyright (C) 2014  Edson Duarte (edsonduarte1990@gmail.com)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package br.com.secomp.mobile.sponsors;

import br.com.secomp.mobile.MainActivity;
import br.com.secomp.mobile.R;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link SponsorsFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link SponsorsFragment#newInstance} factory method to create an instance of
 * this fragment.
 *
 */
public class SponsorsFragment extends Fragment {

	//private OnFragmentInteractionListener mListener; // will be necessary if there's a need to open link from clicking on images.


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_sponsors, container, false);
		view.setBackgroundColor(getActivity().getResources().getColor(android.R.color.white));
		
		ScrollView scrollView = new ScrollView(getActivity());
		
		LinearLayout sponsorsLayout = new LinearLayout(getActivity());
		sponsorsLayout.setOrientation(LinearLayout.VERTICAL);
		sponsorsLayout.setGravity(Gravity.CENTER);
		
		TypedArray groupsArray = getActivity().getResources().obtainTypedArray(R.array.sponsors_groups);
		for (int i = 0; i < groupsArray.length(); i++) {
			TextView groupView = new TextView(getActivity());
			groupView.setText(getActivity().getResources().getStringArray(R.array.sponsors_groups_names)[i]);
			groupView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
			groupView.setTypeface(Typeface.DEFAULT_BOLD);
			groupView.setGravity(Gravity.CENTER);
			groupView.setPadding(0, 20, 0, 5);
			sponsorsLayout.addView(groupView);
			
			LinearLayout groupLayout = new LinearLayout(getActivity());
			groupLayout.setGravity(Gravity.CENTER);
			TypedArray sponsorsArray = getActivity().getResources().obtainTypedArray(groupsArray.getResourceId(i, -1));
			for (int j = 0; j < sponsorsArray.length(); j++) {
				ImageView sponsorView = new ImageView(getActivity());
				sponsorView.setImageResource(sponsorsArray.getResourceId(j, -1));
				sponsorView.setPadding(5, 5, 5, 5);
				groupLayout.addView(sponsorView);
			}
			sponsorsArray.recycle();
			sponsorsLayout.addView(groupLayout);
		}
		
		groupsArray.recycle();
		scrollView.addView(sponsorsLayout);
		view.addView(scrollView);
		
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		try {
//			mListener = (OnFragmentInteractionListener) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
//		}
		((MainActivity) activity).onSectionAttached(2); // weird...
	}

	@Override
	public void onDetach() {
		super.onDetach();
//		mListener = null;
	}
}
