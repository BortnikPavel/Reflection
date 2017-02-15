package classloader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class MyClassloader extends ClassLoader {
    private String jarFileName = "Animal.jar";
    private String className = "Animal";
    private Hashtable classes = new Hashtable();
    private String urlName = "https://github.com/BortnikPavel/Load/blob/master/src/unnamed.jar?raw=true";
    private static String WARNING = "Warning : No jar file found. Packet unmarshalling won't be possible. Please verify your classpath";




    public Class loadClass() throws IOException, ClassNotFoundException {
        try (BufferedInputStream src = new BufferedInputStream(new URL(urlName).openStream());
             BufferedOutputStream dst = new BufferedOutputStream(new FileOutputStream(jarFileName))) {
            URL url = new URL(urlName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            copy(src, dst);
            //cacheClasses();
            connection.disconnect();
            //Class result = Class.forName("Animal");
        }
        return null;
    }

    private void copy(BufferedInputStream reader, BufferedOutputStream writer) throws IOException {
        int data = reader.read();
        while (data != -1){
            writer.write(data);
            data = reader.read();
        }

    }


    public Class findClass() {
        byte classByte[];
        Class result = null;

        result = (Class)classes.get(className); //checks in cached classes
        if (result != null) {
            return result;
        }

        try {
            return findSystemClass(className);
        } catch (Exception e) {
        }

        try {
            JarFile jar = new JarFile(jarFileName);
            JarEntry entry = jar.getJarEntry(className);
            InputStream is = jar.getInputStream(entry);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int nextValue = is.read();
            while (-1 != nextValue) {
                byteStream.write(nextValue);
                nextValue = is.read();
            }

            classByte = byteStream.toByteArray();
            result = defineClass(className, classByte, 0, classByte.length, null);
            classes.put(className, result);
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}