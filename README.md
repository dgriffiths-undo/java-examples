## Java Examples

Java and Kotlin programs to demonstrate LiveRecorder for Java features.

# Building

- import the `examples` directory into IntelliJ to create a new project from external sources
  - we recommend using Java 11
- right click the `src/main/kotlin` directory and mark it as a sources root
- build the project

# Building from command line

- `gradle shadowJar` can be used to create a "fat" jar containing the Kotlin runtime

# Running

- `java -cp ./build/libs/examples-1.0-SNAPSHOT-all.jar io.undo.FruiKt`

# Recording

- `java -XX:-Inline -XX:TieredStopAtLevel=1 -agentpath:/paty/to/lr4j-record-1.0.so=save_on=always -cp ./build/libs/examples-1.0-SNAPSHOT-all.jar io.undo.FruitKt`
