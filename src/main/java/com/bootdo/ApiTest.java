package com.bootdo;

import com.baidu.aip.ocr.AipOcr;
import com.bootdo.utils.PDFToIMGUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ApiTest {

    public static List<String> fileList = Lists.newArrayList();
    static Map<String, BaiDuOcrResult> dataMap = new HashMap<>();

    public static void main(String[] args) {
        String dirPath = "D:\\document\\pdf\\";
        List<String> fileList = Lists.newLinkedList();
        getFile(dirPath, fileList);
        fileList.forEach(filePath -> {
            PDFToIMGUtil.pdf2Png(filePath, dirPath, 300);
            read(filePath.replace(".pdf", ".png"));
        });
        final LinkedList<BaiDuOcrResult> baiDuOcrResults = Lists.newLinkedList(dataMap.values());

        AnalysicOcrData data = new AnalysicOcrData();
        baiDuOcrResults.forEach(obj -> {
            if(obj.getResult()) data.getSuccess().add(obj);
            else data.getFails().add(obj);
        });
        System.out.println(data);
    }
    public static void read(String path) {
        String APP_ID = "17005522";
        String API_KEY = "tGEDAGQBdMEk2EhgGzYXf4Hr";
        String SECRET_KEY = "ubGEcI8tF1F9hdpm7gzbgmeiou25bIDB";
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 调用接口
        JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
        final String[] split = path.split("\\\\");
        final BaiDuOcrResult baiDuOcrResult = tranJsonBean(res.toString(2), BaiDuOcrResult.class);
        final String fileName = split[split.length - 1].replace(".png", "");
        baiDuOcrResult.setFileName(fileName);
        dataMap.put(split[split.length - 1].replace(".png", ""), baiDuOcrResult);
    }
    private static void getFile(String path , List<String> fileList) {
        File file = new File(path);
        File[] array = file.listFiles();
        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                fileList.add(array[i].getPath());
            } else if (array[i].isDirectory()) {
                getFile(array[i].getPath(), fileList);
            }
        }
    }

    public  static <F> F tranJsonBean(String str,  Class<F> t) {
        Gson gson = new Gson();
        return gson.fromJson(str, t);
    }

}
