Browse all the Spider-man comics
================================
This project uses the [Marvel API](http://developer.marvel.com/documentation/getting_started) to fetch all the comics where Spider-man appears.
It uses a constant size for the comic thumbnails and an autofit grid view to show them. 
The grid will adapt the number of columns according to the screen size. Screen orientation change is supported, the adapter will remember the previous position.

Retrofit2 with the Rx adapter is used to make the API calls. For every call, data of 47 comics is requested.
A scroller is attached to the grid view so that it will trigger a new request when there's less than 10 items after the current scroll position.

Set up
------

Clone the project and set a real API key in [config.xml](../master/app/src/main/res/values/config.xml). You can get one [here](http://developer.marvel.com/documentation/getting_started). 


**IMPORTANT NOTE:** The app will crash if a key is not set.

Screenshots
-----------
![Phone portrait](/../master/screenshots/phone_portrait.png?raw=true "Phone portrait")
![Phone detail](/../master/screenshots/phone_detail.png?raw=true "Phone detail")
![Phone landscape](/../master/screenshots/phone_landscape.png?raw=true "Phone landscape")
![Tablet portrait](/../master/screenshots/tablet_portrait.png?raw=true "Tablet portrait")
![Tablet landscape](/../master/screenshots/tablet_lansdcape.png?raw=true "Tablet landscape")

Limitations
-----------
The app is not network aware. It will blindly try to get data from the internet and stop after 3 consecutive failures.


CardViews don't look good in SDKs below 21.
