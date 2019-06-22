package Client.Sign_up;

import Client.Log_in.AlertBox;
import Sever.ConnectToDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class signupController {
    @FXML
    private Button signUpButton;
    @FXML
    private TextField userNameTextFiled;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private AnchorPane Pane;

    @FXML
    private void signUpCheck(){
        Stage signUp =  (Stage)Pane.getScene().getWindow();
        String username = userNameTextFiled.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (!("".equals(username))) {
            if (!("".equals(password) || "".equals(confirmPassword))) {
                if (password.equals(confirmPassword)) {
                    ConnectToDatabase connectToDatabase = new ConnectToDatabase();
                    //查询用户名是否存在
                    if (!connectToDatabase.checkAccountExist(username)) {
                        //注册成功
                        if (connectToDatabase.creatAccount(username, password)) {
                            AlertBox.display("提示", "注册成功！");
                            signUp.close();
                        }
                    } else {
                        AlertBox.display("提示", "用户名已存在！");
                    }
                } else {
                    AlertBox.display("提示", "两次密码输入不相同！");
                }
            } else {
                AlertBox.display("提示", "请输入密码！");
            }
        }else {
            AlertBox.display("提示", "请输入用户名！");
        }
    }
}
