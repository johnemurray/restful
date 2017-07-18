package murray;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


public interface AccountService {
    @GET
    @Path("/reset")
    @Produces("application/json")
    Outcome reset();

    @GET
    @Path("/get")
    @Produces("application/json")
    Outcome getBalance(String accountNumber) ;

    @PUT
    @Path("/get")
    @Produces("application/json")
    Outcome transfer(TransferRequest transferRequest);
}
