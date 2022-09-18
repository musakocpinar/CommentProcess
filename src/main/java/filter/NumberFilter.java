package filter;

import exceptions.NumberFilterException;
import java.util.function.Predicate;

public abstract class NumberFilter implements Process<Long> {
    private Predicate<Long> predicateList = value -> false;

    protected NumberFilter(FilterType type, int value){
        switch (type) {
            case GREATER -> greaterThan(value);
            case GREATER_OR_EQUAL -> greraterThanOrEqualTo(value);
            case EQUAL -> equalTo(value);
            case LESS -> lessThan(value);
            case LESS_OR_EQUAL -> lessThanorEqualTo(value);
            default -> throw new NumberFilterException("Filter type is not handled.");
        }
    }

    private void greaterThan(int value){
        predicateList = val -> val > value;
    }

    private void equalTo(int value){
        predicateList = val -> val == value;
    }

    private void greraterThanOrEqualTo(int value){
        predicateList = val -> val >= value;
    }

    private void lessThan(int value){
        predicateList = val -> val < value;
    }

    private void lessThanorEqualTo(int value){
        predicateList = val -> val <= value;
    }

    @Override
    public boolean process(Long value) {
        return predicateList.test(value);
    }

    public enum FilterType {
        GREATER,
        EQUAL,
        GREATER_OR_EQUAL,
        LESS,
        LESS_OR_EQUAL
    }
}
