package Sever;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SeverThread implements Runnable{
    private ServerSocket serverSocket = null;
    private ConnectToDatabase connectToDatabase = null;
    private TextArea receiveMessage;
    private ObservableList<String> clientName = null;
    private ArrayList<SocketForSever> all = new ArrayList<SocketForSever>();

    public SeverThread(ServerSocket serverSocket,TextArea receiveMessage,ObservableList<String> clientName){
        this.serverSocket = serverSocket;
        this.receiveMessage = receiveMessage;
        this.clientName = clientName;
    }
    @Override
    public void run() {
        try {
            //这个线程是用来等待客户端连接
            while (true) {
                //每循环一次就会有一个客户端已经连接成功，并会等待下一个客户端接入
                Socket clientSocket = serverSocket.accept();
                //获取用户名
                String username = new DataInputStream(clientSocket.getInputStream()).readUTF();
                receiveMessage.appendText((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()).toString()
                                            + "\n" + "连接成功！\n");
                //创建一个客户线程
                SocketForSever socketForSever = new SocketForSever(clientSocket,username,receiveMessage,all,clientName);
                //在ArrayList里添加该客户线程
                all.add(socketForSever);
                new Thread(socketForSever).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
