package services

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import domain.{TaskError, TaskId}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import repositories.InMemoryTaskRepository

class TaskServiceSpec extends AsyncWordSpec with AsyncIOSpec with Matchers:

  private def makeService: IO[TaskService] =
    InMemoryTaskRepository.make().map(TaskService(_))

  "TaskService.listAll" should {
    "return an empty list on a fresh repository" in {
      makeService.flatMap(_.listAll()).asserting(_ shouldBe empty)
    }
  }

  "TaskService.create" should {
    "create a task and return it with a generated ID" in {
      makeService.flatMap { svc =>
        svc.create("Buy milk").asserting {
          case Right(task) =>
            task.title shouldBe "Buy milk"
          case Left(err)   =>
            fail(s"Expected Right but got Left($err)")
        }
      }
    }

    "reject a blank title" in {
      makeService.flatMap(_.create("   ")).asserting {
        _ shouldBe Left(TaskError.ValidationError("title must not be blank"))
      }
    }

    "trim leading and trailing whitespace from the title" in {
      makeService.flatMap { svc =>
        svc.create("  Walk the dog  ").asserting {
          case Right(task) => task.title shouldBe "Walk the dog"
          case Left(err)   => fail(s"Unexpected error: $err")
        }
      }
    }
  }

  "TaskService.findById" should {
    "return NotFound for an unknown ID" in {
      val unknownId = TaskId.generate()
      makeService.flatMap(_.findById(unknownId)).asserting {
        _ shouldBe Left(TaskError.NotFound(unknownId))
      }
    }

    "return the task after it has been created" in {
      makeService.flatMap { svc =>
        for
          result <- svc.create("Learn Scala 3")
          task    = result.toOption.get
          found  <- svc.findById(task.id)
        yield found shouldBe Right(task)
      }
    }
  }

  "TaskService.listAll after creates" should {
    "return all created tasks" in {
      makeService.flatMap { svc =>
        for
          _     <- svc.create("Task A")
          _     <- svc.create("Task B")
          tasks <- svc.listAll()
        yield tasks should have size 2
      }
    }
  }
