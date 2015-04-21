package com.mr_faton.entity;

import java.io.File;

/**
 * Created by root on 21.04.2015.
 */
public class CacheCleaner {
    private File cacheDir = new File(System.getProperty("user.dir") + "\\cache");

    public void cleanCache() {
        if (cacheDir.exists()) {
            File[] filesList = cacheDir.listFiles();
            for (File file : filesList) {
                file.delete();
            }
        }
    }
}
