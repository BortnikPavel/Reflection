

import classloader.MyClassloader;
import ser.DeserObj;
import ser.People;
import ser.SerObj;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Field;


public class Main {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        /*People people = new People("Pasha", 28, 2000);
        SerObj serObj = new SerObj();
        DeserObj deser = new DeserObj();
        try {
            serObj.serObj(people);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        People p = (People) deser.deserObj(".xml");
        System.out.println(p);*/

        MyClassloader classloader = new MyClassloader();
        Object classLoad = classloader.findClass().newInstance();
        classloader.loadClass();
        SerObj serObj = new SerObj();
        DeserObj deser = new DeserObj();
        try {
            serObj.serObj(classLoad);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //Object p = deser.deserObj(".xml");
        //System.out.println(p);
    }
}
