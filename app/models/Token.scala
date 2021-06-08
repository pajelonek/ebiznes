package models

import java.util.UUID

import play.api.libs.json.{Json, OFormat}

/**
 * A token to authenticate a user against an endpoint for a short time period.
 *
 * @param id     The unique token ID.
 * @param userID The unique ID of the user the token is associated with.
 * @param expiry The date-time the token expires.
 */
case class Token(
                      id: UUID,
                      userID: String,
                      expiry: Long
                    )

object Token {
  implicit val fmt: OFormat[Token] = Json.format[Token]
}
