package com.zhangqiang.sl.java;

import com.zhangqiang.sl.framework.render.SLRenderBuffer;
import com.zhangqiang.sl.framework.view.SLRenderBufferFactory;
import com.zhangqiang.sl.framework.view.SLViewRoot;

import java.awt.Component;
import java.util.HashMap;

public class JavaSLView extends Component {


    public JavaSLView() {


    }


    public static void main(String[] args) {

//        Starlight.getInstance().init(new JavaContext(), new DefaultFramePoster(), new Starlight.SLHandlerFactory() {
//            @Override
//            public SLHandler createHandler(SLHandler.Callback callback) {
//                return new JavaHandler();
//            }
//        });
//        Starlight.getInstance().createHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("你好");
//            }
//        });
//        JFrame.setDefaultLookAndFeelDecorated(true);
//        JFrame jFrame = new JFrame();
//        jFrame.add(new JavaSLView());
//        jFrame.setBounds(0,0,500,500);
//        jFrame.setVisible(true);

        int mode = 1 << 28;
        int width = 1024<<14;
        int height = 2048;
        int MODE_MASK = 0xF << 28;
        int WIDTH_MASK = 0xFFFB << 14;
        int HEIGHT_MASK = 0xFFFB;


        System.out.println(mode);
        System.out.println(MODE_MASK);
        System.out.println(mode & MODE_MASK);

        System.out.println("----------------");
        System.out.println(width);
        System.out.println(WIDTH_MASK);
        System.out.println(width & WIDTH_MASK);

        System.out.println("----------------");
        System.out.println(height);
        System.out.println(HEIGHT_MASK);
        System.out.println(height & HEIGHT_MASK);

        System.out.println("----------------");
        int result = (mode & MODE_MASK) | (width & WIDTH_MASK) | (height & HEIGHT_MASK);
        System.out.println(result);

        System.out.println("----------------");
        System.out.println(result & MODE_MASK);
        System.out.println(result & WIDTH_MASK);
        System.out.println(result & HEIGHT_MASK);


        System.out.println("----------------" + (WIDTH_MASK >> 14));
    }


}
