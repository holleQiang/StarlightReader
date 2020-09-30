package com.zhangqiang.starlightreader.ui.curl;

import android.content.Context;
import android.graphics.Bitmap;

import com.zhangqiang.sl.android.AndroidContext;
import com.zhangqiang.sl.android.render.AndroidFramePoster;
import com.zhangqiang.sl.android.render.AndroidRenderBuffer;
import com.zhangqiang.sl.android.render.AndroidRenderBufferFactory;
import com.zhangqiang.sl.framework.render.SLFramePoster;
import com.zhangqiang.sl.framework.render.SLRenderBuffer;
import com.zhangqiang.sl.framework.view.FramePosterFactory;
import com.zhangqiang.sl.framework.view.SLViewRoot;
import com.zhangqiang.sl.reader.page.PageView;
import com.zhangqiang.sl.reader.parser.Book;
import com.zhangqiang.sl.reader.position.TextWordPosition;

public class PageProvider implements CurlView.PageProvider {

    private Context context;
    private SLViewRoot viewRoot1;
    private Book book;
    private TextWordPosition position;
    private OnPageChangedListener listener;

    public PageProvider(Context context, Book book, TextWordPosition position) {
        this.context = context;
        viewRoot1 = new SLViewRoot(new AndroidContext(context, true), new AndroidRenderBufferFactory(null, true),
                new FramePosterFactory() {
                    @Override
                    protected SLFramePoster onCreateFramePoster() {
                        return new AndroidFramePoster();
                    }
                });
        this.book = book;
        this.position = position;
    }

    @Override
    public int getPageCount() {
        return 1;
    }

    @Override
    public void updatePage(final CurlPage page, int width, int height, int index) {
        if (viewRoot1.getRootView() == null) {
            PageView pageView = new PageView(viewRoot1.getContext());
            pageView.setBook(book,position,false);
            viewRoot1.setView(pageView,width,height);
            viewRoot1.getRenderBuffer().setCallback(new SLRenderBuffer.Callback() {
                @Override
                public void onRenderBufferChanged() {
                    if (listener != null) {
                        listener.onPageChanged();
                    }
                    Bitmap bitmap = ((AndroidRenderBuffer) viewRoot1.getRenderBuffer()).getBitmap();
                    page.setTexture(Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),true),CurlPage.SIDE_FRONT);
                }
            });
            Bitmap bitmap = ((AndroidRenderBuffer) viewRoot1.getRenderBuffer()).getBitmap();
            page.setTexture(Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),true),CurlPage.SIDE_FRONT);
        }
    }

    public interface OnPageChangedListener{

        void onPageChanged();
    }

    public void setListener(OnPageChangedListener listener) {
        this.listener = listener;
    }
}
