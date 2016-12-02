package com.hth.datmon;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hth.data.ServiceProcess;
import com.hth.service.ChatUser;
import com.hth.service.Conversation;

import java.util.ArrayList;

/**
 * Created by Lenovo on 12/1/2016.
 */

public class ChatView extends RelativeLayout {

    final int SERVICE_PROCESS_PUT_CONVERSATION = 1;

    /*private SignalRService mService;
    private boolean mBound = false;
*/
    private ChatUser fromUser;
    private ChatUser toUser;
    private Context context;
    private ListView lvChat;
    private EditText etMessage;
    private Button btSend;
    private View rootView;
    private ArrayList<Conversation> conversations;

    ChatItemRowAdapter chatItemRowAdapter;

    public ChatView(Context context, ChatUser fromUser, ChatUser toUser, ArrayList<Conversation> conversations) {
        super(context);
        this.context = context;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.conversations = conversations;
        if(this.conversations == null){
            this.conversations = new ArrayList<>();
        }
        init();
    }

    private void init() {
        rootView = inflate(getContext(), R.layout.chat_view, null);
        addView(rootView);
        lvChat = (ListView) rootView.findViewById(R.id.lvChat);
        etMessage = (EditText) rootView.findViewById(R.id.etMessage);
        btSend = (Button) rootView.findViewById(R.id.btSend);
        btSend.setEnabled(false);
        btSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if( etMessage.getText()!=null && !etMessage.getText().toString().isEmpty())
                {
                    new PerformServiceProcessBackgroundTask().execute(SERVICE_PROCESS_PUT_CONVERSATION, etMessage.getText().toString());
                    etMessage.setText("");
                }
            }
        });

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s==null || s.length() == 0)
                {
                    btSend.setEnabled(false);
                }else{
                    btSend.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        chatItemRowAdapter = new ChatItemRowAdapter(context, fromUser, toUser, conversations);
        lvChat.setAdapter(chatItemRowAdapter);
        lvChat.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lvChat.setSelection(chatItemRowAdapter.getCount() - 1);
            }
        });

       /* Intent intent = new Intent();
        intent.setClass(context, SignalRService.class);
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);*/
    }
   /* private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to SignalRService, cast the IBinder and get SignalRService instance
            SignalRService.LocalBinder binder = (SignalRService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
*/

    public class PerformServiceProcessBackgroundTask extends AsyncTask< Object, Object, Object > {
        private ProgressDialog loadingDialog = new ProgressDialog(context);
        private int type;

        protected void onPreExecute() {
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setTitle("Processing");
            loadingDialog.setMessage("Please wait");
           // loadingDialog.show();
        }

        protected Object doInBackground(Object... parameters) {
            type = Integer.parseInt(parameters[0].toString());
            switch (type)
            {
                case SERVICE_PROCESS_PUT_CONVERSATION:
                    Conversation conversation = new Conversation();
                    conversation.setToUserId(toUser.getUserId());
                    conversation.setMessage(parameters[1].toString());
                    return ServiceProcess.addConversation(conversation);
            }
            return  null;
        }

        protected void onPostExecute(Object object) {
            loadingDialog.dismiss();
            switch (type)
            {
                case SERVICE_PROCESS_PUT_CONVERSATION:
                    break;
            }

        }
    }
}
