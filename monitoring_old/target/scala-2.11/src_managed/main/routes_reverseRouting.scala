// @SOURCE:/Users/hegglin/git/dyndco/monitoring_old/conf/routes
// @HASH:7bcf17481f4646294dd96a5f6a4303dd1664782b
// @DATE:Thu Sep 18 17:05:52 CEST 2014

import Routes.{prefix => _prefix, defaultPrefix => _defaultPrefix}
import play.core._
import play.core.Router._
import play.core.Router.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._
import _root_.controllers.Assets.Asset

import Router.queryString


// @LINE:15
// @LINE:14
// @LINE:11
// @LINE:10
// @LINE:6
package controllers {

// @LINE:11
// @LINE:10
class ReverseMonitoring {


// @LINE:10
def updateAgent(id:String): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "utility/agent/" + implicitly[PathBindable[String]].unbind("id", dynamicString(id)))
}
                        

// @LINE:11
def monitor(): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "monitor")
}
                        

}
                          

// @LINE:15
// @LINE:14
class ReverseAssets {


// @LINE:15
def at(file:String): Call = {
   implicit val _rrc = new ReverseRouteContext(Map(("path", "/public")))
   Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[String]].unbind("file", file))
}
                        

// @LINE:14
def versioned(file:Asset): Call = {
   implicit val _rrc = new ReverseRouteContext(Map(("path", "/public")))
   Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[Asset]].unbind("file", file))
}
                        

}
                          

// @LINE:6
class ReverseApplication {


// @LINE:6
def index(): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix)
}
                        

}
                          
}
                  


// @LINE:15
// @LINE:14
// @LINE:11
// @LINE:10
// @LINE:6
package controllers.javascript {
import ReverseRouteContext.empty

// @LINE:11
// @LINE:10
class ReverseMonitoring {


// @LINE:10
def updateAgent : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Monitoring.updateAgent",
   """
      function(id) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "utility/agent/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("id", encodeURIComponent(id))})
      }
   """
)
                        

// @LINE:11
def monitor : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Monitoring.monitor",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "monitor"})
      }
   """
)
                        

}
              

// @LINE:15
// @LINE:14
class ReverseAssets {


// @LINE:15
def at : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
   """
)
                        

// @LINE:14
def versioned : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Assets.versioned",
   """
      function(file) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[Asset]].javascriptUnbind + """)("file", file)})
      }
   """
)
                        

}
              

// @LINE:6
class ReverseApplication {


// @LINE:6
def index : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.index",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + """"})
      }
   """
)
                        

}
              
}
        


// @LINE:15
// @LINE:14
// @LINE:11
// @LINE:10
// @LINE:6
package controllers.ref {


// @LINE:11
// @LINE:10
class ReverseMonitoring {


// @LINE:10
def updateAgent(id:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Monitoring.updateAgent(id), HandlerDef(this.getClass.getClassLoader, "", "controllers.Monitoring", "updateAgent", Seq(classOf[String]), "GET", """ Monitoring Functionality
GET       /utility/global     controllers.Application.global()""", _prefix + """utility/agent/$id<[^/]+>""")
)
                      

// @LINE:11
def monitor(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Monitoring.monitor(), HandlerDef(this.getClass.getClassLoader, "", "controllers.Monitoring", "monitor", Seq(), "GET", """""", _prefix + """monitor""")
)
                      

}
                          

// @LINE:15
// @LINE:14
class ReverseAssets {


// @LINE:15
def at(path:String, file:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this.getClass.getClassLoader, "", "controllers.Assets", "at", Seq(classOf[String], classOf[String]), "GET", """""", _prefix + """assets/$file<.+>""")
)
                      

// @LINE:14
def versioned(path:String, file:Asset): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Assets.versioned(path, file), HandlerDef(this.getClass.getClassLoader, "", "controllers.Assets", "versioned", Seq(classOf[String], classOf[Asset]), "GET", """ Static assets""", _prefix + """assets/$file<.+>""")
)
                      

}
                          

// @LINE:6
class ReverseApplication {


// @LINE:6
def index(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.index(), HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "index", Seq(), "GET", """ Home page""", _prefix + """""")
)
                      

}
                          
}
        
    