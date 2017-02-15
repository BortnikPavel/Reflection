package ser;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created by admin on 10.02.2017.
 */
public class SerObj {
    public static void serObj(Object object) throws ParserConfigurationException, IllegalAccessException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation impl = builder.getDOMImplementation();
        Document doc = impl.createDocument(null, null, null);
        Element e1 = doc.createElement("object");
        e1.setAttribute("type", object.getClass().getSimpleName());
        Field[] fields = object.getClass().getDeclaredFields();
        Constructor[] constructors = object.getClass().getConstructors();
        Method[] methods = object.getClass().getMethods();

        addFields(fields, object, doc, e1);

        addConstructors(constructors, doc, e1);

        addMethods(methods,doc,e1);

        doc.appendChild(e1);
        try {
            saveXML(doc);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private static void addConstructors(Constructor[] constructors, Document doc,Element el){
        for (Constructor c :
                constructors) {
            Element element = doc.createElement("constructor");
            c.setAccessible(true);
            element.setAttribute("id", c.getName());
            Parameter[] parameters = c.getParameters();
            for (Parameter p :
                    parameters) {
                Element element1 = doc.createElement("constructorParam");
                element1.setAttribute("type", p.getType().getSimpleName());
                element.appendChild(element1);
            }
            el.appendChild(element);
        }
    }

    private static void addFields(Field[] f,Object object, Document doc, Element el) throws IllegalAccessException {
        for (Field field :
                f) {
            Element e2 = doc.createElement("field");
            field.setAccessible(true);
            e2.setAttribute("id", field.getName());
            e2.setAttribute("type", field.getType().getSimpleName());
            e2.setAttribute("value", field.get(object).toString());
            el.appendChild(e2);
        }
    }

    private static void addMethods(Method[] m, Document doc, Element el){
        for (Method method :
                m) {
            Element element = doc.createElement("method");
            method.setAccessible(true);
            element.setAttribute("id", method.getName());
            element.setAttribute("returnType", method.getReturnType().getTypeName());
            Parameter[] parameters = method.getParameters();
            for (Parameter p :
                    parameters) {
                Element element1 = doc.createElement("methodParam");
                element1.setAttribute("type", p.getType().getSimpleName());
                element.appendChild(element1);
            }
            el.appendChild(element);
        }
    }

    private static void saveXML(Document document) throws TransformerException {
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(".xml"));
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        transformer.transform(source, result);
    }
}
