package Client.Log_in;
import Client.ChatNow.ChatNow;
import Client.ChatNow.ChatNowController;
import Client.Sign_up.*;
import Sever.ConnectToDatabase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;

public class loginController {
    @FXML
    private Button logInButton;
    @FXML
    private TextField userNameTextFiled;
    @FXML
    private PasswordField passwordField;
    @FXML
    private AnchorPane Pane;

    @FXML
    private void logInButtonClicked(){
        Stage login = (Stage)Pane.getScene().getWindow();
        String username = userNameTextFiled.getText();
        String password = passwordField.getText();
        if(checkAccount(username,password)){
            login.close();
            new ChatNow(username);
        }
    }

    @FXML
    private void createAccountLabelClicked(){
        new SignUp();
    }

    /**
     *验证是否能够登录的方法
     */
    private boolean checkAccount(String username,String password){
        boolean flag = false;
        //判断用户名输入框是否为空
        if(!("".equals(username))){
            //判断密码输入框是否为空
            if(!("".equals(password))){
                ConnectToDatabase connectToDatabase = new ConnectToDatabase();
                //判断用户名是否存在
                if(connectToDatabase.checkAccountExist(username)){
                    //验证用户名和密码
                    if(connectToDatabase.loginCheck(username,password)){
                        //验证用户是否已登录
                        if (connectToDatabase.checkState(username)){
                            //若为真，则已登录，
                            AlertBox.display("提示","用户已在别处登录！");
                        }
                        else {
                            //修改数据库登录状态
                            connectToDatabase.updateState(username,1);
                            flag = true;
                        }
                    }else {
                        AlertBox.display("提示","密码错误！");
                    }
                }else {
                    AlertBox.display("提示","用户不存在！");
                }
            }else {
                AlertBox.display("提示","请输入密码！");
            }
        }else {
            AlertBox.display("提示","请输入用户名！");
        }
        return flag;
    }
}
