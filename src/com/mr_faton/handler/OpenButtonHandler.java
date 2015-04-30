package com.mr_faton.handler;

import com.mr_faton.Satements.Variables;
import com.mr_faton.entity.ErrorMessenger;
import com.mr_faton.entity.GCSTMap;
import com.mr_faton.entity.SettingsWorker;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 21.04.2015.
 */
public final class OpenButtonHandler {
    private static List<GCSTMap> gcstMapList;//список найденных карт-объектов
    private File outputImageFile;//выходной файл, т.е. куда будем сохранять скачанную карту
    private File outputDirectory;//кеш папка, сюда будет загружена выбранаая пользователем карта
    private SettingsWorker settingsWorker = SettingsWorker.getInstance();//обработчик файла с настройками

    public OpenButtonHandler() {
        //получить мапу найденных карт из обработчика кнопки "Искать"
        gcstMapList = SearchButtonHandler.gcstMapList;
        outputImageFile = null;
        //получаем кеш папку
        outputDirectory = new File(System.getProperty("user.dir") + "\\" + settingsWorker.getCacheDir());
        //если кеш папки не существует, создаём её
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }
    }

    //открывает карту в редакторе. Параетр для метода - заголовок карты
    public void openMapInEditor(String mapHeader) {
        //пройтись по списку карт-объектов и найти ту объект карту, которая содержит данный заголовок
        for (GCSTMap map : gcstMapList) {
            //если такая объект-карта найдена
            if (map.getHeader().equals(mapHeader)) {
                //создаём для карты конечный файл с именем заголовка карты и расширением bmp
                outputImageFile = new File(outputDirectory.getAbsolutePath() + "\\" + mapHeader + ".bmp");
                //передаём управление загрузчику карт и уникальную часть ссылки для загрузки карты
                downloadAndOpenMap(map.getDownloadLink());
                //прервываем цикл
                break;
            }
        }
    }

    //качает карту и передаёт её в редактор карт
    private void downloadAndOpenMap(String secondPartOfDownloadLink) {
        /*
        берём ссылку для поиска карт на сайте и заменяем её концовку уникальной ссылкой для загрузки карты и получаем
        премую ссылку на скачивание файла (картинки в формате png)
         */
        String downloadLink = Variables.MAP_URL.replace("index.phtml", secondPartOfDownloadLink);
        //окрываем соединение по сформированной ссылке
        try {
            URLConnection connection = new URL(downloadLink).openConnection();
            connection.setConnectTimeout(1 * 60 * 1000);
            connection.setReadTimeout(1 * 60 * 1000);
            //добавляем к запросу авторазационные данные
            connection.setRequestProperty("Authorization", getAuthorizeKey());
            connection.connect();

            //читаем ответ от сервера (это изображение в фомате "png")
            BufferedImage bufferedImage = ImageIO.read(connection.getInputStream());
            //сохраняем уже в формате "bmp"
            ImageIO.write(bufferedImage, "bmp", outputImageFile);

            //получаем путь к экзешнику эдитора карт
            String mapEditorPath = settingsWorker.getMapEditorPath();
            //стартуем карт этодор и передаём ему параметром нашу скачаную и переконвертированную карту
            new ProcessBuilder(mapEditorPath, outputImageFile.getAbsolutePath()).start();
        } catch (IOException e) {
            new ErrorMessenger("Вы указали неверный путь к программе редактору карт:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    //получает логин и пароль из файла настроек и возвращает в виде закодированной по Base64 кодировки строку
    private String getAuthorizeKey() {
        Map<String, String> authMap = settingsWorker.getLoginAndPass();
        String login = authMap.get("login");
        String password = authMap.get("password");
        String authorization = login + ":" + password;
        authorization = Base64.getEncoder().encodeToString(authorization.getBytes());
        return "Basic " + authorization;
    }
}
/*
Объект этого класса создаётся когда выбрали карту из списка найденных и нажали кнопку открыть. Или же по двойному
щелчку на строке с найденной картой
 */