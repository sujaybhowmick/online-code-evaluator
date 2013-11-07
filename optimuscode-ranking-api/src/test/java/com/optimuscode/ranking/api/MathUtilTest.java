package com.optimuscode.ranking.api;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/5/13
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class MathUtilTest {
    /*double scores[] = {
            100, 100,
            95, 95, 95, 95, 95,
            90, 90, 90, 90, 90, 90, 90,
            85, 85, 85, 85, 85, 85, 85, 85, 85,
            80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80,
            75, 75, 75, 75, 75, 75, 75, 75, 75, 75, 75, 75, 75,
            70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70,
            65, 65, 65, 65, 65, 65, 65,
            60, 60, 60
    };*/
    double scores[] = {65, 66, 67, 69, 70, 70, 70, 71, 71, 72, 73, 74, 76};
    double scores2[] = {50, 53, 54, 55, 55, 58, 59, 59, 75, 95, 98, 100, 100};

    @Test
    public void testVariance() throws Exception {
        double variance = MathUtil.variance(scores);
        DecimalFormat df = new DecimalFormat("#.##");
        assertEquals(9.73, Double.parseDouble(df.format(variance)), 0.00);
        double variance2 = MathUtil.variance(scores2);
        assertEquals(417.91, Double.parseDouble(df.format(variance2)), 0.00);
    }

    @Test
    public void testMean() throws Exception {
        double mean = MathUtil.mean(scores);
        DecimalFormat df = new DecimalFormat("#.##");
        assertEquals(70.31, Double.parseDouble(df.format(mean)), 0.00);
        double mean2 = MathUtil.mean(scores2);
        assertEquals(70.08, Double.parseDouble(df.format(mean2)), 0.00);
    }

    @Test
    public void testStddev() throws Exception {
        double stddev = MathUtil.stddev(scores);
        DecimalFormat df = new DecimalFormat("#.##");
        assertEquals(3.12, Double.parseDouble(df.format(stddev)), 0.00);
        double stddev2 = MathUtil.stddev(scores2);
        assertEquals(20.44, Double.parseDouble(df.format(stddev2)), 0.00);

    }
}
