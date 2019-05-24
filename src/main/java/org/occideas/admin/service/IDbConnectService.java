package org.occideas.admin.service;

import org.occideas.entity.NodePlain;
import org.occideas.vo.DBConnectVO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IDbConnectService {

  Connection connectToDb(DBConnectVO dbConnect);

  List<NodePlain> importLibrary(DBConnectVO dbConnect) throws SQLException;
}
