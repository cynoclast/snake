# Terminal based snake game implementation written in Java

## Build:

`./gradlew clean fatJar`

## Run:

`java -classpath ./build/libs/snake.jar com.vevo.hiring.snake.MySnakeGame`

## One-liner:

`./gradlew clean fatJar && java -classpath ./build/libs/snake.jar com.vevo.hiring.snake.MySnakeGame`

## Notes

* Board size is hard coded at 79x23
* Running from the IDE starts a Swing window that flickers badly.
* May not work on Windows at all. It hasn't been tested.
* Lots of room for improvement as it was written in two days.
* Runs at ~100ms between moves
