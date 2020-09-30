package com.zhangqiang.sl.reader.parser.impl;

import com.zhangqiang.sl.reader.parser.Book;
import com.zhangqiang.sl.reader.parser.BookParser;
import com.zhangqiang.sl.reader.parser.Element;
import com.zhangqiang.sl.reader.parser.Paragraph;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTxtBookParser extends BookParser {

    private String charset;

    public BaseTxtBookParser(String charset) {
        this.charset = charset;
    }

    protected abstract InputStream openStream() throws IOException;

    @Override
    public Book parse() {
        InputStreamReader reader = null;
        try {

            List<Paragraph> paragraphs = new ArrayList<>();
            reader = new InputStreamReader(openStream(), charset);

            List<Element> elements = new ArrayList<>();
            int start = 0;
            int offset = 0;
            char[] buffer = new char[1];
            while (reader.read(buffer) != -1) {
                char c = buffer[0];
                elements.add(new TextElement(c));
                offset++;
                if (c == '\n') {
                    Paragraph paragraph = new ParagraphImpl(elements, start, offset);
                    start += offset;
                    offset = 0;
                    paragraphs.add(paragraph);
                }
            }
            if (!elements.isEmpty()) {
                Paragraph paragraph = new ParagraphImpl(elements, start, offset);
                paragraphs.add(paragraph);
            }
            return new TextBookImpl(paragraphs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
