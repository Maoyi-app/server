package no.fishapp.app.listing.boundry;


import no.fishapp.app.auth.entity.Group;
import no.fishapp.app.listing.control.ListingService;
import no.fishapp.app.listing.entity.BuyRequest;
import no.fishapp.app.listing.entity.OfferListing;
import no.fishapp.app.user.control.UserService;
import no.fishapp.app.user.entity.User;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("listing")
public class ListingResource {

    @Inject
    ListingService listingService;

    @Inject
    UserService userService;

    /**
     * Server endpoint for creating a new offer listing
     *
     * @param endDate End date for the offering
     * @param commodityId the id of the commodity being sold
     * @param price The price the commodity is sold at
     * @param maxAmount The maximum total amount of the commodity
     * @param latitude The latitude for the pickup point
     * @param longitude The longitude for the pickup point
     * @param additionalInfo Additional info about the listing
     *
     * @return return the offer listing if successful, error msg if not
     */
    @POST
    @Path("newOfferListing")
    @RolesAllowed(value = {Group.SELLER_GROUP_NAME, Group.ADMIN_GROUP_NAME})
    public Response newOfferListing(
            @NotNull @HeaderParam("endDate") long endDate,
            @NotNull @HeaderParam("commodityId") long commodityId,
            @NotNull @HeaderParam("price") double price,
            @NotNull @HeaderParam("maxAmount") int maxAmount,
            @NotNull @HeaderParam("latitude") double latitude,
            @NotNull @HeaderParam("longitude") double longitude,
            @HeaderParam("additionalInfo") String additionalInfo
    ) {
        Response.ResponseBuilder resp;
        User user = userService.getLoggedInUser();
        if (user == null) {
            resp = Response.ok("Could not find user").status(Response.Status.FORBIDDEN);
        } else {
            try {
                OfferListing offerListing = listingService.newOfferListing(endDate, commodityId, price, maxAmount,
                        latitude, longitude, user, additionalInfo);
                resp = Response.ok(offerListing);
            } catch (PersistenceException e) {
                resp = Response.ok("Unexpected error creating the offer listing").status(Response.Status.INTERNAL_SERVER_ERROR);
            }
        }
        return resp.build();
    }

    /**
     * Server endpoint for creating a new buy request
     *
     * @param endDate End date for the offering
     * @param commodityId the id of the commodity being bought
     * @param price The price the commodity is bought at
     * @param amount The amount of the commodity that is wanted
     * @param info Additional info about the request
     * @param maxDistance Maximum distance wanted to travel
     *
     * @return the buy request object if successful, error msg if not
     */
    @POST
    @Path("newBuyRequest")
    @RolesAllowed(value = {Group.USER_GROUP_NAME, Group.SELLER_GROUP_NAME, Group.ADMIN_GROUP_NAME})
    public Response newBuyRequest(
            @NotNull @HeaderParam("endDate") long endDate,
            @NotNull @HeaderParam("commodity") long commodityId,
            @NotNull @HeaderParam("price") double price,
            @NotNull @HeaderParam("amount") int amount,
            @HeaderParam("info") String info,
            @NotNull @HeaderParam("maxDistance") double maxDistance
    ) {
        Response.ResponseBuilder resp;
        User user = userService.getLoggedInUser();
        if (user == null) {
            resp = Response.ok("Could not find user").status(Response.Status.FORBIDDEN);
        } else {
            try {
                BuyRequest buyRequest = listingService.newBuyRequest(endDate, commodityId, price, amount, info,
                        maxDistance, user);
                resp = Response.ok(buyRequest);
            } catch (PersistenceException e) {
                resp = Response.ok("Unexpected error creating the offer listing").status(Response.Status.INTERNAL_SERVER_ERROR);
            }
        }
        return resp.build();
    }
}