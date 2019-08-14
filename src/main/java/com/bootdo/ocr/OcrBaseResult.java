package com.bootdo.ocr;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.regex.Pattern;

public abstract class OcrBaseResult<T> {

    private static final String BLANK_CHAR = " ";

    private static final String LINE_CHAR = "-";

    /**
     * 特舒符号转换成空格
     */
    protected Map<String, String> specialCharsToBlankMap = Maps.newHashMap();

    {
        specialCharsToBlankMap.put(" ","");
        specialCharsToBlankMap.put("—","");
        specialCharsToBlankMap.put("-","");
    }

    /**
     * 去掉特殊字符
     * @param val
     * @return
     */
    protected String trimBySpecialMap(String val){
        for (String key : specialCharsToBlankMap.keySet()) {
            val = val.replace(key, specialCharsToBlankMap.get(key));
        }
        return val;
    }

    /**
     * 校验缺少的年月字段
     */
    protected MatchType validateOcrGbCodeCorrectFileName(String fileNameTemp ,String ocrGbCode){
        final int fLen = fileNameTemp.length();
        final int ocrLen = ocrGbCode.length();
        this.correctFileName = this.fileName;
            if (fileNameTemp.equalsIgnoreCase(ocrGbCode)) {
            return MatchType.MATCH;
        }
        if (Math.abs(fLen - ocrLen) != 2) {
            return MatchType.NOTMATCH;
        }
        String filePrefixCode = null;
        String fileSuffixCode  = null;
        String ocrPrefixCode  = null;
        String ocrSuffixCode = null;
        fileSuffixCode = fileNameTemp.substring(fileNameTemp.length() - 2, fileNameTemp.length());
        ocrSuffixCode = ocrGbCode.substring(ocrGbCode.length() - 2, ocrGbCode.length());
        String differVal = null;
        if (fLen > ocrLen) {
             filePrefixCode = fileNameTemp.substring(0,fileNameTemp.length() - 4);
             ocrPrefixCode = ocrGbCode.substring(0, ocrGbCode.length() - 2);
             differVal = fileNameTemp.substring(fileNameTemp.length() - 4, fileNameTemp.length() - 2);
             this.correctFileName = ocrPrefixCode + LINE_CHAR +  differVal + ocrSuffixCode;
        } else {
            ocrPrefixCode  = ocrGbCode.substring(0, ocrGbCode.length() - 4);
            filePrefixCode = fileNameTemp.substring(0, fileNameTemp.length() - 2);
            differVal = ocrGbCode.substring(ocrGbCode.length() - 4, ocrGbCode.length() - 2);
        }
        if ((Integer.valueOf(differVal) == 19 || Integer.valueOf(differVal) == 20)
                    && filePrefixCode.equalsIgnoreCase(ocrPrefixCode)
                    && ocrSuffixCode.equalsIgnoreCase(fileSuffixCode)) {
            return MatchType.PARTICAL;
        }
        /**
         * 没校验通过按照  编号来命名
         */
        this.correctFileName = ocrGbCode;
        return MatchType.NOTMATCH;
    }

    private T data;
    /**
     * 采集文字内容正则表达式
     */
   // protected Pattern pattern = Pattern.compile("^GB1");

    public abstract String fetchGbCode(String pattern);

    public abstract void matchCode(String pattern);

    private String filePath;

    private String fileName;

    private String fileNameTemp;

    protected String ocrGbCode;

    private MatchType matchType;

    private String correctFilePath;

    private String correctFileName;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public String getFileNameTemp() {
        return fileNameTemp;
    }

    public void setFileNameTemp(String fileNameTemp) {
        this.fileNameTemp = fileNameTemp;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOcrGbCode() {
        return ocrGbCode;
    }

    public void setOcrGbCode(String ocrGbCode) {
        this.ocrGbCode = ocrGbCode;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public String getCorrectFileName() {
        return correctFileName;
    }

    public void setCorrectFileName(String correctFileName) {
        this.correctFileName = correctFileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCorrectFilePath() {
        return correctFilePath;
    }

    public void setCorrectFilePath(String correctFilePath) {
        this.correctFilePath = correctFilePath;
    }
}
