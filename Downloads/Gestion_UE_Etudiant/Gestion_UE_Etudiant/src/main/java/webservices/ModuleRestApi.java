package webservices;

import entities.Module;
import entities.UniteEnseignement;
import metiers.ModuleBusiness;
import metiers.UniteEnseignementBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/modules")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModuleRestApi {

    private ModuleBusiness moduleBusiness = new ModuleBusiness();
    private UniteEnseignementBusiness ueBusiness = new UniteEnseignementBusiness();

    // 1️⃣ Récupérer tous les modules
    @GET
    @Path("/list")
    public Response getAllModules() {
        return Response.ok(moduleBusiness.getAllModules()).build();
    }

    // 2️⃣ Récupérer un module par matricule
    @GET
    @Path("/{matricule}")
    public Response getModuleByMatricule(@PathParam("matricule") String matricule) {
        Module module = moduleBusiness.getModuleByMatricule(matricule);
        if (module == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(module).build();
    }

    // 3️⃣ Récupérer les modules par type
    @GET
    @Path("/type/{type}")
    public Response getModulesByType(@PathParam("type") Module.TypeModule type) {
        return Response.ok(moduleBusiness.getModulesByType(type)).build();
    }

    // 4️⃣ Ajouter un module
    @POST
    @Path("/add")
    public Response addModule(Module module) {
        boolean added = moduleBusiness.addModule(module);
        if (added) {
            return Response.status(Response.Status.CREATED).build();
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("UE inexistante ou données invalides")
                .build();
    }

    // 5️⃣ Mettre à jour un module
    @PUT
    @Path("/update/{matricule}")
    public Response updateModule(@PathParam("matricule") String matricule, Module module) {
        boolean updated = moduleBusiness.updateModule(matricule, module);
        if (updated) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // 6️⃣ Supprimer un module
    @DELETE
    @Path("/delete/{matricule}")
    public Response deleteModule(@PathParam("matricule") String matricule) {
        boolean deleted = moduleBusiness.deleteModule(matricule);
        if (deleted) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // 7️⃣ Récupérer les modules d'une UE
    @GET
    @Path("/ue/{codeUE}")
    public Response getModulesByUE(@PathParam("codeUE") int codeUE) {
        UniteEnseignement ue = ueBusiness.getUEByCode(codeUE);
        if (ue == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Unité d'enseignement introuvable")
                    .build();
        }
        return Response.ok(moduleBusiness.getModulesByUE(ue)).build();
    }
}