package com.cu.carrental.repository;

import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class XmlStore {
  private XmlStore() {}

  public static synchronized Document read(Path path, String root) {
    try {
      Files.createDirectories(path.getParent());
      if (!Files.exists(path)) {
        Document document = newBuilder().newDocument();
        document.appendChild(document.createElement(root));
        write(path, document);
        return document;
      }
      return newBuilder().parse(path.toFile());
    } catch (Exception e) {
      throw new IllegalStateException("Could not read XML store: " + path, e);
    }
  }

  public static synchronized void write(Path path, Document document) {
    try {
      Files.createDirectories(path.getParent());
      Path temp = path.resolveSibling(path.getFileName() + ".tmp");
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.transform(new DOMSource(document), new StreamResult(temp.toFile()));
      try {
        Files.move(temp, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
      } catch (AtomicMoveNotSupportedException e) {
        Files.move(temp, path, StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (Exception e) {
      throw new IllegalStateException("Could not persist XML store: " + path, e);
    }
  }

  private static DocumentBuilder newBuilder() throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
    factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
    factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    factory.setXIncludeAware(false);
    factory.setExpandEntityReferences(false);
    return factory.newDocumentBuilder();
  }

  public static String text(Element element, String tag) {
    NodeList nodes = element.getElementsByTagName(tag);
    return nodes.getLength() == 0 ? "" : nodes.item(0).getTextContent();
  }

  public static void put(Document document, Element element, String key, Object value) {
    Element node = (Element) element.getElementsByTagName(key).item(0);
    if (node == null) {
      node = document.createElement(key);
      element.appendChild(node);
    }
    node.setTextContent(String.valueOf(value));
  }
}
