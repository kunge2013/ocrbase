package com.bootdo.ocr.baidu.dto;

import com.bootdo.ocr.MatchType;
import com.bootdo.ocr.OcrBaseResult;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaiduOcrResult extends OcrBaseResult<BaiduOcrResult> {

    @SerializedName("log_id")
    private String logId;

    @SerializedName("words_result")
    private List<WordInfo> wordsResult;

    @SerializedName("words_result_num")
    private int wordsResultNum;

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

    @Override
    public String fetchGbCode(String pattern) {
        String result = "";
        for (WordInfo wordInfo : wordsResult) {
            result = matchWord(wordInfo, Pattern.compile(pattern));
            if (StringUtils.isNotEmpty(result)) {
                break;
            }
        }
        return result;
    }

    @Override
    public void matchCode(String pattern) {
      this.ocrGbCode = trimBySpecialMap(fetchGbCode(pattern));
      this.setFileNameTemp(trimBySpecialMap(this.getFileName()));
      if (StringUtils.isNotEmpty(this.ocrGbCode)
          && StringUtils.isNotEmpty(getFileNameTemp())) {
          setMatchType(validateOcrGbCodeCorrectFileName(getFileNameTemp(), ocrGbCode));
        } else {
          setMatchType(MatchType.NOTMATCH);
      }
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
