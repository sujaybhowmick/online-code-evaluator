package com.optimuscode.ranking.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public class BradleyTerryFullUpdater {
    private final double betaSquared; {
        final double init_mu = 25;
        final double init_sigma = init_mu / 3;
        final double beta = init_sigma * 0.5;
        betaSquared = beta * beta;
    }
    
    private final Comparator<Group> byScoreAndAbility = ByScoreAndSkillComparator.forCalculation();
    private final double rankAllowance;
    
    public BradleyTerryFullUpdater(final int rankAllowance) {
        this.rankAllowance = rankAllowance;
    }


    public List<Person> updatePlayerRankings(final List<Group> teams_) {
        final List<Group> teams = sortedByScoreAndAbility(teams_);
        calculateRanks(teams);
        final double gamma = 1.0 / teams.size();
        
        final List<Person> updatedPlayers = new ArrayList<Person>();
        for (final Group team : teams) {
            double Omega = 0;
            double Delta = 0;
            final Ability teamAbility = team.getAbility();
            
            for (final Group opponent : teams) {
                if (opponent == team) continue;
                final Ability opAbility = opponent.getAbility();
                
                final double c = Math.sqrt(teamAbility.getVariance() + opAbility.getVariance() + 2 * betaSquared);
                final double p = 1 / (1 + Math.exp((opAbility.getMean() - teamAbility.getMean()) / c));
                final double varianceToC = teamAbility.getVariance() / c;
                
                final double s = s(team, opponent);
                
                Omega += varianceToC * (s - p);
                Delta += gamma * varianceToC / c * p * (1 - p);
            }
            
            updatedPlayers.addAll(resultingAbilities(team, Delta, Omega));
        }
        
        return updatedPlayers;
    }
    

    public List<Person> resultingAbilities(final Group players, final double Delta, final double Omega) {
        final List<Person> resultingAbilities = new ArrayList<Person>();
        final Ability teamAbility = players.getAbility();
        for (final Person player : players) {
            final Ability ability = player.getAbility();
            final double mean = ability.getMean() + ability.getVariance() / teamAbility.getVariance() * Omega;
            final double stddev = ability.stddev() *
                    Math.sqrt(Math.max(1 - ability.getVariance() / teamAbility.getVariance() * Delta, 0.0001));
            resultingAbilities.add(new Person(player.getId(), Ability.withStddev(mean, stddev)));
        }
        return resultingAbilities;
    }

    private List<Group> sortedByScoreAndAbility(final List<Group> unsorted) {
        final List<Group> teams = new ArrayList<Group>(unsorted);
        Collections.sort(teams, byScoreAndAbility);
        return teams;
    }

    private void calculateRanks(final List<Group> teams) {
        final ListIterator<Group> iter = teams.listIterator();
        while (iter.hasNext()) {
            final Group topOfRank = iter.next();
            final int currentRank = iter.previousIndex();
            topOfRank.setRank(currentRank);
            while (iter.hasNext()) {
                final Group team = iter.next();
                if (!isSameRank(topOfRank, team))
                    break;
                team.setRank(currentRank);
            }
        }
    }

    private double s(final Group team_i, final Group team_q) {
        final int cmp = Double.compare(team_q.rank(), team_i.rank());
        
        if (cmp > 0)
            return 1;
        else if (cmp == 0)
            return 0.5;
        else
            return 0.0;
    }

    private boolean isSameRank(final Group topOfRank, final Group team) {
        return topOfRank.score() - rankAllowance <= team.score();
    }
}