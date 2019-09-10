package com.lee.yueftshare.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class DialogUtil {

    public static Dialog mDialog;

    public interface ConfirmAndCancelListener {
        void cancel();

        void confirm();

    }

    public interface ConfirmAndCancelWithEditTextListener {
        void cancel();

        void confirm(String str);

    }

    public interface ConfirmListener {
        void confirm();
    }

    public static void cancelDialog() {
        if (mDialog != null) {
            mDialog.cancel();
            mDialog = null;
        }
    }

    /**
     * 自定义弹窗，从底部弹起
     *
     * @param customDialog
     * @param dialogView
     * @param context
     */
    public static void customBottomDialog(Dialog customDialog, View dialogView, Context context) {
        //底部开始
        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        localLayoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
        customDialog.onWindowAttributesChanged(localLayoutParams);

        //设置宽度
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        dialogView.setMinimumWidth(screenWidth);

        customDialog.setCanceledOnTouchOutside(false);
        customDialog.setCancelable(true);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setContentView(dialogView);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {
                customDialog.show();
            }
        }
    }

    /**
     * 自定义弹窗，从顶部弹起
     *
     * @param customDialog
     * @param dialogView
     * @param context
     */
    public static void customTopDialog(Dialog customDialog, View dialogView, Activity context) {
        Window win = customDialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.TOP;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        customDialog.setCanceledOnTouchOutside(false);
        customDialog.setCancelable(true);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setContentView(dialogView);
    }
}
