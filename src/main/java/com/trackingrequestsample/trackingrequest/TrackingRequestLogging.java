package com.trackingrequestsample.trackingrequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

@Service
@Scope( value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS )
public class TrackingRequestLogging {

    private static final Logger LOGGER = Logger.getLogger( TrackingRequestLogging.class );

    @Autowired
    private HttpServletRequest request;

    public void trackRequest() {
        String requestTrackingId = ( String ) request.getAttribute( "REQUEST_TRACKING_ID" );

        if ( !StringUtils.hasText( requestTrackingId ) )
            throw new IllegalStateException( "No attribute \"REQUEST_TRACKING_ID\" was found on the request object." );

        String requestTrackingMessage = "RequestTrackingId=" + requestTrackingId + ", " +
                                        "RequestURI=" + request.getRequestURI() + ", " +
                                        "RequestMethod=" + request.getMethod() + ", " +
                                        "RequestParameters=" + request.getParameterMap();

        new Thread(
            () -> LOGGER.info( requestTrackingMessage )
        ).start();
    }

}
