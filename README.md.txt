# Menit.com - Android News App

This is an Android news app that displays the latest news articles from around the web. It is written in Kotlin and follows the Clean Architecture principles. I made this application to complete my internship program at Schoter

## Features

- Display a list of the latest news articles
- View the full article and read the content
- Search article with filter
- Share articles with friends and colleagues
- Bookmark the article and display the list

## Screenshots

Include some screenshots of your app here to give users an idea of what it looks like.

## Getting Started

To get started with the app, follow these steps:

1. Clone the repository:

  ```git clone https://github.com/ferdifir/VIX-Schoters-Ferdi.git```

2. Obtain an API key from News API:

- Go to https://newsapi.org/
- Click on the "Get API KEY" button in the top right corner
- Follow the prompts to sign up for a free API key

3. Add your API key to the app:

- Open the project in Android Studio:
- File > Open > Select the android-news-app folder
- Add the following line to the `local.properties` file in the root of the project:

  apiKey="YOUR_API_KEY_HERE"
  
  Replace `YOUR_API_KEY_HERE` with your actual API key.

4. Build and run the app:

- Click the Run button in the top toolbar or use the Shift + F10 shortcut

## Dependencies

The app uses the following libraries and frameworks:

- [Kotlin](https://kotlinlang.org/) - The programming language used
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) - The architectural pattern used
- [News API](https://newsapi.org/) - The API used to retrieve news articles
- [Single Activity Architecture](https://developer.android.com/jetpack/docs/guide#recommended-app-structure) - The architecture pattern used for the app's activities
- [Room](https://developer.android.com/topic/libraries/architecture/room) - The database library used to persist data
- [Retrofit](https://square.github.io/retrofit/) - The HTTP client library used to make API requests
- [Glide](https://bumptech.github.io/glide/) - The image loading library used to display images in the app
- [Facebook Shimmer](https://developers.facebook.com/docs/android/reference/com/facebook/shimmer/ShimmerFrameLayout) - The loading animation library used to show a loading indicator

### Logo

logo designed by [Irham Andaiman](https://www.behance.net/irhamandaiman)