package org.occideas.security.dao;

import org.occideas.security.model.AuditLog;

public interface IAuditDao {

	void save(AuditLog auditLog);

}
