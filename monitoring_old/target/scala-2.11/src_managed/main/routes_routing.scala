// @SOURCE:/Users/hegglin/git/dyndco/monitoring_old/conf/routes
// @HASH:7bcf17481f4646294dd96a5f6a4303dd1664782b
// @DATE:Thu Sep 18 17:05:52 CEST 2014


import play.core._
import play.core.Router._
import play.core.Router.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._
import _root_.controllers.Assets.Asset

import Router.queryString

object Routes extends Router.Routes {

import ReverseRouteContext.empty

private var _prefix = "/"

def setPrefix(prefix: String) {
  _prefix = prefix
  List[(String,Routes)]().foreach {
    case (p, router) => router.setPrefix(prefix + (if(prefix.endsWith("/")) "" else "/") + p)
  }
}

def prefix = _prefix

lazy val defaultPrefix = { if(Routes.prefix.endsWith("/")) "" else "/" }


// @LINE:6
private[this] lazy val controllers_Application_index0_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix))))
private[this] lazy val controllers_Application_index0_invoker = createInvoker(
controllers.Application.index,
HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "index", Nil,"GET", """ Home page""", Routes.prefix + """"""))
        

// @LINE:10
private[this] lazy val controllers_Monitoring_updateAgent1_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("utility/agent/"),DynamicPart("id", """[^/]+""",true))))
private[this] lazy val controllers_Monitoring_updateAgent1_invoker = createInvoker(
controllers.Monitoring.updateAgent(fakeValue[String]),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Monitoring", "updateAgent", Seq(classOf[String]),"GET", """ Monitoring Functionality
GET       /utility/global     controllers.Application.global()""", Routes.prefix + """utility/agent/$id<[^/]+>"""))
        

// @LINE:11
private[this] lazy val controllers_Monitoring_monitor2_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("monitor"))))
private[this] lazy val controllers_Monitoring_monitor2_invoker = createInvoker(
controllers.Monitoring.monitor(),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Monitoring", "monitor", Nil,"GET", """""", Routes.prefix + """monitor"""))
        

// @LINE:14
private[this] lazy val controllers_Assets_versioned3_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("assets/"),DynamicPart("file", """.+""",false))))
private[this] lazy val controllers_Assets_versioned3_invoker = createInvoker(
controllers.Assets.versioned(fakeValue[String], fakeValue[Asset]),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Assets", "versioned", Seq(classOf[String], classOf[Asset]),"GET", """ Static assets""", Routes.prefix + """assets/$file<.+>"""))
        

// @LINE:15
private[this] lazy val controllers_Assets_at4_route = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("assets/"),DynamicPart("file", """.+""",false))))
private[this] lazy val controllers_Assets_at4_invoker = createInvoker(
controllers.Assets.at(fakeValue[String], fakeValue[String]),
HandlerDef(this.getClass.getClassLoader, "", "controllers.Assets", "at", Seq(classOf[String], classOf[String]),"GET", """""", Routes.prefix + """assets/$file<.+>"""))
        
def documentation = List(("""GET""", prefix,"""controllers.Application.index"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """utility/agent/$id<[^/]+>""","""controllers.Monitoring.updateAgent(id:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """monitor""","""controllers.Monitoring.monitor()"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """assets/$file<.+>""","""controllers.Assets.versioned(path:String = "/public", file:Asset)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """assets/$file<.+>""","""controllers.Assets.at(path:String = "/public", file:String)""")).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
  case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
  case l => s ++ l.asInstanceOf[List[(String,String,String)]]
}}
      

def routes:PartialFunction[RequestHeader,Handler] = {

// @LINE:6
case controllers_Application_index0_route(params) => {
   call { 
        controllers_Application_index0_invoker.call(controllers.Application.index)
   }
}
        

// @LINE:10
case controllers_Monitoring_updateAgent1_route(params) => {
   call(params.fromPath[String]("id", None)) { (id) =>
        controllers_Monitoring_updateAgent1_invoker.call(controllers.Monitoring.updateAgent(id))
   }
}
        

// @LINE:11
case controllers_Monitoring_monitor2_route(params) => {
   call { 
        controllers_Monitoring_monitor2_invoker.call(controllers.Monitoring.monitor())
   }
}
        

// @LINE:14
case controllers_Assets_versioned3_route(params) => {
   call(Param[String]("path", Right("/public")), params.fromPath[Asset]("file", None)) { (path, file) =>
        controllers_Assets_versioned3_invoker.call(controllers.Assets.versioned(path, file))
   }
}
        

// @LINE:15
case controllers_Assets_at4_route(params) => {
   call(Param[String]("path", Right("/public")), params.fromPath[String]("file", None)) { (path, file) =>
        controllers_Assets_at4_invoker.call(controllers.Assets.at(path, file))
   }
}
        
}

}
     