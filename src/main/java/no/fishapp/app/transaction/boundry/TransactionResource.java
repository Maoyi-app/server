package no.fishapp.app.transaction.boundry;


import no.fishapp.app.auth.entity.Group;
import no.fishapp.app.rating.entity.Rating;
import no.fishapp.app.transaction.control.TransactionService;
import no.fishapp.app.transaction.entity.DTO.StartTransactionData;
import no.fishapp.app.transaction.entity.Transaction;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("transaction")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
public class TransactionResource {

    @Inject
    TransactionService transactionService;


    // todo: remove this is for testing
    @POST
    @RolesAllowed({Group.BUYER_GROUP_NAME})
    public Response newTransaction(StartTransactionData transactionData){
        Response.ResponseBuilder resp;

        try {
            Transaction transaction = transactionService.newTransaction(transactionData);
            resp = Response.ok(transaction);
        } catch (PersistenceException e) {
            resp = Response.ok("Unexpected error")
                           .status(Response.Status.INTERNAL_SERVER_ERROR);
        }

        return resp.build();
    }


    @GET
    @Path("{id}")
    public Response getTransactionReceipt(@PathParam("id") int transactionId){
        Response.ResponseBuilder resp;

        try {
            Transaction transaction = transactionService.getTransaction(transactionId);
            resp = Response.ok(transaction);
        } catch (PersistenceException e) {
            resp = Response.ok("Unexpected error creating the offer listing")
                           .status(Response.Status.INTERNAL_SERVER_ERROR);
        }

        return resp.build();
    }

    @GET
    @Path("all")
    @RolesAllowed({Group.USER_GROUP_NAME})
    public Response getAllUserTransactionReceipt(){
        Response.ResponseBuilder resp;

        try {
            List<Transaction> transactions = transactionService.getUserTransactions();
            resp = Response.ok(transactions);
        } catch (PersistenceException e) {
            resp = Response.ok("Unexpected error creating the offer listing")
                           .status(Response.Status.INTERNAL_SERVER_ERROR);
        }

        return resp.build();
    }

}
