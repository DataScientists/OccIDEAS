package org.occideas.mapper;

import org.occideas.entity.GenericNode;
import org.occideas.vo.GenericNodeVO;

public interface GenericNodeMapper {

  GenericNodeVO convertToGenNodeVO(GenericNode entity);

}
