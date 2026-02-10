package webservices;

import entities.UniteEnseignement;
import metiers.UniteEnseignementBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/ue")

public class UERestApi {


    //methode->web service-->rest api
    //URI
    //getALLUEs


    public UniteEnseignementBusiness helper = new UniteEnseignementBusiness();
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(){
        return Response.status(200)
                .entity(this.helper.getListeUE())
                .build();
    }

    @GET
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUEByCode(@PathParam("code") int code) {
        UniteEnseignement ue = helper.getUEByCode(code);
        if (ue != null) {
            return Response.status(200)
                    .entity(ue)
                    .build();
        } else {
            return Response.status(404)
                    .entity("UE avec code " + code + " non trouvée")
                    .build();
        }
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUniteEnseignement(UniteEnseignement ue) {
        boolean success = helper.addUniteEnseignement(ue);
        if (success) {
            return Response.status(201)
                    .entity(ue)
                    .build();
        } else {
            return Response.status(500)
                    .entity("Erreur lors de l'ajout de l'UE")
                    .build();
        }
    }

    @GET
    @Path("/domaine/{domaine}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUEByDomaine(@PathParam("domaine") String domaine) {
        List<UniteEnseignement> liste = helper.getUEByDomaine(domaine);
        return Response.status(200)
                .entity(liste)
                .build();
    }


    // 5. Récupérer les UE par semestre
    @GET
    @Path("/semestre/{semestre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUEBySemestre(@PathParam("semestre") int semestre) {
        List<UniteEnseignement> liste = helper.getUEBySemestre(semestre);
        return Response.status(200)
                .entity(liste)
                .build();
    }

    // 6. Supprimer une UE par code
    @DELETE
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUniteEnseignement(@PathParam("code") int code) {
        boolean success = helper.deleteUniteEnseignement(code);
        if (success) {
            return Response.status(200)
                    .entity("UE avec code " + code + " supprimée avec succès")
                    .build();
        } else {
            return Response.status(404)
                    .entity("UE avec code " + code + " non trouvée")
                    .build();
        }
    }

    // 7. Mettre à jour une UE
    @PUT
    @Path("/{code}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUniteEnseignement(
            @PathParam("code") int code,
            UniteEnseignement updatedUE) {
        boolean success = helper.updateUniteEnseignement(code, updatedUE);
        if (success) {
            return Response.status(200)
                    .entity(updatedUE)
                    .build();
        } else {
            return Response.status(404)
                    .entity("UE avec code " + code + " non trouvée")
                    .build();
        }
    }



    // 8. Alternative avec QueryParam pour certaines recherches
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchUE(
            @QueryParam("domaine") String domaine,
            @QueryParam("semestre") Integer semestre) {

        if (domaine != null && semestre != null) {
            // Filtrer par domaine ET semestre
            List<UniteEnseignement> result = helper.getListeUE().stream()
                    .filter(ue -> ue.getDomaine().equalsIgnoreCase(domaine)
                            && ue.getSemestre() == semestre)
                    .toList();
            return Response.status(200).entity(result).build();
        } else if (domaine != null) {
            return getUEByDomaine(domaine);
        } else if (semestre != null) {
            return getUEBySemestre(semestre);
        } else {
            return Response.status(400)
                    .entity("Spécifiez au moins un critère de recherche (domaine ou semestre)")
                    .build();
        }
    }

}
