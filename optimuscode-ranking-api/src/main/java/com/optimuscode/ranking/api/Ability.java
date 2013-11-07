package com.optimuscode.ranking.api;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/5/13
 * Time: 11:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class Ability {
    private double mean;
    private double variance;

    private Ability(final double mean,
                    final double variance){
        this.mean = mean;
        this.variance = mean;
    }

    public static Ability withVariance(final double mean, final double variance){
        return new Ability(mean, variance);
    }

    public static Ability withStddev(final double mean,
                                final double stdDeviation){
        return new Ability(mean, stdDeviation * stdDeviation);
    }

    public static Ability zero(){
        return new Ability(0.0, 0.0);
    }

    /**
     * 68–95–99.7 three sigma rule
     * @return
     */
    public double skill(){
        return this.mean - 3 * stddev();
    }

    public Ability plus(final Ability other) {
        return withVariance(
                this.mean + other.mean,
                this.variance + other.variance);
    }

    public double stddev() {
        return Math.sqrt(this.variance);
    }

    public double getMean(){
        return this.mean;
    }

    public double getVariance(){
        return this.variance;
    }
}
