package Client.ChatNow;

import Client.Log_in.AlertBox;
import Sever.ConnectToDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatNowController {
    @FXML
    private ListView<String> onlineMember;
    @FXML
    private Label onlineLabel;
    @FXML
    private Button sendButton;
    @FXML
    private Button clearButton;
    @FXML
    private TextArea sendMessage;
    @FXML
    private TextArea receiveMessage;
    @FXML
    private MenuItem exit;
    @FXML
    private AnchorPane anchorPane;
    private ObservableList<String> clientName = FXCollections.observableArrayList();

    private String username;
    private static DataOutputStream dataOutputStream = null;

    private static Socket socket = null;
    private static ChatNowController instance;

    public ChatNowController() {
        instance = this;
    }

    public static ChatNowController getInstance() {
        return instance;
    }

    public void start(String username) {
        try {
            this.username = username;
            //连接Socket
            socket = new Socket("localhost", 8080);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将ListView和ObservableList绑定
        onlineMember.setItems(clientName);

        //设置"发送文本框"的按键监听
        sendMessage.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            //若按键为回车，则调用发送消息方法
            if (key.getCode().equals(KeyCode.ENTER)) {
                sendButtonClicked();
                key.consume();
            }
        });
        //创建并运行新的接收线程
        new Thread(new Receive(socket, receiveMessage, clientName, onlineLabel)).start();
    }

    /**
     * 发送消息方法
     */
    @FXML
    private void sendButtonClicked() {
        try {
            String message = sendMessage.getText();
            //检查消息格式
            if (checkMessage(message)) {
                //向服务器端发送消息
                dataOutputStream.writeUTF(message + "\n");
                //清空发送消息框
                sendMessage.clear();
                //本客户端显示已发送消息
                receiveMessage.appendText((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()).toString()
                        + "\n" + username +"：" + message.substring(message.indexOf(":") + 1) + "\n\n");
            } else {
                AlertBox.display("提示", "消息格式错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.close(dataOutputStream, socket);
        }
    }

    /**
     * 设置在线列表的OnMousePressed方法
     * 方便用户发送消息
     */
    @FXML
    private void listviewClicked() {
        if (onlineMember.getSelectionModel().getSelectedItem() != null) {
            sendMessage.setText("@" + onlineMember.getSelectionModel().getSelectedItem() + ":");
        }
    }

    /**
     * 接收消息框清空方法
     */
    @FXML
    private void clearButtonClicked() {
        receiveMessage.clear();
    }

    /**
     * 检查消息格式的方法
     */
    private boolean checkMessage(String message) {
        boolean flag;
        if ("".equals(message)) {
            flag = false;
        } else {
            if (message.startsWith("@ALL:")) {
                if (message.substring(message.indexOf(":")).equals(":")) {
                    flag = false;
                } else {
                    flag = true;
                }
            } else if (message.indexOf("@") == 0 && (message.indexOf(":") >= 2)) {
                if (message.substring(message.indexOf(":")).equals(":")) {
                    flag = false;
                } else {
                    flag = true;
                }
            } else {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 退出方法
     */
    @FXML
    public void close() {
        ConnectToDatabase connectToDatabase = new ConnectToDatabase();
        connectToDatabase.updateState(username, 0);
        System.exit(0);
    }
}


