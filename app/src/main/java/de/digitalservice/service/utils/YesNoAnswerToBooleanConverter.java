package de.digitalservice.service.utils;

import de.digitalservice.model.common.YesNoAnswer;

public class YesNoAnswerToBooleanConverter {

    public static Boolean convert(YesNoAnswer answer) {
        if (answer == YesNoAnswer.YES) {
            return true;
        } else {
            return false;
        }
    }

}
