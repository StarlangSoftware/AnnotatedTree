# AnnotatedTree
For Developers
============

## Requirements

* [Java Development Kit 8 or higher](#java), Open JDK or Oracle JDK
* [Maven](#maven)
* [Git](#git)

### Java 

To check if you have a compatible version of Java installed, use the following command:

    java -version
    
If you don't have a compatible version, you can download either [Oracle JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or [OpenJDK](https://openjdk.java.net/install/)    

### Maven
To check if you have Maven installed, use the following command:

    mvn --version
    
To install Maven, you can follow the instructions [here](https://maven.apache.org/install.html).      

### Git

Install the [latest version of Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).

## Download Code

In order to work on code, create a fork from GitHub page. 
Use Git for cloning the code to your local or below line for Ubuntu:

	git clone <your-fork-git-link>

A directory called WordNet will be created. Or you can use below link for exploring the code:

	git clone https://github.com/olcaytaner/AnnotatedTree.git

## Open project with IntelliJ IDEA

Steps for opening the cloned project:

* Start IDE
* Select **File | Open** from main menu
* Choose `AnnotatedTree/pom.xml` file
* Select open as project option
* Couple of seconds, dependencies with Maven will be downloaded. 


## Compile

**From IDE**

After being done with the downloading and Maven indexing, select **Build Project** option from **Build** menu. After compilation process, user can run AnnotatedTree.

**From Console**

Go to `AnnotatedTree` directory and compile with 

     mvn compile 

## Generating jar files

**From IDE**

Use `package` of 'Lifecycle' from maven window on the right and from `AnnotatedTree` root module.

**From Console**

Use below line to generate jar file:

     mvn install



------------------------------------------------

WordNet
============
+ [Maven Usage](#maven-usage)


### Maven Usage

    <groupId>NlpToolkit</groupId>
    <artifactId>AnnotatedTree</artifactId>
    <version>1.0.1</version>
    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>
    <repositories>
        <repository>
            <id>NlpToolkit</id>
            <url>http://haydut.isikun.edu.tr:8081/artifactory/NlpToolkit</url>
        </repository>
    </repositories>
    <dependencies>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>ParseTree</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>AnnotatedSentence</artifactId>
            <version>1.0.2</version>
        </dependency>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>MorphologicalAnalysis</artifactId>
            <version>1.0.2</version>
        </dependency>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>Translation</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>Dictionary</artifactId>
            <version>1.0.3</version>
        </dependency>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>Sampling</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>NamedEntityRecognition</artifactId>
            <version>1.0.1</version>
        </dependency>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>Util</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>PropBank</artifactId>
            <version>1.0.2</version>
        </dependency>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>Corpus</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>WordNet</artifactId>
            <version>1.0.2</version>
        </dependency>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>SyntacticParser</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>DataStructure</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>NlpToolkit</groupId>
            <artifactId>MorphologicalDisambiguation</artifactId>
            <version>1.0.1</version>
        </dependency>
    </dependencies>
