package com.debug.demo.consumer;

import lombok.Getter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "book")
@Getter
public class TestXmlObject {
    private Long id;
    private String name;
    private String author;

    @XmlAttribute
    public void setId(Long id) {
        this.id = id;
    }

    @XmlTransient
    public void setAuthor(String author) {
        this.author = author;
    }

    @XmlElement(name = "title")
    public void setName(String name) {
        this.name = name;
    }
}
