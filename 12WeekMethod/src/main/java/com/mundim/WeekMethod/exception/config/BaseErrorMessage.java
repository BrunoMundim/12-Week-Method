package com.mundim.WeekMethod.exception.config;

import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.ResourceBundle;

@RequiredArgsConstructor
public class BaseErrorMessage {

    private final String DEFAULT_RESOURCE = "messages";

    public static final BaseErrorMessage USER_NOT_FOUND_BY_ID = new BaseErrorMessage("userNotFoundById");
    public static final BaseErrorMessage USER_NOT_FOUND_BY_EMAIL = new BaseErrorMessage("userNotFoundByEmail");
    public static final BaseErrorMessage WEEK_CARD_NOT_FOUND_BY_ID = new BaseErrorMessage("weekCardNotFoundById");
    public static final BaseErrorMessage TASK_NOT_FOUND_BY_ID = new BaseErrorMessage("taskNotFoundById");
    public static final BaseErrorMessage GOAL_NOT_FOUND_BY_ID = new BaseErrorMessage("goalNotFoundById");
    public static final BaseErrorMessage GOAL_NOT_IN_PROGRESS = new BaseErrorMessage("goalNotInProgress");
    public static final BaseErrorMessage KEY_RESULT_NOT_FOUND_BY_ID = new BaseErrorMessage("keyResultNotFoundById");
    public static final BaseErrorMessage KEY_RESULT_NOT_EXISTS_IN_GOAL = new BaseErrorMessage("keyResultNotExistsInGoal");
    public static final BaseErrorMessage EMAIL_INVALID_FORMAT = new BaseErrorMessage("invalidFormatEmail");
    public static final BaseErrorMessage UNAUTHORIZED_USER = new BaseErrorMessage("unauthorizedUser");
    public static final BaseErrorMessage NULL_FIELD = new BaseErrorMessage("nullField");


    private final String key;
    private String[] params;

    public BaseErrorMessage params(final String... params) {
        this.params = Arrays.copyOf(params, params.length);
        return this;
    }

    public String getMessage() {
        String message = tryGetMessageFromBundle();
        if (params != null && params.length > 0) {
            MessageFormat fmt = new MessageFormat(message);
            message = fmt.format(params);
        }
        return message;
    }

    private String tryGetMessageFromBundle(){
        return getResource().getString(key);
    }

    public ResourceBundle getResource(){
        return ResourceBundle.getBundle(DEFAULT_RESOURCE);
    }

}
