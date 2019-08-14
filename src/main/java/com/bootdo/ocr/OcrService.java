package com.bootdo.ocr;
import com.bootdo.utils.PDFToIMGUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.apache.commons.io.FileUtils.*;

public abstract class OcrService<F extends OcrBaseResult> implements IOcrService {

    protected  Logger logger = null;

    public static final int DPI_500 = 500;

    public static final int DPI_300 = 300;

    public static final int DPI_400 = 400;

    public OcrService() {
        this.logger = Logger.getLogger(getClass());
    }

    protected String apiKey;

    protected String secretKey;

    protected String appId;

    /**
     * 特舒符号转换成空格
     */
    protected Map<String, String> specialCharsToBlankMap = Maps.newHashMap();
    /**
     * 国标 正则匹配
     */
    public static final String GB_PATTERN = "^GB1";

    /**
     * 初始化参数
     * @param apiKey
     * @param secretKey
     * @param appId
     */
    protected abstract void init(String apiKey, String secretKey, String appId);

    protected abstract List<F> fetchOcrDataByFileUrlDTO(FileUrlDTO dto);

    protected abstract void cpFile(F f) throws IOException;
    /**
     * 根据 目录来调用
     *
     * @param dir
     * @return
     */
    @Override
    public  List<F> updateFileByDir(String dir, FileType fileType, String pattern, int dpi) {
        List<String> fileList = Lists.newArrayList();
        getFileUrl(dir, fileList, fileType);
        FileUrlDTO fileUrlDTO = FileUrlDTO.initFilePath(fileList, pattern == null ? GB_PATTERN : pattern);
        if (fileType == FileType.PDF) {
           // 转化成 png
            fileList.forEach(filePath -> {
                fileUrlDTO.put(filePath, PDFToIMGUtil.pdf2Png(filePath, dir, dpi));
            });
        }
        List<F> list = fetchOcrDataByFileUrlDTO(fileUrlDTO);
        list.forEach(obj -> {
            try{
                cpFile(obj);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        /**
         * 清空生成的文件
         */
        Map<String, String> map = fileUrlDTO.getFilePathMap();
        if (fileType == FileType.PDF) {
            for(String key : map.keySet()) {
                final String pngFilePath = map.get(key);
                File pngFile = new File(pngFilePath);
                if (pngFile.exists()) {
                    pngFile.delete();
                }
            }
        }
        return list;
    }

    protected String cpFileByPath(String source, String format, String newFileName, String temp) throws IOException {
        File file = new File(source);
        final String fileName = file.getName();
        String newFilePath = source.replaceAll(fileName, temp + File.separator + File.separator + newFileName + "." + format);
        System.out.println("source : " + source + ", desc :" + newFilePath);
        copyFile(new File(source), new File(newFilePath));
        return newFilePath;
    }

    protected  static <F> F tranJsonBean(String str,  Class<F> t) {
        Gson gson = new Gson();
        return gson.fromJson(str, t);
    }

    private  void getFileUrl(String path , List<String> fileList, FileType fileType) {
        File file = new File(path);
        File[] array = file.listFiles();
        for (int i = 0; i < array.length; i++) {
            File tempFile = array[i];
            if (tempFile.isFile()) {
                String fileName = tempFile.getName();
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
                if (FileType.valueOf(suffix) == fileType) {
                    fileList.add(tempFile.getPath());
                }
            } else if (tempFile.isDirectory()) {
                getFileUrl(tempFile.getPath(), fileList, fileType);
            }
        }
    }
}
