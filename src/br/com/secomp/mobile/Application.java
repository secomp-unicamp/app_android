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

package br.com.secomp.mobile;

import br.com.secomp.mobile.sponsors.SponsorsFragment;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class Application extends android.app.Application {
	
	private static String FIRST_TIME = "FIRST_TIME";
	
	public Application() {
		super();
		
		if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(FIRST_TIME, true)) {
			PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(FIRST_TIME, false);
		}
	}
}
