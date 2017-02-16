/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import bd.Autobuses;
import bd.Ubicaciones;
import bd.Conexion;
import com.google.gson.Gson;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.POST;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Lluis_2
 */
@Path("generic")
public class GenericResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }

    /**
     * Retrieves representation of an instance of rest.GenericResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listarAutobuses() {
        Conexion conexion = new Conexion();
        List<Autobuses> lista = null;
        try {
            lista = conexion.obtenerAutobuses();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        Gson gson = new Gson();

        return gson.toJson(lista);
    }

    @GET
    @Path("{matricula}")
    @Produces(MediaType.APPLICATION_JSON)
    public String mostrarBus(@PathParam("matricula") String matricula) {
        Autobuses auto = null;
        Conexion conexion = new Conexion();
        try {
            auto = conexion.obtenerAutobus(matricula);
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        Gson gson = new Gson();
        return gson.toJson(auto);
    }

    @GET
    @Path("ultima/{matricula}")
    @Produces(MediaType.APPLICATION_JSON)
    public String mostrarUbicacionAutobus(@PathParam("matricula") String matricula) {
        Conexion conexion = new Conexion();
        List<Ubicaciones> lista = null;
        try {
            lista = conexion.obtenerUbicacionBus(matricula);
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        Gson gson = new Gson();

        return gson.toJson(lista);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean insertarUbicacio (String ubi) throws ParseException {
         Conexion conexion = new Conexion();
         Gson gson = new Gson();
         Ubicaciones ubicacio;
         ubicacio = gson.fromJson(ubi, Ubicaciones.class);
         boolean result = true;
        try {          
            conexion.insertarUbicacion(ubicacio);
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            result = false;             
        }
        return result;
    }
}
