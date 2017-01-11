package com.maxeremin.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Use {@link #validate(String)} to check if the target sentence matches the {@link #SEARCH_PATTERN pattern}
 * @author Max Eremin
 * @since 1.0
 */
public class SearchValidator {
    private Pattern pattern;
    private Matcher matcher;

    private static final String SEARCH_PATTERN = "([-à-ÿÀ-ß\"]*\\s*)+";

    public SearchValidator() {
        pattern = Pattern.compile(SEARCH_PATTERN);
    }

    /**
     * @param target Sentence to check
     * @return true if the target sentence matches the {@link #SEARCH_PATTERN pattern} or false if it doesn't
     */

    public boolean validate(String target) {
        matcher = pattern.matcher(target);
        return matcher.matches();
    }
}
