<img src=".github/assets/greenmachine_gear_eyes.svg" height="100" alt="The Green Machine"/>

# Zeta

Zeta is FRC Team 1816's robot for the 2019 FRC season, *Destination: Deep Space*. This software uses Java 11 and the [WPILib](https://github.com/wpilibsuite/allwpilib) library.

## Prerequisites
1. You must have Java 11 installed on your system. You can [download OpenJDK 11 Here](https://openjdk.java.net/projects/jdk/11/). Alternatively, this will be installed through the WPILib One-Click Installer
2. You must have an IDE of your choice installed. [Visual Studio Code](https://code.visualstudio.com/) or [IntelliJ IDEA](https://www.jetbrains.com/idea/) are recommended as official and unofficial options. VSCode can be optionally installed using the WPILib One-Click Installer
3. You must have [Git](https://git-scm.com/) installed.
4. It is recommended that you run the [WPILib One-Click Installer](link:todo). This includes the VSCode plugins as well as all NI software tools. It does **NOT** include any CTRE software tools.

## Cloning

Open a new Bash shell and clone:
```bash
$ git clone https://github.com/TheGreenMachine/Zeta.git
```
## Importing

### Importing into IntelliJ IDEA

1. Clone the project into your desired folder.
2. Open IntelliJ IDEA to the welcome screen.
3. **Do NOT select 'Open Project'**. Instead, select 'Import Project'.
4. Select `Import project from external model`. Then select 'Gradle'.
5. Configure your Gradle options:
    * Make sure to select the "Use gradle 'wrapper' task configuration' option.
    * Verify that the Gradle JVM is set to 'Use Project JDK' (which should be Java 8).
    * Keep other options as their defaults.
6. Select 'Finish'. Your project should load in and be set up without issues.

### Importing into Visual Studio Code

<!-- TODO: Complete section -->

## Build and Deploy
Build the project by running the build task through the Gradle wrapper. Just issue the following command in a Bash shell:
```bash
$ ./gradlew build
```
This command isn't always necessary as it is often run by your IDE.

Deploy the project to the robot by first connecting to the robot's wi-fi network and then running the following command:
```bash
$ ./gradlew deploy
```
The deploy task will call the build task automatically.

To clear previous built binaries and minimize the possibility of bugs, it is recommended to run the clean task before deploying:
```bash
$ ./gradlew clean deploy
```
---
2018 - FRC Team 1816 The Green Machine
