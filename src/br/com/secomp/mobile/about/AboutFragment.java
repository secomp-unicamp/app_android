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

package br.com.secomp.mobile.about;

import br.com.secomp.mobile.MainActivity;
import br.com.secomp.mobile.OnFragmentInteractionListener;
import br.com.secomp.mobile.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link AboutFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link AboutFragment#newInstance} factory method to create an instance of
 * this fragment.
 *
 */
public class AboutFragment extends Fragment {

	private OnFragmentInteractionListener mListener;


	public AboutFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_about, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		((TextView) getActivity().findViewById(R.id.versionTextView)).setText(getActivity().getString(R.string.app_name) + " " + getActivity().getString(R.string.app_version));

		((View) getActivity().findViewById(R.id.secompImageView)).setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				onImageViewClicked(getString(R.string.url_website));
			}
		});

		((View) getActivity().findViewById(R.id.facebookImageView)).setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				try {
					getActivity().getPackageManager().getPackageInfo("com.facebook.katana", 0);
					new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_facebook_apk)));
				} catch (Exception e) {
					onImageViewClicked(getString(R.string.url_facebook));
				}
			}
		});

		((View) getActivity().findViewById(R.id.twitterImageView)).setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				try {
					getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
					new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_twitter)));
				} catch (Exception e) {
					onImageViewClicked(getString(R.string.url_twitter));
				}
			}
		});

		((View) getActivity().findViewById(R.id.githubImageView)).setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				onImageViewClicked(getString(R.string.url_github));
			}
		});
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
		}
		((MainActivity) activity).onSectionAttached(4); // weird...
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public void onImageViewClicked(String url) {
		mListener.onFragmentInteraction(url);
	}

}
