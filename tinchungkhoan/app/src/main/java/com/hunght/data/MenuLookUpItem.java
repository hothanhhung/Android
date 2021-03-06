package com.hunght.data;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Constructor;

/**
 * Created by Lenovo on 10/28/2016.
 */

public class MenuLookUpItem {
    String name;
    String detail;
    String url;
    int drawableIcon;
    String viewClassName;
    MenuLookUpItemKind kind;
    boolean isTrangBao = false;

    public MenuLookUpItem(){}
    public MenuLookUpItem(String name, int drawableIcon, String viewClassName, String detail)
    {
        this(name, drawableIcon, viewClassName, detail, MenuLookUpItemKind.None);
    }
    public MenuLookUpItem(String name, int drawableIcon, String viewClassName, String detail, MenuLookUpItemKind kind)
    {
        this.name = name;
        this.drawableIcon = drawableIcon;
        this.viewClassName = viewClassName;
        this.detail = detail;
        this.kind = kind;
    }

    public MenuLookUpItem(String name, int drawableIcon, String viewClassName, String detail, MenuLookUpItemKind kind, String url)
    {
        this.name = name;
        this.drawableIcon = drawableIcon;
        this.viewClassName = viewClassName;
        this.detail = detail;
        this.kind = kind;
        this.url = url;
    }

    public MenuLookUpItem(String name, int drawableIcon, String viewClassName, String detail, MenuLookUpItemKind kind, String url, Boolean isTrangBao)
    {
        this.name = name;
        this.drawableIcon = drawableIcon;
        this.viewClassName = viewClassName;
        this.detail = detail;
        this.kind = kind;
        this.url = url;
        this.isTrangBao = isTrangBao;
    }

    public int getDrawableIcon() {
        return drawableIcon;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDetail() {
        return detail;
    }

    public String getViewClassName() {
        return viewClassName;
    }

    public MenuLookUpItemKind getKind() {
        return kind;
    }

    public View getView(Context context)
    {
        try {
            Class<?> clazz = Class.forName(getViewClassName());
            if(kind == MenuLookUpItemKind.None)
            {
                Constructor<?> constructor = clazz.getConstructor(Context.class);
                return (View)constructor.newInstance(context);
            }else{
                Constructor<?> constructor = clazz.getConstructor(Context.class, MenuLookUpItemKind.class);
                return (View)constructor.newInstance(context, kind);
            }
        }catch (Exception ex)
        {
            Log.d("getView", this.name +" - "+ this.viewClassName );
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

    public boolean isTrangBao()
    {
        return isTrangBao;
    }

    public boolean hasAction()
    {
        if(getViewClassName() == null || getViewClassName().isEmpty())
        {
            return false;
        }
        return true;
    }
}
