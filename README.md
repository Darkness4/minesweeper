# minesweeper

A multiplayer minesweeper.

## Getting Started

### Prerequisites

- Java JDK  `>=11`

### Executing

#### a) Executing using the Gradle Java plugin

Server :

```sh
./gradlew :server:run
```

Client :

```sh
./gradlew :client:run
```

#### b) Exporting and executing

#### Export

```sh
# Server and Client
./gradlew exportJar
```

The fat JARs are stored in `./build/bin/app-server-*.jar` and `./build/bin/app-client-*.jar`

#### Executing

You can execute it with :

```sh
# Server
java -jar ./build/bin/app-server-*.jar

# Client
java -jar ./build/bin/app-client-*.jar
```

## Docs

https://darkness4.github.io/minesweeper

## License

```txt
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <http://unlicense.org/>
```

