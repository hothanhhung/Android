package com.hunght.solarlunarcalendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunght.data.DateItemForGridview;
import com.hunght.data.NoteItem;
import com.hunght.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Lenovo on 4/21/2018.
 */

public class NoteItemAdapter extends ArrayAdapter<NoteItem> {
    private ArrayList<NoteItem> data;
    private static LayoutInflater inflater = null;
    public Resources res;
    Context context;

    public NoteItemAdapter(Context a, ArrayList<NoteItem> d, Resources resLocal) {
        super(a, R.layout.notes_view_item, R.id.tvNoteItemSubject, d);
        context = a;
        data = d;
        res = resLocal;
        inflater = LayoutInflater.from(a);
    }

    public int getCount() {
        return data.size();
    }

    public Object onRetainNonConfigurationInstance() {
        return data;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        NoteItemViewHolder viewHolder;
        TextView tvNoteItemSubject, tvNoteItemRemainingDate, tvNotesItemDate, tvNoteItemContent, tvNoteItemNextDate;
        ImageView imgNoteItemImage, imgNoteItemImageDelete;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.notes_view_item, null);
            viewHolder = new NoteItemViewHolder();

            viewHolder.tvNoteItemSubject = tvNoteItemSubject = (TextView) convertView.findViewById(R.id.tvNoteItemSubject); // title
            viewHolder.tvNoteItemRemainingDate = tvNoteItemRemainingDate = (TextView) convertView.findViewById(R.id.tvNoteItemRemainingDate); // title
            viewHolder.tvNotesItemDate = tvNotesItemDate = (TextView) convertView.findViewById(R.id.tvNotesItemDate);// title
            viewHolder.tvNoteItemContent = tvNoteItemContent = (TextView) convertView.findViewById(R.id.tvNoteItemContent);// title
            viewHolder.tvNoteItemNextDate = tvNoteItemNextDate = (TextView) convertView.findViewById(R.id.tvNoteItemNextDate);
            viewHolder.imgNoteItemImage = imgNoteItemImage = (ImageView) convertView.findViewById(R.id.imgNoteItemImage);
            viewHolder.imgNoteItemImageDelete = imgNoteItemImageDelete = (ImageView) convertView.findViewById(R.id.imgNoteItemImageDelete);

            convertView.setTag(R.layout.notes_view_item, viewHolder);
        } else {
            viewHolder = (NoteItemViewHolder) convertView.getTag(R.layout.notes_view_item);
            tvNoteItemSubject = viewHolder.tvNoteItemSubject; // title
            tvNoteItemRemainingDate = viewHolder.tvNoteItemRemainingDate;
            tvNotesItemDate = viewHolder.tvNotesItemDate;
            tvNoteItemContent = viewHolder.tvNoteItemContent;
            tvNoteItemNextDate = viewHolder.tvNoteItemNextDate;
            imgNoteItemImage = viewHolder.imgNoteItemImage;
            imgNoteItemImageDelete = viewHolder.imgNoteItemImageDelete;
        }
        NoteItem item = data.get(position);

        imgNoteItemImageDelete.setTag(item);
        imgNoteItemImageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final NoteItem deletingItem = (NoteItem) v.getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Xóa Sự Kiện");
                builder.setMessage("Bạn muốn xóa sự kiện: "+ deletingItem.getSubject());

                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        data =  SharedPreferencesUtils.deleteNoteItem(context, deletingItem.getId());
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }

                });

                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // I do not need any action here you might
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        if (item != null) {
            tvNoteItemSubject.setText(item.getSubject());
            tvNotesItemDate.setText(item.getDateItem().getSolarInfo(false)+"\nNhằm "+item.getDateItem().getLunarInfo(false));
            if(item.hasContentInSomeText())
            {
                tvNoteItemContent.setVisibility(View.VISIBLE);
                tvNoteItemContent.setText(item.getContentInSomeText());
            }else{
                tvNoteItemContent.setVisibility(View.GONE);
            }
            DateItemForGridview nextRemind = item.getRemindDate();
            if(nextRemind == null)
            {
                tvNoteItemRemainingDate.setText("");
                tvNoteItemNextDate.setText("Không lặp lại");
            }else {
                DateItemForGridview today = new DateItemForGridview("", new Date(), false);
                tvNoteItemNextDate.setText(nextRemind.getSolarInfo(false) + "\nNhằm " + nextRemind.getLunarInfo(false));
                int remaingDate = nextRemind.difference(today);
                if(remaingDate == 0){
                    tvNoteItemRemainingDate.setText("(hôm nay)");
                    tvNoteItemRemainingDate.setTextColor(Color.RED);
                }else {
                    tvNoteItemRemainingDate.setText("(còn " + remaingDate + " ngày)");
                    tvNoteItemRemainingDate.setTextColor(Color.parseColor("#ff99cc00"));
                }
            }
        }
        if( position%2 == 0)
        {
            convertView.setBackgroundColor(Color.parseColor("#f7f7f7"));
        }else {
            convertView.setBackgroundColor(Color.WHITE);
        }
        convertView.setTag(item);
        return convertView;
    }


    class NoteItemViewHolder {

        TextView tvNoteItemSubject, tvNoteItemRemainingDate, tvNotesItemDate, tvNoteItemContent, tvNoteItemNextDate;
        ImageView imgNoteItemImage, imgNoteItemImageDelete;
    }
}