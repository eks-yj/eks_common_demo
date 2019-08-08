package com.eks.utils;

import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

public class JaxbUtils {
    @SuppressWarnings("unchecked")
    public static <T> T documentToBean(Document document, Class<T> t) throws TransformerException, JAXBException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        ByteArrayOutputStream byteArrayOutputStream = null;
        byteArrayOutputStream = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(document), new StreamResult(byteArrayOutputStream));
        String documentString = byteArrayOutputStream.toString();
        StringReader stringReader = new StringReader(documentString);
        JAXBContext jaxbContext = JAXBContext.newInstance(t);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (T)unmarshaller.unmarshal(stringReader);
    }
    public static <T> String beanToDocument(T t) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(t.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();
        //对象转xml,默认报文头与要生成的报文头不相符，因此去掉
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);//格式化输出
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");//编码格式
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);//去掉默认报文头
        StringWriter stringWriter = null;
        stringWriter = new StringWriter();
        stringWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");//重写报文头
        stringWriter.write("\n");
        marshaller.marshal(t, stringWriter);
        return stringWriter.toString();
    }
}
