package GUI;

import java.io.IOException;
import java.sql.SQLException;

import Model.DatabaseCommand;
import Model.LoginModel;
import Model.ProfModel;
import Model.ServerRequest;
import Model.UserTypes;
import Networking.Networker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController {
	
	@FXML
	TextField idField;
	
	@FXML
	PasswordField passwordField;
	
	@FXML
	Button login;
	
	String ID;
	String password;
	UserTypes type;
	LoginModel model;
	Networker networker;
	
	
	@FXML
	private void initialize() throws ClassNotFoundException, SQLException{
		model = new LoginModel();
		networker = model.getNetworker();
		idField.setText("Ferrer");
		passwordField.setText("ILoveRobotics");
	}
	
	@FXML
	private void login() throws SQLException, IOException{
		UserTypes user = checkUserType();
		if (user == UserTypes.PROFESSOR){
			startProfView();
		}
		else if ( user == UserTypes.ADMIN){
			startAdminView();
		}
		else if (user == UserTypes.STUDENT){
			startStudentView();
		}
		else{
			sendError(); 
		}
	}
	
	private void showNewStage(String FXMLFile, UserTypes type) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLFile));
		Parent home_page_parent = (Parent)loader.load();
		
		Scene home_page_scene = new Scene(home_page_parent);
		Stage app_stage = (Stage) login.getScene().getWindow();
		app_stage.setScene(home_page_scene);
		app_stage.show();
		ProfModel controller = (ProfModel)loader.getController();
		controller.setUser(ID);
	}
	
	private void startAdminView() throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminUI.fxml"));
		Parent home_page_parent = (Parent)loader.load();
		
		Scene home_page_scene = new Scene(home_page_parent);
		Stage app_stage = (Stage) login.getScene().getWindow();
		app_stage.setScene(home_page_scene);
		app_stage.show();
		AdminController controller = (AdminController)loader.getController();
		controller.setUser(ID);
		controller.setModel(model);
	}
	
	private void startProfView() throws IOException, SQLException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfessorUI.fxml"));
		Parent home_page_parent = (Parent)loader.load();
		ProfessorController controller = (ProfessorController)loader.getController();
		controller.setUser(idField.getText());
		Scene home_page_scene = new Scene(home_page_parent);
		Stage app_stage = (Stage) login.getScene().getWindow();
		app_stage.setScene(home_page_scene);
		app_stage.show();
		//controller.setNetworker(networker);
		System.out.println("Sent networker to ProfessorController...");
	}
	
	private void startStudentView() throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentUI.fxml"));
		Parent home_page_parent = (Parent)loader.load();
		
		Scene home_page_scene = new Scene(home_page_parent);
		Stage app_stage = (Stage) login.getScene().getWindow();
		app_stage.setScene(home_page_scene);
		app_stage.show();
		StudentController controller = (StudentController)loader.getController();
		controller.setUser(ID);
		controller.setNetworker(networker);
		System.out.println("Sent networker to StudentController...");
	}
	
	private void sendError(){
		errorWindow();
	}
	
	private UserTypes checkUserType() throws SQLException{
		System.out.println("in checkUserType");
		ID = idField.getText();
		password = passwordField.getText();
		//type = model.getDatabase().getUserType(ID, password);
		DatabaseCommand cmd = DatabaseCommand.GET_USER_TYPE;
		String[] args = {ID, password};
		ServerRequest request = new ServerRequest(cmd, args);
		type = (UserTypes) networker.sendServerRequest(request).getResult();
		System.out.println("The type is " + type.toString());
		return type;
	}
	
	@FXML
	public void errorWindow(){
		Stage newStage = new Stage();
		VBox root = new VBox();
		Label nameField = new Label("\n     Invalid username/password.     \n ");
		root.getChildren().addAll(nameField);
		Scene stageScene = new Scene(root);
		VBox.setVgrow(root, Priority.ALWAYS);
		newStage.setScene(stageScene);
		newStage.show();
		newStage.requestFocus();
		newStage.setTitle("ERROR");
	}

}
