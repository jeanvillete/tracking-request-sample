package com.trackingrequestsample.trackingrequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TrackingRequestController {

    private static final Logger LOGGER = Logger.getLogger( TrackingRequestController.class );

    @Autowired
    private TrackingRequestLogging logging;

    @RequestMapping( value = "/", method = RequestMethod.GET )
    public String initialize( Model model, HttpServletRequest request ) {
        LOGGER.debug( "[TrackingRequestController.initialize] [+] Invoking logging.trackRequest service." );
        logging.trackRequest();
        LOGGER.debug( "[TrackingRequestController.initialize] [-] Invoking logging.trackRequest service." );

        model.addAttribute( "trackingRecordId", ( String ) request.getAttribute( "REQUEST_TRACKING_ID" ) );

        return "success";
    }

}
