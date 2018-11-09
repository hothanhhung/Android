package com.hunght.utils;

import java.util.ArrayList;

/**
 * Created by Lenovo on 6/20/2016.
 */
public class ResponseJson<T> {
    public boolean IsSuccess = false;
    public boolean NeedChangeDomain = false;
    public String NewDomain = "";
    public ArrayList<T> Data;
}
