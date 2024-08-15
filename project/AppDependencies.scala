import sbt._

object AppDependencies {

  private val bootstrapVersion = "9.0.0"
  private val internalAuthVersion = "3.0.0"

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-30"    % bootstrapVersion,
    "uk.gov.hmrc"             %% "internal-auth-client-play-30" % internalAuthVersion
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"     % bootstrapVersion            % Test,
  )

  val it = Seq.empty

}
