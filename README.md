# RESTAPITesting
Get HTTP Request to retrieve movie titles, you are requested to write am HTTP GET method to retrieve information from a movie database.
The query response from the website is JSON response with the following fields:

page: The current page

per_page: The maximum number of results per page.

total: The total number of movies in the search result

total_pages: The total number of pages which must be queried to get all the results.

Data: An array of JSON objects containing movie information where the Title field denotes the title of the movie. In order to get all results, you may have to make 
multiple page requests. To request a page by number, your query should read https://jsonmock.hackerrank.com/api/movies/search/?Title=substr&page=pageNumber, 
replacing substr and pageNumber
