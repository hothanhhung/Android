package com.hth.service;

/**
 * Created by Lenovo on 11/23/2016.
 */

public class ImageData {
    public String FileName;
    public String Data;

    public ImageData()
    {

    }

    public ImageData(String fileName, String data)
    {
        FileName = fileName;
        Data = data;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
