package com.ming.common.imageLoder.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.bumptech.glide.Glide;
import com.ming.common.imageLoder.loader.ImageDownLoadCallBack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.bumptech.glide.request.target.Target;

/**
 * Created by Administrator on 2017/7/20.
 */

public class DownLoadImageService implements Runnable {

    private String url, fileName;
    private Context context;
    private ImageDownLoadCallBack callBack;
    private File currentFile;
    private boolean isSetMediaStore;
    private Exception exception;

    /**
     *保存图片
     * @param context 上下文
     * @param url 图片url
     * @param isSetMediaStore 是否保存到图库
     * @param fileName 保存的文件夹名称
     * @param callBack 保存结果的回调
     */
    public DownLoadImageService(Context context, String url, boolean isSetMediaStore, String fileName, ImageDownLoadCallBack callBack) {
        this.url = url;
        this.callBack = callBack;
        this.context = context;
        this.isSetMediaStore = isSetMediaStore;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();

            if (bitmap != null) {
                //保存到手机图库
                saveImageToGallery(context, bitmap);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            exception = e;
        } catch (ExecutionException e) {
            e.printStackTrace();
            exception = e;
        } finally {
            if (bitmap != null && currentFile.exists()) {
                callBack.onDownLoadSuccess(bitmap);
            } else {
                if (exception != null) {
                    callBack.onDownLoadFailed(exception);
                } else {
                    callBack.onDownLoadFailed();
                }

            }
        }
    }

    /**
     * 保存图片到图库
     * @param context
     * @param bitmap
     */
    private void saveImageToGallery(Context context, Bitmap bitmap) {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
        File appDir = new File(file, fileName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        fileName = System.currentTimeMillis() + ".jpg";
        currentFile = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(currentFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isSetMediaStore) {
            setMediaStore(fileName);
        }
        //通知图库更新照片
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(currentFile.getPath()))));
    }

    private void setMediaStore(String fileName) {
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    currentFile.getAbsolutePath(),fileName,null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
