package com.mr_faton.entity;

import com.mr_faton.Satements.Variables;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Mr_Faton on 18.04.2015.
 */
public final class SettingsWorker {
    private File settingsFile;
    DocumentBuilderFactory documentBuilderFactory;
    DocumentBuilder documentBuilder;
    Document document;
    private static SettingsWorker settingsWorker = null;

    private SettingsWorker() {
        settingsFile = new File("C:\\GcstMapDownloader\\Settings.xml");
//        try {
//            settingsFile = new File(SettingsWorker.class
//                    .getProtectionDomain()
//                    .getCodeSource()
//                    .getLocation()
//                    .toURI()
//                    .getPath() + "\\Settings.xml");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }

        try {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            if (settingsFile.exists()) {
                document = documentBuilder.parse(settingsFile);
            } else {
                document = createEmptySettingsFile();
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized SettingsWorker getInstance() {
        if (settingsWorker == null) {
            settingsWorker = new SettingsWorker();
        }
        return settingsWorker;
    }

    public Map<String, String> getLoginAndPass() {
        Node loginNode = document.getElementsByTagName(Variables.LOGIN_STR).item(0);
        Node passNode = document.getElementsByTagName(Variables.PASSWORD_STR).item(0);
        String login = loginNode.getTextContent();
        String password = passNode.getTextContent();
        HashMap<String, String> mapAuthorize = new HashMap<>(3, 1);
        mapAuthorize.put(Variables.LOGIN_STR, login);
        mapAuthorize.put(Variables.PASSWORD_STR, password);
        return mapAuthorize;
    }

    public void setLoginAndPass(Map<String, String> authorizeMap) {
        String login = authorizeMap.get(Variables.LOGIN_STR);
        String password = authorizeMap.get(Variables.PASSWORD_STR);

        Node loginNode = document.getElementsByTagName(Variables.LOGIN_STR).item(0).getFirstChild();
        Node passNode = document.getElementsByTagName(Variables.PASSWORD_STR).item(0).getFirstChild();

        loginNode.setNodeValue(login);
        passNode.setNodeValue(password);

        saveSettings(document);
    }

    public Map<String, String> getAllPatterns() {
        LinkedHashMap<String, String> patternsMap = new LinkedHashMap<>();
        NodeList patternsList = document.getElementsByTagName("pattern");
        Element pattern = null;
        Node mapNameNode = null;
        Node mapHeaderNode = null;
        //for each pattern
        for (int i = 0; i < patternsList.getLength(); i++) {
            pattern = (Element) patternsList.item(i);
            mapNameNode = pattern.getElementsByTagName("map_name").item(0);
            mapHeaderNode = pattern.getElementsByTagName("map_header").item(0);

            patternsMap.put(mapNameNode.getTextContent(), mapHeaderNode.getTextContent());
        }
        return patternsMap;
    }

    private synchronized void saveSettings(Document docToWrite) {
        try (FileOutputStream fileOutput = new FileOutputStream(settingsFile)) {
            //построить холостое преобразование
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            //установить отступ
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            //выполнить холостое преобразование и вывести узел в файл
            transformer.transform(new DOMSource(docToWrite), new StreamResult(fileOutput));
        } catch (IOException | TransformerException ex) {
            ex.printStackTrace();
        }
    }

    private Document createEmptySettingsFile() {
        Document doc = documentBuilder.newDocument();

        Element settings = doc.createElement("settings");

        Element program_settings = doc.createElement("program_settings");
        Element authorization = doc.createElement("authorization");

        Element login = doc.createElement(Variables.LOGIN_STR);
        Text loginText = doc.createTextNode("логин");
        login.appendChild(loginText);

        Element password = doc.createElement(Variables.PASSWORD_STR);
        Text passwordText = doc.createTextNode("пароль");
        password.appendChild(passwordText);

        authorization.appendChild(login);
        authorization.appendChild(password);

        program_settings.appendChild(authorization);

        Element patterns = doc.createElement("patterns");

        Element pattern = doc.createElement("pattern");

        Element map_name = doc.createElement("map_name");
        Text map_nameText = doc.createTextNode("имя карты");
        map_name.appendChild(map_nameText);

        Element map_header = doc.createElement("map_header");
        Text map_headerText = doc.createTextNode("заголовок карты");
        map_header.appendChild(map_headerText);

        pattern.appendChild(map_name);
        pattern.appendChild(map_header);

        patterns.appendChild(pattern);

        settings.appendChild(program_settings);
        settings.appendChild(patterns);

        doc.appendChild(settings);

        saveSettings(doc);
        return doc;
    }
}

class Test {
    public static void main(String[] args) {
        SettingsWorker settingsWorker = SettingsWorker.getInstance();

//        Map<String, String> map = new HashMap<>();
//        map.put("login", "kharmet");
//        map.put("password", "9NCzR##?3z");
//        settingsWorker.setLoginAndPass(map);

//        System.out.println(settingsWorker.getLoginAndPass());

        System.out.println(settingsWorker.getAllPatterns());
    }
}