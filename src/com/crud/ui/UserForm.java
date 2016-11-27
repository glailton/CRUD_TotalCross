package com.crud.ui;

import com.crud.business.Constants;
import com.crud.dao.UserDAO;
import com.crud.model.User;

import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.Grid;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class UserForm extends Container{
	private static final int 	INSERT_BTN_ID  	= 	100;
	private static final int	UPDATE_BTN_ID	= 	101;
	private static final int	DELETE_BTN_ID	=	102;
	private final UserDAO 		userDao;
	private Grid 				grid;
	
	public UserForm(){
		userDao = new UserDAO();
	}
	
	public void initUI(){
		Button insertButton = new Button("New");
		Button updateButton = new Button("Update");
		Button deleteButton = new Button("Delete");
		
		insertButton.appId = INSERT_BTN_ID;
		updateButton.appId = UPDATE_BTN_ID;
		deleteButton.appId = DELETE_BTN_ID;
		
		add(deleteButton, RIGHT - 2, TOP + 2, PREFERRED + 5, PREFERRED + 5);
		add(updateButton, BEFORE - 2, SAME, SAME, SAME);
		add(insertButton, BEFORE - 2, SAME, SAME, SAME);
		
		grid = new Grid(new String[] {"ID", "Name", "Age"}, 
				new int[] { -10, -70, -10}, 
				new int [] {CENTER, LEFT, CENTER},
				false);
		
		add(grid, LEFT, AFTER + 2, FILL - 4, FILL - 4 );
		
		grid.setItems(userDao.listAll());
		
	}
	
	public void onEvent(Event event){
		
		switch (event.type) {
		case ControlEvent.PRESSED:
			switch (((Control) event.target).appId) {
				case INSERT_BTN_ID:
					new UserEditForm(userDao, new User(), Constants.INSERT).popupNonBlocking();
					break;
	
				case UPDATE_BTN_ID:
					if(isSelected()){
						new UserEditForm(userDao, getSelected(), Constants.UPDATE).popupNonBlocking();
					}
					break;
				
				case DELETE_BTN_ID:
					if(isSelected()){
						userDao.delete(getSelected());
						grid.setItems(userDao.listAll());
						grid.setSelectedIndex(-1);
					}
					break;
				}
				break;

		case ControlEvent.WINDOW_CLOSED:
			if(event.target instanceof UserEditForm){
				grid.setItems(userDao.listAll());
				grid.setSelectedIndex(-1);
			}
			break;
		}
	}

	private User getSelected() {
		User user = new User();
		String[] col = grid.getSelectedItem();
		try {
			user.setId(Convert.toInt(col[0]));
			user.setName(col[1]);
			user.setAge(Convert.toInt(col[2]));
		} catch (InvalidNumberException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	private boolean isSelected() {
		boolean selectedItem = grid.getSelectedIndex() >= 0;
		if(!selectedItem){
			new MessageBox("Error", "Select a Item!").popup();
		}
		return selectedItem;
	}
}
