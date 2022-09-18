package filter;

import org.apache.commons.lang3.StringUtils;
import utils.Regex;

public abstract class StringFilter implements Process<String> {
    private final FilterType type;
    private final String val;

    protected StringFilter(FilterType type, String val){
        this.type = type;
        this.val = val;
    }

    @Override
    public boolean process(String comment) {
        if (type == FilterType.REGEX){
            boolean isMatch = Regex.isMatch(val, comment);
            if (!isMatch){
                return false;
            }
        }

        if (type == FilterType.EQUALS){
            if (!comment.equalsIgnoreCase(val)) {
                return false;
            }
        }

        if (type == FilterType.CONTAINS){
            if (!StringUtils.containsIgnoreCase(comment, val)) {
                return false;
            }
        }

        return true;
    }

    public enum FilterType {
        REGEX,
        EQUALS,
        CONTAINS
    }
}
