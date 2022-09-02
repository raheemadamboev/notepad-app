<h1 align="center">Notepad</h1>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
</p>

<p align="center">
✏ "Notepad" app demonstrates modern Android app development with Jetpack Compose, Hilt, Material3, Coroutines, Flows, Room based on MVVM architecture.
</p>
</br>

<p align="center">
  <img src="/preview/Notepad%201.1.2.jpg"/>
</p>

# Download

You can download the release app on Google Play Store:

<a href="https://play.google.com/store/apps/details?id=xyz.teamgravity.notepad">
  <img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" width="200"/>
</a>

🎉 _More than 25 000 downloads and overall 4.4 star rating_

# Tech stack

- [Kotlin](https://kotlinlang.org/): first class programming language for native Android development.
- [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines): structured concurrency.
- [Kotlin Flows](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/): reactive communication.
- [Material3](https://m3.material.io/): modern UI/UX guidelines and components.
- [Jetpack Compose](https://developer.android.com/jetpack/compose): modern, declarative way of building UI in Kotlin.
- [Jetpack Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle): observe Android lifecycles and handle UI states upon the lifecycle changes.
- [Jetpack ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel): manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
- [Jetpack SavedStateHandle](https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate): in case of process death, key-value map that allows to write and retrieve bundle data to and from the saved state.
- [Jetpack DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore): uses Kotlin Coroutines and Flows to store data asynchronously, consistently, and transactionally.
- [Room](https://developer.android.com/training/data-storage/room): SQLite abstraction and database solution.
- [Dagger Hilt](https://dagger.dev/hilt/): first class dependency injection for native Android development.
- [Firebase](https://firebase.google.com/): tracks analytics and crashes using the Firebase services.
- [Compose Destinations](https://composedestinations.rafaelcosta.xyz/): a type-safe navigation for composables.
- [Timber](https://github.com/JakeWharton/timber): a logger with a small, extensible API.

# Architecture

"Notepad" is based on the MVVM architecture pattern, Repository pattern, Mapper pattern.

<img src="/preview/mvvm-pattern.png"/>

# MAD Score

<p align="center">
  <img src="/preview/summary_1.png"/>
  <img src="/preview/summary_2.png"/>
</p>

# About

Notepad is completely offline and ad-free. Also, it is very lightweight and fast as a rocket. Its interface is so user-friendly and simple that even your granny can use it. Regarding adding notes, it has never been straightforward like this.

# Features

- Add, edit and delete notes.
- Share notes.
- Auto-save.
- Delete all notes at once, delete selected note.
- Beautiful animations and indicators.
- Dark/Light theme support.
- Multiple language support.
- Multiple screen sizes support.
- Light that is less than 2 MB.
- Works without the internet.
- Simplicity that has only five screens.
- Adaptive screens that change according to screen orientation.
- Completely robust to process death.
- Completely robust to configuration change.

# License

```xml
Designed and developed by raheemadamboev (Raheem) 2022.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
