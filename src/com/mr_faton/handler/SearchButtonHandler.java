package com.mr_faton.handler;

import com.mr_faton.Satements.Variables;
import com.mr_faton.handler.exception.GCSTMapDownloaderConnectionException;
import com.mr_faton.handler.exception.GCSTMapDownloaderLoginException;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by Mr_Faton on 16.04.2015.
 */
public final class SearchButtonHandler {
    private String mapHeader;
    private String deepSearch;
    private URLConnection connection;


    public String[][] getSearchResult(String mapHeader, String deepSearch) {
        this.mapHeader = mapHeader;
        this.deepSearch = deepSearch;

        try {
            checkConnection();
            checkLogin();

        } catch (GCSTMapDownloaderConnectionException ex) {
            System.out.println("No connection...");
        } catch (GCSTMapDownloaderLoginException ex) {
            System.out.println("Bad Login");
        } catch (IOException ex) {
            System.out.println("Some SystemException");
            ex.printStackTrace();
        }

        return null;
    }

    private void checkConnection() throws IOException, GCSTMapDownloaderConnectionException {
        connection = new URL(Variables.MAIN_PAGE_URL).openConnection();
        connection.setConnectTimeout(2 * 60 * 1000);//ожидать соединение 2 мин
        connection.setReadTimeout(1 * 60 * 1000);//читать из канала не дольше 1 мин
        connection.connect();

        Scanner in = new Scanner(connection.getInputStream());
        if (!in.hasNextLine()) {
            throw new GCSTMapDownloaderConnectionException("НЕ могу прочитать данные с главной страницы");
        } else {
            System.out.println("connection successful");
        }
    }

    private void checkLogin() throws IOException, GCSTMapDownloaderLoginException {

    }

}
