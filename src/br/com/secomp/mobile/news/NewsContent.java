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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.com.secomp.mobile.R;
import br.com.secomp.mobile.R.string;
import android.content.Context;
import android.os.AsyncTask;

public class NewsContent {

	public NewsContent(Context context) {
		// TODO can't always update.
		NewsTask task = new NewsTask();
		task.execute(context.getString(R.string.url_rss));
	}

	// List used when building ArrayAdapter.
	private static List<NewsItem> ITEMS = new ArrayList<NewsItem>();
	// needs this?
	public static Map<String, NewsItem> ITEM_MAP = new HashMap<String, NewsItem>();

	public static List<NewsItem> getItems() {
		return ITEMS;
	}

	private class NewsTask extends AsyncTask<String, Void, List<NewsItem>> {

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 */
		@Override
		protected List<NewsItem> doInBackground(String... urls) {
			// TODO Auto-generated method stub
				SAXParser parser;
				try {
					NewsHandler handler = new NewsHandler();
					parser = SAXParserFactory.newInstance().newSAXParser();
					parser.parse(urls[0], handler);
					return handler.getItems();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			return null;
		}
		
		@Override
		protected void onPostExecute(List<NewsItem> result) {
			NewsContent.ITEMS = new ArrayList<NewsItem>(result);
			// need to send broadcast to update list.
		}

	}
}
