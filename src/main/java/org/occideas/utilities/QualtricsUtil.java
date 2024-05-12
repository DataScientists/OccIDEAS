package org.occideas.utilities;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QualtricsUtil {

    public static List<Long> parseAnswers(String occideasAnswerIdNode) {
        String stringToParse = "";
        if(occideasAnswerIdNode != null) {
        	stringToParse = occideasAnswerIdNode.trim().replace(" ", "");
        }
        List<Long> results = new ArrayList<>();
        if (stringToParse.contains("[") && stringToParse.contains("]")) {
            String newAnswerString = stringToParse.replace("[", "").replace("]", "");
            if (newAnswerString.contains(",")) {
                String[] answers = newAnswerString.split(",");
                Arrays.stream(answers).forEach(answer -> results.add(Long.valueOf(answer)));
            } else {
                results.add(Long.valueOf(newAnswerString));
            }
        } else if (StringUtils.isNotEmpty(stringToParse) && StringUtils.isNumeric(stringToParse)) {
            results.add(Long.valueOf(stringToParse));
        }

        return results;
    }

}
