package common.string;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class JsonConvertor {

    private static final String TAG = "JsonConvertor";

    /**
     * 注册日期转换的格式
     */
    private static Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new UtilDateSerializer())
            .registerTypeAdapter(java.sql.Date.class, new UtilSqlDateSerializer())
            .registerTypeAdapter(Calendar.class, new UtilCalendarSerializer())
            .registerTypeAdapter(GregorianCalendar.class, new UtilCalendarSerializer())
            .setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting().create();

    /**
     * 把对象转换成json
     *
     * @param bean
     * @return
     */
    public static String toJson(Object bean) {
        return gson.toJson(bean);
    }

    /**
     * 把json转换成对象
     *
     * @param <T>
     * @param json
     * @param type
     * @return
     */
    public static <T> T fromJson(String json, Type type) {
        try {
            return gson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "fromJson: " + e.getMessage(), null);
        }
        return null;
    }

    /**
     * 常规日期转换器
     *
     * @author Administrator
     */
    private static class UtilDateSerializer implements JsonSerializer<Date>, JsonDeserializer<Date> {
        private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {
            String dateFormatAsString = format.format(new Date(date.getTime()));
            return new JsonPrimitive(dateFormatAsString);
        }

        public Date deserialize(JsonElement element, Type type, JsonDeserializationContext context)
                throws JsonParseException {
            if (!(element instanceof JsonPrimitive)) {
                throw new JsonParseException("The date should be a string value");
            }

            try {
                Date date = format.parse(element.getAsString());
                return date;
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }

    /**
     * sql日期格式转换器
     *
     * @author Administrator
     */
    private static class UtilSqlDateSerializer
            implements JsonSerializer<java.sql.Date>, JsonDeserializer<java.sql.Date> {
        private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        public JsonElement serialize(java.sql.Date date, Type type, JsonSerializationContext context) {
            String dateFormatAsString = format.format(new Date(date.getTime()));
            return new JsonPrimitive(dateFormatAsString);
        }

        public java.sql.Date deserialize(JsonElement element, Type type, JsonDeserializationContext context)
                throws JsonParseException {
            if (!(element instanceof JsonPrimitive)) {
                throw new JsonParseException("The date should be a string value");
            }

            try {
                Date date = format.parse(element.getAsString());
                return new java.sql.Date(date.getTime());
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }

    /**
     * 日历日期格式转换器
     *
     * @author Administrator
     */
    private static class UtilCalendarSerializer implements JsonSerializer<Calendar>, JsonDeserializer<Calendar> {

        public JsonElement serialize(Calendar cal, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(Long.valueOf(cal.getTimeInMillis()));
        }

        public Calendar deserialize(JsonElement element, Type type, JsonDeserializationContext context)
                throws JsonParseException {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(element.getAsJsonPrimitive().getAsLong());
            return cal;
        }

    }

}