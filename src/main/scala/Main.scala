import CallApi.callApi
import InitializeSpark.initializeSparkSession
import UserInputs._
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

object Main {

  def main(args: Array[String]) {

    // initialize spark session
    val spark: SparkSession = initializeSparkSession(appName = "Spark test", isLocal = true)
    import spark.implicits._

    //------------------------------------------------------- Movie Titles Dataframe (Dataframe 1) --------------------------------------------------------//

    // change the startYear and endYear in the UserInputs.scala to control the range of included movie titles based on release years
    val years: IndexedSeq[String] = (startYear to endYear).map(_.toString)
    years.foreach( {
      Thread.sleep(1001)
      callApi(apiKey, s"$apiUrl", _, "years_data") // create a json of movie titles that span the previously defined release years
    })
    val movieData = spark.read.json(s"$inputPath/target/years_data") // create a dataframe of movie titles that span the previously defined release years

    // Extract useful information from the dataframe of movie titles that span the previously defined release years
    val explodeMovieData = movieData.withColumn("explode_results", explode(col("results")))
    val selectMovieData = explodeMovieData.select(col("explode_results.*"))
    // create the final movie titles dataframe (select relevant columns and rename them)
    val finalMovieDf = selectMovieData.select(col("id"), col("originalTitleText.text").as("title"), col("titleText.text"), col("primaryImage.url").as("image_url"), col("releaseDate.year").as("release_year"), col("titleType.isEpisode"), col("titleType.isSeries"), col("titleType.text").as("titleType.text"))


    //------------------------------------------------------- Movie Ratings Dataframe (Dataframe 2) --------------------------------------------------------//

    // get ratings for all the collected movie titles from Dataframe 1
    finalMovieDf.select("id").rdd.foreach { row =>
      val ids = row.getString(0)
      println(s"ids: $ids")
      Thread.sleep(1001)
      // create ratings data json files based on the movie titles and ids obtained from Dataframe 1
      callApi(apiKey = apiKey, uriLink = s"$apiUrl/$ids/ratings", content = ids, "ratings_data")
    }
    // create a ratings dataframe based on the ratings data json files
    val ratingsData: DataFrame = spark.read.json(s"$inputPath/target/ratings_data/")

    // create the final movie ratings dataframe (select relevant columns and rename them)
    val ratingsDataCleaned = ratingsData.select(col("results.averageRating").as("averageRating"), col("results.numVotes").as("numVotes"), col("results.tconst").as("tconst"))


    //------------------------------------------------------- Join the two dataframes --------------------------------------------------------//

    // rename the id column (tconst) in the ratings dataframe to match the id column in the movies dataframe (id)
    val ratingsId = ratingsDataCleaned.withColumnRenamed("tconst", "id")

    // join the movie title and ratings dataframe
    val joinedDf = finalMovieDf.join(ratingsId, "id", "left")
    // rearrange columns for more relevance
    val rearrangeColumns = joinedDf.select(col("id"), col("title"), col("release_year"), col("averageRating"), col("numVotes"), col("image_url"), col("isEpisode"), col("isSeries"), $"`titleType.text`".as("classification"))

    // quick preview
    rearrangeColumns.show(truncate=false)
    println(s"count is ${joinedDf.count()}")
    println(s"count after dropping duplicates is ${joinedDf.dropDuplicates().count()}")

    // write the joined dataframes
    rearrangeColumns.write.mode("append").csv(s"$inputPath/target/output")


    spark.stop()

  }

}
