package com.mr_faton.entity;

import java.util.Objects;

/**
 * Created by root on 17.04.2015.
 */
public class GCSTMap {
    private String downloadLink;//часть ссылки на скачивание карты
    private String name;//имя карты
    private String header;//заголовок карты
    private String term;//термин, за который выложена карта

    //установить в конструкторе все поля объекта карты
    public GCSTMap(String downloadLink, String name, String header, String term) {
        this.downloadLink = downloadLink;
        this.name = name;
        this.header = header;
        this.term = term;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public String getName() {
        return name;
    }

    public String getHeader() {
        return header;
    }

    public String getTerm() {
        return term;
    }

    /*
    переопределяем методы hashCode и equals для того, чтобы в наш список карт не добавлялось одинаковых карт, т.к.
    на сайте ГЦСТ иногда в результат поиска может выпасть 2 и более одинаковых карт
     */
    @Override
    public int hashCode() {
        return Objects.hash(downloadLink, name, header, term);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (this.getClass() != other.getClass()) return false;
        GCSTMap otherObject = (GCSTMap) other;
        return this.downloadLink.equals(otherObject.getDownloadLink()) &&
                this.name.equals(otherObject.getName()) &&
                this.header.equals(otherObject.getHeader()) &&
                this.term.equals(otherObject.getTerm());
    }

    @Override
    public String toString() {
        return "GCSTMap{\n" +
                "downloadLink=" + downloadLink + '\n' +
                "name=" + name + '\n' +
                "header=" + header + '\n' +
                "term=" + term + '\n' +
                "}\n";
    }
}
/*
Объект этого класса олицетворяет карту, найденную на сайте ГЦСТ. Объект содержит в себе: название карты, её заголовок,
часть ссылки на скачивание, термин за который была выложена карта
 */