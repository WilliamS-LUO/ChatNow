package Sever;

import Client.ChatNow.Utils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author WilliamS
 */
public class SocketForSever implements Runnable{
    private Socket clientSocket;
    private TextArea receiveMessage;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private String username;
    private ObservableList<String> clientName;
    private ArrayList<SocketForSever> all = new ArrayList<SocketForSever>();
    private boolean isRunning = true;

    SocketForSever(Socket socket,String username,TextArea receiveMessage,ArrayList<SocketForSever> all,
                   ObservableList<String> clientName){
        this.clientSocket = socket;
        this.username = username;
        this.receiveMessage = receiveMessage;
        this.all = all;
        this.clientName = clientName;

        try {
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream= new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(SocketForSever other: all){
            if(this.username.equals(other.username)){
                continue;
            }
            other.send((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()).toString()
                        + "\n" + this.username + " 上线啦！\n");
        }
    }

    private String receive(){
        String message = "";
        try {
            message = dataInputStream.readUTF();
        } catch (Exception e) {
            release();
        }
        return message;
    }

    private void send(String message){
        try {
            dataOutputStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
            release();
        }
    }

    private void sendOthers(String message){
        boolean wetherSendAll = message.startsWith("@ALL:");
        if(!wetherSendAll){
            //若不为所有人的消息则执行下面的代码
            int nameEnd = message.indexOf(":");
            if (!(nameEnd == -1)) {
                //提取接收用户的用户名
                String name = message.substring(1, nameEnd);
                //提取消息
                message = message.substring(nameEnd + 1);
                //遍历所有在线用户，找到需要发送的用户
                for (SocketForSever other : all) {
                    if (name.equals(other.username)) {
                        other.send((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()).toString()
                                + "\n（私聊）" + this.username + "：" + message);
                    }
                }
                //服务器端显示信息
                receiveMessage.appendText((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()).toString()
                        + "\n" + this.username + " 私聊 " + name + "：" + message);
            }
        }
        else {
            int end = message.indexOf(":");
            //提取消息
            message = message.substring(end + 1);
            for(SocketForSever other: all){
                //若为自己则跳过
                if(other == this){ continue; }
                other.send((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()).toString()
                            + "\n（所有人）"  + this.username +"："+ message);
            }
            receiveMessage.appendText(this.username + "：" + message);
        }
    }
    @Override
    public void run() {
        while (isRunning){
            //设置在线用户列表
            setOnlineMember();
            sendOthers(receive());
        }
    }

    /**
     * 释放资源
     */
    private void release(){
        this.isRunning = false;
        Utils.close(dataInputStream,dataOutputStream,clientSocket);
        //在ArrayList中移除本线程
        all.remove(this);
        //移除后设置在线用户列表
        setOnlineMember();
        //向所有在线用户发送有用户下线的消息
        for(SocketForSever socketForSever: all){
            socketForSever.send((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()).toString()
                    + "\n" + this.username + " 退出聊天室！" + "\n\n");
        }
        receiveMessage.appendText((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()).toString()
                + "\n" + this.username + " 退出聊天室！" + "\n");
    }

    /**
     * 设置在线用户列表方法
     */
    private void setOnlineMember(){
        //用字符串保存在线用户，以ONLINE为字符串的开头来区分普通消息
        String online = "ONLINE";
        Platform.runLater(() -> clientName.clear());
        for (SocketForSever socketForSever: all){
            //以空格分隔用户名
            online = online + " "+ socketForSever.username;
            Platform.runLater(() -> clientName.add(socketForSever.username));
        }
        //向所有在线用户发送在线用户字符串
        for (SocketForSever socketForSever: all) {
            socketForSever.send(online);
        }
    }
}
