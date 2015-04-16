package com.mr_faton.handler.exception;

/**
 * Created by Mr_Faton on 16.04.2015.
 */
public class GCSTMapDownloaderLoginException extends GCSTMapDownloaderException {
    public GCSTMapDownloaderLoginException() {
    }

    public GCSTMapDownloaderLoginException(String message) {
        super(message);
    }

    public GCSTMapDownloaderLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public GCSTMapDownloaderLoginException(Throwable cause) {
        super(cause);
    }
}
