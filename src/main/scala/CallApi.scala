import UserInputs.inputPath
import sttp.client3.{HttpURLConnectionBackend, basicRequest, _}

import java.io.PrintWriter
object CallApi {

  // See README.md for API subscription details.
  // After subscribing to the API, leverage this function by providing ths API key, API URL link to call the API.
  // Parameters: apiKey (API key), uriLink (API URL link), content (describes the API output, leveraged for the file name - see comments in Main.scala for example), folder (folder where the API output will be located)
  def callApi(apiKey: String, uriLink: String, content: String, folder: String): Unit = {
    val request = basicRequest.header("x-rapidapi-key", apiKey)
      .get(uri"$uriLink")

    val backend = HttpURLConnectionBackend()
    val response = request.send(backend)

    val folderPath = s"$inputPath/target/$folder" // Replace with your custom folder path
    val filePath = s"$folderPath/output_$content.json"

    val pw = new PrintWriter(filePath)
    println(response.body.toString)
    val result = response.body.toString.stripPrefix("Right(").stripSuffix(")")
    pw.write(result)
    pw.close()
  }


}
