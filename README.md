# AnnotatedTree

A treebank is a corpus where the sentences in each language are syntactically (if necessary morphologically) annotated. In the  treebanks, the syntactic annotation usually follows constituent and/or dependency structure.

Treebanks annotated for the syntactic or semantic structures of the sentences are essential for developing state-of-the-art statistical natural language processing (NLP) systems including part-of-speech-taggers, syntactic parsers, and machine translation systems. There are two main groups of syntactic treebanks, namely treebanks annotated for constituency (phrase structure) and the ones that are annotated for dependency structure.

## Tree Transfer

### Removal of \*NONE\* Leafs
The translated trees contain \*NONE\* leafs vacated by English constituents embedded in the morphemes of Turkish stems. After morphological annotation, the semantic aspects of those English constituents will be represented in the morpheme leaves. Hence, we remove all \*NONE\* leaves and all their ancestors until we reach an ancestor that has more than one child.

### Branching Multiword Leaves
A single English word may be translated to Turkish as a multiword expression. In such cases, we branch the multiword leaf into multiple leaves. We assign tags to the parents of the new leaf nodes according to their morphological analyses.

### Fixing Plural Nouns
Plural nouns tagged as NNS in an English tree are sometimes translated as singular to Turkish. For example, while English nouns next to a cardinality are attached the plurality suffix, Turkish nouns are not. In such cases, we check the morpholog- ical analyses of the nouns to detect whether they have the plurality suffix “-lAr” which is equivalent to the “-s” plurality suffix in English.

### Removal of Unnecessary Ancestors
After removal of \*NONE\* leafs, we generally end up with trees that have unnecessary ancestors. For each node, we remove all its ancestors until we reach an ancestor that has more than one child. However, if the leaf node does not contain any suffixes, we do not remove the immediate ancestor of that node.

### Branching Morphemes
The final and probably the most important operation is to branch the morphemes. We need to exploit the morphological analysis to compensate the information loss that occurred during the removal operations in the previous stages.

We treat the suffixes attached to nouns and verbs differently. While all suffixes attached to a noun will be siblings of that noun, each suffix attached to a verb will generate a new parent node and the next suffix will be a sibling of that new node.

In Turkish, suffixes may convert nouns to verbs or vice versa. In such cases, the corresponding node will be treated according to its final form and any additional suffixes will be attached following the above rule. In this stage, we introduced new tags for morphological annotation. Verb and noun roots are tagged as VERB and NOUN respectively. Suffixes attached to verbs determine the new tag that will be created. We simply concatenate their morpholog- ical annotation to VB-.

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

AnnotatedTree
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
