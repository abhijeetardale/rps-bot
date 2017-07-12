import controllers.{StartController, MoveController}
import play.api.ApplicationLoader.Context
import play.api.{BuiltInComponentsFromContext, BuiltInComponents}
import play.api.cache.EhCacheComponents
import play.api.routing.Router
import services.{CachingService}
import router.Routes

class ApplicationLoader extends play.api.ApplicationLoader {
  def load(context: Context) = {
    (new BuiltInComponentsFromContext(context) with ApplicationModule).application
  }
}

trait ApplicationModule extends BuiltInComponents with EhCacheComponents {

  lazy val cacheService: CachingService = new CachingService(defaultCacheApi)
  lazy val moveController = new MoveController(cacheService)
  lazy val startController = new StartController(cacheService)
  lazy val router: Router = new Routes(httpErrorHandler, moveController, startController)
}