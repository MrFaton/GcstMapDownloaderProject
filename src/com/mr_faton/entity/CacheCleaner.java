package com.mr_faton.entity;

import java.io.File;

/**
 * Created by root on 21.04.2015.
 */
public class CacheCleaner {
    //папка кэша
    private File cacheDir;

    public CacheCleaner() {
        //получить имя папки кэша из файла настроек
        String cacheDirName = SettingsWorker.getInstance().getCacheDir();
        //сформировать путь к папке с кэшем
        cacheDir = new File(System.getProperty("user.dir") + "\\" + cacheDirName);
    }

    //удаляет все файлы в папке с кешем
    public void cleanCache() {
        //если папка с кешем существует, то удалить в ней все файлы
        if (cacheDir.exists()) {
            File[] filesList = cacheDir.listFiles();
            for (File file : filesList) {
                file.delete();
            }
        }
    }
}
/*
Внутри корневой папки проекта (там, где лежит jar-архив) храниться папка кэша - когда пользователь пытается открыть
выбранную карту в редакторе, то карта сначала загружается в эту папку, а затем открывается в редакторе. Объект этого
класса удаляет все файлы из этой папки при старте программы.
 */