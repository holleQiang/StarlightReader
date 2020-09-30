package com.zhangqiang.sl.reader.parser;

import com.zhangqiang.sl.reader.parser.impl.ParagraphImpl;
import com.zhangqiang.sl.reader.parser.impl.TextBookImpl;
import com.zhangqiang.sl.reader.parser.impl.TextElement;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import sun.misc.IOUtils;

public abstract class BookParser {

    public abstract Book parse();

}
