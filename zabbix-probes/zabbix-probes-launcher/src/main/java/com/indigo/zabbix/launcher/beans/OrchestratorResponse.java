package com.indigo.zabbix.launcher.beans;

import java.util.List;

public class OrchestratorResponse<T> {

    private List<T> content;

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
