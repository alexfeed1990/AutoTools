***This mod is a work-in-progress project of mine. I may never work on it again. Depends if I'm feeling up to it.***

# AutoTools
This is a mod generally made for OneBlock (and since 1.16.4 is the latest version, you can put two-and-two together.)
My friends have been playing it on Controllable (which is a forge mod) so I made this mod for them to make their playthrough easier.

The main item introduced is called the *Tool Ring*. If you hold it in your offhand, and you have the correct tools in your hotbar, the correct tool will be selected automatically once you start mining a block.

# Known bugs:

- If you have multiple tools of the same type in your hotbar, it will just switch over and over again inbetween them.
- ***IF YOU WANT TO RUN THIS MOD, ON FORGE 1.16.4, BEWARE THAT FORGE VERSIONS BELOW 1.17 (iirc) CRASH ON THE LATEST JAVA 8 RELEASE. THIS IS NOT A PROBLEM WITH MY MOD.*** (download an older release [here](https://github.com/adoptium/temurin8-binaries/releases/tag/jdk8u312-b07).)

# Irrelevant information

Building this is as easy as building it. (i know, creative, right?) This project was made with IntelliJ Idea (community edition), so open the build.gradle file as a project and type IN THE TERMINAL (won't work otherwise):
  - ``.\gradlew build`` on Windows
  - ```chmod +X gradlew
       ./gradlew build``` on Linux
       
You will find the mod jar in the build/libs folder.
