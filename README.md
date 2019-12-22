# Wiki Game
The point of the game is to find the target article by using hyperlinks from other articles. Every article is fetched from Wikipedia.

## Architecture
The app uses an MVI-like architecture inspired by 
- Kaushik Gopal's talk at Mobilization IX and the accompanying App [movies-usf-android](https://github.com/kaushikgopal/movies-usf-android)
- [Roxie](https://github.com/ww-tech/roxie)
- Jake Wharton's talk [The State of Managing State with RxJava](https://jakewharton.com/the-state-of-managing-state-with-rxjava/)

The data flow inside the App is unidirectional, there is only one entry point (**process** function), and two exit points (**viewState** and **viewEffects**).

### State
Every screen that will have its own ViewModel should define the four base state classes that will be used on that screen.

- **BaseAction** - actions that are send usually from the UI e.g. initialization, user clicks. They define an intent (not the android one) that something in the UI should change. These action can contain data e.g. EditText input.
- **BaseResult** - they are the "result" of transforming an action. For example, a user types something into a search bar and clicks search. A SearchAction is sent into the ViewModel, this starts a search API request, when it fulfills a list is returned. This list is then wrapped with a Result class and sent downstream.
- **BaseViewState** - specifies the whole state of the screen. To change something in the UI the state has to change. This state and everything inside is immutable, this means that every time something changes a new object is created. The Kotlin data class **copy** functions shines here.
- **BaseViewEffect** - one time "effects" that happen in the App. For example, a Toast/Snackbar message or in-app navigation.

### ViewModel

All of the UI data flow logic is orchestrated inside the abstract BaseViewModel class using RxJava2. Just like [Redux](https://redux.js.org/) every state change is preceded with an action. The BaseViewModel provides a public **process** function that takes in an Action. That Action is then saved inside a [BehaviorRelay](https://github.com/JakeWharton/RxRelay) and transformed into a Result (**actionToResult**). Both the **viewState** and **viewEffects** are listening to the Result stream. 

- The **resultToViewState** changes the **viewState** depending on the Result

- The **resultToViewEffect** emits a one time **viewEffect**  that should not be persisted inside the state.

Activities and Fragments listen to the **viewState** and on every emit update the UI according to the state. A **viewEffects** emit "trigger" a one time effect in the App.

The BaseViewModel takes in four generic types each of which implement (**BaseAction**, **BaseResult**, **BaseViewState**, **BaseViewEffect**). Every ViewModel that extends the BaseViewModel and has to provide the four generics along with the **actionToResult**, **resultToViewState**, **resultToViewEffect** functions.

