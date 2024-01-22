/*
 * Copyright 2024 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.hipapiexampleclient.controllers

import org.apache.pekko.util.CompactByteString
import com.google.inject.Inject
import play.api.http.{ContentTypes, HttpEntity}
import play.api.mvc._
import uk.gov.hmrc.hipapiexampleclient.controllers.actions.IdentifierAction
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HttpResponse, StringContextOps}
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import java.util.Base64
import scala.concurrent.ExecutionContext

class ExampleRequestController @Inject() (
  cc: ControllerComponents,
  httpClient: HttpClientV2,
  servicesConfig: ServicesConfig,
  identify: IdentifierAction
)(implicit ec: ExecutionContext) extends BackendController(cc) {

  def exampleRequest: Action[AnyContent] = identify.async {
    implicit request =>
      httpClient.get(url"${buildUrl()}")
        .setHeader(
          (ACCEPT, ContentTypes.JSON),
          (AUTHORIZATION, authorization())
        )
        .execute[HttpResponse]
        .map(
          response =>
            Result(
              ResponseHeader(
                status = response.status
              ),
              body = buildBody(response.body, response.headers)
            )
        )
  }

  private def authorization(): String = {
    val clientId = servicesConfig.getConfString(s"example-api.clientId", "")
    val secret = servicesConfig.getConfString(s"example-api.secret", "")

    val encoded = Base64.getEncoder.encodeToString(s"$clientId:$secret".getBytes("UTF-8"))
    s"Basic $encoded"
  }

  private def buildUrl(): String = {
    val baseUrl = servicesConfig.baseUrl("example-api")
    val path = servicesConfig.getConfString("example-api.path", "")

    if (path.isEmpty) {
      baseUrl
    }
    else {
      s"$baseUrl/$path"
    }
  }

  private def buildBody(body: String, headers: Map[String, Seq[String]]): HttpEntity = {
    if (body.isEmpty) {
      HttpEntity.NoEntity
    }
    else {
      HttpEntity.Strict(CompactByteString(body), buildContentType(headers))
    }
  }

  private def buildContentType(headers: Map[String, Seq[String]]): Option[String] = {
    headers
      .find(_._1.equalsIgnoreCase(CONTENT_TYPE))
      .map(_._2.head)
  }

}
