package gg.attitude.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.util.UUID;

import gg.attitude.api.ConstantValues;
import gg.attitude.helper.PreferenceHelper;
import gg.attitude.views.CircleImageView;

public final class PhotoUtils {

    public static final int CAMERA = 1;

    private PhotoUtils(){}


    public static void openCamera(Activity c) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// ACTION_OPEN_DOCUMENT
        c.startActivityForResult(intent, CAMERA);
    }

    public static void openPhoto(Activity c) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            c.startActivityForResult(intent, ConstantValues.SELECT_PIC_KITKAT);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// ACTION_OPEN_DOCUMENT
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            c.startActivityForResult(intent, ConstantValues.SELECT_PIC);
        }
    }

    public static void handleCameraOrPhotoResult(Activity activity
            , int requestCode, int resultCode, Intent data
            , CircleImageView circleImageView, OnResetAvatarResultListener mListener) {

        //当被取消时，return
        if (resultCode==0){
            return;
        }
        if (requestCode != CAMERA) {// 相册
            Uri uri = data.getData();
            String path = "";
            if (uri != null)
                path = uri.toString();

            if (path.contains("content://")) {
                uri = Uri.parse(path);
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualimagecursor = activity.managedQuery(uri, proj, null, null, null);
                if (actualimagecursor != null) {
                    int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    actualimagecursor.moveToFirst();
                    path = actualimagecursor.getString(actual_image_column_index);
                } else {
                    path = "";
                }
            }
            String matchString = "(?i).+?\\.(jpg|gif|bmp|jpeg|png)";
            if (!path.isEmpty()) {
                if (path.trim().matches(matchString)) {
                    PreferenceHelper.write(activity, ConstantValues.SP_CONFIG, "photo", path);
                    mListener.onSuccess(path);
                } else {
                    mListener.onFailed("只能选择格式为(jpg|gif|bmp|jpeg|png)的图片");
//                    Toast.makeText(activity,"只能选择格式为(jpg|gif|bmp|jpeg|png)的图片",Toast.LENGTH_SHORT).show();
                }
            } else {
                mListener.onFailed("选择图片失败了,可能是没有兼容到您的系统...");
//                Toast.makeText(activity,"选择图片失败了,可能是没有兼容到您的系统...",Toast.LENGTH_SHORT).show();
            }
        } else {// 相机
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("SD not avaiable now.","-.-");
                mListener.onFailed("SD不可存在或不可用.");
//                Toast.makeText(activity,"SD不可存在或不可用.",Toast.LENGTH_SHORT).show();
                return;
            } else {
                String path = FileUtils.getSDCardPath() + new PreferenceHelper(activity).getFilePath() + "myPhotos/" + UUID.randomUUID().toString() + ".jpg";
                if (FileUtils.bitmapToFile(bitmap, path)) {
//                    roundImageView.setImageBitmap(bitmap);
                    PreferenceHelper.write(activity, ConstantValues.SP_CONFIG, "photo", path);
                    mListener.onSuccess(path);
                } else
                    mListener.onFailed("存储文件失败...");
//                    Toast.makeText(activity,"存储文件失败...",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface OnResetAvatarResultListener {
        void onSuccess(String filePath);
        void onFailed(String errorMsg);
    }


    private static final int COLORDRAWABLE_DIMENSION = 1;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    public static Bitmap bitmap2Drawable(Drawable mDrawable){
        if (mDrawable == null) {
            return null;
//			throw new NullPointerException("DrawableśÔĎóÎŞnull");
        }
        if (mDrawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)mDrawable).getBitmap();
        }

        try {
            Bitmap mBitmap;
            if (mDrawable instanceof ColorDrawable) {
                mBitmap=Bitmap.createBitmap(COLORDRAWABLE_DIMENSION
                        , COLORDRAWABLE_DIMENSION,BITMAP_CONFIG);
            }else {
                mBitmap=Bitmap.createBitmap(mDrawable.getIntrinsicWidth()
                        ,mDrawable.getIntrinsicHeight(),BITMAP_CONFIG);
            }
            Canvas canvas=new Canvas(mBitmap);
            mDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            mDrawable.draw(canvas);
            return mBitmap;
        } catch (OutOfMemoryError e) {
            // TODO ×ÔśŻÉúłÉľÄ catch żé
            e.printStackTrace();
            return null;
        }
    }
}