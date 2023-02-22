package be.vinci.pae.utils;

import be.vinci.pae.views.Views;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Json.
 *
 * @param <T> is the params.
 */
public class Json<T> {

  private static final ObjectMapper jsonMapper = new ObjectMapper();

  /**
   * Method used to return the public attribute of a item in a jsonMapper.
   *
   * @param item item containing the attribute
   * @param type class of the item
   * @param <T>  ...
   * @return the deserialized item
   */
  public static <T> T filterPublicJsonView(T item, Class<T> type) {
    try {
      // serialize using JSON Views : public view (all fields not required in the
      // views are not serialized)
      //com.fasterxml.jackson.databind.exc.InvalidDefinitionException:
      // Java 8 date/time type `java.time.LocalDate`
      // not supported by default: new JavaTimeModule as solution
      String publicItemAsString = jsonMapper.registerModule(new JavaTimeModule())
          .writerWithView(Views.Public.class)
          .writeValueAsString(item);
      // deserialize using JSON Views : Public View (all fields that are not serialized
      // are set to their default values in the POJO)
      return jsonMapper.readerWithView(Views.Public.class).forType(type)
          .readValue(publicItemAsString);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }

  }

}
