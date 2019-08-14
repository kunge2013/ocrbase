package com.bootdo.ocr;


import java.io.IOException;
import java.util.List;

public interface IOcrService<T> {

    /**
     * 根据 目录来调用
     * @param dir
     * @return
     */
    default List<T> updateFileByDir(String dir, FileType fileType, String pattern, int dpi)  {throw new UnsupportedOperationException();}

}
