package com.application.cesar.project.chaturl.preview;

/**
 * Created by Cesar on 05/07/2017.
 */

public interface LinkPreviewCallback {

    void onPre();

    /**
     *
     * @param sourceContent
     *            Class with all contents from preview.
     * @param isNull
     *            Indicates if the content is null.
     */
    void onPos(SourceContent sourceContent, boolean isNull);
}