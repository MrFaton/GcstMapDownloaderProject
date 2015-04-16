package com.mr_faton.handler.exception;

/**
 * Created by Mr_Faton on 16.04.2015.
 */
public class GCSTMapDownloaderException extends Exception {
    public GCSTMapDownloaderException() {
    }

    public GCSTMapDownloaderException(String message) {
        super(message);
    }

    public GCSTMapDownloaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public GCSTMapDownloaderException(Throwable cause) {
        super(cause);
    }
}
