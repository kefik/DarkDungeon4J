# Dark Dungeon 4J

Rogue-like scenario simulator for AI experiments, highly modular implementation.

## IN DEVELOPMENT STATE

The best way to interact with the code is to download/clone it completely, import at least DarkDungeon4J-Adventure project
into favourite IDE of yours (preferably Eclipse), open Dungeon01 file and run it (for IDEA users, you will have to modify some paths within the file...).

## PROJECT STRUCTURE

**DarkDungeon4J** -> main project containing the simulator and visualizer (so far console only) of the game

**DarkDungeon4J-Adventure** -> Project with example DD adventures.

**DarkDungeon4J-Agents** -> Hero/Monster/Trap agent (mind) implementations.

**DarkDungeon4J-Generator** -> PCG routines for DD.

**DarkDungeon4J-Loader** -> XML description of DDs including XML loaders.

**DarkDungeon4J-Utils** -> Various utilities used by other projects.

**DarkDungeon4J-Visualization** -> Contains graphical representation of grid-based DD adventures; uses pure Java-based [Clear2D](https://github.com/kefik/Clear2D) framework.

## COMPILATION

Compile any of DarkDungeon4J projects from within its directory by issuing:

Windows (from cmd; assuming you have mvn on path):

    mvn package
    
Linux (from bash, assuming you have mvn on path):

    mvn package

## MAVEN [REPOSITORY](http://diana.ms.mff.cuni.cz:8081/artifactory)

    <repository>
        <id>amis-artifactory</id>
        <name>AMIS Artifactory</name>
        <url>http://diana.ms.mff.cuni.cz:8081/artifactory/repo</url>
    </repository>
    
## MAVEN DEPENDENCY

    Compiled with Java 1.8!

    <dependency>
        <groupId>cz.dd4j</groupId>
	    <artifactId>dd4j</artifactId>
	    <version>0.0.1-SNAPSHOT</version>
    </dependency>
