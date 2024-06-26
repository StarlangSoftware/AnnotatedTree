package AnnotatedTree;

import AnnotatedSentence.ViewLayerType;
import ParseTree.ParseNode;
import Xml.XmlAttribute;
import Xml.XmlElement;

import java.util.ArrayList;

public class ParseNodeSearchable extends ParseNode {

    private final ArrayList<SearchType> searchTypes;
    private final ArrayList<ViewLayerType> viewLayerTypes;
    private final ArrayList<String> searchValues;
    private final boolean isLeaf;

    /**
     * Constructs a ParseNodeSearchable from a xml node. If the node is a leaf node, it only sets the search type, layer
     * name and value. Otherwise, it only sets the parent node. It also calls itself recursively to generate its child
     * parseNodes.
     * @param parent The parent node of this node.
     * @param node Xml node that contains the node information.
     */
    public ParseNodeSearchable(ParseNodeSearchable parent, XmlElement node){
        XmlElement child;
        children = new ArrayList<>();
        this.parent = parent;
        isLeaf = node.getName().equalsIgnoreCase("leaf");
        searchTypes = new ArrayList<>();
        viewLayerTypes = new ArrayList<>();
        searchValues = new ArrayList<>();
        if (node.hasAttributes()){
            for (int i = 0; i < node.attributeSize(); i++){
                XmlAttribute attribute = node.getAttribute(i);
                String viewLayerType = attribute.getName().substring(0, 3);
                String searchType = attribute.getName().substring(3);
                searchValues.add(attribute.getValue());
                if (searchType.equalsIgnoreCase("equals")){
                    searchTypes.add(SearchType.EQUALS);
                } else {
                    if (searchType.equalsIgnoreCase("contains")){
                        searchTypes.add(SearchType.CONTAINS);
                    } else {
                        if (searchType.equalsIgnoreCase("matches")){
                            searchTypes.add(SearchType.MATCHES);
                        } else {
                            if (searchType.equalsIgnoreCase("starts")){
                                searchTypes.add(SearchType.STARTS);
                            } else {
                                if (searchType.equalsIgnoreCase("ends")){
                                    searchTypes.add(SearchType.ENDS);
                                } else {
                                    if (searchType.equalsIgnoreCase("equalsignorecase")){
                                        searchTypes.add(SearchType.EQUALS_IGNORE_CASE);
                                    } else {
                                        if (searchType.equalsIgnoreCase("isnull")){
                                            searchTypes.add(SearchType.IS_NULL);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (viewLayerType.equalsIgnoreCase("mor") || viewLayerType.equalsIgnoreCase("inf")){
                    viewLayerTypes.add(ViewLayerType.INFLECTIONAL_GROUP);
                } else {
                    if (viewLayerType.equalsIgnoreCase("tur")){
                        viewLayerTypes.add(ViewLayerType.TURKISH_WORD);
                    } else {
                        if (viewLayerType.equalsIgnoreCase("per")){
                            viewLayerTypes.add(ViewLayerType.PERSIAN_WORD);
                        } else {
                            if (viewLayerType.equalsIgnoreCase("eng")){
                                viewLayerTypes.add(ViewLayerType.ENGLISH_WORD);
                            } else {
                                if (viewLayerType.equalsIgnoreCase("ner")){
                                    viewLayerTypes.add(ViewLayerType.NER);
                                } else {
                                    if (viewLayerType.equalsIgnoreCase("sem") || viewLayerType.equalsIgnoreCase("tse")){
                                        viewLayerTypes.add(ViewLayerType.SEMANTICS);
                                    } else {
                                        if (viewLayerType.equalsIgnoreCase("met")){
                                            viewLayerTypes.add(ViewLayerType.META_MORPHEME);
                                        } else {
                                            if (viewLayerType.equalsIgnoreCase("pro")){
                                                viewLayerTypes.add(ViewLayerType.PROPBANK);
                                            } else {
                                                if (viewLayerType.equalsIgnoreCase("dep")){
                                                    viewLayerTypes.add(ViewLayerType.DEPENDENCY);
                                                } else {
                                                    if (viewLayerType.equalsIgnoreCase("sha") || viewLayerType.equalsIgnoreCase("chu")){
                                                        viewLayerTypes.add(ViewLayerType.SHALLOW_PARSE);
                                                    } else {
                                                        if (viewLayerType.equalsIgnoreCase("ese")){
                                                            viewLayerTypes.add(ViewLayerType.ENGLISH_SEMANTICS);
                                                        } else {
                                                            if (viewLayerType.equalsIgnoreCase("epr")){
                                                                viewLayerTypes.add(ViewLayerType.ENGLISH_PROPBANK);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        child = node.getFirstChild();
        while (child != null){
            if (child.getName().equalsIgnoreCase("node") || child.getName().equalsIgnoreCase("leaf"))
                children.add(new ParseNodeSearchable(this, child));
            child = child.getNextSibling();
        }
    }

    /**
     * Accessor for the search type at the given position
     * @param index Position of the search type
     * @return Search type at the given position index.
     */
    public SearchType getType(int index){
        return searchTypes.get(index);
    }

    /**
     * Accessor for the search value at the given position
     * @param index Position of the search value
     * @return Search value at the given position index.
     */
    public String getValue(int index){
        return searchValues.get(index);
    }

    /**
     * Accessor for the layer name at the given position
     * @param index Position of the layer name
     * @return Layer name at the given position index.
     */
    public ViewLayerType getViewLayerType(int index){
        return viewLayerTypes.get(index);
    }

    /**
     * Accessor for the isLeaf attribute
     * @return IsLeaf attribute
     */
    public boolean isLeaf(){
        return isLeaf;
    }

    public int size(){
        return searchValues.size();
    }
}
