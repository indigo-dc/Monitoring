package com.indigo.mesosprobe.mesos.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetMetricsResponse extends MesosClientRequest {

    public class MetricType {

        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public class GetMetricsType {
        private List<MetricType> metrics;

        public List<MetricType> getMetrics() {
            return metrics;
        }

        public void setMetrics(List<MetricType> metrics) {
            this.metrics = metrics;
        }
    }

    public GetMetricsResponse(boolean requestMode) {
        super(RequestType.GET_METRICS);
        if (requestMode) {
            this.getMetrics = new GetMetricsType();
        }
    }

    public GetMetricsResponse() {
        this(false);
    }

    @SerializedName("get_metrics")
    private GetMetricsType getMetrics;

    public GetMetricsType getGetMetrics() {
        return getMetrics;
    }

    public void setGetMetrics(GetMetricsType getMetrics) {
        this.getMetrics = getMetrics;
    }
}
