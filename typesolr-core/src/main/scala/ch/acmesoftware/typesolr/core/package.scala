package ch.acmesoftware.typesolr

import ch.acmesoftware.typesolr.core.FieldDecoder.{EmptyValue, TypeMismatch}

import cats.data._
import cats.data.Validated._
import cats.implicits._

package object core {

  implicit def fieldToDocument[T](field: Field[T])(implicit fieldEncoder: FieldEncoder[T]): Document =
    Document(Map(fieldEncoder.encode(field)))

  implicit def tupleToField[T](t: (String, T)): Field[T] = Field(t._1, t._2)

  // field encoders

  private def toStringFieldEncoder[T]: FieldEncoder[T] = f => f.key -> List(f.value.toString)

  implicit val emptyFieldEncoder: FieldEncoder[Unit] = _.key -> Nil
  implicit val stringFieldEncoder: FieldEncoder[String] = toStringFieldEncoder[String]
  implicit val intFieldEncoder: FieldEncoder[Int] = toStringFieldEncoder[Int]
  implicit val longFieldEncoder: FieldEncoder[Long] = toStringFieldEncoder[Long]
  implicit val floatFieldEncoder: FieldEncoder[Float] = toStringFieldEncoder[Float]
  implicit val doubleFieldEncoder: FieldEncoder[Double] = toStringFieldEncoder[Double]
  implicit val booleanFieldEncoder: FieldEncoder[Boolean] = toStringFieldEncoder[Boolean]
  implicit val bigDecimalFieldEncoder: FieldEncoder[BigDecimal] = toStringFieldEncoder[BigDecimal]

  implicit def optionFieldEncoder[T](implicit enc: FieldEncoder[T]): FieldEncoder[Option[T]] =
    f => f.key -> f.value.toList.flatMap(valueEncode(_, enc))

  implicit def listFieldEncoder[T](implicit enc: FieldEncoder[T]): FieldEncoder[List[T]] =
    f => f.key -> f.value.flatMap(valueEncode(_, enc))


  def valueEncode[T](v: T, enc: FieldEncoder[T]): List[String] = enc.encode(Field("", v))._2

  // field decoders

  private def fromStringFieldDecoder[T](f: String => T): FieldDecoder[T] = (k, v) => v.headOption.map{strV =>
    try{
      Field(k, f(strV)).validNel
    } catch {
      case e: Throwable => TypeMismatch(k).invalidNel
    }
  }.getOrElse(EmptyValue(k).invalidNel)

  implicit val stringFieldDecoder: FieldDecoder[String] = fromStringFieldDecoder(_.toString)
  implicit val intFieldDecoder: FieldDecoder[Int] = fromStringFieldDecoder(_.toInt)
  implicit val longFieldDecoder: FieldDecoder[Long] = fromStringFieldDecoder(_.toLong)
  implicit val floatFieldDecoder: FieldDecoder[Float] = fromStringFieldDecoder(_.toFloat)
  implicit val doubleFieldDecoder: FieldDecoder[Double] = fromStringFieldDecoder(_.toDouble)
  implicit val booleanFieldDecoder: FieldDecoder[Boolean] = fromStringFieldDecoder(_.toBoolean)
  implicit val bigDecimalFieldDecoder: FieldDecoder[BigDecimal] = fromStringFieldDecoder(BigDecimal(_))

  //implicit def optionFieldDecoder[T](implicit dec: FieldDecoder[T]): FieldDecoder[Option[T]] = (k, v) => dec.decode(k, v) match {
  //  case Left(e) => Left[](Some(e))
  //}



}
