
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
object index extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template1[RequestHeader,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/()(implicit req: RequestHeader):play.twirl.api.HtmlFormat.Appendable = {
      _display_ {

Seq[Any](format.raw/*1.33*/("""

"""),_display_(/*3.2*/main/*3.6*/ {_display_(Seq[Any](format.raw/*3.8*/("""

    """),format.raw/*5.5*/("""<div class="container">
        <nav class="navbar navbar-default" role="navigation">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <span class="navbar-brand">Reactive Maps</span>
            </div>
            <div class="collapse navbar-collapse navbar-ex1-collapse">
                <ul class="nav navbar-nav">
                    <li data-bind="ifnot: disconnected, click: disconnect"><a href="#">Disconnect</a></li>
                    <li data-bind="ifnot: disconnected, click: toggleMockGps, css: """),format.raw/*19.84*/("""{"""),format.raw/*19.85*/("""active: mockGps() != null"""),format.raw/*19.110*/("""}"""),format.raw/*19.111*/(""""><a href="#">Mock GPS</a></li>
                </ul>
            </div>
        </nav>

        <div data-bind="if: disconnected">
            <form role="form">
                <div class="form-group">
                    <input type="email" class="form-control" data-bind="value: email" placeholder="Email address"/>
                </div>
            </form>
            <button class="btn btn-primary" data-bind="click: submitEmail">Connect</button>
        </div>

        <div data-bind="if: connecting">
            <span data-bind="text: connecting"></span>
        </div>

        <div class="row maps" data-bind="ifnot: disconnected">
            <div data-bind="css: """),format.raw/*38.34*/("""{"""),format.raw/*38.35*/("""'col-md-12': mockGps() == null, 'col-md-8': mockGps() != null"""),format.raw/*38.96*/("""}"""),format.raw/*38.97*/("""" id="map"></div>
            <div class="mockGpsContainer col-md-4">
                <div id="mockGps"></div>
            </div>
        </div>
    </div>
""")))}),format.raw/*44.2*/("""
"""))}
  }

  def render(req:RequestHeader): play.twirl.api.HtmlFormat.Appendable = apply()(req)

  def f:(() => (RequestHeader) => play.twirl.api.HtmlFormat.Appendable) = () => (req) => apply()(req)

  def ref: this.type = this

}
              /*
                  -- GENERATED --
                  DATE: Thu Sep 18 17:05:53 CEST 2014
                  SOURCE: /Users/hegglin/git/dyndco/monitoring_old/app/views/index.scala.html
                  HASH: 88d7dd4a86873cc2cff50956ae32b5909a4be22e
                  MATRIX: 512->1|631->32|659->35|670->39|708->41|740->47|1647->926|1676->927|1730->952|1760->953|2467->1632|2496->1633|2585->1694|2614->1695|2801->1852
                  LINES: 19->1|22->1|24->3|24->3|24->3|26->5|40->19|40->19|40->19|40->19|59->38|59->38|59->38|59->38|65->44
                  -- GENERATED --
              */
          