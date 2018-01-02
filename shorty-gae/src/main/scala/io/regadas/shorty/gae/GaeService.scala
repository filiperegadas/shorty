package io.regadas.shorty.gae

import cats.effect._
import io.regadas.shorty.core.RandomBase36
import io.regadas.shorty.gae.GaeDatastore
import io.regadas.shorty.service.ShortyHttpService
import fs2.StreamApp
import fs2.StreamApp.ExitCode
import org.http4s.server.blaze._

object GaeService extends StreamApp[IO] {
  val Host: String = Option(System.getenv("SHORTY_HOST")).getOrElse("0.0.0.0")
  val Port: Int =
    Option(System.getenv("SHORTY_PORT")).map(_.toInt).getOrElse(8080)

  override def stream(args: List[String],
                      requestShutdown: IO[Unit]): fs2.Stream[IO, ExitCode] =
    BlazeBuilder[IO]
      .bindHttp(Port, Host)
      .mountService(ShortyHttpService.service(new GaeDatastore, RandomBase36),
                    "/")
      .serve
}
