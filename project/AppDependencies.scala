import sbt._

object AppDependencies {

  private val bootstrapVersion = "7.22.0"
  private val internalAuthVersion = "1.6.0"

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-28"    % bootstrapVersion,
    "uk.gov.hmrc"             %% "internal-auth-client-play-28" % internalAuthVersion
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"     % bootstrapVersion            % "test, it",
    "org.mockito"             %% "mockito-scala"              % "1.17.14"                   % "test, it"
  )

}
