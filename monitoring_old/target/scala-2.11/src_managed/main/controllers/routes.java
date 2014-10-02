// @SOURCE:/Users/hegglin/git/dyndco/monitoring_old/conf/routes
// @HASH:7bcf17481f4646294dd96a5f6a4303dd1664782b
// @DATE:Thu Sep 18 17:05:52 CEST 2014

package controllers;

public class routes {
public static final controllers.ReverseMonitoring Monitoring = new controllers.ReverseMonitoring();
public static final controllers.ReverseAssets Assets = new controllers.ReverseAssets();
public static final controllers.ReverseApplication Application = new controllers.ReverseApplication();

public static class javascript {
public static final controllers.javascript.ReverseMonitoring Monitoring = new controllers.javascript.ReverseMonitoring();
public static final controllers.javascript.ReverseAssets Assets = new controllers.javascript.ReverseAssets();
public static final controllers.javascript.ReverseApplication Application = new controllers.javascript.ReverseApplication();
}
          

public static class ref {
public static final controllers.ref.ReverseMonitoring Monitoring = new controllers.ref.ReverseMonitoring();
public static final controllers.ref.ReverseAssets Assets = new controllers.ref.ReverseAssets();
public static final controllers.ref.ReverseApplication Application = new controllers.ref.ReverseApplication();
}
          
}
          