package com.hth.datmon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hth.data.MenuOption;

import java.util.ArrayList;

public class MenuOptionAdapter extends ArrayAdapter<MenuOption>
{
  private LayoutInflater mInflater;
  private ArrayList<MenuOption> mOptions;

  public MenuOptionAdapter(Context context, ArrayList<MenuOption> menuOptions)
  {
    super(context, R.layout.menu_option_selector, menuOptions);
    this.mOptions = menuOptions;
    this.mInflater = LayoutInflater.from(context);
  }

  public View getView(int position, View convertView, ViewGroup parent)
  {
    if (convertView == null)
      convertView = this.mInflater.inflate(R.layout.menu_option_selector, parent, false);
    MenuOption menuOption = (MenuOption)mOptions.get(position);
    if (menuOption != null)
    {
      ((ImageView)convertView.findViewById(R.id.iv_icon)).setImageDrawable(menuOption.icon);
      ((TextView)convertView.findViewById(R.id.tv_name)).setText(menuOption.title);
    }
    return convertView;
  }
}
