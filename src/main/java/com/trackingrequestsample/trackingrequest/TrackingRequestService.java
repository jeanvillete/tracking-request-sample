package com.trackingrequestsample.trackingrequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Scope( scopeName = "singleton" )
public class TrackingRequestService {

    public String nextTrackId() {
        return UUID.randomUUID().toString();
    }

}
