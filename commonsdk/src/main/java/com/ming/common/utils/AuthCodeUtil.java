package com.ming.common.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * 随机生成验证码工具类
 * Created by Administrator on 2017-08-04.
 * 需要区分大小写
 * 用法:imageView.setImageBitmap(AuthCodeUtil.getInstance().createBitmap())
 */

public class AuthCodeUtil {
    private static AuthCodeUtil instance;

    /**
     * 验证码长度
     */
    private int codeLength = 4;

    /**
     * 画线的数量
     */
    private int lineNumber = 3;

    /**
     * 得到的验证码
     */
    private String mAuth;

    /**
     * 画布的宽
     */
    private int with = 100;

    /**
     * 画布的高
     */
    private int height = 60;

    /**
     * 产生随机数
     */
    private Random random = new Random();

    /**
     * 字体大小
     */
    private int font_size = 25;

    private int base_padding_left = 15, range_padding_left = 5,
            base_padding_top = 25, range_padding_top = 30;
    private int padding_left, padding_top;

    /**
     * 验证码展示的内容
     */
    private static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm',
            'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A',
            'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private AuthCodeUtil() {

    }

    /**
     * 单例模式获取实例
     * @return
     */
    public static AuthCodeUtil getInstance() {
        if (instance == null) {
            synchronized (AuthCodeUtil.class) {
                if (instance == null) {
                    instance = new AuthCodeUtil();
                }
            }
        }

        return instance;
    }

    /**
     * 创建位图
     * @return
     */
    public Bitmap createBitmap() {
        padding_left = 0;
        Bitmap bitmap = Bitmap.createBitmap(with, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mAuth = createCode();
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setTextSize(font_size);
        paint.setFakeBoldText(true);

        for (int i = 0; i < mAuth.length(); i++) {
            randomTextStyle(paint);
            randomPadding();
            canvas.drawText(mAuth.charAt(i) + "", padding_left, padding_top, paint);
        }

        for (int i = 0; i < lineNumber; i++) {
            drawLine(canvas, paint);
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bitmap;
    }

    /**
     * 画线
     *
     * @param canvas
     * @param paint
     */
    private void drawLine(Canvas canvas, Paint paint) {
        int color = randomColor();
        int startX = random.nextInt(with);
        int startY = random.nextInt(height);
        int stopX = random.nextInt(with);
        int stopY = random.nextInt(height);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    private void randomPadding() {
        padding_left += base_padding_left + random.nextInt(range_padding_left);
        padding_top = base_padding_top + random.nextInt(range_padding_top);
    }

    private String createCode() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < codeLength; i++) {
            sb.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return sb.toString();
    }

    private void randomTextStyle(Paint paint) {
        int color = randomColor();
        paint.setColor(color);
        paint.setFakeBoldText(random.nextBoolean());
        float skewX = random.nextInt(11) / 10;
        skewX = random.nextBoolean() ? skewX : -skewX;
        paint.setTextSkewX(skewX);
    }

    private int randomColor() {
        return randomColor(1);
    }

    private int randomColor(int rate) {
        int red = random.nextInt(256) / rate;
        int green = random.nextInt(256) / rate;
        int blue = random.nextInt(256) / rate;
        return Color.rgb(red, green, blue);
    }

    /**
     * 获取生成的验证码，一般作为验证来使用
     * @return
     */
    public String getmAuth() {
        return mAuth;
    }
}
