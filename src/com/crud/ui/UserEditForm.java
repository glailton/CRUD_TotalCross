package com.crud.ui;

import com.crud.business.Constants;
import com.crud.dao.UserDAO;
import com.crud.model.User;

import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.ui.Button;
import totalcross.ui.Control;
import totalcross.ui.Edit;
import totalcross.ui.Label;
import totalcross.ui.Window;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class UserEditForm extends Window{
	
	private UserDAO userDao;
	private Edit edId, edName, edAge;
	private Button btSave, btCancel;
	
	private static final int 	SAVE_BTN_ID	  =	100;
	private static final int 	CANCEL_BTN_ID =	101;
	
	private String age = "";

	public UserEditForm(UserDAO userDao, User user, int editMode) {
		this.userDao = userDao;
		this.appObj = user;
		this.appId = editMode;
		
	}
	
	protected void postPopup(){
		super.postPopup();
		initUI();
	}
	
	public void initUI(){
		 
		if (((User) appObj).getAge() <= 0){
			age = "";
		}else{
			age = Convert.toString(((User) appObj).getAge());
		}
		
		btSave = new Button("Save");
		btCancel = new Button("Cancel");
		edId = new Edit("999999");
		edName = new Edit();
		edAge = new Edit("99999");
		edAge.setMode(edAge.NORMAL, true);
		edAge.setValidChars("0123456789");
		
		btSave.appId = SAVE_BTN_ID;
		btCancel.appId = CANCEL_BTN_ID;
		edId.setEditable(false);
		edId.setText(Convert.toString(((User) appObj).getId()));
		edName.setText(((User) appObj).getName());
		edAge.setText(age);
		
		add(btCancel, RIGHT - 2, TOP + 2, PREFERRED + 2, PREFERRED + 2 );
		add(btSave, BEFORE - 2, SAME, SAME, SAME );
		/*add(new Label("ID"), LEFT + 2, AFTER + 5);
		add(edId, SAME, AFTER + 2);*/
		add(new Label("Name"), LEFT + 2, AFTER + 5);
		add(edName, SAME, AFTER + 2 );
		add(new Label("Age"), LEFT + 2, AFTER + 5);
		add(edAge, SAME, AFTER + 2 );
		
	}
	
	public void onEvent(Event event){
		
		switch (event.type) {
			case ControlEvent.PRESSED:
				switch (((Control) event.target).appId) {
					case SAVE_BTN_ID:
						if(validateDate()){
							switch (appId) {
								case Constants.INSERT:
									userDao.insert((User) appObj);
									break;
								case Constants.UPDATE:
									userDao.update((User) appObj);
									break;
							}
							unpop();
						}
						break;
					
					case CANCEL_BTN_ID:
						unpop();
						break;
				}
				break;

		}
	}

	private boolean validateDate() {
		
		if(!(this.edName.getLength() > 0)){
			new MessageBox("ERROR!", "Invalid Name").popup();
			return false;
		}
		
		try {
			if(!(Convert.toInt(this.edAge.getText()) > 0)){
				new MessageBox("ERROR!", "Invalid Age").popup();
				return false;
			}
		} catch (InvalidNumberException e1) {
			e1.printStackTrace();
		}
		
		try {
			((User) appObj).setId(Convert.toInt(this.edId.getText()));
			((User) appObj).setName(this.edName.getText());
			((User) appObj).setAge(Convert.toInt(this.edAge.getText()));
		} catch (InvalidNumberException e) {
			e.printStackTrace();
		}
		
		return true;
	}
}
