package com.mr_faton.entity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

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
import java.util.Map;

/**
 * Created by Mr_Faton on 18.04.2015.
 */
public class SettingsWorker {
    private File settingsFile;

    public SettingsWorker() {
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
        if (!settingsFile.exists()) {
            createEmptySettingsFile();
        }
    }

    private void createEmptySettingsFile() {
        try(FileOutputStream fileOutput = new FileOutputStream(settingsFile)) {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.newDocument();

            Element settings = doc.createElement("settings");

            Element program_settings = doc.createElement("program_settings");
            Element authorization = doc.createElement("authorization");

            Element login = doc.createElement("login");
            Text loginText = doc.createTextNode(" ");
            login.appendChild(loginText);

            Element password = doc.createElement("password");
            Text passwordText = doc.createTextNode(" ");
            password.appendChild(passwordText);

            authorization.appendChild(login);
            authorization.appendChild(password);

            program_settings.appendChild(authorization);

            Element patterns = doc.createElement("patterns");

            Element pattern = doc.createElement("pattern");

            Element map_name = doc.createElement("map_name");
            Text map_nameText = doc.createTextNode(" ");
            map_name.appendChild(map_nameText);

            Element map_header = doc.createElement("map_header");
            Text map_headerText = doc.createTextNode(" ");
            map_header.appendChild(map_headerText);

            pattern.appendChild(map_name);
            pattern.appendChild(map_header);

            patterns.appendChild(pattern);

            settings.appendChild(program_settings);
            settings.appendChild(patterns);

            doc.appendChild(settings);

            //построить холостое преобразование
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            //установить свойства вывода, чтобы получить узел DOCTYPE
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "systemIdentifier");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "publicIdentifier");
            //установить отступ
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            //выполнить холостое преобразование и вывести узел в файл
            transformer.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(settingsFile)));
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            //ignore
            ex.printStackTrace();
        }
    }

    public Map<String, String> getLoginAndPass() {
        return null;
    }
}
