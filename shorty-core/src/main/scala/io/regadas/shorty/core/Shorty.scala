package io.regadas.shorty.core

import cats.effect._
import eu.timepit.refined.api.Refined
import io.regadas.shorty.core.refined._
import simulacrum._

import scala.language.implicitConversions
import scala.util.Random
import scala.util.hashing.MurmurHash3

final case class ShortyUrl(id: String, location: String Refined ValidUrl)

object ShortyUrl {
  val kind = "Url"
}

@typeclass trait UrlHashing[A] {
  def hash(value: String): A
}

object UrlHashing {
  implicit val RandomBase36: UrlHashing[String] = (_: String) =>
    Integer.toString(new Random().nextInt(Integer.MAX_VALUE), 36)

  implicit val MurmurHash: UrlHashing[String] = (value: String) =>
    Integer.toString(MurmurHash3.stringHash(value), 36)
}

trait Datastore {
  def get(id: String): IO[Option[ShortyUrl]]

  def put(su: ShortyUrl*): IO[Unit]
}
