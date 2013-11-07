package com.optimuscode.ranking.api;

import java.util.Comparator;

/**
 * Comparators which sorts a team by score and ability.
 * <p>
 * These comparators will produce the same sorted results no matter the initial
 * order of the teams. This is important when using a partial-pair updater
 * instead of a full-pair updater.
 * </p>
 */
public final class ByScoreAndSkillComparator {
    private ByScoreAndSkillComparator() {}

    /**
     * A comparator which gives preference to teams with
     * <ol>
     * <li>Higher score;</li>
     * <li>More players;</li>
     * <li>Higher skill;</li>
     * <li>Lower id</li>
     * </ol>
     * 
     * <p>
     * <b>Rationale for tie-breakers</b> A partial-pair updater only updates
     * players' abilities based on how it compared to the surrounding teams by
     * rank. When two teams tie, one team will be compared with a better team
     * while the other is compared with a worse team, in terms of score. In
     * other words, of these two teams the team this comparator puts first will
     * get attributed a loss while the other team is attributed a win. (Both
     * teams will also be attributed a draw.)
     * </p>
     * <p>
     * The team with less players and equal score likely had better individual
     * ability, and are accordingly attributed the "win."
     * </p>
     * <p>
     * Accounting for number of players, it is assumed a team with higher skill
     * should have beaten a team with lower skill. Thus, this comparator
     * positions the underdog to gain the win.
     * </p>
     * <p>
     * <b>Note:</b> The tie-breaking doesn't matter when using a full-pair
     * updater because a team is always compared to every other team.
     * </p>
     */
    public static Comparator<Group> forCalculation() {
        return new Comparator<Group>() {
            @Override
            public int compare(Group a, Group b) {
                final int scoreCmp = -Double.compare(a.score(), b.score());
                if (scoreCmp != 0)
                    return scoreCmp;
                
                final int countCmp = -Double.compare(a.size(), b.size());
                if (countCmp != 0)
                    return countCmp;
                
                final int abilityCmp = -Double.compare(a.skill(), b.skill());
                if (abilityCmp != 0)
                    return abilityCmp;
                
                return 0;
            }
        };
    }
    
    /**
     * A comparator which gives preference to teams with
     * <ol>
     * <li>Higher score;</li>
     * <li>Less players;</li>
     * <li>Lower skill;</li>
     * <li>Higher id</li>
     * </ol>
     * 
     * See the documentation of {@link #forCalculation()} for rationale of the
     * tie-breakers which are flipped in this method to provide better user
     * experience.
     */
    public static Comparator<Group> forDisplay() {
        return new Comparator<Group>() {
            @Override
            public int compare(Group a, Group b) {
                final int scoreCmp = -Double.compare(a.score(), b.score());
                if (scoreCmp != 0)
                    return scoreCmp;
                
                final int countCmp = Double.compare(a.size(), b.size());
                if (countCmp != 0)
                    return countCmp;
                
                final int abilityCmp = Double.compare(a.skill(), b.skill());
                if (abilityCmp != 0)
                    return abilityCmp;

                return 0;
            }
        };
    }
}