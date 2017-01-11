package com.maxeremin.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void readTypes(AsyncCallback<Void> callback);
    void readMenu(AsyncCallback<Void> callback);
    void search(String name, AsyncCallback<String> callback);
    void add(String name, int type, double price, AsyncCallback<Void> callback);
    void remove(String name, AsyncCallback<Void> callback);
    void update(String name, String newName, int type, double price, AsyncCallback<Void> callback);
    void save(AsyncCallback<Void> callback);
    void getMenu(AsyncCallback<String[]> callback);
}
