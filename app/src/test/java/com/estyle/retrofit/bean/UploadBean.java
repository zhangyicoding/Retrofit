package com.estyle.retrofit.bean;

import java.util.List;

public class UploadBean {

    private int code;
    private String info;
    private List<String> file_paths;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<String> getFile_paths() {
        return file_paths;
    }

    public void setFile_paths(List<String> file_paths) {
        this.file_paths = file_paths;
    }

    @Override
    public String toString() {
        return "UploadBean{" +
                "code=" + code +
                ", info='" + info + '\'' +
                ", file_paths=" + file_paths +
                '}';
    }
}
