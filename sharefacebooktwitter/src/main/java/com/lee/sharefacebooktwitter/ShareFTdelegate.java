package com.lee.sharefacebooktwitter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.functions.Consumer;

/**
 * by yue ,created 2019-8-17
 * 分享至facebook twitter的代理类，也算是presenter类
 * 升级版本直接copy过去
 */
public class ShareFTdelegate {

    private String TAG = ShareFTdelegate.class.getSimpleName();
    private Uri uri;
    private Bitmap bitmap;
    private ShareDialog shareDialog;
    private AppCompatActivity mActivity;
    private CallbackManager callbackManager;
    private ShareListener listener;

    private final int TWEET_COMPOSER_REQUEST_CODE = 0x1;

    /**点击"save"按钮后将生成的图片保存在本地*/
    public void saveImg(RecyclerView recView ,SaveImgListener saveListener){
        Bitmap bitmap = shotRecyclerView(recView);
        if (bitmap != null) {
            Uri uri = saveImageToGallery(mActivity, bitmap);
            if(uri != null){
                if(saveListener != null)
                    saveListener.saveSuc();
                else
                    saveListener.saveFail();
            }
        }

    }

    public ShareFTdelegate(AppCompatActivity mActivity , ShareListener listener){
        this.mActivity = mActivity;
        shareDialog = new ShareDialog(mActivity);
        callbackManager = CallbackManager.Factory.create();
        this.listener = listener;
    }


    public void shareToTitter(RecyclerView recView){
        Bitmap bitmap = shotRecyclerView(recView);
        if (bitmap != null) {
            Uri uri = saveImageToGallery(mActivity, bitmap);
            if (uri != null) {
                shareYourTwitter(uri);
            }
        }

    }
    public void shareYourTwitter(Uri uri) {
        Intent intent = new TweetComposer.Builder(mActivity).image(uri).createIntent();

        mActivity.startActivityForResult(intent, TWEET_COMPOSER_REQUEST_CODE);
    }

    public void shareToFaceBook(RecyclerView recView){
        Bitmap bitmap = shotRecyclerView(recView);
        if (bitmap != null) {
            shareYourFb(bitmap);
        }
    }

    public void shareYourFb(Bitmap sharedBmp) {
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(sharedBmp)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        shareDialog.show(mActivity, content);

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                listener.shareSuc();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                listener.shareFail();
                Log.e("facebook error","error = "+error);
            }
        });

    }

    public  Bitmap shotRecyclerView(RecyclerView view) {
        RecyclerView.Adapter adapter = view.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(
                        View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(),
                        holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            Drawable lBackground = view.getBackground();
            if (lBackground instanceof ColorDrawable) {
                ColorDrawable lColorDrawable = (ColorDrawable) lBackground;
                int lColor = lColorDrawable.getColor();
                bigCanvas.drawColor(lColor);
            }

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }
        }
        this.bitmap = bigBitmap;
        return bigBitmap;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public Uri saveImageToGallery(final AppCompatActivity mActivity, final Bitmap bmp) {
        RxPermissions rxPermissions = new RxPermissions(mActivity);
        rxPermissions.
                request(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE).
                subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean)  {
                        if(aBoolean){
                            String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                            String dirName = "erweima16";
                            File appDir = new File(root , dirName);
                            if (!appDir.exists()) {
                                appDir.mkdirs();
                            }

                            long timeStamp = System.currentTimeMillis();
                            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String sd = sdf.format(new Date(timeStamp));
                            String fileName = sd + ".jpg";

                            File file = new File(appDir, fileName);
                            FileOutputStream fos = null;
                            try {
                                fos = new FileOutputStream(file);
                                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                fos.flush();
                                mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                        Uri.fromFile(new File(file.getPath()))));
                                uri = FileProvider.getUriForFile(mActivity,mActivity.getApplicationContext().getPackageName()+".provider",new File(file.getPath()));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
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
                        }
                    }
                });



        return uri;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TWEET_COMPOSER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                listener.shareSuc();
            } else {
               Log.e("TZC2.0","resultCode = "+requestCode);
               listener.shareFail();
            }
        }
    }



    public interface ShareListener{

        void shareSuc();
        void shareFail();

   }

   public interface SaveImgListener{
        void saveSuc();
        void saveFail();
   }

}
