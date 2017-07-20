package mr.li.dance.ui.otherload;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * 作者: Administrator
 * 时间: 2017/5/31.
 * 功能:
 */

public interface BaseUiListener {
    void onComplete(Object var1);

    void onError(UiError var1);

    void onCancel();
}
