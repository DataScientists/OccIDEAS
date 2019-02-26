package org.occideas.admin.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.occideas.vo.DBConnect;
import org.occideas.vo.NodeVO;

public interface IDbConnectService {

	Connection connectToDb(DBConnect dbConnect);
	
	List<NodeVO> importLibrary(DBConnect dbConnect) throws SQLException;
}
