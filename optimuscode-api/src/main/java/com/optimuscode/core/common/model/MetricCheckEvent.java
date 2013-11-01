package com.optimuscode.core.common.model;

import java.util.EventObject;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 10/14/13
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class MetricCheckEvent extends EventObject {


    private List<String> messages;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException
     *          if source is null.
     */
    public MetricCheckEvent(Object source) {
        this(source, null);
    }

    public MetricCheckEvent(Object source, List<String> messages) {
        super(source);
        this.messages = messages;
    }

}
