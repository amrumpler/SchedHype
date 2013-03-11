package com.amrumpler.schedhype.util;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Provider
@Slf4j
public class WebApplicationExceptionHandler implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException wae) {
        // The default Resteasy behaviour is to print the stack trace, but we don't want that as we
        // throw WebApplicationExceptions around like candy, and therefore fill the logs with noise
        LogUtils.debug(log, "Returning status {}: {}", wae.getResponse().getStatus(), wae.getResponse().getEntity().toString());
        return wae.getResponse();
    }

}

