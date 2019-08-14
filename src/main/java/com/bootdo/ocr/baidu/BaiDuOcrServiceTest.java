package com.bootdo.ocr.baidu;

import com.alibaba.fastjson.JSON;
import com.bootdo.ocr.FileType;
import com.bootdo.ocr.FileUrlDTO;
import com.bootdo.ocr.MatchType;
import com.bootdo.ocr.baidu.dto.BaiduOcrResult;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class BaiDuOcrServiceTest {

    String apiKey = "tGEDAGQBdMEk2EhgGzYXf4Hr";
    String secretKey = "ubGEcI8tF1F9hdpm7gzbgmeiou25bIDB";
    String appId = "17005522";
    BaiDuOcrService service = new BaiDuOcrService(apiKey, secretKey, appId);

    @org.junit.Test
    public void updateFileByDir() {
        final List<BaiduOcrResult> list = service.updateFileByDir("D:\\document\\pdf\\", FileType.PDF, /*BaiDuOcrService.GB_PATTERN*/ "GB", BaiDuOcrService.DPI_500);
        final List<BaiduOcrResult> matchs = list.stream().filter(obj -> obj.getMatchType() == MatchType.MATCH || obj.getMatchType() == MatchType.PARTICAL).collect(Collectors.toList());
        final List<BaiduOcrResult> failMatchs = list.stream().filter(obj ->  obj.getMatchType() == MatchType.NOTMATCH).collect(Collectors.toList());
        System.out.println(matchs + " size == >>" + matchs.size());
        System.out.println(JSON.toJSONString(failMatchs));
    }
    @org.junit.Test
    public void testFetchOcr() {
        FileUrlDTO dto = FileUrlDTO.initFilePath(Lists.newArrayList("D:\\document\\aaa\\test.png"), BaiDuOcrService.GB_PATTERN);
        service.fetchOcrDataByFileUrlDTO(dto);
    }
}