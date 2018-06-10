/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import modelo.Usuario;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author equipo_2
 */
@ServerEndpoint("/wsNotificacion/{idUsuario}")
public class wsNotificacion {

    static ArrayList<Session> sessions = new ArrayList<>();    
    static HashMap<String, Integer> usuarios = new HashMap<>();
    
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            JSONObject obj = new JSONObject(message);
            String evento = obj.getString("evento");
            switch(evento){
                case "notificar":
                    notificar(obj);
                    break;
            }
        } catch (JSONException ex) {
            Logger.getLogger(wsNotificacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void notificar(JSONObject obj) {
        try {
            int idReceptor = obj.getInt("idUsuario");            
            
            Iterator it = usuarios.entrySet().iterator();
            String id_session="";            
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();
                if(((Integer)pair.getValue())==idReceptor){                    
                    id_session=(String)pair.getKey();
                    break;
                }                
            }
                       
            for(Session sesion:sessions){
                if(sesion.getId().equals(id_session)){
                    sesion.getAsyncRemote().sendText(obj.toString());
                    break;
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(wsNotificacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @OnOpen
    public void onOpen(@PathParam("idUsuario") int idUsuario,Session s) {
        agregarSession(s, idUsuario);
    }

    @OnClose
    public void onClose(Session s) {
        quitarSession(s);
    }

    private void agregarSession(Session session, int idUsuario){
        usuarios.put(session.getId(), idUsuario);
        sessions.add(session);        
    }    
    
    private void quitarSession(Session session){
        usuarios.remove(session.getId());
        sessions.remove(session);        
    } 
    
}
