package org.acme.api;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Random;

@Path("/fault")
public class FaultApi {

    @GET
    @Path("/time-out/{id}")
    @Timeout(300)
    @Produces(MediaType.TEXT_PLAIN)
    @Fallback(fallbackMethod = "retryFallback")
    public Response timeOut(@PathParam(value = "id") Long id) throws InterruptedException {
        Thread.sleep(new Random().nextInt(500));
        return Response.ok("TIME-OUT").build();
    }

    public Response retryFallback(Long id)  {
        return Response.ok("FALLBACK-TIME-OUT").build();
    }

    @GET
    @Path("/retry")
    @Retry(maxRetries = 1)
    @Produces(MediaType.TEXT_PLAIN)
    public Response retry() {
        if(new Random().nextBoolean()) {
            throw new RuntimeException();
        }
        return Response.ok("RETRY").build();
    }

    @GET
    @Path("/circuit-breaker")
    @CircuitBreaker(requestVolumeThreshold = 5, failureRatio = 0.1, delay = 1000)
    @Produces(MediaType.TEXT_PLAIN)
    public Response circuitBreaker() {
        if(new Random().nextInt(3) >= 2) {
            throw new RuntimeException();
        }
        return Response.ok("RETRY").build();
    }

}
