package com.zhangqiang.sl.reader.parser.impl.txt;

import com.zhangqiang.sl.reader.parser.Book;
import com.zhangqiang.sl.reader.parser.BookParser;
import com.zhangqiang.sl.reader.parser.Element;
import com.zhangqiang.sl.reader.parser.Paragraph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseTxtBookParser extends BookParser {

    private static final String CHAPTER_PATTERN = "[^，。]*[一二三四五六七八九十千百万0-9]+[章卷篇集节部][^，。]*";
    private String charset;

    public BaseTxtBookParser(String charset) {
        this.charset = charset;
    }

    protected abstract InputStream openStream() throws IOException;

    @Override
    public Book parse() {
        BufferedReader reader = null;
        try {

            Pattern pattern = Pattern.compile(CHAPTER_PATTERN);
            List<Paragraph> paragraphs = new ArrayList<>();
            List<Chapter> chapters = new ArrayList<>();
            reader = new BufferedReader(new InputStreamReader(openStream(), charset));

            List<Element> elements = new ArrayList<>();
            int start = 0;
            int offset = 0;
            String line;
            while ((line = reader.readLine()) != null) {

                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    Chapter chapter = new Chapter();
                    chapter.setName(line);
                    chapter.getPosition().set(paragraphs.size(),offset);
                    chapters.add(chapter);
                }

                int length = line.length();
                for (int i = 0; i < length; i++) {
                    char c = line.charAt(i);
                    elements.add(new TextElement(c));
                    offset++;
                    if (i == length - 1) {
                        Paragraph paragraph = new ParagraphImpl(elements, start, offset);
                        start += offset;
                        offset = 0;
                        paragraphs.add(paragraph);
                    }
                }
            }
            if (!elements.isEmpty()) {
                Paragraph paragraph = new ParagraphImpl(elements, start, offset);
                paragraphs.add(paragraph);
            }
            return new TxtBook(parseBookName(),chapters, paragraphs);
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

    protected abstract String parseBookName();
}
