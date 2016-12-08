package com.hth.datmon;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hth.service.ChatUser;
import com.hth.service.Conversation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lenovo on 12/1/2016.
 */

public class ChatItemRowAdapter  extends ArrayAdapter<Conversation> {
    private ChatUser fromUser;
    private ChatUser toUser;
    ArrayList<Conversation> data;

    public ChatItemRowAdapter(Context context, ChatUser fromUser, ChatUser toUser, ArrayList<Conversation> messages) {
        super(context, 0, messages);
        this.fromUser = fromUser;
        this.toUser = toUser;
        if(this.data == null)
        {
            this.data = new ArrayList<Conversation>();
        }
        this.data.addAll(messages);
    }

    public int getCount() {
        return data.size();
    }

    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageOther;
        ImageView imageMe;
        TextView body;
        TextView tvLastTime;
        LinearLayout llChat;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.chat_item, parent, false);
        }
        llChat = (LinearLayout)convertView.findViewById(R.id.llChat);
        imageOther = (ImageView)convertView.findViewById(R.id.ivProfileOther);
        imageMe = (ImageView)convertView.findViewById(R.id.ivProfileMe);
        body = (TextView)convertView.findViewById(R.id.tvBody);
        tvLastTime = (TextView)convertView.findViewById(R.id.tvLastTime);

        final Conversation message = data.get(position);
        final boolean isMe = message.getFromUserId() != null && message.getFromUserId().equals(fromUser.getUserId());
        // Show-hide image based on the logged-in user.
        // Display the profile image to the right for our user, left for other users.
        if (isMe) {
            imageMe.setVisibility(View.VISIBLE);
            imageOther.setVisibility(View.GONE);
            //body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            llChat.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            llChat.setBackgroundResource(R.drawable.border_me_chat_rectangle);
        } else {
            imageOther.setVisibility(View.VISIBLE);
            imageMe.setVisibility(View.GONE);
            //body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            llChat.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            llChat.setBackgroundResource(R.drawable.border_other_chat_rectangle);
        }
        final ImageView profileView = isMe ? imageMe : imageOther;
        final String profileUrl = isMe ? fromUser.getPathImage() : toUser.getPathImage();
        if(profileUrl.isEmpty()){
            profileView.setImageResource(R.drawable.avatar);
        }
        else{
            Picasso.with(getContext()).load(profileUrl).into(profileView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    profileView.setImageResource(R.drawable.avatar);
                }
            });
        }
        body.setText(message.getMessage());
        tvLastTime.setText(MethodsHelper.getTimeChat(message.getCreatedDate()));
        return convertView;
    }

    public void addConversation(Conversation conversation)
    {
        data.add(conversation);
        notifyDataSetChanged();
    }

}
