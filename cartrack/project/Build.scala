import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "cartrack"
    val appVersion      = "1.1-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
    	"mysql" % "mysql-connector-java" % "5.1.19"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here      
    )

}
