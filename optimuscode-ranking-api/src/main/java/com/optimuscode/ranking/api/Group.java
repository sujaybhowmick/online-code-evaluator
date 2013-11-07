package com.optimuscode.ranking.api;

import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/5/13
 * Time: 12:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class Group implements Iterable<Person> {

    private String id;
    private final double score;
    private final List<Person> people;
    private final Ability ability;
    private int rank = -1;

    public Group(final String id, final double score,
                 final List<Person> people) {
        this.id = id;
        this.score = score;
        this.people = people;
        this.ability = calculateAbility(people);
    }

    private static Ability calculateAbility(final List<Person> people) {
        Ability aggregate = Ability.zero();
        for (final Person person : people) {
            aggregate = aggregate.plus(person.getAbility());
        }
        return aggregate;
    }

    public double score() {
        return score;
    }

    public int rank() {
        if (rank < 0)
            throw new IllegalArgumentException("Rank not calculated yet");
        return rank;
    }

    public void setRank(final int rank) {
        this.rank = rank;
    }

    public double skill() {
        return ability.skill();
    }

    public Ability getAbility() {
        return ability;
    }

    public String getId() {
        return id;
    }

    public int size() {
        return people.size();
    }

    @Override
    public Iterator<Person> iterator() {
        return people.iterator();
    }

    @Override
    public String toString() {
        return getId();
    }
}
