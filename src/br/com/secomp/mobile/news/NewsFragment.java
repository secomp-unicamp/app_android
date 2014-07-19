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

package br.com.secomp.mobile.news;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import br.com.secomp.mobile.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class NewsFragment extends ListFragment {

	private OnFragmentInteractionListener mListener;
	
	private static NewsFragment INSTANCE = new NewsFragment();
	
	public static NewsFragment getInstance() {
		return NewsFragment.INSTANCE;
	}

	private ArrayList<NewsItem> newsList = null;
	
	public void setNewsList(ArrayList<NewsItem> newsList) {
		this.newsList = newsList;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.mListener = (OnFragmentInteractionListener) activity;
			this.update();
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null)
			return null;

		if (this.newsList != null)
			setListAdapter(new ArrayAdapter<NewsItem>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, this.newsList));
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// TODO save list to disk;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		this.mListener = null;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if (this.mListener != null && this.newsList != null) {
			// Notify the active callbacks interface (the activity, if the
			// fragment is attached to one) that an item has been selected.
			this.mListener.onFragmentInteraction(this.newsList.get(position).url);
		}
	}

	private void update() {
		NewsTask task = new NewsTask();
		task.execute(getString(R.string.url_rss));
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(String url);
	}

	private class NewsTask extends AsyncTask<String, Void, List<NewsItem>> {

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected List<NewsItem> doInBackground(String... urls) {
			Log.d("secomp", "started");
			try {
				NewsHandler handler = new NewsHandler();
				SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
				parser.parse(urls[0], handler);
				return handler.getItems();
			} catch (IOException e) {
				Log.d("secomp", "no connection");
			} // catching ParserConfigurationException and SAXException
			catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<NewsItem> result) {
			if (result != null) {
				NewsFragment.getInstance().setNewsList((ArrayList<NewsItem>) result);
				// send broadcast!
				NewsFragment.getInstance().setListAdapter(new ArrayAdapter<NewsItem>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, NewsFragment.getInstance().newsList));
				Log.d("secomp", "done");
			}
		}

	}
}
