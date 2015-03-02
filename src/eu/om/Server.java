package eu.om;

import javax.xml.ws.Endpoint;

public class Server {
    protected Server() throws Exception {
        System.out.println("Starting Server");
        OntologyManagerImpl implementor = new OntologyManagerImpl();
        implementor.clear();
        implementor.load();
        String address = "http://"+System.getProperty("service.server")+":"+System.getProperty("service.port")+"/"+System.getProperty("service.name");
        Endpoint.publish(address, implementor);
    }

    public static void main(String args[]) throws Exception {
        new Server();
        System.out.println("Server ready...");
    }

}
