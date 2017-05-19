
package controllers

import javax.inject.Inject

import play.api.i18n.MessagesApi
import play.api.mvc.Controller
import sample.helloworld.api.HelloService
import sample.helloworldconsumer.api.HelloConsumerService

import scala.concurrent.ExecutionContext

abstract class AbstractController @Inject()(messagesApi: MessagesApi, helloService: HelloService, helloConsumerService: HelloConsumerService)
                                           (implicit ec: ExecutionContext) extends Controller {

}







