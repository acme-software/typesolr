package ch.acmesoftware.typesolr

import scalaz.zio.IO

package object zio {
  type ThrowableIO[T] = IO[Throwable, T]
}
