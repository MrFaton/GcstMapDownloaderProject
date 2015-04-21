package com.mr_faton.handler;

import com.mr_faton.Satements.Variables;
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
    private static List<GCSTMap> gcstMapList;
    private File outputImageFile;
    private File outputDirectory;
    private SettingsWorker settingsWorker = SettingsWorker.getInstance();

    public OpenButtonHandler() {
        gcstMapList = SearchButtonHandler.gcstMapList;
        outputImageFile = null;
        outputDirectory = new File(System.getProperty("user.dir") + "\\cache");
        if (!outputDirectory.exists()) {
            outputDirectory.mkdir();
        }
    }

    public void openMapInEditor(String mapHeader) {
        for (GCSTMap map : gcstMapList) {
            if (map.getHeader().equals(mapHeader)) {
                outputImageFile = new File(outputDirectory.getAbsolutePath() + "\\" + mapHeader + ".bmp");
                downloadAndOpenMap(mapHeader, map.getDownloadLink());
                break;
            }
        }
    }

    private void downloadAndOpenMap(String mapHeader, String secondPartOfDownloadLink) {
        String downloadLink = Variables.MAP_URL.replace("index.phtml", secondPartOfDownloadLink);
        try {
            URLConnection connection = new URL(downloadLink).openConnection();
            connection.setConnectTimeout(1 * 60 * 1000);
            connection.setReadTimeout(1 * 60 * 1000);
            connection.setRequestProperty("Authorization", getAuthorizeKey());
            connection.connect();


            BufferedImage bufferedImage = ImageIO.read(connection.getInputStream());
            ImageIO.write(bufferedImage, "bmp", outputImageFile);

            String mapEditorPath = settingsWorker.getMapEditorPath();
            new ProcessBuilder(mapEditorPath, outputImageFile.getAbsolutePath()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getAuthorizeKey() {
        Map<String, String> authMap = settingsWorker.getLoginAndPass();
        String login = authMap.get("login");
        String password = authMap.get("password");
        String authorization = login + ":" + password;
        authorization = Base64.getEncoder().encodeToString(authorization.getBytes());
        return "Basic " + authorization;
    }
}
