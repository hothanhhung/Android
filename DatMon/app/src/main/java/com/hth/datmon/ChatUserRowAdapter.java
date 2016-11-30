package com.hth.datmon;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hth.data.ChatUser;
import com.hth.service.Customer;
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

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.chat_user_row, null);
        }
        tvFullname = (TextView) convertView.findViewById(R.id.tvFullname);
        tvNumberOfCommingText = (TextView) convertView.findViewById(R.id.tvNumberOfCommingText);
        imvAvatar = (ImageView) convertView.findViewById(R.id.imvAvatar);

        ChatUser chatUser = data.get(position);

        // Setting all values in listview
        tvFullname.setText(chatUser.getName());
        tvNumberOfCommingText.setText(chatUser.getNumberOfCommingMessageInString());
        if(chatUser.hasImage())
        {
            Picasso.with(context).load(chatUser.getImage()).into(imvAvatar);
        }else {
            imvAvatar.setImageResource(R.drawable.avatar);
        }

        if(position % 2 == 0) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.ordered_item_odd_color));
        }else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.ordered_item_even_color));
        }
        return convertView;
    }

}
