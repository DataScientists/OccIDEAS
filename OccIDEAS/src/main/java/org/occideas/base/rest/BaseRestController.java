package org.occideas.base.rest;

import javax.ws.rs.core.Response;

public interface BaseRestController<T> {

    public Response listAll();

    public Response get(Long id);
    
    public Response create(T json);

    public Response update(T json);

    public Response delete(T json);
}