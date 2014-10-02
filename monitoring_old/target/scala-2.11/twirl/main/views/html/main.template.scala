
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
object main extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template2[Html,RequestHeader,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(content: Html)(implicit req: RequestHeader):play.twirl.api.HtmlFormat.Appendable = {
      _display_ {

Seq[Any](format.raw/*1.46*/("""

"""),format.raw/*3.1*/("""<!DOCTYPE html>

<html>
    <head>
        <title>Reactive Maps</title>

        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>

        <link rel="stylesheet" media="screen" href=""""),_display_(/*11.54*/routes/*11.60*/.Assets.versioned("lib/bootstrap/css/bootstrap.css")),format.raw/*11.112*/(""""/>
        <link rel="stylesheet" media="screen" href=""""),_display_(/*12.54*/routes/*12.60*/.Assets.versioned("lib/bootstrap/css/bootstrap-theme.css")),format.raw/*12.118*/(""""/>
        <link rel="stylesheet" media="screen" href=""""),_display_(/*13.54*/routes/*13.60*/.Assets.versioned("stylesheets/main.css")),format.raw/*13.101*/(""""/>
        <link rel="stylesheet" href=""""),_display_(/*14.39*/routes/*14.45*/.Assets.versioned("lib/leaflet/leaflet.css")),format.raw/*14.89*/("""" />

        <link rel="shortcut icon" type="image/png" href=""""),_display_(/*16.59*/routes/*16.65*/.Assets.versioned("images/favicon.png")),format.raw/*16.104*/("""">

        <script data-main=""""),_display_(/*18.29*/routes/*18.35*/.Assets.versioned("javascripts/main.js")),format.raw/*18.75*/("""" type="text/javascript" src=""""),_display_(/*18.106*/routes/*18.112*/.Assets.versioned("lib/requirejs/require.js")),format.raw/*18.157*/(""""></script>
    </head>
    <body>
        """),_display_(/*21.10*/content),format.raw/*21.17*/("""
    """),format.raw/*22.5*/("""</body>
</html>
"""))}
  }

  def render(content:Html,req:RequestHeader): play.twirl.api.HtmlFormat.Appendable = apply(content)(req)

  def f:((Html) => (RequestHeader) => play.twirl.api.HtmlFormat.Appendable) = (content) => (req) => apply(content)(req)

  def ref: this.type = this

}
              /*
                  -- GENERATED --
                  DATE: Thu Sep 18 17:05:53 CEST 2014
                  SOURCE: /Users/hegglin/git/dyndco/monitoring_old/app/views/main.scala.html
                  HASH: 245b88d6ecd56e1009f39c8f021c9a6e28838817
                  MATRIX: 516->1|648->45|676->47|947->291|962->297|1036->349|1120->406|1135->412|1215->470|1299->527|1314->533|1377->574|1446->616|1461->622|1526->666|1617->730|1632->736|1693->775|1752->807|1767->813|1828->853|1887->884|1903->890|1970->935|2041->979|2069->986|2101->991
                  LINES: 19->1|22->1|24->3|32->11|32->11|32->11|33->12|33->12|33->12|34->13|34->13|34->13|35->14|35->14|35->14|37->16|37->16|37->16|39->18|39->18|39->18|39->18|39->18|39->18|42->21|42->21|43->22
                  -- GENERATED --
              */
          