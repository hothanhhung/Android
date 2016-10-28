package com.hth.data;

import android.content.Context;
import android.view.View;

import java.lang.reflect.Constructor;

/**
 * Created by Lenovo on 10/28/2016.
 */
public class MenuLookUpItem {
    String name;
    String detail;
    int drawableIcon;
    String viewClassName;

    public MenuLookUpItem(){}

    public MenuLookUpItem(String name, int drawableIcon, String viewClassName, String detail)
    {
        this.name = name;
        this.drawableIcon = drawableIcon;
        this.viewClassName = viewClassName;
        this.detail = detail;
    }

    public int getDrawableIcon() {
        return drawableIcon;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public String getViewClassName() {
        return viewClassName;
    }

    public View getView(Context context)
    {
        try {
            Class<?> clazz = Class.forName(viewClassName);
            Constructor<?> constructor = clazz.getConstructor(Context.class);
            return (View)constructor.newInstance(context);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }
    public boolean hasDetail()
    {
        if(detail == null || detail.isEmpty())
        {
            return false;
        }
        return true;
    }

    public boolean hasIcon()
    {
        if(drawableIcon == 0)
        {
            return false;
        }
        return true;
    }


    public boolean hasAction()
    {
        if(viewClassName == null || viewClassName.isEmpty())
        {
            return false;
        }
        return true;
    }
}
