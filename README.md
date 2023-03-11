# Standard Readme

This is an early stage Android app that allows people to buy and sell time from a group of trusted people called a Scoop (or Service Co-Op). A Scoop Manager creates a Scoop (like a Dog Walking Scoop, a Personal Driver Scoop, a Babysitting Scoop) and adds Buyers (people who want to buy time) and Sellers (people who want to sell time). Buyers can post Jobs, Sellers can Accept->Start->Complete Jobs, and Buyers can Pay for Completed Jobs. 

The App uses Firestore and Firebase Authentication (including Google Authentication). 

The app will work with a couple of quick modifications: 
1. Create a Firebase Project and add a Firestore Database with these rules: https://blog.dyor.com/rules-for-firestore
2. for Authentication (including a SHA1 fingerprint) https://blog.dyor.com/authenticating-with-google-sign-in-and-firebase-auth-and-firebase-ui-on-kotlin-for-android
3. Connect that Firebase Project to your local Android App by coping your google-services.json from Firebase into the App directory. 

Over the coming weeks, I am going to turn this from a flaming dumpster fire of an app to a reasonably high quality app. 

* Properly implementing an MVVM architecture
* Getting the job workflow working (e.g., so that once a job has been accepted, only the seller can start it, only once a seller has started a job can it be completed, only once a seller has completed a job can the buyer pay for the job). 
* Making the addition of new users (that have not yet signed in) possible - and generally improving the buyer/seller management. 
* Making the UI non-terrible. 
* Implementing payments. 
* Setting up the basic analytics. 
* Creating update and delete pages (currently there are just create, list, and detail pages) 
* Implementing notifications when a new job is added. 
* Porting over to iOS and Web using Kotlin Multi Platform. 
* Creating a home page that shows important data (e.g., jobs that have been posted in a scoop that the user is listed as a seller in, upcoming jobs the seller needs to start, jobs that have been completed and the buyer needs to pay for). 
* Getting all of the needed forms of authentication working (currently just Google works)
* Getting Issues set up to track and prioritize the backlog. 

If you know Android Architecture and/or Kotlin and would like to help out, please email matt at dyor dot com and get involved. If you have some ideas on design or functioality, I would love to see/read them. I am hoping to build out this basic pattern for buying and selling time between trusted parties, and then apply it to some specific areas that are interesting to me. 
