package com.cofco.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * 接口附件处理方式工具类
 *
 * @author handi
 * @date 2019/10/31 15:38:15
 */
public class PhotoUtils {
    /**
     * 将图片转换成二进制数组
     *
     * @param path    图片地址
     * @param imgType 图片类型
     * @return bytes
     */
    public static byte[] getImageBinary(String path, String imgType) {
        File f = new File(path);
        BufferedImage bi;
        try {
            bi = ImageIO.read(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, imgType, baos);  //经测试转换的图片是格式这里就什么格式，否则会失真
            byte[] bytes = baos.toByteArray();
            //String bytesBase64=getImageString(bytes,imgType);
            //return bytesBase64;
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 将二进制数组用BASE64编码
     *
     * @param bytes 二进制数组
     * @return bytesBase64
     */
    public static String getImageString(byte[] bytes, String imgType) {
        BASE64Encoder encoder = new BASE64Encoder();
        //二进制用BASE64编码，在字符串前添加图片格式。样例：data:image/gif/png/jpeg;bas64
        if (bytes != null && bytes.length != 0) {
            if (imgType.equals("gif")) {
                return "data:image/gif;base64," + encoder.encode(bytes);
            } else if (imgType.equals("png")) {
                return "data:image/png;base64," + encoder.encode(bytes);
            } else if (imgType.equals("jpeg")) {
                return "data:image/jpeg;base64," + encoder.encode(bytes);
            }
        }
        return null;
    }

    /**
     * 将Base64编码转换成图片，并存放在本地
     *
     * @param str Base64字符串
     */
    public static void base64Decoder(String str) {
        try {
            BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            byte[] bytes = decoder.decodeBuffer(str);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            BufferedImage bi = ImageIO.read(bais);
            File file = new File("d://QQ.bmp");//可以是jpg,png,gif格式
            ImageIO.write(bi, "jpg", file);//不管输出什么格式图片，此处不需改动
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断图片文件格式
     *
     * @param bytes 二进制数组
     * @return 图片文件格式
     * @throws IOException
     */
    public static String getImageType(byte[] bytes) {
        String type = "";
        ByteArrayInputStream bais = null;
        MemoryCacheImageInputStream mcis = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            mcis = new MemoryCacheImageInputStream(bais);
            Iterator itr = ImageIO.getImageReaders(mcis);
            while (itr.hasNext()) {
                ImageReader reader = (ImageReader) itr.next();
                String imageName = reader.getClass().getSimpleName();
                if (imageName != null) {
                    if ("GIFImageReader".equals(imageName)) {
                        type = "gif";
                    } else if ("JPEGImageReader".equals(imageName)) {
                        type = "jpeg";
                    } else if ("PNGImageReader".equals(imageName)) {
                        type = "png";
                    } else {
                        type = "noPic";
                    }
                }
            }
        } catch (Exception e) {
            type = "noPic";
        } finally {
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException ioe) {
                    type = "noPic";
                }
            }
            if (mcis != null) {
                try {
                    mcis.close();
                } catch (IOException ioe) {
                    type = "noPic";
                }
            }
        }
        return type;
    }
}
