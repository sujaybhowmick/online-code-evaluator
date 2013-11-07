package com.optimuscode.ranking.api;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/5/13
 * Time: 9:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Rank {

    public void doRank(Group[] groups){
        List<Group> groupsToRank = Arrays.asList(groups);
        Collections.shuffle(groupsToRank);
        final List<Person> updatedPlayers = new BradleyTerryFullUpdater(10).
                                            updatePlayerRankings(groupsToRank);
        System.out.println("Game outcome:");
        System.out.println(split(sort(groupsToRank, ByScoreAndSkillComparator.
                                                               forDisplay())));

    }

    private static Comparator<Person> byId() {
        return new Comparator<Person>() {
            public int compare(Person a, Person b) {
                return a.getId().compareTo(b.getId());
            }
        };
    }

    private static <T> String split(final List<T> list) {
        final StringBuilder builder = new StringBuilder();
        for (final T item : list) {
            builder.append(item).append("\n");
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    private static <T> List<T> sort(final List<T> list,
                                    final Comparator<? super T> cmp) {
        final ArrayList<T> sorted = new ArrayList<T>(list);
        Collections.sort(sorted, cmp);
        return sorted;
    }

    public static void main(final String[] args) {
        final Group team1 = new Group("1", 500,
                Arrays.asList(new Person("1", Ability.withStddev(25, 8)),
                        new Person("2", Ability.withStddev(27, 5)),
                        new Person("3", Ability.withStddev(22, 3))));


        final Group team2 = new Group("2", 400,
                Arrays.asList(new Person("4", Ability.withStddev(25, 8)),
                        new Person("5", Ability.withStddev(27, 5)),
                        new Person("6", Ability.withStddev(22, 3))));

        final Group team3 = new Group("3", 395,
                Arrays.asList(new Person("7", Ability.withStddev(25, 8)),
                        new Person("8", Ability.withStddev(27, 5)),
                        new Person("9", Ability.withStddev(22, 3))));

        final List<Group> teams = Arrays.asList(team1, team2, team3);
        Collections.shuffle(teams);

        System.out.println("Game outcome:");
        System.out.println(split(sort(teams, ByScoreAndSkillComparator.forDisplay())));

        final List<Person> updatedPlayers = new BradleyTerryFullUpdater(10).updatePlayerRankings(teams);

        System.out.println();
        System.out.println("Updated abilities:");
        System.out.println(split(sort(updatedPlayers, byId())));
    }
}
