package ser;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Павел on 11.02.2017.
 */
public class DeserObj {
    public static Object deserObj(String fileName) throws ParserConfigurationException, IOException, SAXException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Object people = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(fileName));
        people = parser(doc, people);
        return people;
    }


    public static Object parser(Node node, Object object) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ArrayList<String> str = new ArrayList<>();
        Field[] fields;
        NodeList list1 = node.getChildNodes();
        Node chNode = list1.item(0);
        if (chNode.getNodeName().equals("object")){
            Element element = (Element) chNode;
            Class p = Class.forName("com.bortnikpp." + element.getAttribute("type"));
            object = p.newInstance();
            fields = object.getClass().getDeclaredFields();
            NodeList list2 = chNode.getChildNodes();
            setFields(object, list2, fields);
        }
        return object;
    }

    private static Object setFields(Object object, NodeList nodeList, Field[] fields) throws IllegalAccessException {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node ch = nodeList.item(i);
            Element el = (Element) ch;
            if(el.getNodeName().equals("field")){
                for (Field f :
                        fields) {
                    if (el.getAttribute("id").equals(f.getName())) {
                        f.setAccessible(true);
                        switch (f.getType().getSimpleName()){
                            case "int":
                                //f.getType().getDeclaringClass().getMethods()[]
                                f.set(object, Integer.parseInt(el.getAttribute("value")));
                                break;
                            case "char":
                                f.set(object, el.getAttribute("value").charAt(0));
                                break;
                            case "double":
                                f.set(object, Double.parseDouble(el.getAttribute("value")));
                                break;
                            case "float":
                                f.set(object, Float.parseFloat(el.getAttribute("value")));
                                break;
                            case "byte":
                                f.set(object, Byte.parseByte(el.getAttribute("value")));
                                break;
                            case "short":
                                f.set(object, Short.parseShort(el.getAttribute("value")));
                                break;
                            case "long":
                                f.set(object, Long.parseLong(el.getAttribute("value")));
                                break;
                            case "boolean":
                                f.set(object, Boolean.parseBoolean(el.getAttribute("value")));
                                break;
                            case "String":
                                f.set(object, el.getAttribute("value"));
                                break;
                            default:
                                f.set(object, null);
                                break;
                        }
                    }
                }
            }
        }
        return object;
    }
}
