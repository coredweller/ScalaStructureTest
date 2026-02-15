package loader

import cats.effect.unsafe.implicits.global   // gives us IORuntime.global as a given
import controllers.{HealthController, TaskController}
import play.api.ApplicationLoader.Context
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext}
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import play.filters.HttpFiltersComponents
import repositories.InMemoryTaskRepository
import router.Routes
import services.TaskService

import scala.concurrent.ExecutionContext

class AppLoader extends ApplicationLoader:
  def load(context: Context): Application =
    new AppComponents(context).application

class AppComponents(context: Context)
    extends BuiltInComponentsFromContext(context)
    with HttpFiltersComponents:

  given ExecutionContext = executionContext

  // ── Wire the application ─────────────────────────────────────────────────
  // `unsafeRunSync()` is acceptable once at startup to bootstrap the in-memory store.
  // For a real DB, use Resource[IO, _].allocated and store the finaliser.
  private val repo: InMemoryTaskRepository =
    InMemoryTaskRepository.make().unsafeRunSync()

  private val taskService     = TaskService(repo)
  private val taskController  = TaskController(taskService, controllerComponents)
  private val healthController = HealthController(controllerComponents)

  // Play's route compiler generates router.Routes from conf/routes
  override def router: Router =
    new Routes(httpErrorHandler, healthController, taskController)

  override def httpFilters: Seq[EssentialFilter] = super.httpFilters
