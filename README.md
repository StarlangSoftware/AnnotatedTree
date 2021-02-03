For Developers
============

You can also see [Python](https://github.com/starlangsoftware/AnnotatedTree-Py), [C++](https://github.com/starlangsoftware/AnnotatedTree-CPP), or [C#](https://github.com/starlangsoftware/AnnotatedTree-CS) repository.

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

	git clone https://github.com/starlangsoftware/AnnotatedTree.git

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

## Maven Usage

        <dependency>
            <groupId>io.github.starlangsoftware</groupId>
            <artifactId>AnnotatedTree</artifactId>
            <version>1.0.8</version>
        </dependency>

Detailed Description
============

+ [TreeBankDrawable](#treebankdrawable)
+ [ParseTreeDrawable](#parsetreedrawable)
+ [LayerInfo](#layerinfo)
+ [Automatic Annotation](#automatic-annotation)

## TreeBankDrawable

To load an annotated TreeBank:

	TreeBankDrawable(File folder, String pattern)
	a = new TreeBankDrawable(new File("/Turkish-Phrase"), ".train")

	TreeBankDrawable(File folder)
	a = new TreeBankDrawable(new File("/Turkish-Phrase"))

	TreeBankDrawable(File folder, String pattern, int from, int to)
	a = new TreeBankDrawable(new File("/Turkish-Phrase"), ".train", 1, 500)

To access all the trees in a TreeBankDrawable:

	for (int i = 0; i < a.sentenceCount(); i++){
		ParseTreeDrawable parseTree = (ParseTreeDrawable) a.get(i);
		....
	}

## ParseTreeDrawable

To load a saved ParseTreeDrawable:

	ParseTreeDrawable(FileInputStream file)
	
is used. Usually it is more useful to load TreeBankDrawable as explained above than to load ParseTree one by one.

To find the node number of a ParseTreeDrawable:

	int nodeCount()
	
the leaf number of a ParseTreeDrawable:

	int leafCount()
	
the word count in a ParseTreeDrawable:

	int wordCount(boolean excludeStopWords)
	
above methods can be used.

## LayerInfo

Information of an annotated word is kept in LayerInfo class. To access the morphological analysis
of the annotated word:

	MorphologicalParse getMorphologicalParseAt(int index)

meaning of an annotated word:

	String getSemanticAt(int index)

the shallow parse tag (e.g., subject, indirect object etc.) of annotated word: 

	String getShallowParseAt(int index)

the argument tag of the annotated word:

	Argument getArgumentAt(int index)
	
the word count in a node:

	int getNumberOfWords()

## Automatic Annotation

To assign the arguments of a sentence automatically:

	TurkishAutoArgument()

above class is used.

	void autoArgument(ParseTreeDrawable parseTree, Frameset frameset);

With above line, the arguments of the tree are annotated automatically.

To automatically disambiguate a sentence's morphology:

	TurkishTreeAutoDisambiguator(RootWordStatistics rootWordStatistics)
								  
above class is used. For example,

	a = TurkishTreeAutoDisambiguator(new RootWordStatistics());
	a.autoDisambiguate(parseTree);

with the above code, automatic morphological disambiguation of the tree can be made.

To apply named entity recognition to a sentence:

	TurkishSentenceAutoNER()

above class is used. For example,

	a = TurkishTreeAutoNER();
	a.autoNER(parseTree);

with the above code, automatic named entity recognition of a tree can be made.

To make semantic annotation in a sentence:

	TurkishTreeAutoSemantic()

above class can be used. For example,

	a = TurkishTreeAutoSemantic();
	a.autoSemantic(parseTree);

with above code, automatic semantic annotation of the tree can be made.
