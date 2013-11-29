package com.optimuscode.core.java.metrics.result;

import org.gradle.api.Action;
import org.gradle.internal.UncheckedException;
import org.gradle.messaging.serialize.Decoder;
import org.gradle.messaging.serialize.Encoder;
import org.gradle.messaging.serialize.FlushableEncoder;
import org.gradle.messaging.serialize.kryo.KryoBackedDecoder;
import org.gradle.messaging.serialize.kryo.KryoBackedEncoder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/29/13
 * Time: 9:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetricSerializer {

    private static final int RESULT_VERSION = 2;

    private final File metricsFile;

    public MetricSerializer(File resultsDir) {
        this.metricsFile = new File(resultsDir, "metrics.bin");
    }

    public void write(MetricsResult metricsResult){
        try {
            OutputStream outputStream = new FileOutputStream(this.metricsFile);
            try {
                if (!metricsResult.getMetrics().isEmpty()) {
                    FlushableEncoder encoder = new KryoBackedEncoder(outputStream);
                    encoder.writeSmallInt(RESULT_VERSION);
                    write(metricsResult, encoder);
                    encoder.flush();
                }
            } finally {
                outputStream.close();
            }
        }catch(IOException ioe){
            throw new RuntimeException(ioe);
        }
    }

    private void write(MetricsResult metricsResult, Encoder encoder)
                                                        throws IOException{
        List<Metric> metrics = metricsResult.getMetrics();
        int metricsSize = metrics.size();
        encoder.writeInt(metricsSize);
        for(Metric metric: metrics){
            write(metric, encoder);
        }
    }

    private void write(Metric metric, Encoder encoder) throws IOException{
        encoder.writeString(metric.getId());
        encoder.writeString(metric.getErrorType());
        encoder.writeString(metric.getSourceName());
        encoder.writeString(metric.getMessage());
    }

    public void read(Action<? super MetricsResult> visitor){
        if (!isHasResults()) {
            return;
        }
        try {
            InputStream inputStream = new FileInputStream(metricsFile);
            try {
                Decoder decoder = new KryoBackedDecoder(inputStream);
                int version = decoder.readSmallInt();
                if (version != RESULT_VERSION) {
                    throw new IllegalArgumentException(
                            String.format(
                                    "Unexpected result file version %d found in %s.",
                                    version, metricsFile));
                }
                readResults(decoder, visitor);
            } finally {
                inputStream.close();
            }
        } catch (Exception e) {
            throw UncheckedException.throwAsUncheckedException(e);
        }

    }

    private void readResults(Decoder decoder,
                             Action<? super MetricsResult> visitor)
                            throws ClassNotFoundException, IOException{
        int metricsSize = decoder.readInt();
        MetricsResult metrics = new MetricsResult();
        for(int i = 0; i < metricsSize; i++){
            Metric metric = readClassMetric(decoder);
            metrics.addMetric(metric);
        }
        visitor.execute(metrics);
    }

    private Metric readClassMetric(Decoder decoder) throws IOException {
        String id = decoder.readString();
        String errorType = decoder.readString();
        String sourceName = decoder.readString();
        String message = decoder.readString();
        Metric metrics = new Metric(id, errorType, sourceName, message);
        return metrics;
    }

    public boolean isHasResults() {
        return metricsFile.exists() && metricsFile.length() > 0;
    }
}
