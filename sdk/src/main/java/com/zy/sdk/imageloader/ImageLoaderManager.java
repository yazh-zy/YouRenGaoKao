package com.zy.sdk.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zy.sdk.R;

/**
 * @author Yang
 * @function 初始化UniverImageLoader,并且用来加载网络图片
 * @date 2017/3/16.
 */

public class ImageLoaderManager {

    /**
     * 默认参数配置
     */
    private static final int THREAD_COUNT = 4; //最多可以有多少线程
    private static final int PROPRITY = 2; //图片加载优先级
    private static final int DISK_CACHE_SIZE = 50 * 1024; //缓存多少图片
    private static final int CONNECTION_TIME_OUT = 5*1000; //连接超时时间
    private static final int READ_TIME_OUT = 30 * 1000; //读取超时时间

    private static ImageLoader mImageLoader = null;
    private static ImageLoaderManager mInstance = null;

    public static ImageLoaderManager getInstance (Context context) {
        if (mInstance == null) {
            synchronized (ImageLoaderManager.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderManager(context);
                }
            }

        }
        return mInstance;
    }

    /**
     * 单例模式的私有构造方法
     * @param context
     */
    private ImageLoaderManager(Context context) {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(context)
                .threadPoolSize(THREAD_COUNT) //
                .threadPriority(Thread.NORM_PRIORITY - PROPRITY) //配置图片下载线程的最大数量
                .denyCacheImageMultipleSizesInMemory() //防止缓存多套尺寸图片
                .memoryCache(new WeakMemoryCache()) //使用弱引用内存缓存
                .diskCacheSize(DISK_CACHE_SIZE) //分配硬盘缓存大小
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //使用MD5命名
                .tasksProcessingOrder(QueueProcessingType.LIFO) //图片下载顺序
                .defaultDisplayImageOptions(getDefultOptions()) //默认图片加载Options
                .imageDownloader(new BaseImageDownloader(context, CONNECTION_TIME_OUT, READ_TIME_OUT)) //设置图片下载器
                .writeDebugLogs() //debug环境下会输出日志
                .build();

        ImageLoader.getInstance().init(configuration);
        mImageLoader = ImageLoader.getInstance();
    }

    /**
     * 实现默认的options
     * @return
     */
    private DisplayImageOptions getDefultOptions() {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.nopic) //图片地址为空加载的默认图片
                .showImageOnFail(R.drawable.nopic) //图片下载失败加载的默认图片
                .cacheInMemory(true) //设置图片可以缓存在内存
                .cacheOnDisk(true) //设置图片可以缓存在硬盘
                .bitmapConfig(Bitmap.Config.RGB_565) //使用图片解码类型
                .decodingOptions(new BitmapFactory.Options()) //图片解码配置
                .build();
        return options;
    }

    /**
     * 加载图片api
     * @param imageView
     * @param url
     * @param options
     * @param listener
     */
    public void displayImage(ImageView imageView, String url, DisplayImageOptions options, ImageLoadingListener listener) {

        if (mImageLoader != null) {
            mImageLoader.displayImage(url, imageView, options, listener);
        }
    }

    public void displayImage(ImageView imageView, String url, ImageLoadingListener listener) {
        displayImage(imageView, url, null, listener);
    }

    public void displayImage(ImageView imageView, String url) {
        displayImage(imageView, url, null);
    }
}
