# Movie titles and ratings dataframe
This project uses an API to collect data about:
1. Movie titles released between a select number of years (e.g., movies released between 2010 to 2020).
2. Movie ratings data associated with the movie titles collected in step 1.

Then the API data is processed into a cleaned joined dataframe of movie titles and movie ratings by leveraging Scala and Spark.
## API details
You can find the API details here: https://rapidapi.com/SAdrian/api/moviesdatabase . The API was chosen because it has a free basic plan and contains various interesting data regarding properties of each movie that can be leveraged as the project matures.\
I used the basic plan of this API which is free, but comes with the following usage restrictions: 
1. 1000 API calls per hour. 
2. 50 000 API calls per month.

When you subscribe to this API, you will have access to the API key and API url. To leverage the API key and API url to run this project, you will need to add both in your account environment variables as API_KEY and API_URL, respectively (see **How to run this project section** of the README.md).
## Main project components
1. **Main.scala** - creates the movie ratings dataframe by processing the output of the `callApi` function from the CallApi.scala file. The finalized dataframe is written to a local folder - which local folder exactly depends on specifying the immutable variable `inputPath` in the UserInputs.scala (see **How to run this project section** of the README.md).
2. **CallApi.scala** - contains the `callApi` function that processes the api output into a json.
3. **InitializeSpark.scala** - contains the `createSparkSession` function that sets up a spark session for local use.
4. **UserInputs.scala** - contains immutable variables that allow interacting with the Main.scala file without altering the Main.scala logic. The immutable variables are `startYear`, `endYear`, `apiKey`, `apiUrl`, and `inputPath`.
## How to run this project
1. Please review the comments in **UserInputs.scala** to understand how to modify the immutable variables to interact with the Main.scala file in the desired way.
2. Run the main function in Main.scala **after you have adjusted the UserInputs.scala and created the required account system variables.** Read the comments in the Main.scala folder if you wish to understand each step of the Main.scala logic that generates the movie ratings dataframe.

**Note**: Based on how you define the "MOCK_PROJECT_INPUT_PATH" environment variable for your account, data will be added into the following folders:
1. MOCK_PROJECT_INPUT_PATH/target/output - stores the finalized movie dataframe
2. MOCK_PROJECT_INPUT_PATH/target/years_data - stores movie titles for the span of years specified as `startYear` and `endYear` in UserInputs.scala
3. MOCK_PROJECT_INPUT_PATH/target/ratings_data - stores ratings data associated with the movie titles collected in the "MOCK_PROJECT_INPUT_PATH/target/years_data" folder

This is because the `inputPath` immutable variable from the UserInputs.scala file is created based on the "MOCK_PROJECT_INPUT_PATH" environment variable.

## Example mock output
| id| title     |release_year|averageRating|numVotes|image_url| isEpisode | isSeries |classification|
|---|-----------|------------|-------------|--------|---------|-----------|----------|--------------|
|id1|Movie Title 1|2010|5.0|100| https link | false     | false    |Short|
|id2|Movie Title 2|2020|4.5|200| https link | false     | false    |Long|
|id3|Movie Title 3|2000|4.0|50| https link | false     | true     |Tv Series|
|id4|Movie Title 4|1990|3.5|25| NULL    | true      | false    |Tv Episode|
|id5|Movie Title 4|2005|3.0|180| https link | false     | false    |NULL|