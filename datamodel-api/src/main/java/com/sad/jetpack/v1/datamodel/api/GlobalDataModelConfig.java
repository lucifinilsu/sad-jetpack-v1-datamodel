package com.sad.jetpack.v1.datamodel.api;

public class GlobalDataModelConfig {
    private boolean enableLogUtils=true;
    private boolean enableWholeLog=false;
    private IDataModelProductEngine engine;
    private long timeout=3000l;
    private final static GlobalDataModelConfig globalDataModelConfig=new GlobalDataModelConfig();
    private GlobalDataModelConfig(){}
    public static GlobalDataModelConfig getInstance(){
        return globalDataModelConfig;
    }

    public boolean isEnableWholeLog() {
        return enableWholeLog;
    }

    public boolean isEnableLogUtils() {
        return enableLogUtils;
    }

    public IDataModelProductEngine getEngine() {
        return engine;
    }

    public long getTimeout() {
        return timeout;
    }

    public GlobalDataModelConfig enableLogUtils(boolean enableLogUtils) {
        this.enableLogUtils = enableLogUtils;
        return this;
    }
    public GlobalDataModelConfig enableWholeLog(boolean enableWholeLog){
        this.enableWholeLog=enableWholeLog;
        return this;
    }

    public GlobalDataModelConfig engine(IDataModelProductEngine engine){
        this.engine=engine;
        return this;
    }

    public GlobalDataModelConfig timeout(long timeout){
        this.timeout=timeout;
        return this;
    }
}
