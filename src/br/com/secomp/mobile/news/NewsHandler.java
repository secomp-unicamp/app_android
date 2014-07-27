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

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NewsHandler extends DefaultHandler {
	private List<NewsItem> items;

	private NewsItem currentItem;

	private boolean parsingTitle;
	private StringBuffer titleBuffer;

	private boolean parsingLink;
	private StringBuffer linkBuffer;

	public NewsHandler() {
		items = new ArrayList<NewsItem>();
	}

	public List<NewsItem> getItems() {
		return this.items;
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		if (name.equals("item")) {
			currentItem = new NewsItem();
		} else if (name.equals("title")) {
			parsingTitle = true;
			titleBuffer = new StringBuffer();
		} else if (name.equals("link")) {
			parsingLink = true;
			linkBuffer = new StringBuffer();
		}
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		if (name.equals("item")) {
			items.add(currentItem);
			currentItem = null;
		} else if (name.equals("title")) {
			parsingTitle = false;
			if (currentItem != null)
				currentItem.setTitle(titleBuffer.toString());
		} else if (name.equals("link")) {
			parsingLink = false;
			if (currentItem != null)
				currentItem.setURL(linkBuffer.toString());
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (currentItem != null) {
			if (parsingTitle)
				titleBuffer.append(new String(ch, start, length));
			else if (parsingLink)
				linkBuffer.append(new String(ch, start, length));
		}
	}

}
