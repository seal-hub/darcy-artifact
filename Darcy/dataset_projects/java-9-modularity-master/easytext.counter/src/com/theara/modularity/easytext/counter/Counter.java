package com.theara.modularity.easytext.counter;

public class Counter {

    private String statement;

    public Counter(String statement){
        this.statement = statement;
    }

    public double getStatementLength(){
        return ((double) this.statement.length()) / 100;
    }

}
