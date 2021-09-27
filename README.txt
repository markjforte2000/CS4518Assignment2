1 Working with Intent: data and control sequence diagram
2 Working with Intent: intent implementation and Logcat output
    Part 1 Source directory contains the code for a working app that passes data to a second intent and then
    returns a result from the intent. The writeup includes screenshots from this app as well as log output showing
    data being passed

3 Working with Fragment: dynamic fragment embedding
    Part 2 Source directory contains code for the app that uses an fragment embeded in the main activity

4 Working with RecyclerViewer: ViewModel implementation
    Part 3 Source directory has a class GameList that is a View Model which randomly generates 100 basketball
    games on init

5 Working with RecyclerViewer: RecyclerViewer Fragment implementation
    Part 3 Source directory GameListFragment is a fragment that is embedded in the activity MainActivity
    on app init

6 Working with RecyclerViewer: child view of the RecyclerViewer implementation
    Each RecyclerView list iten is a view defined in fragment_list_item_game.xml in the res/layout directory
    This view is inflated in the GameAdapter inner class of GameListFragment

7 Working with RecyclerViewer: RecyclerViewer Adapter implementation
    GameListFragment contains a inner class GameAdapter which is used to bind BasketBallGame to a view holder

8 Working with RecyclerViewer: adding the RecyclerViewer to the Main activity
    The recycler view fragment is attached to the main activity in MainActivity on application init

9 Working with ConstraintLayout: required screenshots
    Screenshots are shown in pdf

10 Data Persistence with Room: use database to populate the list game fragment
    Screenshot in pdf shows game list being populated by given database. Code for this is in GameList class
    as well as the database helper classes BasketballDatabase, BasketballRepository, BasketballDAO and
    BasketballConverter

11 Data Persistence with Room: write to database
    150 generated entries are inserted into the database as shown in the pdf writeup. This is handled in the
    MainFragment class in the saveGame method

12 Supporting Fragment Navigation: refactor to enable fragment navigation
    Screenshots of fragment navigation are shown in the pdf. This is able to be tested using the submitted
    application. This is implemented in both the GameListFragment class and the MainFragment class

13 Supporting Fragment Navigation: implementation of fragment data passing
    Data passing is demonstrated in the logs shown in the pdf as well as in the debug application.
    This is implemented in the MainFragment and GameListFragment classes
