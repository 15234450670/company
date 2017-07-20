package mr.li.dance.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Nereo on 2015/4/8.
 */
public class FileUtils {

    public static File createTmpFile(Context context, String picName) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!pic.exists()) {
                pic.mkdirs();
            }
            File tmpFile = new File(pic, picName + ".png");
            return tmpFile;
        } else {
            File cacheDir = context.getCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            File tmpFile = new File(cacheDir, picName + ".png");
            return tmpFile;
        }

    }

    public static File getFile(Context context, String picName) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File tmpFile = new File(pic, picName + ".png");
            return tmpFile;
        } else {
            File cacheDir = context.getCacheDir();
            File tmpFile = new File(cacheDir, picName + ".png");
            return tmpFile;
        }

    }
}
