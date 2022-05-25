package json;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;

import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Helper class for reading a list of objects from JSON.
 */
public class JacksonHelper {

    /**
     * Mapper object to be returned.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * json olvasas
     * @param in
     * @param elementClass
     * @param <T>
     * @return
     * @throws IOException
     */
    public static<T> List<T> readList(InputStream in, Class<T> elementClass) throws IOException {
        JavaType type = MAPPER.getTypeFactory().constructCollectionType(List.class, elementClass);
        return MAPPER.readValue(in, type);
    }

    /**
     * json helyenek meghatarozasa
     * @param elementClass
     * @param resource
     * @param <T>
     * @return
     */
    public static<T> File getClassLoaderPath(Class<T> elementClass, String resource){
        return new File(elementClass.getResource(resource).getPath());
    }
}