package controllers

import javax.inject.Inject

import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import sample.helloworld.api.HelloService
import sample.helloworldconsumer.api.HelloConsumerService

import scala.concurrent.{ExecutionContext, Future}

class Main @Inject()(val messagesApi: MessagesApi, helloService: HelloService, helloConsumerService: HelloConsumerService)(implicit ec: ExecutionContext)
  extends AbstractController(messagesApi, helloService,helloConsumerService) with I18nSupport {

  val helloUserForm = Form(mapping(
    "name" -> text.verifying(value => value.length > 0)
  )(HelloUserForm.apply)(HelloUserForm.unapply))

  val helloForm = Form(mapping(
    "name" -> nonEmptyText,
    "message" -> nonEmptyText
  )(HelloForm.apply)(HelloForm.unapply))

  def index: Action[AnyContent] = Action.async { implicit request =>
    Future(Ok(views.html.index(helloUserForm)))
  }

  def show: Action[AnyContent] = Action.async { implicit request =>
    Future(Ok(views.html.show(helloForm)))
  }
}

case class HelloUserForm(name: String)

case class HelloForm(name: String, message: String)