package com.amrumpler.schedhype.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.io.JsonStringEncoder;

@Provider
@Slf4j
public class GenericExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable e) {
        LogUtils.error(log, e.getMessage(), e);
        String msg = e.getMessage() == null ? "" : new String(JsonStringEncoder.getInstance().quoteAsString(e.getMessage()));

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"errors\": [\"Generic.error\"], \"message\": \"" + msg + "\"}")
                .build();
    }

}