
import spray.routing._
import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.handlers.DefaultBSONHandlers._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object Main extends App with SimpleRoutingApp {

  import system.dispatcher

  // Create a document with
  //
  // $ mongo rm
  // > db.foo.insert({name: "this", "value": "something"})

  val collection = MongoConnection(List("localhost:27017")).
    apply("rm").
    collection("foo")

  val query = BSONDocument("name" -> BSONString("this"))

  startServer("localhost", 9090) {
    path("static") {
      complete("static")
    } ~
    path("sync") {
      complete {
        Await.
          result(collection.find(query).toList, 1 second).headOption.
          flatMap(_.getAs[BSONString]("value")).map(_.value).
          getOrElse("-").toString
      }
    } ~
    path("async") {
      complete {
        for(result <- collection.find(query).toList)
          yield result.headOption.
            flatMap(_.getAs[BSONString]("value")).map(_.value).
            getOrElse("-").toString
      }
    }
  }

}
