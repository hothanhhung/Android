package com.hth.datmon;

import android.content.Context;
import android.view.Gravity;
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
        this.data = messages;
        if(this.data == null)
        {
            this.data = new ArrayList<Conversation>();
        }
    }

    public int getCount() {
        return data.size();
    }

    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.chat_item, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.imageOther = (ImageView)convertView.findViewById(R.id.ivProfileOther);
            holder.imageMe = (ImageView)convertView.findViewById(R.id.ivProfileMe);
            holder.body = (TextView)convertView.findViewById(R.id.tvBody);
            convertView.setTag(holder);
        }
        final Conversation message = data.get(position);
        final ViewHolder holder = (ViewHolder)convertView.getTag();
        final boolean isMe = message.getFromUserId() != null && message.getFromUserId().equals(fromUser.getUserId());
        // Show-hide image based on the logged-in user.
        // Display the profile image to the right for our user, left for other users.
        if (isMe) {
            holder.imageMe.setVisibility(View.VISIBLE);
            holder.imageOther.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        } else {
            holder.imageOther.setVisibility(View.VISIBLE);
            holder.imageMe.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }
        final ImageView profileView = isMe ? holder.imageMe : holder.imageOther;
        final String profileUrl = isMe ? fromUser.getPathImage() : toUser.getPathImage();
        if(profileUrl.isEmpty()){
            profileView.setImageResource(R.drawable.avatar);
        }
        else{
            Picasso.with(getContext()).load(profileUrl).into(profileView);
        }
        holder.body.setText(message.getMessage());
        return convertView;
    }

    final class ViewHolder {
        public ImageView imageOther;
        public ImageView imageMe;
        public TextView body;
    }
}
