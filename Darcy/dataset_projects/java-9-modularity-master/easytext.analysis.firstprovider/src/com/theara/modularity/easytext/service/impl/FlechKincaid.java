package com.theara.modularity.easytext.service.impl;

import com.theara.modularity.easytext.internal.SyllableCounter;
import com.theara.modularity.easytext.counter.Counter;

public class FlechKincaid implements com.theara.easytext.service.Analysis {

    public Counter analyze(String statement){
        // TODO : dumy code
        return new SyllableCounter().getCounter(statement);
    }

}
