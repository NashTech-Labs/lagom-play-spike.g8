package controllers

import javax.inject.Inject

import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import sample.helloworld.api.HelloService
import sample.helloworld.api.model.GreetingMessage
import sample.helloworldconsumer.api.HelloConsumerService

import scala.concurrent.{ExecutionContext, Future}


class HelloController @Inject()(val messagesApi: MessagesApi, helloService: HelloService, helloConsumerService: HelloConsumerService)
                               (implicit ec: ExecutionContext) extends AbstractController(messagesApi, helloService, helloConsumerService) with I18nSupport {


  val helloUserForm = Form(mapping(
    "name" -> nonEmptyText
  )(HelloUserForm.apply)(HelloUserForm.unapply))

  val helloForm = Form(mapping(
    "name" -> nonEmptyText,
    "message" -> nonEmptyText
  )(HelloForm.apply)(HelloForm.unapply))

  def sayHello(): Action[AnyContent] = Action.async { implicit request =>
    helloUserForm.bindFromRequest.fold(
      badForm => Future {
        BadRequest(views.html.index(badForm))
      },
      validForm => {
        for {
          result <- helloService.hello(validForm.name).invoke()
        }
          yield {
            Ok(result)
          }
      }
    )
  }

  def changeMessage(): Action[AnyContent] = Action.async { implicit request =>
    helloForm.bindFromRequest.fold(
      badForm => Future {
        BadRequest(views.html.show(badForm))
      },
      validForm => {
        for {
          result <- helloService.useGreeting(validForm.name).invoke(GreetingMessage(validForm.message))
        }
          yield {
            Ok("Message successfully changed !!")
          }
      }
    )
  }

  def wordCount(): Action[AnyContent] = Action.async { implicit request =>
    helloConsumerService.findTopHundredWordCounts().invoke().map {
      (result: Map[String, Int]) => Ok(result.toString())
    }
  }

  def latestMessage(): Action[AnyContent] = Action.async { implicit request =>
    helloConsumerService.latestMessage().invoke().map {
      result => Ok(result)
    }

  }
}



