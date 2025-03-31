# City Bike Map

Information about the available [HSL city bikes](https://www.hsl.fi/en/citybikes) and locations of the bike stations on map.

## History

This project was initially done as an interview project for a company that I was applying to (and I got in, yay!). Because it was an interview project there was a lot of useless features like a list of bikes in addition to the map and a lot of tests I would maybe not normally do. You can check the version I made for the interview [here](https://github.com/PGHM/CityBikeMap/releases/tag/1.0.0).

HSL has changed their API twice after the initial version. First time was not so bad, needed to just [update the url](https://github.com/PGHM/CityBikeMap/commit/f390c19fd3b098f9b64f0913c4ad4d76410c9769). April 2025 was different though as they changed from REST API to GraphQL so I actually needed to get the project building again and do some coding. Getting it to build with modern Android Studio took some time so I did more than just make it work with new API. I removed all the things I made just for interview to demonstrate my skills and left only the core that was relevant, the map. This was partly done because all the simple city bike maps had disappeared from app store and I thought maybe someone else would like to use it too, so I made a proper version for the store. You can check out that version [here](https://github.com/PGHM/CityBikeMap/releases/tag/2.0.0).

## Why Java and not Kotlin?

You can see that the project was done using Java, as 2016 it was still the main language and Kotlin was just released. It was interesting coding it again ten years later when I did the bigger changes to accomodate the new GraphQL API and noticing everything that has changed since. I remembered how painful it was back in the day, having to put semicolons everywhere and having to write out every type and not having nullable types.

Chose to not convert the project to Kotlin even now as there is really little code left. It is a nice artifact from the past.

## Usage

It is pretty straight forward app, just a map with bikes of three different colors. Red means no bikes, yellow 1-4 bikes and green 5+ bikes. You can see the exact amount when clicking the bike icon. That's it, nothing else needed. The information updates every 10 seconds while in foreground.
