package com.ms.assignment;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by shrawangupta on 05/05/16.
 */
public class MSNetworkHandler {
    private static Context      mInstance;
    private static RequestQueue mRequestQueue;
    private ImageLoader         mImageLoader;
    private static MSNetworkHandler sSelf = new MSNetworkHandler();

    String TAG = MSNetworkHandler.class.getSimpleName();

    private MSNetworkHandler() {

    }

    private MSNetworkHandler(Context ctx) {
        mInstance = ctx;
    }

    public static MSNetworkHandler getInstance(Context ctx) {
        mInstance = ctx;
        return sSelf;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {

            // Instantiate the cache
            Cache cache = new DiskBasedCache(mInstance.getCacheDir(), 5 * 1024 * 1024); // 5MB cap

            // Set up the network to use HttpURLConnection as the HTTP client.
            Network network = new BasicNetwork(new HurlStack());

            // Instantiate the RequestQueue with the cache and network.
            mRequestQueue = new RequestQueue(cache, network);

            // Start the queue
            mRequestQueue.start();
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(String tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TextUtils.isEmpty(tag) ? TAG : tag);
        }
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }
}
