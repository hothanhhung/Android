package com.hth.datmon;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hth.service.ChatUser;
import com.hth.service.Conversation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatUserRowAdapter extends ArrayAdapter<ChatUser> {
    private ArrayList<ChatUser> data;
    private static LayoutInflater inflater = null;
    public Resources res;
    private Activity context;

    public ChatUserRowAdapter(Activity a, ArrayList<ChatUser> d, Resources resLocal) {
        super(a, R.layout.chat_user_row, R.id.title, d);
        context = a;
        this.data = d;
        if(this.data == null)
        {
            this.data = new ArrayList<ChatUser>();
        }
        res = resLocal;
        inflater = LayoutInflater.from(a);
    }

    public int getCount() {
        return data.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imvAvatar;
        TextView tvNumberOfCommingText;
        TextView tvFullname;
        TextView tvLastMessage;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chat_user_row, null);
        }
        tvLastMessage = (TextView) convertView.findViewById(R.id.tvLastMessage);
        tvFullname = (TextView) convertView.findViewById(R.id.tvFullname);
        tvNumberOfCommingText = (TextView) convertView.findViewById(R.id.tvNumberOfCommingText);
        imvAvatar = (ImageView) convertView.findViewById(R.id.imvAvatar);

        ChatUser chatUser = data.get(position);
        convertView.setTag(chatUser);
        // Setting all values in listview
        tvFullname.setText(chatUser.getName());
        if(chatUser.isOnline()) {
            tvFullname.setTextColor(Color.BLUE);
        }else{
            tvFullname.setTextColor(Color.BLACK);
        }
        tvNumberOfCommingText.setText(chatUser.getNumberOfCommingConversationInString());
        if(chatUser.hasImage())
        {
            Picasso.with(context).load(chatUser.getPathImage()).into(imvAvatar);
        }else {
            imvAvatar.setImageResource(R.drawable.avatar);
        }
        if(chatUser.getConversations()!=null && chatUser.getConversations().size() > 0)
        {
            Conversation conversation = chatUser.getConversations().get(chatUser.getConversations().size() - 1);
            if(conversation.isToUserIsRead())
            {
                tvLastMessage.setText(conversation.getMessage());
                tvLastMessage.setTextColor(Color.BLUE);
            }else{
                tvLastMessage.setText(conversation.getMessage());
                tvLastMessage.setTextColor(Color.BLACK);
            }
        }
        if(position % 2 == 0) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.ordered_item_odd_color));
        }else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.ordered_item_even_color));
        }
        return convertView;
    }

}
