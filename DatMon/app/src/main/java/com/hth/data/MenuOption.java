package com.hth.data;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public class MenuOption
{
  public Intent appIntent;
  public Drawable icon;
  public CharSequence title;

  public MenuOption()
  {
    this.title = "";
    this.icon = null;
    this.appIntent = null;
  }

  public MenuOption(CharSequence title, Drawable icon, Intent appIntent)
  {
    this.title = title;
    this.icon = icon;
    this.appIntent = appIntent;
  }
}
