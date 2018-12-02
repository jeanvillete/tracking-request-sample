package com.trackingrequestsample.trackingrequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TrackingRequestFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger( TrackingRequestFilter.class );

    private List< String > ignoreContainsEntries;

    @Override
    public void init( FilterConfig filterConfig ) throws ServletException {
        String ignoreContainsEntries = filterConfig.getInitParameter( "ignoreContainsEntries" );
        ignoreContainsEntries = ignoreContainsEntries.trim();
        ignoreContainsEntries = ignoreContainsEntries.replace( "\\n", "\\s" );
        ignoreContainsEntries = ignoreContainsEntries.replace( ",", "\\s" );

        this.ignoreContainsEntries = Arrays.stream( Optional.ofNullable( ignoreContainsEntries.split( "\\s" ) ).orElse( new String[]{} ) )
            .map( String::trim )
            .filter( StringUtils::hasText )
            .sorted()
            .collect( Collectors.toList() );
    }

    @Override
    public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain ) throws IOException, ServletException {
        LOGGER.debug( "[TrackingRequestFilter.doFilter] [+] Initializing filter." );
        HttpServletRequest request = ( HttpServletRequest ) servletRequest;

        boolean ignoreRequestedUri = this.ignoreContainsEntries.stream().anyMatch( checkIgnoreItem -> request.getRequestURI().contains( checkIgnoreItem ) );
        if ( !ignoreRequestedUri ) {
            LOGGER.debug( "[TrackingRequestFilter.doFilter] Validating requested URI (not ignored); " + request.getRequestURI() );

            TrackingRequestService trackingRequestService = null;
            try {
                trackingRequestService = WebApplicationContextUtils.findWebApplicationContext( request.getServletContext() ).getBean( TrackingRequestService.class );
            } catch ( BeansException e ) {
                LOGGER.error( e );
            }

            /*
             * Note: At this point, is desired code the point of auditing (tracking requested data),
             * the code where retrieves data from the request object must be done within the current thread execution,
             * so when every data has been retrieved, we could provide the resulting string (method, parameters, etc)
             * to a service responsible for persisting the data in some place (database, file system, etc).
             * It is necessary only pay attention that the request object cannot be passed to the other thread than the
             * current one.
             */
            request.setAttribute( "REQUEST_TRACKING_ID", trackingRequestService.nextTrackId() );
        } else {
            LOGGER.debug( "[TrackingRequestFilter.doFilter] Ignoring requested URI; " + request.getRequestURI() );
        }

        LOGGER.debug( "[TrackingRequestFilter.doFilter] [-] Concluding filter." );
        filterChain.doFilter( servletRequest, servletResponse );
    }

    @Override
    public void destroy() {
        this.ignoreContainsEntries = null;
    }

}