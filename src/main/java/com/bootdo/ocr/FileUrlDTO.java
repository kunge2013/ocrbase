package com.bootdo.ocr;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUrlDTO {

    private Map<String, String> filePathMap = new HashMap<>();

    private String pattern ;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Map<String, String> getFilePathMap() {
        return filePathMap;
    }

    public void setFilePathMap(Map<String, String> filePathMap) {
        this.filePathMap = filePathMap;
    }

    public void put(String src, String desc) {
        filePathMap.put(src, desc);
    }

    public static FileUrlDTO initFilePath(List<String> filePaths, String pattern) {
        if (null == filePaths || filePaths.size() == 0) {
            throw new NullPointerException();
        }
        FileUrlDTO dto = new FileUrlDTO();
        for (String key : filePaths)
        dto.filePathMap.put(key, key);
        dto.setPattern(pattern);
        return dto;
    }
}
