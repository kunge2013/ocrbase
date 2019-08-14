package com.bootdo.ocr.baidu;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.ocr.AipOcr;
import com.bootdo.ocr.*;
import com.bootdo.ocr.baidu.dto.BaiduOcrResult;
import com.google.common.collect.Lists;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaiDuOcrService extends OcrService<BaiduOcrResult> {

    AipOcr client = null;

    public BaiDuOcrService(String apiKey, String secretKey, String appId) {
        init(apiKey, secretKey, appId);
    }
    /**
     * 初始化参数
     * @param apiKey
     * @param secretKey
     * @param appId
     */
    @Override
    protected void init(String apiKey, String secretKey, String appId) {
        this.apiKey = apiKey;
        this.appId = appId;
        this.secretKey = secretKey;
        client = new AipOcr(appId, apiKey, secretKey);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        specialCharsToBlankMap.put(" ","");
        specialCharsToBlankMap.put("—","");
        specialCharsToBlankMap.put("-","");
    }

    @Override
    protected List<BaiduOcrResult> fetchOcrDataByFileUrlDTO(FileUrlDTO dto) {
        final Map<String, String> filePathMap = dto.getFilePathMap();
        List<BaiduOcrResult> results = Lists.newLinkedList();
        for (String sourcePath : filePathMap.keySet()) {
            // 调用接口
            String path = filePathMap.get(sourcePath);
            JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
            final String[] split = path.split("\\\\");
            final BaiduOcrResult baiDuOcrResult = tranJsonBean(res.toString(2), BaiduOcrResult.class);
            final String fileName = split[split.length - 1].replace(".png", "");
            baiDuOcrResult.setFileName(fileName);
            baiDuOcrResult.setFilePath(sourcePath);
            results.add(baiDuOcrResult);
        }
        logger.info("cover data " + JSON.toJSONString(results));
        results.forEach(obj -> obj.matchCode(dto.getPattern()));
        return results;
    }


    @Override
    protected void cpFile(BaiduOcrResult baiduOcrResult) throws IOException {
        super.cpFileByPath(
                baiduOcrResult.getFilePath(),
                FileType.PDF.getType(),
                baiduOcrResult.getCorrectFileName(),
                baiduOcrResult.getMatchType().getValue());

        /**
         * 如果发现不对就以编码命名
         */
        if (baiduOcrResult.getMatchType() == MatchType.NOTMATCH) {
            super.cpFileByPath(
                    baiduOcrResult.getFilePath(),
                    FileType.PDF.getType(),
                    baiduOcrResult.getOcrGbCode(),
                    MatchType.NOTMATCHDEAL.getValue());
        }
    }
}
