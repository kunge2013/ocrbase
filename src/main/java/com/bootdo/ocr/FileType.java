package com.bootdo.ocr;

public enum FileType {

    PDF("PDF","PDF"), PNG("PNG", "PNG");

    private String type;
    private String desc;

    FileType(String type, String desc) {
       this.desc = desc;
       this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
