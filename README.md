
<p align="center">
<img src="https://github.com/RuitiariGibson/Filio/blob/master/app/src/main/ic_launcher-playstore.png" alt="home" width="100"/>
</p>

# Filio
An Android app which displays and allows you to delete files ,that is audios, images, videos as well as downloaded files,
present in your phone. The app has been built with clean architecture principles including use of architecture components, 
the repository Pattern and mvvm pattern .

Min Api Level : 23 [Supports Over 84% Devices ](https://developer.android.com/about/dashboards)

Build System : [Gradle](https://gradle.org/)

## Table of Contents

- [Architecture](#architecture)
- [Libraries](#libraries)
- [Demos](#Demos)

## Architecture

The Application is split into a three layer architecture:
- Presentation
- Domain
- Data


The 3 layers, based on clean architecture principals,
provides the abstraction needed to ensure complete seperation between our logic and
the ui. Moreover, this addresses, issue of the seperation concerns.

The `domain` and `data` layers are android module libraries which rely, on the core android apis. 
They two layers do not in anyway use any ui-android api.

#### Presentation

The application presentation layer contains the Activities, the Fragments and 
the respective view models. Further, the presentation layer handles dependency injection.

The UI layer  contains `audioactivity`, `launcheractivity`, `downloadactivity` & `videosactivity` and their corresponding
viewmodels.


The viewmodels are lazily provided at runtime by the Koin.

The viewmodels ideally receive data from the properties which serve as a bridge between repositories and the view models
. In turn the view models furnish the activities and fragments with the data needed subsequently displaying the data to the user.


#### Domain

The domain  contains  model classes which hold data gotten from the data source. The data is then
propagated towards the repositories. The domain in a nut shell, acts as a bridge between repositories and
the data source. 

The domain moreover, contains read only properties, inspired by kotlin delegates. That is,
the read only properties ensure that we only have a one way data pipeline: 
from the source->repository->properties->viewModels->activties, by only allowing production of results <out T> of the given type.
In addition to the aforementioned function, the read only properties not only ensure intergratity of the data but also
maintains the data flow needed by the app. 

#### Data

The Data layer on the other hand utilizes the **Repository Pattern**. Essentially, 
the data source is responsible for fetching data from the user's storage device utilizing media store apis.
The Media store apis are essentially like tables which provide data in form of cursors hence making
it easy to fetch the data from the storage device.
The data is then propagated to the repositories. Subsequently, the repositories
are responsible for fetching data(data only needed) from the source. This decouples 
the functions of the repository thus enabling one repository to provide only one type
of specific data. Hence enabling testing/scaling if need be easier.
For instance, the `DocumentFilesRepoImpl` focuses on fetching document-type 
of data. 

## Libraries

Libraries used in the whole application are:

  - [Jetpack](https://developer.android.com/jetpack)🚀
  - [Viewmodel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Manage UI related data in a lifecycle conscious way 
  and act as a channel between use cases and ui
 - [Data Binding](https://developer.android.com/topic/libraries/data-binding) - support library that allows binding of UI components in  layouts to data sources,binds character details and search results to UI
- [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Library Support for coroutines,provides `runBlocking` coroutine builder used in tests
- [Material Design](https://material.io/develop/android/docs/getting-started/) - build awesome beautiful UIs.🔥🔥 and material transformations
- [Koin](https://github.com/InsertKoinIO/koin) - A pragmatic lightweight dependency injection framework for Kotlin
- [Glide] (https://bumptech.github.io/glide/) - Glide is a fast and efficient image loading library for Android focused on smooth scrolling. 

## Demo
<p>
<img src="https://github.com/RuitiariGibson/Filio/blob/master/art/pic_1.png" width=200/> 
<img src="https://github.com/RuitiariGibson/Filio/blob/master/art/pic_3.png" width=200/>
<img src="https://github.com/RuitiariGibson/Filio/blob/master/art/pic_4.png" width=200/>
</p>

## License

 ```
   Copyright 2020 Ruitiari Gibson
   
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




