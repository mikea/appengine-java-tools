package com.mikea.gae.objectify

import com.googlecode.objectify.ObjectifyService._
import com.googlecode.objectify.{Work, Key}
import scala.reflect.runtime.universe._
import com.mikea.util.TypeTags
import com.mikea.util.TypeNeq.=!=

/**
 * @author mike.aizatsky@gmail.com
 */
object Ofy {
  def loadAll[T](implicit tag: TypeTag[T], d : T =!= Nothing) : (AnyRef) => List[T] = (s) => {
    val clazz: Class[T] = TypeTags.getClazz[T]

    import scala.collection.JavaConverters._
    ofy.load.`type`(clazz).list.asScala.toList
  }

  def loadNow[T] (id: String)(implicit tag: TypeTag[T], d : T =!= Nothing) : Option[T] = {
    val clazz: Class[T] = TypeTags.getClazz[T]
    val t : T = ofy.load.`type`(clazz).id(id).now
    if (t == null) {
      None
    } else {
      Some(t)
    }
  }

  def loadSafe[T] (id: String)(implicit tag: TypeTag[T], d : T =!= Nothing) : T = {
    val clazz: Class[T] = TypeTags.getClazz[T]
    ofy.load.`type`(clazz).id(id).safe()
  }

  def createKey[T](name: String)(implicit tag: TypeTag[T], d : T =!= Nothing): Key[T] = Key.create(TypeTags.getClazz[T], name)

  def transactNew[T](tx: => T) : T = {
    ofy.transactNew(new Work[T] {
      def run() = tx
    })
  }
}
