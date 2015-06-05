package hello;

/**
 * Created with IntelliJ IDEA.
 * User: WEI
 * Date: 6/5/15
 * Time: 5:31 AM
 * To change this template use File | Settings | File Templates.
 */

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import org.glassfish.jersey.server.ChunkedOutput;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

@Path("/hello-world")
@Produces(MediaType.TEXT_HTML)
public class HelloWorldResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public HelloWorldResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
        final String value = String.format(template, name.or(defaultName));
        return new Saying(counter.incrementAndGet(), value);
    }

    @Path("/testAsync")
    @GET
    @Timed
    public void asyncGet(@Suspended final javax.ws.rs.container.AsyncResponse asyncResponse) {

        int i = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Async API started");
                String result = veryExpensiveOperation();
                asyncResponse.resume(result);
                System.out.println("Async API done");
            }

            private String veryExpensiveOperation() {
                try {
                    for (int i = 0; i < 10; i++) {
                        System.out.println("Async API running");
                        System.out.println(i);
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {

                }
                return "hello";// ... very expensive operation
            }
        }).start();
    }

    @Path("/testAsyncChunking")
    @GET
    public ChunkedOutput<String> getChunkedResponse() {
        final ChunkedOutput<String> output = new ChunkedOutput<String>(String.class);

        new Thread() {
            public void run() {
                try {
                    String chunk;

                    for (int i=0; i < 10; i++) {
                        Thread.sleep(500);
                        chunk = getNextString();
                        output.write(chunk);
                    }
                } catch (Exception e) {
                    // IOException thrown when writing the
                    // chunks of response: should be handled
                } finally {

                    try{
                    output.close();
                    } catch (Exception e) {

                    }

                    // simplified: IOException thrown from
                    // this close() should be handled here...
                }
            }
        }.start();

        // the output will be probably returned even before
        // a first chunk is written by the new thread
        return output;
    }

    private String getNextString() {
        return " hello world ";
    }

}
