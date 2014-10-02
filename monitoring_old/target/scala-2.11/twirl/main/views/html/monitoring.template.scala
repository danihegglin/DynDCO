
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._

import play.api.templates.PlayMagic._
import models._
import controllers._
import play.api.i18n._
import play.api.mvc._
import play.api.data._
import views.html._

/**/
object monitoring extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template0[play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply():play.twirl.api.HtmlFormat.Appendable = {
      _display_ {import play.mvc.Http.Context.Implicit._

Seq[Any](format.raw/*1.1*/("""<!DOCTYPE html>

"""),format.raw/*4.1*/("""
"""),format.raw/*5.1*/("""<html>
<head>
    <title>Reactive Stock News Dashboard</title>
    <link rel='stylesheet' href='"""),_display_(/*8.35*/routes/*8.41*/.Assets.at("lib/bootstrap/css/bootstrap.min.css")),format.raw/*8.90*/("""'>
    <link rel="stylesheet" media="screen" href=""""),_display_(/*9.50*/routes/*9.56*/.Assets.at("stylesheets/main.css")),format.raw/*9.90*/("""">
    <link rel="shortcut icon" type="image/png" href=""""),_display_(/*10.55*/routes/*10.61*/.Assets.at("images/favicon.png")),format.raw/*10.93*/("""">
    <script type='text/javascript' src='"""),_display_(/*11.42*/routes/*11.48*/.Assets.at("lib/jquery/jquery.min.js")),format.raw/*11.86*/("""'></script>
    <script type='text/javascript' src='"""),_display_(/*12.42*/routes/*12.48*/.Assets.at("lib/flot/jquery.flot.js")),format.raw/*12.85*/("""'></script>
    <script type='text/javascript' src='"""),_display_(/*13.42*/routes/*13.48*/.Assets.at("javascripts/index.js")),format.raw/*13.82*/("""'></script>
</head>
<body data-ws-url=""""),_display_(/*15.21*/routes/*15.27*/.Monitoring.monitor.webSocketURL(request)),format.raw/*15.68*/("""">
    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container">
                <a class="brand" href="#">Reactive Stocks</a>
                <form id="addsymbolform" class="navbar-form pull-right">
                    <input id="addsymboltext" type="text" class="span2" placeholder="SYMBOL">
                    <button type="submit" class="btn">Add Stock</button>
                </form>
            </div>
        </div>
    </div>

    <div id="stocks" class="container">

    </div>
</body>
</html>"""))}
  }

  def render(): play.twirl.api.HtmlFormat.Appendable = apply()

  def f:(() => play.twirl.api.HtmlFormat.Appendable) = () => apply()

  def ref: this.type = this

}
              /*
                  -- GENERATED --
                  DATE: Thu Sep 18 17:05:53 CEST 2014
                  SOURCE: /Users/hegglin/git/dyndco/monitoring_old/app/views/monitoring.scala.html
                  HASH: 3a113231f6e322bc1117cd8a35870e0e9484dec9
                  MATRIX: 624->0|667->58|694->59|817->156|831->162|900->211|978->263|992->269|1046->303|1130->360|1145->366|1198->398|1269->442|1284->448|1343->486|1423->539|1438->545|1496->582|1576->635|1591->641|1646->675|1713->715|1728->721|1790->762
                  LINES: 22->1|24->4|25->5|28->8|28->8|28->8|29->9|29->9|29->9|30->10|30->10|30->10|31->11|31->11|31->11|32->12|32->12|32->12|33->13|33->13|33->13|35->15|35->15|35->15
                  -- GENERATED --
              */
          