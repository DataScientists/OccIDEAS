package org.occideas.utilities;

import org.apache.commons.lang3.StringUtils;
import org.occideas.entity.Node;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class CommonUtil {


  public static boolean isListEmpty(List<? extends Object> list) {
    return list == null || list.isEmpty();
  }

  public static boolean isNumeric(String str) {
    try {
      Integer.parseInt(str);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  public static String getNextQuestionByCurrentNumber(String number) {
    if (number == null) {
      number = "0";
    } else if (number.length() == 0) {
      number = "0";
    }
    StringBuilder sb = new StringBuilder(number);
    String lastChar = sb.substring(sb.length() - 1);
    if (isInteger(lastChar)) {
      String[] numArray = number.split("[a-zA-Z]+");
      String lastLetter = numArray[numArray.length - 1];
      numArray[numArray.length - 1] = String.valueOf(Integer.parseInt(lastLetter) + 1);
      sb.delete(sb.lastIndexOf(lastLetter), sb.length());
      sb.append(numArray[numArray.length - 1]);
    } else {
      sb.append("1");
    }
    return sb.toString();
  }

  private static boolean isInteger(String lastLetter) {
    try {
      Integer.parseInt(lastLetter);
      return true;
    } catch (NumberFormatException exception) {
      return false;
    }
  }

  /**
   * Get list of Long objects
   *
   * @param str
   * @return
   */
  public static List<Long> convertToLongList(String[] str) {
    List<Long> list = new ArrayList<Long>();
    for (String module : str) {
      list.add(Long.valueOf(module));
    }
    return list;
  }

  public static void replaceListWithLowerCaseAndTrim(List<String> nodeList) {
    ListIterator<String> listIterator = nodeList.listIterator();
    while (listIterator.hasNext()) {
      String index = listIterator.next();
      listIterator.set(index.toLowerCase().trim());
    }
  }

  public static void removeNonUniqueNames(final List<? extends Node> nodeList) {
    final java.util.ListIterator<? extends Node> iterator = nodeList.listIterator();
    String temp = null;
    while (iterator.hasNext()) {
      if (temp == null) {
        temp = iterator.next().getName();
      }
      temp = removeSpacesAndLowerCase(temp);
      final Node next = iterator.next();
      if (temp.equals(removeSpacesAndLowerCase(next.getName()))) {
        iterator.remove();
      } else {
        temp = next.getName();
      }
    }
  }

  public static void removeNonUniqueString(final List<String> list) {
    final java.util.ListIterator<String> iterator = list.listIterator();
    String temp = null;
    while (iterator.hasNext()) {
      if (temp == null) {
        temp = iterator.next();
      }
      temp = removeSpacesAndLowerCase(temp);
      if (iterator.hasNext()) {

        final String next = iterator.next();
        if (temp.equals(removeSpacesAndLowerCase(next))) {
          iterator.remove();
        } else {
          temp = next;
        }
      }
    }
  }

  public static String removeSpacesAndLowerCase(String temp) {
    return temp.toLowerCase().trim();
  }

  public static BigDecimal deriveWorkshift(String workShift) {
 /*
	if (workShift.contains(".")) {
      String[] split = workShift.split("\\.");
      String wholeNumber = split[0];
      String decimal = split[1];
      BigDecimal decimalValue = new BigDecimal(decimal).divide(new BigDecimal(60), 4, RoundingMode.HALF_UP);
      BigDecimal wholeNumberValue = new BigDecimal(wholeNumber);
      return wholeNumberValue.add(decimalValue).setScale(4, RoundingMode.HALF_UP);
    }
*/
    return new BigDecimal(workShift).setScale(15);

  }

  public static String getValue(Object obj) {
    if (Objects.nonNull(obj)) {
      return obj.toString();
    }
    return StringUtils.EMPTY;
  }


}
