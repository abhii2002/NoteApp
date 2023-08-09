# NoteApp
 Simple Note application which stores data locally in the device is used for making short text notes, updating when you need them, and trashing when you are done.

 ![fOODIES (1)](https://github.com/abhii2002/NoteApp/assets/87520905/61b6ea85-41a2-4169-96c2-978ae34155a6)

 # Download
 Download app from here https://github.com/abhii2002/NoteApp/releases/tag/v1.0.0-alpha
 
# Tech stack & Open-source libraries. 
 - Minimum SDK level 21
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://developer.android.com/kotlin/coroutines) + [Flow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.

  - Lifecycle: Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - Room: Constructs Database by providing an abstraction layer over SQLite to allow fluent database access.
  - Hilt: Dagger Hilt dependency injection library is crucial since it offers a uniform method of utilizing DI in your application by handling the containers for all Android classes in the project.
-  Architecture 
   - MVVM : MVVM stands for Model-View-ViewModel architecture. There are several advantages of using MVVM in projects also it is highly recommended by google and android developers teams to use MVVM architecture.
- [Lottie](https://github.com/airbnb/lottie-android) : Render After Effects animations natively on Android and iOS, Web, and React Native
