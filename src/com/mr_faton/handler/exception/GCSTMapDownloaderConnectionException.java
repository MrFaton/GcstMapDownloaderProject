package com.mr_faton.handler.exception;

/**
 * Created by Mr_Faton on 16.04.2015.
 */
public class GCSTMapDownloaderConnectionException extends GCSTMapDownloaderException {
    public GCSTMapDownloaderConnectionException() {
    }

    public GCSTMapDownloaderConnectionException(String message) {
        super(message);
    }

    public GCSTMapDownloaderConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public GCSTMapDownloaderConnectionException(Throwable cause) {
        super(cause);
    }
}
