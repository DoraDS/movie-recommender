
object UserInputs {

  // change the startYear and endYear to control the range of included movie release years - please note, if you choose a large range of years, you might exceed the API limits
  val startYear: Int = 2000
  val endYear: Int = 2010

  // set the api key and api url in your account environment variables once you have subscribed to the API (see README.md - API details)
  val apiKey: String = sys.env.getOrElse("API_KEY", throw new Exception("API_KEY is not set"))
  val apiUrl: String = sys.env.getOrElse("API_URL", throw new Exception("API_URL is not set"))

  // set the desired main path of your project in your account environment variables, this should point to your local project repository
  val inputPath: String = sys.env.getOrElse("MOCK_PROJECT_INPUT_PATH", throw new Exception("MOCK_PROJECT_INPUT_PATH is not set"))



}
