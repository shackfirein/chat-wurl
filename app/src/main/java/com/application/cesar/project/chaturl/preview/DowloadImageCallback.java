package com.application.cesar.project.chaturl.preview;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Cesar on 05/07/2017.
 */

public interface DowloadImageCallback {

    /**
     *
     * @param imageView
     *            ImageView to receive the bitmap.
     * @param loadedBitmap
     *            Bitmap downloaded from url.
     * @param url
     *            Image url.
     */
    void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url);

}
