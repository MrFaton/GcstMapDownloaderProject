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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Mr_Faton on 18.04.2015.
 */
public final class SettingsWorker {
    private File settingsFile;//файл с настройками
    DocumentBuilderFactory documentBuilderFactory;
    DocumentBuilder documentBuilder;
    Document document;//наш xml документ
    private static SettingsWorker settingsWorker = null;//наш класс - обработчик xml должен быть синглетоном

    //приватный конструктор нашего класса, т.к. он синглетон
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
                //если файл существует, парсим из него данные
                document = documentBuilder.parse(settingsFile);
            } else {
                //если файл не существует создаём пустой - дефолтный файл настроек
                document = createEmptySettingsFile();
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            //при возникновении каких-либо ошибок при чтении файла (значит он повреждён) создаём дефолтный файл настроек
            document = createEmptySettingsFile();
        }
    }

    //ДОСТУПНЫЕ ДЛЯ ВСЕХ МЕТОДЫ

    //получить экземпляр нашего класса - обработчика xml файла с настройками
    public static synchronized SettingsWorker getInstance() {
        if (settingsWorker == null) {
            settingsWorker = new SettingsWorker();
        }
        return settingsWorker;
    }

    //получить логин и пароль
    public Map<String, String> getLoginAndPass() {
        Node loginNode = document.getElementsByTagName("login").item(0);
        Node passNode = document.getElementsByTagName("password").item(0);
        String login = loginNode.getTextContent();
        String password = passNode.getTextContent();
        Map<String, String> mapAuthorize = new LinkedHashMap<>(3, 1);
        mapAuthorize.put("login", login);
        mapAuthorize.put("password", password);
        return mapAuthorize;
    }

    //установить логин и пароль
    public void setLoginAndPass(Map<String, String> authorizeMap) {
        String login = authorizeMap.get("login");
        String password = authorizeMap.get("password");

        Node loginNode = document.getElementsByTagName("login").item(0).getFirstChild();
        Node passNode = document.getElementsByTagName("password").item(0).getFirstChild();

        loginNode.setNodeValue(login);
        passNode.setNodeValue(password);

        saveSettings(document);
    }

    //получить путь к редактору карт
    public String getCardEditorPath() {
        Node cardEditorNode = document.getElementsByTagName("card_editor").item(0);
        return cardEditorNode.getTextContent();
    }

    //установить путь для редактора карт
    public void setCardEditorPath(String path) {
        Node cardEditorNode = document.getElementsByTagName("card_editor").item(0).getFirstChild();
        cardEditorNode.setNodeValue(path);

        //можно было устанавлитвать значение и таким образом
//        Node cardEditorNode = document.getElementsByTagName("card_editor").item(0);
//        cardEditorNode.setTextContent(path);

        saveSettings(document);
    }

    //получить все имена и заголовки карт
    public Map<String, String> getAllPatterns() {
        LinkedHashMap<String, String> patternsMap = new LinkedHashMap<>();
        NodeList patternList = document.getElementsByTagName("pattern");
        Element pattern;
        Node mapNameNode;
        Node mapHeaderNode;
        //for each pattern
        for (int i = 0; i < patternList.getLength(); i++) {
            pattern = (Element) patternList.item(i);
            mapNameNode = pattern.getElementsByTagName("map_name").item(0);
            mapHeaderNode = pattern.getElementsByTagName("map_header").item(0);

            patternsMap.put(mapNameNode.getTextContent(), mapHeaderNode.getTextContent());
        }
        return patternsMap;
    }

    //заменить все имена и заголовки карт данными именами и заголовками
    public void setPatterns(Map<String, String> patternsMap) {
        deleteAllPatterns();

        Node patternsNode = document.getElementsByTagName("patterns").item(0);
        for (Map.Entry<String, String> entry : patternsMap.entrySet()) {
            String mapName = entry.getKey();
            String mapHeader = entry.getValue();

            Element patternElement = document.createElement("pattern");

            Element mapNameElement = document.createElement("map_name");
            Text mapNameElementText = document.createTextNode(mapName);
            mapNameElement.appendChild(mapNameElementText);

            Element mapHeaderElement = document.createElement("map_header");
            Text mapHeaderElementText = document.createTextNode(mapHeader);
            mapHeaderElement.appendChild(mapHeaderElementText);

            patternElement.appendChild(mapNameElement);
            patternElement.appendChild(mapHeaderElement);

            patternsNode.appendChild(patternElement);
        }

        saveSettings(document);
    }

    //ПРИВАТНЫЕ - СЛУЖЕБНЫЕ МЕТОДЫ

    //сохранить xml-файл с настройками
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

    //создать дефолтный - пустой файл с настройками
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

        Element system = doc.createElement("system");

        Element card_editor = doc.createElement("card_editor");
        Text card_editorText = doc.createTextNode("путь к редактору карт");
        card_editor.appendChild(card_editorText);

        system.appendChild(card_editor);

        program_settings.appendChild(authorization);
        program_settings.appendChild(system);

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

    //удалить все имена и заголовки карт
    private void deleteAllPatterns() {
        Node patternsNode = document.getElementsByTagName("patterns").item(0);
        while (patternsNode.hasChildNodes()) {
            patternsNode.removeChild(patternsNode.getFirstChild());
        }
    }
}

class SettingsWorker_Test {
    public static void main(String[] args) {
        SettingsWorker settingsWorker = SettingsWorker.getInstance();

        System.out.println(settingsWorker.getLoginAndPass());

//        Map<String, String> map = new HashMap<>();
//        map.put("login", "kharmet");
//        map.put("password", "9NCzR##?3z");
//        settingsWorker.setLoginAndPass(map);

//        System.out.println(settingsWorker.getCardEditorPath());

//        settingsWorker.setCardEditorPath("C:\\test.exe");
//

//        System.out.println(settingsWorker.getAllPatterns());

//        Map<String, String> map = new LinkedHashMap<>();
//        map.put("Микрокольцовка", "QYUA98");
//        map.put("Nmae2", "Header2");
//        settingsWorker.setPatterns(map);
    }

    /*
    Тестирующий класс. Зпускать тольок по группам строк
     */
}