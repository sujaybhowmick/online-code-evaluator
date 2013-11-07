package com.optimuscode.ranking.api;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/5/13
 * Time: 11:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class Person {
    private String id;
    private Ability ability;
    private double score;


    public Person(final String id, final Ability ability){
        this.id = id;
        this.ability = ability;
    }

    public Ability getAbility(){
        return this.ability;
    }

    public double getScore(){
        return this.score;
    }

    public String getId(){
        return this.id;
    }

    @Override
    public String toString() {
        return getId();
    }
}
