package org.occideas.admin.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.occideas.entity.NodePlain;
import org.occideas.vo.DBConnect;

public interface IDbConnectService {

	Connection connectToDb(DBConnect dbConnect);
	
	List<NodePlain> importLibrary(DBConnect dbConnect) throws SQLException;
}
