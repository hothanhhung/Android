package com.hth.datmon;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hth.data.ServiceProcess;
import com.hth.service.ChatUser;
import com.hth.service.ConstData;
import com.hth.service.Conversation;

import java.util.ArrayList;

/**
 * Created by Lenovo on 12/1/2016.
 */

public class ChatView extends RelativeLayout {

    final int SERVICE_PROCESS_PUT_CONVERSATION = 1;

    private ChatUser fromUser;
    private ChatUser toUser;
    private Context context;
    private ListView lvChat;
    private TextView tvFullname;
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
        tvFullname = (TextView) rootView.findViewById(R.id.tvFullname);
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
        tvFullname.setText(toUser.getName());
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

    }

    @Override
    public void onDetachedFromWindow() {
        //
        super.onDetachedFromWindow();
    }


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

    public void addConversation(Conversation conversation) {
            if((fromUser.getUserId().equalsIgnoreCase(conversation.getFromUserId()) && toUser.getUserId().equalsIgnoreCase(conversation.getToUserId()))
                    || (fromUser.getUserId().equalsIgnoreCase(conversation.getToUserId()) && toUser.getUserId().equalsIgnoreCase(conversation.getFromUserId())))
            {
                chatItemRowAdapter.addConversation(conversation);
                lvChat.post(new Runnable() {
                    @Override
                    public void run() {
                        // Select the last row so it will scroll into view...
                        lvChat.setSelection(chatItemRowAdapter.getCount() - 1);
                    }
                });
            }
    };

    public boolean isCurrentChat(String userId)
    {
        return fromUser.getUserId().equalsIgnoreCase(userId) || toUser.getUserId().equalsIgnoreCase(userId);
    }
}
