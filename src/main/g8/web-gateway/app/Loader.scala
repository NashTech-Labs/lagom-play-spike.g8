
import javax.inject.Inject

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.api.{ServiceAcl, ServiceInfo}
import com.lightbend.lagom.scaladsl.client.LagomServiceClientComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.softwaremill.macwire._
import controllers.{Assets, HelloController, Main}
import play.api.ApplicationLoader.Context
import play.api.i18n.I18nComponents
import play.api.libs.ws.ahc.AhcWSComponents
import play.api._
import sample.helloworld.api.HelloService
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import sample.helloworldconsumer.api.HelloConsumerService
import router.Routes

import scala.collection.immutable
import scala.concurrent.ExecutionContext

abstract class WebGateway @Inject()(context: Context) extends BuiltInComponentsFromContext(context)
  with I18nComponents
  with AhcWSComponents
  with LagomServiceClientComponents {

  override lazy val serviceInfo: ServiceInfo = ServiceInfo(
    "web-gateway",
    Map(
      "web-gateway" -> immutable.Seq(ServiceAcl.forPathRegex("(?!/api/).*"))
    )
  )
  override implicit lazy val executionContext: ExecutionContext = actorSystem.dispatcher
  override lazy val router = {
    val prefix = "/"
    wire[Routes]
  }


  lazy val helloService = serviceClient.implement[HelloService]
  lazy val helloConsumerService = serviceClient.implement[HelloConsumerService]
  lazy val main = wire[Main]
  lazy val helloController = wire[HelloController]
  lazy val assets = wire[Assets]
}

class WebGatewayLoader extends ApplicationLoader {
  override def load(context: Context): Application = context.environment.mode match {
    case Mode.Dev =>
      new WebGateway(context) with LagomDevModeComponents {}.application
    case _ =>
      new WebGateway(context) {
        override def serviceLocator = NoServiceLocator
      }.application
  }
}
