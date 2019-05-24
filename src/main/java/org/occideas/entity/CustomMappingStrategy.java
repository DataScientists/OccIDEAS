package org.occideas.entity;

import com.opencsv.bean.BeanField;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

public class CustomMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {
  @Override
  public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {

    super.setColumnMapping(new String[FieldUtils.getAllFields(bean.getClass()).length]);
    final int numColumns = findMaxFieldIndex();
    if (!isAnnotationDriven() || numColumns == -1) {
      return super.generateHeader(bean);
    }

    String[] header = new String[numColumns + 1];

    BeanField<T> beanField;
    for (int i = 0; i <= numColumns; i++) {
      beanField = findField(i);
      String columnHeaderName = extractHeaderName(beanField);
      header[i] = columnHeaderName;
    }
    return header;
  }

  private String extractHeaderName(final BeanField<T> beanField) {
    if (beanField == null || beanField.getField() == null
      || beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class).length == 0) {
      return StringUtils.EMPTY;
    }

    final CsvBindByName bindByNameAnnotation = beanField.getField()
      .getDeclaredAnnotationsByType(CsvBindByName.class)[0];
    return bindByNameAnnotation.column();
  }

}
