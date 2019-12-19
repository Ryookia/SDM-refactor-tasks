package com.vitting.rcpsudoku.config;


public static class BuildConfig {
  private final static Boolean isDebug = false;

  public setIsDebug(Boolean isDebug){
    this.isDebug = isDebug;
  }

  public getIsDebug(){
    return this.isDebug;
  }
}
