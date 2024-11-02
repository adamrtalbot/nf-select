package com.nextflow.plugin

import nextflow.Session
import nextflow.trace.TraceObserver
import nextflow.trace.TraceObserverFactory

class SelectTraceObserverFactory implements TraceObserverFactory{

    @Override
    Collection<TraceObserver> create(Session session) {
        return []
    }
}
