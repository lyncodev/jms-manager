package uk.gov.dwp.jms.manager.core.service.replace;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringReplacementService {
    private final static StringReplacementService INSTANCE = new StringReplacementService();

    public static StringReplacementService stringReplaceService () {
        return INSTANCE;
    }

    private StringReplacementService() {}

    public String replaceRegex (String content, String groupPattern, String replacementPattern) {
        Matcher m = Pattern.compile(groupPattern).matcher(content);
        StringBuffer sb = new StringBuffer();
        while(m.find()) {
            m.appendReplacement(sb, replacementPattern);
        }
        m.appendTail(sb);

        return sb.toString();
    }
}
