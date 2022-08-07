package web.petHotel.scripts;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

public class Scripts {

    public static String replacePolishChars(String text){

        return StringUtils.stripAccents(text);
    }
}
