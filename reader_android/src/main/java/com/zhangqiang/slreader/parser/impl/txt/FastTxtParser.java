package com.zhangqiang.slreader.parser.impl.txt;


import com.zhangqiang.slreader.parser.Book;
import com.zhangqiang.slreader.parser.BookParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class FastTxtParser extends BookParser {

    private String charset;

    public FastTxtParser(String charset) {
        this.charset = charset;
    }

    protected abstract InputStream openStream() throws IOException;


    @Override
    public Book parse() {

        InputStreamReader streamReader = null;
        try {
            InputStream inputStream = openStream();
            streamReader = new InputStreamReader(inputStream, charset);
            char[] buffer = new char[1];
            while (streamReader.read(buffer) != -1) {

                char c = buffer[0];
                if (c == '\n') {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (streamReader != null) {
                try {
                    streamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
