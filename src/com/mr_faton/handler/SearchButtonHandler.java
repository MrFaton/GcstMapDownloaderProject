package com.mr_faton.handler;

import com.mr_faton.Satements.Variables;
import com.mr_faton.StartProgram;
import com.mr_faton.entity.ErrorMessenger;
import com.mr_faton.entity.GCSTMap;
import com.mr_faton.entity.SettingsWorker;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mr_Faton on 16.04.2015.
 */
public final class SearchButtonHandler {
    private String login;
    private String password;
    public static List<GCSTMap> gcstMapList;//список найденных карт-объектов

    public SearchButtonHandler() {
        //получаем экземпляр работника с файлом настроек
        SettingsWorker settingsWorker = SettingsWorker.getInstance();
        //получаем карту с логином и паролем
        Map<String, String> authorizeMap = settingsWorker.getLoginAndPass();
        //извлекаем из карты логин и пароль
        login = authorizeMap.get("login");
        password = authorizeMap.get("password");
    }

    /*
    основной метод. Результат его работы - найденный список карт по запросу пользователя. Получает заголовок карты и
     глубину поиска
     */
    public String[][] getSearchResult(String mapHeader, String deepSearch) {
        URLConnection connection = null;
        //ответ от сервера с результатми поиска по запросу будет сохранён в этой переменной
        StringBuilder page = new StringBuilder();

        try {
            //настраиваем соединение
            connection = new URL(Variables.MAP_URL).openConnection();
            connection.setConnectTimeout(1 * 60 * 1000);
            connection.setReadTimeout(1 * 60 * 1000);
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", getAuthorizeKey());

            //сформировать POST запрос (параметры запроса берутся из сайта гцст)
            Map<String, String> postParameters = new LinkedHashMap<>();
            postParameters.put("find", mapHeader);
            postParameters.put("Header", "");
            postParameters.put("dip", deepSearch);

            //отправить на сервер ПОСТ запрос
            try (PrintWriter writer = new PrintWriter(connection.getOutputStream())) {
                boolean firstEntry = true;
                for (Map.Entry<String, String> entry : postParameters.entrySet()) {
                    if (firstEntry) {
                        firstEntry = false;
                    } else {
                        writer.print('&');
                    }
                    writer.print(entry.getKey());
                    writer.print('=');
                    writer.print(entry.getValue());
                }
            }

//            прочитать ответ от сервера
            try (Scanner input = new Scanner(connection.getInputStream(), "koi8-u")) {
                while (input.hasNextLine()) {
                    page.append(input.nextLine() + "\n");
                }
            }

            //передаём страницу с результатми методу-парсеру инфы о картых, а назад получаем список карт-обхектов
            List<GCSTMap> gcstMapList = findMaps(page.toString());
            /*
            передаём список карт-объектов, а назад получаем двухмерный массив. Внутренний массив содержит информацию
            которая затем выведется на экран как результаты поиска, а именно это: имя карты, её заголовк и термин
             */
            String[][] convertedMapList = getConvertedMapList(gcstMapList);
            //вовзращаем строки для таблицы, содержащие имя карты, её заголовк и термин
            return convertedMapList;
        } catch (IOException outerEx) {
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            Integer responseCode = 0;
            try {
                responseCode = httpURLConnection.getResponseCode();
            } catch (IOException innerEx) {
                new ErrorMessenger("Возможно на вашем компьютере отсутствует продключение к интернет");
            }

            if (responseCode != 0 && responseCode != null) {
                switch (responseCode) {
                    case 401: {
                        new ErrorMessenger("Внимание! Логин или пароль не верен!\n" +
                                "Попробуйте зайти в настройки и заменить логин и пароль.");
                        break;
                    }
                }
            } else {
                new ErrorMessenger("Обнаружена критическая ошибка.\n" +
                        "Программа будет завершена...");
                StartProgram.mainFrame.dispose();
                System.exit(-1);
                outerEx.printStackTrace();
            }

            return null;
        }
    }

    //возвращает логин и пароль в виде закодированной по Base64 кодировки строки
    private String getAuthorizeKey() {
        String authorization = login + ":" + password;
        authorization = Base64.getEncoder().encodeToString(authorization.getBytes());
        return "Basic " + authorization;
    }

    /*
    парсит всю информацию о каждой карте и сохраняет её в один объект типа GCSTMap, а объект в список таких объектов.
    Параметр - ответ от сервера с результатми поисков
     */
    private List<GCSTMap> findMaps(String htmlPage) {
        gcstMapList = new LinkedList<>();
        String downloadLink = "";
        String mapName = "";
        String mapHeader = "";
        String mapTerm = "";

        String searchRegExp = "" +
                ".*class=result><a href=\"(.*?&s=1)\">|" + /*download link*/
                ".*class=result><a href=\".*?>(.*?)<a|" + /*map name*/
                ".*?tt>(.*?)</tt|" + /*map header*/
                ".*class=result>(.*?)<"; /*map term*/
        Pattern pattern = Pattern.compile(searchRegExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(htmlPage);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                downloadLink = matcher.group(1).trim();
            }
            if (matcher.group(2) != null) {
                mapName = matcher.group(2);
            }
            if (matcher.group(3) != null) {
                mapHeader = matcher.group(3);
            }
            if (matcher.group(4) != null) {
                mapTerm = matcher.group(4).trim();

                gcstMapList.add(new GCSTMap(downloadLink, mapName, mapHeader, mapTerm));
            }

        }
        return gcstMapList;
    }

    //преобразовывает список карт-объектов в двухмерный массив, который содержит только 3 колонки
    private String[][] getConvertedMapList(List<GCSTMap> mapList) {
        //для того чтобы данные выводились в обратном порядке (от саой давней карты, до самой новой)
        Collections.reverse(mapList);
        /*
        парметры для таблицы, которая будет сформирована на остнове полученных результатов поиска

        т.к. у нас в будущей таблице только 3 колонки (имя карты, заголовок и термин), т.е. это кол-во ячеек
        во внутреннем массиве, т.е. внутренний масси состоит из 3-х ячеек
         */
        int columnCount = 3;
        /*
        кол-во строк будущей таблице. Оно равно колличеству найденных поиском и собранных парсером карт. т.е. это
        кол-во ячеек внешнего массива. т.е. в одну это ячейку помещается массив, содержащий
        (имя карты, заголовок и термин)
         */
        int rowCount = mapList.size();
        //создаём двухмерный массив
        String[][] convertedMaps = new String[rowCount][columnCount];
        /*
        проходимся по списку карт-объектов и помещаем необходимые нам поля в двухмерный массив (из него будет состоять
        таблица с результатми на главном окне)
         */
        int i = 0;
        for (GCSTMap gcstMap : mapList) {
            convertedMaps[i][0] = gcstMap.getName();
            convertedMaps[i][1] = gcstMap.getHeader();
            convertedMaps[i][2] = gcstMap.getTerm();
            i++;
        }
        //возвращаем двухмерный массив
        return convertedMaps;
    }
}
