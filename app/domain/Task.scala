package domain

import java.time.Instant
import java.util.UUID
import play.api.libs.json.*

// ── Opaque type — wraps UUID with domain identity, zero boxing cost ───────────
opaque type TaskId = UUID

object TaskId:
  def apply(uuid: UUID): TaskId = uuid
  def generate(): TaskId        = UUID.randomUUID()

  def fromString(s: String): Either[String, TaskId] =
    try Right(UUID.fromString(s))
    catch case _: IllegalArgumentException => Left(s"Invalid TaskId: '$s'")

  given Format[TaskId] = Format(
    Reads(_.validate[String].flatMap { s =>
      fromString(s).fold(
        err => JsError(err),
        id  => JsSuccess(id)
      )
    }),
    Writes(id => JsString(id.toString))
  )

// ── Aggregate ─────────────────────────────────────────────────────────────────
case class Task(
  id:        TaskId,
  title:     String,
  createdAt: Instant
)

object Task:
  given Format[Instant] = Format(
    Reads(_.validate[String].map(Instant.parse)),
    Writes(i => JsString(i.toString))
  )
  given OFormat[Task] = Json.format[Task]
