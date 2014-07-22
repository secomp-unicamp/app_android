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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import br.com.secomp.mobile.MainActivity;
import br.com.secomp.mobile.OnFragmentInteractionListener;
import br.com.secomp.mobile.R;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

	private static final String FILE_NAME = "newsFile";
	
	private static final String UPDATE_LAST = "UPDATE_LAST";
	private static final long UPDATE_INTERVAL = 21600000; // 6 hours

	private long lastUpdate;

	private OnFragmentInteractionListener mListener;

	private ArrayList<NewsItem> newsList = null;

	public void setNewsList(ArrayList<NewsItem> newsList) {
		this.setListAdapter(new ArrayAdapter<NewsItem>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, newsList));
		this.newsList = newsList;
		
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(getActivity().openFileOutput(FILE_NAME, Context.MODE_PRIVATE));
			outputStream.writeObject(this.newsList);
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.mListener = (OnFragmentInteractionListener) activity;
			ObjectInputStream inputStream = new ObjectInputStream(getActivity().openFileInput(FILE_NAME));
			this.newsList = (ArrayList<NewsItem>) inputStream.readObject();
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.lastUpdate = PreferenceManager.getDefaultSharedPreferences(getActivity()).getLong(UPDATE_LAST, 0);
		this.update();
		((MainActivity) activity).onSectionAttached(0); // weird...
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
		if (System.currentTimeMillis() - lastUpdate > UPDATE_INTERVAL) {
			Log.d("secomp", "updating");
			NewsTask task = new NewsTask();
			task.execute(getString(R.string.url_rss));
			lastUpdate = System.currentTimeMillis();
		} else
			Log.d("secomp", "did not update");
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
				setNewsList((ArrayList<NewsItem>) result);

				PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putLong(UPDATE_LAST, lastUpdate).commit();
				// send broadcast!
				Log.d("secomp", "done");
			}
		}

	}
}
