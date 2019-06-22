package Client.ChatNow;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ChatNow{

      public ChatNow(String username){
          try {
              URL location = getClass().getResource("ChatNow.fxml");
              FXMLLoader fxmlLoader = new FXMLLoader();
              fxmlLoader.setLocation(location);
              Parent root = fxmlLoader.load();
              Stage ChatNow = new Stage();
              ChatNow.setTitle("ChatNow 用户：" + username);
              Scene scene = new Scene(root, 750, 500);
              ChatNow.setScene(scene);
              //设置窗口关闭按钮的处理方法
              ChatNow.setOnCloseRequest(event -> { ChatNowController.getInstance().close(); });
              //获取Controller的实例对象
              ChatNowController controller = fxmlLoader.getController();
              controller.start(username);
              ChatNow.show();
          } catch (IOException e) {
              e.printStackTrace();
          }
    }
}
