package com.hth.datmon;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Vibrator;
import android.widget.Toast;

import com.hth.data.ServiceProcess;
import com.hth.service.ConstData;
import com.hth.service.Conversation;


import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;


/**
 * Created by Lenovo on 12/1/2016.
 */
public class SignalRService extends Service {
    final String SERVER_HUB_CHAT = "RestAPI";
    String serverUrl = "http://restapi.quanngonngon.com:80";
    final String CLIENT_METHOD_BROADAST_MESSAGE = "newMessage";
    final String CLIENT_METHOD_BROADAST_MESSAGE_UPDATE_DESK = "DeskRequest";

    private HubConnection mHubConnection;
    private HubProxy mHubProxy;
    private Handler mHandler; // to display Toast message
    private final IBinder mBinder = new LocalBinder(); // Binder given to clients

    public SignalRService() {
        serverUrl = ServiceProcess.getServerLink() + ":80";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        startSignalR();
        return result;
    }

    @Override
    public void onDestroy() {
        mHubConnection.stop();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.

        /*long[] dataVibrate = {500,500,1000,1000};
        Vibrator vibrator = (Vibrator) DatMonApp.getAppContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(dataVibrate, -1);*/
        startSignalR();
        return mBinder;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public SignalRService getService() {
            // Return this instance of SignalRService so clients can call public methods
            return SignalRService.this;
        }
    }

    /**
     * method for clients (activities)
     */
    public void sendMessage(String message) {
        String SERVER_METHOD_SEND = "Send";
        mHubProxy.invoke(SERVER_METHOD_SEND, message);
    }

    private void startSignalR() {
        Platform.loadPlatformComponent(new AndroidPlatformComponent());
/*
        Credentials credentials = new Credentials() {
            @Override
            public void prepareRequest(Request request) {
                request.addHeader("User-Name", "BNK");
            }
        };*/


        mHubConnection = new HubConnection(serverUrl);
      /*  mHubConnection.setCredentials(credentials);
*/
        mHubProxy = mHubConnection.createHubProxy(SERVER_HUB_CHAT);
        ClientTransport clientTransport = new ServerSentEventsTransport(mHubConnection.getLogger());
        SignalRFuture<Void> signalRFuture = mHubConnection.start(clientTransport);

        try {
            signalRFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            /*Notification mNotify  = new Notification.Builder(DatMonApp.getAppContext())
                    .setContentTitle("startSignalR")
                    .setContentText(e.getMessage())
                    .setAutoCancel(true)
                    .build();*/
            e.printStackTrace();
            return;
        }

       // String HELLO_MSG = "Hello from Android!";
       // sendMessage(HELLO_MSG);


        mHubProxy.on(CLIENT_METHOD_BROADAST_MESSAGE,
                new SubscriptionHandler1<Conversation>() {
                    @Override
                    public void run(final Conversation conversation) {

                        Intent intent = new Intent();
                        intent.setAction(ConstData.ACTION_RECEIVE_CONVERSTATION);
                        intent.putExtra(ConstData.RECEIVE_CONVERSTATION_FROM_USER_ID, conversation.getFromUserId());
                        intent.putExtra(ConstData.RECEIVE_CONVERSTATION_TO_USER_ID, conversation.getToUserId());
                        intent.putExtra(ConstData.RECEIVE_CONVERSTATION_MESSAGE, conversation.getMessage());
                        intent.putExtra(ConstData.RECEIVE_CONVERSTATION_CREATE_TIME, conversation.getCreatedDate());
                        sendBroadcast(intent);
                    }
                }
                , Conversation.class);
        mHubProxy.on(CLIENT_METHOD_BROADAST_MESSAGE_UPDATE_DESK,
                new SubscriptionHandler1<String>() {
                    @Override
                    public void run(final String deskIds) {

                        Intent intent = new Intent();
                        intent.setAction(ConstData.ACTION_REQUEST_DESK_UPDATE);
                        intent.putExtra(ConstData.RECEIVE_REQUEST_DESK_UPDATE_DESK_ID, deskIds);
                        sendBroadcast(intent);
                    }
                }
                , String.class);
    }
}