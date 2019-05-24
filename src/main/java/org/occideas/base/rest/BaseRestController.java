package org.occideas.base.rest;

import javax.ws.rs.core.Response;

public interface BaseRestController<T> {

  Response listAll();

  Response get(Long id);

  Response create(T json);

  Response update(T json);

  Response delete(T json);
}