package com.payneteasy.server;

public class ExampleRequest {

    /** Name */
    public String getName() { return name; }
    public void setName(String aName) { name = aName; }

    /** Name */
    private String name;

    @Override
    public String toString() {
        return "ExampleRequest{" +
                "name='" + name + '\'' +
                '}';
    }
}
