package dutytimetable.core

import java.io.OutputStreamWriter
import java.net.{HttpURLConnection, URL}

//import com.google.api.client.googleapis.auth.oauth2.GoogleCredential

//import org.apache.oltu.oauth2.client.request.OAuthClientRequest
//import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse
//import org.apache.oltu.oauth2.common.OAuthProviderType


/**
 * Created by beenotung on 5/28/15.
 */
object O2 {
  val appname = "O2 test"
  val clientId = "121880314592-03qc0o2iks9o193q93jhdbf0g89ic51d.apps.googleusercontent.com"
  val apiKey="AIzaSyDW5xXzYY05pyf6e5pMUBmcv_MNLuAzwOY"
  //  val request = OAuthClientRequest
  //    .authorizationProvider(OAuthProviderType.GOOGLE)
  //    .setClientId(clientId)
  //    .setRedirectURI("http://aabbcc1241.freeiz.com/HKCC/SSC/")
  //    .buildQueryMessage()
  //val oar=OAuthAuthzResponse.oauthCodeAuthzResponse(request.getLocationUri)

  //val service=new SpreadsheetService(appname)
  //service.setUserToken()

 // new GoogleCredential().
  //val credential = new GoogleCredential().setAccessToken(accessToken)

  //google get access token 's url
  val urlObtainToken=new URL("https://accounts.google.com/o/oauth2/token")
  val connectionObtainToken=urlObtainToken.openConnection().asInstanceOf[HttpURLConnection]

  //set connection to use POST
  connectionObtainToken.setRequestMethod("POST")
  connectionObtainToken.setDoOutput(true)

  //start transmit parameter
  val writer=new OutputStreamWriter(connectionObtainToken.getOutputStream)
  //writer.write("code="+req)

}

object O2Test extends App {
  override def main(args: Array[String]) {
    O2
  }
}