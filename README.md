# Dungeon Crawler 2

2D Java Rogue-like rpg dungeon crawler game. 
(Screenshots below)

I implemented many features that LibGDX already supports out of curiosity.educational purposes. I would not recommend doing this in a final product.

## Features

1. Procedurally generated levels
2. 2D fragment based lighting
3. Simple Game AI
   - Pathfinding
   - Targeting/Attacking/Etc.
4. 100% custom GUI/Interface
   - Custom windows and window management
   - Custom widgets
   - Custom event handling
5. Experience/Leveling system
   - Gain attribute points to improve your character by defeating dungeon monsters.
6. Inventory management system
7. Fast-paced gameplay
8. Loot!
   - Items/Loot can be found throughout the dungeon or by defeating dungeon monsters.
9. Record keeping system
   - Keeps track of various in-game stats (time played/attribute points spent/damage dealt/damage taken and many more)
   - Allows for developers to quickly and easily add new stats to record with just two short lines of code.
10. Custom asset/resource manager
    - Ensures duplicate resources aren't loaded into memory.
    - Ensures resources are disposed of correctly.
11. Custom Animations system

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

1. Clone DungeonCrawler2 repo
2. Import DungeonCrawler2 into Eclipse as a Gradle project
   - Import
      - Gradle
         - Existing Gradle Project
   - Welcome Window
      - No actions, hit next
   - Import Gradle Project
      - Selected the project root directory
        - ex. C:\Users\<user>\Documents\GitHub\DungeonCrawler2
      - Hit next
   - Import Options Window
      - No actions, hit next
   - Import Preview
      - If the preview loaded correctly, finish and move on.
      - else:
         - Error #1
            - Gradle user home directory: Unknown
            - Gradle distribution:        Gradle wrapper from target build
            - Gradle version:             Unknown
            - Java home directory:        Unknown

            - Error: Could not fetch model of type 'BuildEnvironment' using Gradle distribution '/htts://services.gradle.org/distributions/gradle-6.1-rc-1-bin.zip'
            - Caused by: java.lang.IllegalArgumentException: Value 'C:\Program Files\Java\jdk-12.0.1' given for org.gradle.java.home Gradle property is invalid (Java home supplied is invalid)
            
            - Solution:
               - Navigate to .../DungeonCrawler2/gradle.properties
               - change org.gradle.java.home to your java jdk home/version
                  - Ex: "org.gradle.java.home=C:\\Program Files\\Java\\jdk-12.0.1
               - Run gradlew.bat (not sure this is necessary)
               - Delete the project from eclipse
                  - NOTE: Do NOT delete project from disk, only from eclipse.
               - Reimport the project 
                  - There's probably an easier way to refresh the gradle project but I don't know what it is.
   
3. Set DungeonCrawler2 to use the assets folder
   - Right-click "DungeonCrawler2-desktop" (in Eclipse project explorer)
   - Run As
   - Run Configurations
   - Arguments (tab)
   - Under "Working directory:"
   - Change from "Default" to "Other"
   - "Workspace..." (button) 
   - Select "DungeonCrawler2 > core > assets"
   - "OK" (button)
4. Starting the game
   - Launch DungeonCrawler2-desktop > src > ecu.se.desktop > DesktopLauncher.java as Java Application

## Built With

* [LibGdx](https://libgdx.badlogicgames.com/) - The graphical framework

## Authors

* **Jason Cole**

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Screenshots

![alt text](https://github.com/jaymcole/DungeonCrawler2/blob/master/screenshots/dungeon_03.png)
![alt text](https://github.com/jaymcole/DungeonCrawler2/blob/master/screenshots/dungeon_04.png)
![alt text](https://github.com/jaymcole/DungeonCrawler2/blob/master/screenshots/dungeon_02.png)
