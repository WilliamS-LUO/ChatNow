package Client.Sign_up;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class SignUp {
    public  SignUp(){
        Stage signUp = new Stage();
        signUp.initModality(Modality.APPLICATION_MODAL);
        Parent root = new Parent() {};
        try {
            root = FXMLLoader.load(getClass().getResource("Sign_up.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        signUp.setScene(new Scene(root,325,400));
        signUp.show();
    }
}
