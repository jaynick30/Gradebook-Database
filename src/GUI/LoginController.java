package GUI;

import java.sql.SQLException;

import Model.Model;
import Model.UserTypes;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {
	
	@FXML
	TextField idField;
	
	@FXML
	TextField passwordField;
	
	@FXML
	Button login;
	
	String ID;
	String password;
	Enum type;
	Model model;
	
	@FXML
	private void initialize(){
		model = new Model();
	}
	
	@FXML
	private void login(){
		Enum user = checkUserType();
		if (user == UserTypes.PROFESSOR){
			startProfView();
		}
		else if (user == UserTypes.STUDENT){
			startStudentView();
		}
		else{
			sendError();
		}
	}
	
	private void startProfView(){
		//TODO
	}
	
	private void startStudentView(){
		//TODO
	}
	
	private void sendError(){
		//TODO
	}
	
	private Enum checkUserType(){
		//TODO:
		ID = idField.getText();
		password = passwordField.getText();
		type = model.getDatabase().getUserType(ID, password);
		return type;
	}
}
