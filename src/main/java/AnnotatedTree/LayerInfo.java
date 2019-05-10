package AnnotatedTree;

import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.LayerNotExistsException;
import AnnotatedSentence.ViewLayerType;
import MorphologicalAnalysis.MorphologicalParse;
import MorphologicalAnalysis.MetamorphicParse;
import AnnotatedTree.Layer.*;
import PropBank.Argument;

import java.util.EnumMap;

public class LayerInfo {
    private EnumMap<ViewLayerType, WordLayer> layers;

    public LayerInfo(String info){
        String[] splitLayers = info.split("[\\{\\}]");
        layers = new EnumMap<ViewLayerType, WordLayer>(ViewLayerType.class);
        for (String layer:splitLayers){
            if (layer.isEmpty())
                continue;
            String layerType = layer.substring(0, layer.indexOf("="));
            String layerValue = layer.substring(layer.indexOf("=") + 1);
            if (layerType.equalsIgnoreCase("turkish")){
                layers.put(ViewLayerType.TURKISH_WORD, new TurkishWordLayer(layerValue));
            } else {
                if (layerType.equalsIgnoreCase("persian")){
                    layers.put(ViewLayerType.PERSIAN_WORD, new PersianWordLayer(layerValue));
                } else {
                    if (layerType.equalsIgnoreCase("english")){
                        layers.put(ViewLayerType.ENGLISH_WORD, new EnglishWordLayer(layerValue));
                    } else {
                        if (layerType.equalsIgnoreCase("morphologicalAnalysis")){
                            layers.put(ViewLayerType.INFLECTIONAL_GROUP, new MorphologicalAnalysisLayer(layerValue));
                            layers.put(ViewLayerType.PART_OF_SPEECH, new MorphologicalAnalysisLayer(layerValue));
                        } else {
                            if (layerType.equalsIgnoreCase("metaMorphemes")){
                                layers.put(ViewLayerType.META_MORPHEME, new MetaMorphemeLayer(layerValue));
                            } else {
                                if (layerType.equalsIgnoreCase("metaMorphemesMoved")){
                                    layers.put(ViewLayerType.META_MORPHEME_MOVED, new MetaMorphemesMovedLayer(layerValue));
                                } else {
                                    if (layerType.equalsIgnoreCase("dependency")){
                                        layers.put(ViewLayerType.DEPENDENCY, new DependencyLayer(layerValue));
                                    } else {
                                        if (layerType.equalsIgnoreCase("semantics")){
                                            layers.put(ViewLayerType.SEMANTICS, new TurkishSemanticLayer(layerValue));
                                        } else {
                                            if (layerType.equalsIgnoreCase("namedEntity")){
                                                layers.put(ViewLayerType.NER, new NERLayer(layerValue));
                                            } else {
                                                if (layerType.equalsIgnoreCase("propBank")){
                                                    layers.put(ViewLayerType.PROPBANK, new TurkishPropbankLayer(layerValue));
                                                } else {
                                                    if (layerType.equalsIgnoreCase("englishPropbank")){
                                                        layers.put(ViewLayerType.ENGLISH_PROPBANK, new EnglishPropbankLayer(layerValue));
                                                    } else {
                                                        if (layerType.equalsIgnoreCase("englishSemantics")){
                                                            layers.put(ViewLayerType.ENGLISH_SEMANTICS, new EnglishSemanticLayer(layerValue));
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

    public LayerInfo() {
        layers = new EnumMap<ViewLayerType, WordLayer>(ViewLayerType.class);
    }

    public LayerInfo clone(){
        return new LayerInfo(getLayerDescription());
    }

    public void setLayerData(ViewLayerType viewLayer, String layerValue){
        switch (viewLayer){
            case PERSIAN_WORD:
                layers.put(ViewLayerType.PERSIAN_WORD, new PersianWordLayer(layerValue));
                layers.remove(ViewLayerType.SEMANTICS);
                break;
            case TURKISH_WORD:
                layers.put(ViewLayerType.TURKISH_WORD, new TurkishWordLayer(layerValue));
                layers.remove(ViewLayerType.INFLECTIONAL_GROUP);
                layers.remove(ViewLayerType.PART_OF_SPEECH);
                layers.remove(ViewLayerType.META_MORPHEME);
                layers.remove(ViewLayerType.META_MORPHEME_MOVED);
                layers.remove(ViewLayerType.SEMANTICS);
                break;
            case ENGLISH_WORD:
                layers.put(ViewLayerType.ENGLISH_WORD, new EnglishWordLayer(layerValue));
                break;
            case PART_OF_SPEECH:
            case INFLECTIONAL_GROUP:
                layers.put(ViewLayerType.INFLECTIONAL_GROUP, new MorphologicalAnalysisLayer(layerValue));
                layers.put(ViewLayerType.PART_OF_SPEECH, new MorphologicalAnalysisLayer(layerValue));
                layers.remove(ViewLayerType.META_MORPHEME_MOVED);
                break;
            case META_MORPHEME:
                layers.put(ViewLayerType.META_MORPHEME, new MetaMorphemeLayer(layerValue));
                break;
            case META_MORPHEME_MOVED:
                layers.put(ViewLayerType.META_MORPHEME_MOVED, new MetaMorphemesMovedLayer(layerValue));
                break;
            case DEPENDENCY:
                layers.put(ViewLayerType.DEPENDENCY, new DependencyLayer(layerValue));
                break;
            case SEMANTICS:
                layers.put(ViewLayerType.SEMANTICS, new TurkishSemanticLayer(layerValue));
                break;
            case ENGLISH_SEMANTICS:
                layers.put(ViewLayerType.ENGLISH_SEMANTICS, new EnglishSemanticLayer(layerValue));
                break;
            case NER:
                layers.put(ViewLayerType.NER, new NERLayer(layerValue));
                break;
            case PROPBANK:
                layers.put(ViewLayerType.PROPBANK, new TurkishPropbankLayer(layerValue));
                break;
            case ENGLISH_PROPBANK:
                layers.put(ViewLayerType.ENGLISH_PROPBANK, new EnglishPropbankLayer(layerValue));
                break;
            case SHALLOW_PARSE:
                layers.put(ViewLayerType.SHALLOW_PARSE, new ShallowParseLayer(layerValue));
                break;
        }
    }

    public void setMorphologicalAnalysis(MorphologicalParse parse){
        layers.put(ViewLayerType.INFLECTIONAL_GROUP, new MorphologicalAnalysisLayer(parse.toString()));
        layers.put(ViewLayerType.PART_OF_SPEECH, new MorphologicalAnalysisLayer(parse.toString()));
    }

    public void setMetaMorphemes(MetamorphicParse parse){
        layers.put(ViewLayerType.META_MORPHEME, new MetaMorphemeLayer(parse.toString()));
    }

    public boolean layerExists(ViewLayerType viewLayerType){
        return layers.containsKey(viewLayerType);
    }

    public ViewLayerType checkLayer(ViewLayerType viewLayer){
        switch (viewLayer){
            case TURKISH_WORD:
            case PERSIAN_WORD:
            case ENGLISH_SEMANTICS:
                if (!layers.containsKey(viewLayer)){
                    return ViewLayerType.ENGLISH_WORD;
                }
            case PART_OF_SPEECH:
            case INFLECTIONAL_GROUP:
            case META_MORPHEME:
            case SEMANTICS:
            case NER:
            case PROPBANK:
            case SHALLOW_PARSE:
            case ENGLISH_PROPBANK:
                if (!layers.containsKey(viewLayer))
                    return checkLayer(ViewLayerType.TURKISH_WORD);
                break;
            case META_MORPHEME_MOVED:
                if (!layers.containsKey(viewLayer))
                    return checkLayer(ViewLayerType.META_MORPHEME);
                break;
        }
        return viewLayer;
    }

    public int getNumberOfWords() throws LayerNotExistsException {
        if (layers.containsKey(ViewLayerType.TURKISH_WORD)){
            return ((TurkishWordLayer) layers.get(ViewLayerType.TURKISH_WORD)).size();
        } else {
            if (layers.containsKey(ViewLayerType.PERSIAN_WORD)){
                return ((PersianWordLayer) layers.get(ViewLayerType.PERSIAN_WORD)).size();
            } else {
                throw new LayerNotExistsException("Turkish");
            }
        }
    }

    private String getMultiWordAt(ViewLayerType viewLayerType, int index, String layerName) throws WordNotExistsException, LayerNotExistsException {
        if (layers.containsKey(viewLayerType)){
            if (layers.get(viewLayerType) instanceof MultiWordLayer){
                MultiWordLayer<String> multiWordLayer = (MultiWordLayer<String>) layers.get(viewLayerType);
                if (index < multiWordLayer.size() && index >= 0){
                    return multiWordLayer.getItemAt(index);
                } else {
                    if (viewLayerType.equals(ViewLayerType.SEMANTICS)){
                        return multiWordLayer.getItemAt(multiWordLayer.size() - 1);
                    }
                    throw new WordNotExistsException(multiWordLayer, index);
                }
            } else {
                throw new LayerNotExistsException(layerName);
            }
        } else {
            throw new LayerNotExistsException(layerName);
        }
    }

    public String getTurkishWordAt(int index) throws LayerNotExistsException, WordNotExistsException {
        return getMultiWordAt(ViewLayerType.TURKISH_WORD, index, "turkish");
    }

    public int getNumberOfMeanings(){
        if (layers.containsKey(ViewLayerType.SEMANTICS)){
            return ((TurkishSemanticLayer) layers.get(ViewLayerType.SEMANTICS)).size();
        } else {
            return 0;
        }
    }

    public String getSemanticAt(int index) throws LayerNotExistsException, WordNotExistsException {
        return getMultiWordAt(ViewLayerType.SEMANTICS, index, "semantics");
    }

    public String getShallowParseAt(int index) throws LayerNotExistsException, WordNotExistsException {
        return getMultiWordAt(ViewLayerType.SHALLOW_PARSE, index, "shallowParse");
    }

    public Argument getArgument() {
        if (layers.containsKey(ViewLayerType.PROPBANK)){
            if (layers.get(ViewLayerType.PROPBANK) instanceof TurkishPropbankLayer){
                TurkishPropbankLayer argumentLayer = (TurkishPropbankLayer) layers.get(ViewLayerType.PROPBANK);
                return argumentLayer.getArgument();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Argument getArgumentAt(int index) throws LayerNotExistsException, WordNotExistsException {
        if (layers.containsKey(ViewLayerType.ENGLISH_PROPBANK)){
            if (layers.get(ViewLayerType.ENGLISH_PROPBANK) instanceof SingleWordMultiItemLayer){
                SingleWordMultiItemLayer<Argument> multiArgumentLayer = (SingleWordMultiItemLayer<Argument>) layers.get(ViewLayerType.ENGLISH_PROPBANK);
                return multiArgumentLayer.getItemAt(index);
            } else {
                throw new LayerNotExistsException("EnglishPropbank");
            }
        } else {
            throw new LayerNotExistsException("EnglishPropbank");
        }
    }

    public MorphologicalParse getMorphologicalParseAt(int index) throws LayerNotExistsException, WordNotExistsException {
        if (layers.containsKey(ViewLayerType.INFLECTIONAL_GROUP)){
            if (layers.get(ViewLayerType.INFLECTIONAL_GROUP) instanceof MultiWordLayer){
                MultiWordLayer<MorphologicalParse> multiWordLayer = (MultiWordLayer<MorphologicalParse>) layers.get(ViewLayerType.INFLECTIONAL_GROUP);
                if (index < multiWordLayer.size() && index >= 0){
                    return multiWordLayer.getItemAt(index);
                } else {
                    throw new WordNotExistsException(multiWordLayer, index);
                }
            } else {
                throw new LayerNotExistsException("MorphologicalAnalysis");
            }
        } else {
            throw new LayerNotExistsException("MorphologicalAnalysis");
        }
    }

    public MetamorphicParse getMetamorphicParseAt(int index) throws WordNotExistsException, LayerNotExistsException {
        if (layers.containsKey(ViewLayerType.META_MORPHEME)){
            if (layers.get(ViewLayerType.META_MORPHEME) instanceof MultiWordLayer){
                MultiWordLayer<MetamorphicParse> multiWordLayer = (MultiWordLayer<MetamorphicParse>) layers.get(ViewLayerType.META_MORPHEME);
                if (index < multiWordLayer.size() && index >= 0){
                    return multiWordLayer.getItemAt(index);
                } else {
                    throw new WordNotExistsException(multiWordLayer, index);
                }
            } else {
                throw new LayerNotExistsException("MetaMorphemes");
            }
        } else {
            throw new LayerNotExistsException("MetaMorphemes");
        }
    }

    public String getMetaMorphemeAtIndex(int index) throws LayerItemNotExistsException, LayerNotExistsException {
        if (layers.containsKey(ViewLayerType.META_MORPHEME)){
            if (layers.get(ViewLayerType.META_MORPHEME) instanceof MetaMorphemeLayer){
                MetaMorphemeLayer metaMorphemeLayer = (MetaMorphemeLayer) layers.get(ViewLayerType.META_MORPHEME);
                if (index < metaMorphemeLayer.getLayerSize(ViewLayerType.META_MORPHEME) && index >= 0){
                    return metaMorphemeLayer.getLayerInfoAt(ViewLayerType.META_MORPHEME, index);
                } else {
                    throw new LayerItemNotExistsException(metaMorphemeLayer, index);
                }
            } else {
                throw new LayerNotExistsException("MetaMorphemes");
            }
        } else {
            throw new LayerNotExistsException("MetaMorphemes");
        }
    }

    public String getMetaMorphemeFromIndex(int index) throws LayerItemNotExistsException, LayerNotExistsException {
        if (layers.containsKey(ViewLayerType.META_MORPHEME)){
            if (layers.get(ViewLayerType.META_MORPHEME) instanceof MetaMorphemeLayer){
                MetaMorphemeLayer metaMorphemeLayer = (MetaMorphemeLayer) layers.get(ViewLayerType.META_MORPHEME);
                if (index < metaMorphemeLayer.getLayerSize(ViewLayerType.META_MORPHEME) && index >= 0){
                    return metaMorphemeLayer.getLayerInfoFrom(index);
                } else {
                    throw new LayerItemNotExistsException(metaMorphemeLayer, index);
                }
            } else {
                throw new LayerNotExistsException("MetaMorphemes");
            }
        } else {
            throw new LayerNotExistsException("MetaMorphemes");
        }
    }

    public int getLayerSize(ViewLayerType viewLayer){
        if (layers.get(viewLayer) instanceof MultiWordMultiItemLayer){
            return ((MultiWordMultiItemLayer) layers.get(viewLayer)).getLayerSize(viewLayer);
        } else {
            if (layers.get(viewLayer) instanceof SingleWordMultiItemLayer){
                return ((SingleWordMultiItemLayer) layers.get(viewLayer)).getLayerSize(viewLayer);
            }
        }
        return 0;
    }

    public String getLayerInfoAt(ViewLayerType viewLayer, int index) throws LayerNotExistsException, LayerItemNotExistsException, WordNotExistsException {
        switch (viewLayer){
            case META_MORPHEME_MOVED:
            case PART_OF_SPEECH:
            case INFLECTIONAL_GROUP:
                if (layers.get(viewLayer) instanceof MultiWordMultiItemLayer){
                    return ((MultiWordMultiItemLayer) layers.get(viewLayer)).getLayerInfoAt(viewLayer, index);
                } else {
                    throw new LayerNotExistsException(viewLayer.toString());
                }
            case META_MORPHEME:
                return getMetaMorphemeAtIndex(index);
            case ENGLISH_PROPBANK:
                return getArgumentAt(index).getArgumentType();
            default:
                return null;
        }
    }

    public String getLayerDescription(){
        String result = "";
        for (ViewLayerType viewLayerType : layers.keySet()){
            if (viewLayerType != ViewLayerType.PART_OF_SPEECH){
                result = result + layers.get(viewLayerType).getLayerDescription();
            }
        }
        return result;
    }

    public String getLayerData(ViewLayerType viewLayer){
        if (layers.containsKey(viewLayer)){
            return layers.get(viewLayer).getLayerValue();
        } else {
            return null;
        }
    }

    public String getRobustLayerData(ViewLayerType viewLayer){
        viewLayer = checkLayer(viewLayer);
        return getLayerData(viewLayer);
    }

    private void updateMetaMorphemesMoved() throws LayerNotExistsException, WordNotExistsException {
        if (layers.containsKey(ViewLayerType.META_MORPHEME)){
            MetaMorphemeLayer metaMorphemeLayer = (MetaMorphemeLayer) layers.get(ViewLayerType.META_MORPHEME);
            if (metaMorphemeLayer.size() > 0){
                String result = metaMorphemeLayer.getItemAt(0).toString();
                for (int i = 1; i < metaMorphemeLayer.size(); i++){
                    result = result + " " + metaMorphemeLayer.getItemAt(i).toString();
                }
                layers.put(ViewLayerType.META_MORPHEME_MOVED, new MetaMorphemesMovedLayer(result));
            } else {
                throw new WordNotExistsException(metaMorphemeLayer, 0);
            }
        } else {
            throw new LayerNotExistsException("MetaMorphemes");
        }
    }

    public void removeLayer(ViewLayerType layerType){
        layers.remove(layerType);
    }

    public void metaMorphemeClear(){
        layers.remove(ViewLayerType.META_MORPHEME);
        layers.remove(ViewLayerType.META_MORPHEME_MOVED);
    }

    public void englishClear(){
        layers.remove(ViewLayerType.ENGLISH_WORD);
    }

    public void dependencyClear(){
        layers.remove(ViewLayerType.DEPENDENCY);
    }

    public void metaMorphemesMovedClear(){
        layers.remove(ViewLayerType.META_MORPHEME_MOVED);
    }

    public void semanticClear(){
        layers.remove(ViewLayerType.SEMANTICS);
    }

    public void englishSemanticClear(){
        layers.remove(ViewLayerType.ENGLISH_SEMANTICS);
    }

    public void morphologicalAnalysisClear(){
        layers.remove(ViewLayerType.INFLECTIONAL_GROUP);
        layers.remove(ViewLayerType.PART_OF_SPEECH);
        layers.remove(ViewLayerType.META_MORPHEME);
        layers.remove(ViewLayerType.META_MORPHEME_MOVED);
    }

    public MetamorphicParse metaMorphemeRemove(int index) throws LayerNotExistsException, WordNotExistsException, LayerItemNotExistsException {
        MetamorphicParse removedParse;
        if (layers.containsKey(ViewLayerType.META_MORPHEME)){
            MetaMorphemeLayer metaMorphemeLayer = (MetaMorphemeLayer) layers.get(ViewLayerType.META_MORPHEME);
            if (index >= 0 && index < metaMorphemeLayer.getLayerSize(ViewLayerType.META_MORPHEME)){
                removedParse = metaMorphemeLayer.metaMorphemeRemoveFromIndex(index);
                updateMetaMorphemesMoved();
            } else {
                throw new LayerItemNotExistsException(metaMorphemeLayer, index);
            }
        } else {
            throw new LayerNotExistsException("MetaMorphemes");
        }
        return removedParse;
    }

    public boolean isVerbal(){
        if (layers.get(ViewLayerType.INFLECTIONAL_GROUP) != null){
            return ((MorphologicalAnalysisLayer) layers.get(ViewLayerType.INFLECTIONAL_GROUP)).isVerbal();
        } else {
            return false;
        }
    }

    public boolean isNominal(){
        if (layers.get(ViewLayerType.INFLECTIONAL_GROUP) != null){
            return ((MorphologicalAnalysisLayer) layers.get(ViewLayerType.INFLECTIONAL_GROUP)).isNominal();
        } else {
            return false;
        }
    }

    public AnnotatedWord toAnnotatedWord(int wordIndex) throws LayerNotExistsException {
        try{
            AnnotatedWord annotatedWord = new AnnotatedWord(getTurkishWordAt(wordIndex));
            if (layerExists(ViewLayerType.INFLECTIONAL_GROUP)){
                annotatedWord.setParse(getMorphologicalParseAt(wordIndex).toString());
            }
            if (layerExists(ViewLayerType.META_MORPHEME)){
                annotatedWord.setMetamorphicParse(getMetamorphicParseAt(wordIndex).toString());
            }
            if (layerExists(ViewLayerType.SEMANTICS)){
                annotatedWord.setSemantic(getSemanticAt(wordIndex));
            }
            if (layerExists(ViewLayerType.NER)){
                annotatedWord.setNamedEntityType(getLayerData(ViewLayerType.NER));
            }
            if (layerExists(ViewLayerType.PROPBANK)){
                annotatedWord.setArgument(getArgument().toString());
            }
            if (layerExists(ViewLayerType.SHALLOW_PARSE)){
                annotatedWord.setShallowParse(getShallowParseAt(wordIndex));
            }
            return annotatedWord;
        } catch (WordNotExistsException e) {
            e.printStackTrace();
        }
        return null;
    }

}
