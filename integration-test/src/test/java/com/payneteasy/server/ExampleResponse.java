package com.payneteasy.server;

public class ExampleResponse {

    public ExampleResponse() {
    }

    public ExampleResponse(String aName) {
        name = aName;
    }

    /** Name */
    public String getName() { return name; }
    public void setName(String aName) { name = aName; }

    /** Name */

    private String name;

    @Override
    public String toString() {
        return "ExampleResponse{" +
                "name='" + name + '\'' +
                '}';
    }
}
