package com.crud;

import com.crud.ui.UserForm;

import totalcross.sys.Settings;
import totalcross.ui.MainWindow;
import totalcross.ui.UIColors;
import totalcross.ui.gfx.Color;

public class Main extends MainWindow{
	
	public Main(){
		super("CRUD", VERTICAL_GRADIENT);
	}
	
	public void initUI(){
		UIColors.controlsBack = Color.BRIGHT;
		setBackColor(Color.BRIGHT);
		setUIStyle(Settings.Android);
		swap(new UserForm());
	}

}
