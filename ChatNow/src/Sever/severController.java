package Sever;

import Client.Log_in.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.ServerSocket;

public class severController {

    private ServerSocket severSocket;
    private SeverThread severThread;
    @FXML
    private Button sendButton;
    @FXML
    private Button startListenButton;
    @FXML
    private TextArea receiveMessage;
    @FXML
    private Label onlineLabel;
    @FXML
    private ListView onlineMember;
    private ObservableList<String> clientName = FXCollections.observableArrayList();

    @FXML
    void startListenButtonFirstClicked(){
        try {
            //服务器端SeverSocket
            severSocket = new ServerSocket(8080);
            //将ListView和ObservableList绑定
            onlineMember.setItems(clientName);
            onlineLabel.setText("在线：" + clientName.size() +" 人");

            //服务器端监听的线程
            severThread = new SeverThread(severSocket,receiveMessage,clientName);
            new Thread(severThread).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startListenButton.setOnAction(event -> startListenButtonClicked());
    }

    private void  startListenButtonClicked(){
        AlertBox.display("提示","已开始监听！");
    }
}
