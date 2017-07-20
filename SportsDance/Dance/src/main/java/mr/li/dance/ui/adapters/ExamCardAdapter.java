package mr.li.dance.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import mr.li.dance.R;
import mr.li.dance.models.CertificateInfo;
import mr.li.dance.utils.DensityUtil;
import mr.li.dance.utils.FileUtils;

/**
 * Created by Lixuewei on 2017/6/4.
 */

public class ExamCardAdapter extends BaseRecyclerAdapter<CertificateInfo> {
    public ExamCardAdapter(Context ctx) {
        super(ctx);
    }


    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_examcard;

    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, CertificateInfo item) {
        holder.setImageByUrlOrFilePath(R.id.imageView2, item.getPhotopath(), R.drawable.kaoji_defaulticon);
        holder.setText(R.id.name_tv, item.getName());
        holder.setText(R.id.sex_tv, item.getGender());
        holder.setText(R.id.shengri_tv, item.getArtlevel());
        holder.setText(R.id.shenfenzheng_tv, item.getBirthday());
        holder.setText(R.id.zhengshu_tv, item.getExamroom());
        holder.setText(R.id.bianhao_tv, item.getExamnumber());
        holder.setImageByUrlOrFilePath(R.id.erweima_icon, create2DCoderBitmap(item.getExamnumber()), R.drawable.default_video);
    }

    public String create2DCoderBitmap(String url) {
        int QR_WIDTH = DensityUtil.dip2px(mContext,167);
        int QR_HEIGHT = QR_WIDTH;
        Bitmap bitmap = null;
        try {
            // 判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            // 显示到一个ImageView上面
        } catch (WriterException e) {

        }
        if (bitmap != null) {
            return saveBitmap(bitmap, url);

        } else {
            return null;
        }
    }

    public String saveBitmap(Bitmap bm, String bianhao) {
        File f = FileUtils.createTmpFile(mContext, bianhao);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            bm.recycle();
        }
        return f.getPath();
    }
}
