package com.optimuscode.ranking.api;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/5/13
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class MathUtil {

    public static double variance(double values[]){
        return StatUtils.variance(values);
    }

    public static double mean(double values[]){
        return StatUtils.mean(values);
    }

    public static double stddev(double values[]){
        SummaryStatistics stats = new SummaryStatistics();
        for (double value: values){
            stats.addValue(value);
        }
        return stats.getStandardDeviation();
    }
}
