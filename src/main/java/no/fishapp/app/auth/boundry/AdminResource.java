package no.fishapp.app.auth.boundry;

import no.fishapp.app.auth.entity.DTO.AdminChangePasswordData;
import no.fishapp.app.auth.entity.Group;
import no.fishapp.app.auth.control.AdminService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Transactional
@RolesAllowed(value = {Group.ADMIN_GROUP_NAME})
public class AdminResource {

    @Inject
    AdminService adminService;

    @PATCH
    @Path("changepassword")
    public Response changePassword(
            AdminChangePasswordData adminChangePasswordData
    ) {
        Response.ResponseBuilder resp;
        boolean                  sucsess = adminService.changeUserPassword(
                adminChangePasswordData.getUserId(),
                adminChangePasswordData.getNewPassword()
        );
        if (! sucsess) {
            resp = Response.ok("Could not find user").status(Response.Status.INTERNAL_SERVER_ERROR);
        } else {
            resp = Response.ok();
        }
        return resp.build();
    }
}
