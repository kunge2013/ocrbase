package com.bootdo;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaiDuOcrResult {
    static Map<String, String> stategyMap = new HashMap<String, String>();
    static {
        stategyMap.put("", "");
    }

    static Pattern pattern = Pattern.compile("^GB1");

    @SerializedName("log_id")
    private String logId;

    @SerializedName("words_result")
    private List<WordInfo> wordsResult;

    @SerializedName("words_result_num")
    private int wordsResultNum;

    private String fileName;

    private Boolean result = false;

    private String currentValue;

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    private void initResult() {
        currentValue = fetch(this.wordsResult);
        currentValue = doWithStrategy(currentValue, true);
        fileName = doWithStrategy(fileName, true);
        if (currentValue.equalsIgnoreCase(fileName)) {
            result = true;
        }
    }

    public String doWithStrategy(String val, boolean trim) {
        String gb ="GB";
        if (trim) {
            val = val.trim().replace(" ", "").replace("—", "").replace("-", "");
            System.out.println(val);
            String[] gbs = val.split(gb);
            if (gbs.length >= 2) {
                String data = gbs[1];
                return gb + " " + "-" + data;
            }
            return val;
        }
        return val;
    }
    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public List<WordInfo> getWordsResult() {
        return wordsResult;
    }

    public void setWordsResult(List<WordInfo> wordsResult) {
        this.wordsResult = wordsResult;
    }

    public int getWordsResultNum() {
        return wordsResultNum;
    }

    public void setWordsResultNum(int wordsResultNum) {
        this.wordsResultNum = wordsResultNum;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        initResult();
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String fetch(List<WordInfo> wordsResult) {
        String result = "";
        for (WordInfo wordInfo : wordsResult) {
            result = matchWord(wordInfo, pattern);
            if (StringUtils.isNotEmpty(result)) {
                break;
            }

        }
        return result;
    }

    public static String matchWord(WordInfo word, Pattern pattern) {
        Matcher m = pattern.matcher(word.getWords());
        while (m.find()) {
            return word.getWords();
        }
        return "";
    }

    static class WordInfo {
        public WordInfo() {
        }

        private String words;

        public String getWords() {
            return words;
        }
        public void setWords(String words) {
            this.words = words;
        }
    }
}
