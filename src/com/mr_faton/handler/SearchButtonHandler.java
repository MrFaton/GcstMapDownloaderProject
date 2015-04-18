package com.mr_faton.handler;

import com.mr_faton.Satements.Variables;
import com.mr_faton.entity.GCSTMap;
import com.mr_faton.entity.SettingsWorker;

import java.io.IOException;
import java.io.PrintWriter;
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

    public SearchButtonHandler() {
        SettingsWorker settingsWorker = SettingsWorker.getInstance();
        login = "";
        password = "";
        System.out.println(login + "=" + password);
    }

    public String[][] getSearchResult(String mapHeader, String deepSearch) {
        URLConnection connection;
        StringBuilder page = new StringBuilder();

        try {
            connection = new URL(Variables.MAP_URL).openConnection();
            connection.setConnectTimeout(1 * 60 * 1000);
            connection.setReadTimeout(1 * 60 * 1000);
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", getAuthorizeKey());

            //сформировать POST запрос
            System.out.println(mapHeader + "=" + deepSearch);
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

            List<GCSTMap> gcstMapList = findMaps(page.toString());
            String[][] convertedMapList = getConvertedMapList(gcstMapList);
            return convertedMapList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getAuthorizeKey() {
        String authorization = login + ":" + password;
        authorization = Base64.getEncoder().encodeToString(authorization.getBytes());
        return "Basic " + authorization;
    }

    private List<GCSTMap> findMaps(String htmlPage) {
        List<GCSTMap> gcstMapList = new LinkedList<>();
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
//        for (GCSTMap gcstMap : gcstMapList) {
//            System.out.println(gcstMap.toString());
//        }


        return gcstMapList;
    }

    private String[][] getConvertedMapList(List<GCSTMap> mapList) {
        Collections.reverse(mapList);//для того чтобы данные выводились в обратном порядке
        int columnCount = 3;
        int rowCount = mapList.size();//т.к. у нас в таблице только 3 колонки
        String[][] convertedMaps = new String[rowCount][columnCount];
        int i = 0;
        for (GCSTMap gcstMap : mapList) {
            convertedMaps[i][0] = gcstMap.getName();
            convertedMaps[i][1] = gcstMap.getHeader();
            convertedMaps[i][2] = gcstMap.getTerm();
            i++;
        }
        return convertedMaps;
    }
}
