package json;

import java.io.File;
import java.io.IOException;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * json olvasas class
 *
 * @param <T> the type of the elements
 */
public abstract class Deserializer<T> {

    final static private Logger logger = LogManager.getLogger(Deserializer.class.getName());

    /**
     * List of the objects in the JSON to be returned.
     */
    private List<T> elements;

    /**
     * Representing the object class.
     */
    private Class<T> c;

    private String resource;

    /**
     * Transforming to java obj.
     * @param elementClass
     * @param resourceName
     */
    protected Deserializer(Class<T> elementClass, String resourceName) {
        try {
            elements = JacksonHelper.readList(elementClass.getResourceAsStream(resourceName), elementClass);
            c = elementClass;
            resource = resourceName;
        } catch(IOException e) {
            logger.error("JSON not found", e);
            AssertionError ae = new AssertionError("Failed to load resource " + resourceName, e);
        }
    }

    /**
     * Returns the list of objects.
     *
     * @return the list of objects
     */
    public List<T> getAll() {
        return elements;
    }

    /**
     * Simple function to get the path of the JSON file.
     *
     * @return the JSON file
     */
    public File getPath() { return JacksonHelper.getClassLoaderPath(this.c, resource); }
}
