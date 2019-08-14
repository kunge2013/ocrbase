package com.bootdo;

import java.util.ArrayList;
import java.util.List;

public class AnalysicOcrData {

    public AnalysicOcrData() {
    }

    private List<BaiDuOcrResult> success = new ArrayList<>();

    private List<BaiDuOcrResult> fails = new ArrayList<>();

    public List<BaiDuOcrResult> getSuccess() {
        return success;
    }

    public void setSuccess(List<BaiDuOcrResult> success) {
        this.success = success;
    }

    public List<BaiDuOcrResult> getFails() {
        return fails;
    }

    public void setFails(List<BaiDuOcrResult> fails) {
        this.fails = fails;
    }
}
