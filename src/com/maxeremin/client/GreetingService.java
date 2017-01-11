package com.maxeremin.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	void readTypes() throws Exception;
    void readMenu() throws Exception;
    String search(String name) throws Exception;
    void add(String name, int type, double price) throws Exception;
    void remove(String name) throws Exception;
    void update(String name, String newName, int type, double price) throws Exception;
    void save() throws Exception;
    String[] getMenu();
}
