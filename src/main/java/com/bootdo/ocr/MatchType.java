package com.bootdo.ocr;

public enum MatchType {

    MATCH("MATCH", "MATCH"),
    NOTMATCH("NOMATCH","NOMATCH"),
    PARTICAL("PARTICAL","PARTICAL"),
    NOTMATCHDEAL("NOTMATCHDEAL","NOTMATCHDEAL");

    private String name;

    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    MatchType(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
