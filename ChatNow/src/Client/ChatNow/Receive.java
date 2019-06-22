package Client.ChatNow;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Receive implements Runnable {
    private TextArea receiveMessage;
    private Label onlineLabel;
    private Socket socket;
    private DataInputStream dataInputStream;
    private ObservableList<String> clientName;
    private boolean isRunning = true;

    public Receive(Socket socket, TextArea receiveMessage, ObservableList<String> clientName,Label onlineLbel) {
        this.socket = socket;
        this.receiveMessage = receiveMessage;
        this.clientName = clientName;
        this.onlineLabel = onlineLbel;
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *接收消息
     */
    public String receive() {
        String message = "";
        try {
            message = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            release();
        }
        return message;
    }

    @Override
    public void run() {
        while (isRunning) {
            String message = receive();
            //判断消息类型
            if (message.startsWith("ONLINE")) {
                Platform.runLater(() -> {
                    //清空ObservableList
                    clientName.clear();
                    clientName.add("ALL");
                });
                int start = 0;
                int end = 0;
                //提取用户名，以空格为分隔
                while (message.indexOf(" ", start) != -1) {
                    start = message.indexOf(" ", start);
                    end = message.indexOf(" ", start + 1);
                    //如果没有空格，则直接提取用户名
                    if (end == -1) {
                        String name = message.substring(start + 1);
                        Platform.runLater(() -> { clientName.add(name); });
                        break;
                    }
                    else {
                        String name = message.substring(start + 1, end);
                        Platform.runLater(() -> { clientName.add(name); });
                    }
                    start = end;
                }
                Platform.runLater(() -> onlineLabel.setText("在线：" + (clientName.size() - 1) + " 人"));
                continue;
            }
            //在接收消息框显示
            receiveMessage.appendText(message + "\n");
        }
    }

    /**
     * 退出后，关闭资源
     */
    private void release(){
        this.isRunning = false;
        Utils.close(dataInputStream,socket);
    }
}
