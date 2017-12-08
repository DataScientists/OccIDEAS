package org.occideas.admin.service;

public interface IAdminService
{

    void cleanOrphans();

    boolean checkIfAllOrphanNodesAreRemoved();
    
}
