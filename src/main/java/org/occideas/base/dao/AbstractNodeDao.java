package org.occideas.base.dao;

import org.occideas.entity.Node;
import org.occideas.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AbstractNodeDao<T extends Node> extends JpaRepository<T, Long> {

    List<T> findByName(String name);

    List<T> findByNameStartsWith(String name);

    List<T> findByDeleted(int deleted);

    List<String> findDistinctNameByIdNode(long idNode);

    List<T> findByLinkAndIdNode(long link, long idNode);

    List<T> findByType(String type);

    List<T> findByParentIdAndTypeContaining(long parentId, String type);

    List<T> findByParentId(long parentId);

    @Query(value="SELECT COALESCE(MAX(idNode),0) FROM Node", nativeQuery = true)
    long generateIdNode();

}
