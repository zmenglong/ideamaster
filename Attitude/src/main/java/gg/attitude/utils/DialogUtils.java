package gg.attitude.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import gg.attitude.R;

public final class DialogUtils {
    private DialogUtils(){}

    public static Dialog creatLoadingDialog(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dialog_loading,
                null);
        Dialog dialog = new Dialog(context,R.style.loading_dialog);
        dialog.setContentView(view);
        dialog.setCancelable(false);//不可取消
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


    public static void showChooseDialog(Context c, String[] strings, String title, int themeId, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c, themeId);
        builder.setTitle(title).setItems(strings, listener);
        builder.create().show();
    }

    /**
     * 配合 showConfirmOrCancelDialog 使用 提供两个确认和取消的回掉方法
     */
    public interface OnConfirmCallBack {
        /**
         * 用户点击了确认
         *
         * @param dialog
         */
        void onConfirmed(DialogInterface dialog);

    }

    /**
     * 显示一个确认提示框的Dialog
     *
     * @param c
     * @param s                 描述
     * @param onConfirmCallBack 回掉 DialogUtils.OnConfirmCallBack
     * @return
     */
    public static AlertDialog showConfirmOrCancelDialog(final Context c, String s, final OnConfirmCallBack onConfirmCallBack) {
        return showCustomConfirmOrCancelDialog(c, "操作提示", s, "确定", "取消", onConfirmCallBack);
    }


    /**
     * 显示一个确认提示框的Dialog
     *
     * @param c
     * @param s                 描述
     * @param onConfirmCallBack 回掉 DialogUtils.OnConfirmCallBack
     * @return
     */
    public static AlertDialog showCustomConfirmOrCancelDialog(
            final Context c,
            String title,
            String s,
            String confirmText,
            String cancelText,
            final OnConfirmCallBack onConfirmCallBack) {
        AlertDialog dialog = new AlertDialog.Builder(c)
//                .setIcon(android.R.drawable.ic_menu_info_details)// TODO 此处更换图标
                .setTitle(title)
                .setMessage(s)
                .setNegativeButton(cancelText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(confirmText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onConfirmCallBack != null)
                            onConfirmCallBack.onConfirmed(dialog);
                    }
                })
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }
}