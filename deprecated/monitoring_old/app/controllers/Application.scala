package controllers

import play.api.mvc._
import play.api.Play.current

object Application extends Controller {

  /**
   * The index page.
   */
  def index = Action { implicit req =>
    Ok(views.html.index())
  }
  
   /**
   * The monitoring page.
   */
  def global = Action { implicit req =>
    Ok(views.html.monitoring())
  }

  /**
   * The WebSocket
   */
//  def stream(email: String) = WebSocket.acceptWithActor[ClientEvent, ClientEvent] { _ => upstream =>
//    ClientConnection.props(email, upstream, Actors.regionManagerClient)
//  }
}