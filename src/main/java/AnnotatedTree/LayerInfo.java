package AnnotatedTree;

import AnnotatedSentence.AnnotatedWord;
import AnnotatedSentence.LayerNotExistsException;
import AnnotatedSentence.ViewLayerType;
import MorphologicalAnalysis.MorphologicalParse;
import MorphologicalAnalysis.MetamorphicParse;
import AnnotatedTree.Layer.*;
import PropBank.Argument;

import java.util.ArrayList;
import java.util.EnumMap;

public class LayerInfo {
    private final EnumMap<ViewLayerType, WordLayer> layers;

    /**
     * Constructs the layer information from the given string. Layers are represented as
     * {layername1=layervalue1}{layername2=layervalue2}...{layernamek=layervaluek} where layer name is one of the
     * following: turkish, persian, english, morphologicalAnalysis, metaMorphemes, metaMorphemesMoved, dependency,
     * semantics, namedEntity, propBank, englishPropbank, englishSemantics, shallowParse. Splits the string w.r.t.
     * parentheses and constructs layer objects and put them layers map accordingly.
     * @param info Line consisting of layer info.
     */
    public LayerInfo(String info) {
        String[] splitLayers = info.split("[{}]");
        layers = new EnumMap<>(ViewLayerType.class);
        for (String layer : splitLayers) {
            if (layer.isEmpty())
                continue;
            String layerType = layer.substring(0, layer.indexOf("="));
            String layerValue = layer.substring(layer.indexOf("=") + 1);
            if (layerType.equalsIgnoreCase("turkish")) {
                layers.put(ViewLayerType.TURKISH_WORD, new TurkishWordLayer(layerValue));
            } else {
                if (layerType.equalsIgnoreCase("persian")) {
                    layers.put(ViewLayerType.PERSIAN_WORD, new PersianWordLayer(layerValue));
                } else {
                    if (layerType.equalsIgnoreCase("english")) {
                        layers.put(ViewLayerType.ENGLISH_WORD, new EnglishWordLayer(layerValue));
                    } else {
                        if (layerType.equalsIgnoreCase("morphologicalAnalysis")) {
                            layers.put(ViewLayerType.INFLECTIONAL_GROUP, new MorphologicalAnalysisLayer(layerValue));
                            layers.put(ViewLayerType.PART_OF_SPEECH, new MorphologicalAnalysisLayer(layerValue));
                        } else {
                            if (layerType.equalsIgnoreCase("metaMorphemes")) {
                                layers.put(ViewLayerType.META_MORPHEME, new MetaMorphemeLayer(layerValue));
                            } else {
                                if (layerType.equalsIgnoreCase("metaMorphemesMoved")) {
                                    layers.put(ViewLayerType.META_MORPHEME_MOVED, new MetaMorphemesMovedLayer(layerValue));
                                } else {
                                    if (layerType.equalsIgnoreCase("dependency")) {
                                        layers.put(ViewLayerType.DEPENDENCY, new DependencyLayer(layerValue));
                                    } else {
                                        if (layerType.equalsIgnoreCase("semantics")) {
                                            layers.put(ViewLayerType.SEMANTICS, new TurkishSemanticLayer(layerValue));
                                        } else {
                                            if (layerType.equalsIgnoreCase("namedEntity")) {
                                                layers.put(ViewLayerType.NER, new NERLayer(layerValue));
                                            } else {
                                                if (layerType.equalsIgnoreCase("propBank")) {
                                                    layers.put(ViewLayerType.PROPBANK, new TurkishPropbankLayer(layerValue));
                                                } else {
                                                    if (layerType.equalsIgnoreCase("englishPropbank")) {
                                                        layers.put(ViewLayerType.ENGLISH_PROPBANK, new EnglishPropbankLayer(layerValue));
                                                    } else {
                                                        if (layerType.equalsIgnoreCase("englishSemantics")) {
                                                            layers.put(ViewLayerType.ENGLISH_SEMANTICS, new EnglishSemanticLayer(layerValue));
                                                        } else {
                                                            if (layerType.equalsIgnoreCase("shallowParse")) {
                                                                layers.put(ViewLayerType.SHALLOW_PARSE, new ShallowParseLayer(layerValue));
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
    }

    /**
     * Empty constructor. Constructs empty map.
     */
    public LayerInfo() {
        layers = new EnumMap<>(ViewLayerType.class);
    }

    public LayerInfo clone() {
        return new LayerInfo(getLayerDescription());
    }

    /**
     * Changes the given layer info with the given string layer value. For all layers new layer object is created and
     * replaces the original object. For turkish layer, it also destroys inflectional_group, part_of_speech,
     * meta_morpheme, meta_morpheme_moved and semantics layers. For persian layer, it also destroys the semantics layer.
     * @param viewLayer Layer name.
     * @param layerValue New layer value.
     */
    public void setLayerData(ViewLayerType viewLayer, String layerValue) {
        switch (viewLayer) {
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

    /**
     * Updates the inflectional_group and part_of_speech layers according to the given parse.
     * @param parse New parse to update layers.
     */
    public void setMorphologicalAnalysis(MorphologicalParse parse) {
        layers.put(ViewLayerType.INFLECTIONAL_GROUP, new MorphologicalAnalysisLayer(parse.toString()));
        layers.put(ViewLayerType.PART_OF_SPEECH, new MorphologicalAnalysisLayer(parse.toString()));
    }

    /**
     * Updates the metamorpheme layer according to the given parse.
     * @param parse New parse to update layer.
     */
    public void setMetaMorphemes(MetamorphicParse parse) {
        layers.put(ViewLayerType.META_MORPHEME, new MetaMorphemeLayer(parse.toString()));
    }

    /**
     * Checks if the given layer exists.
     * @param viewLayerType Layer name
     * @return True if the layer exists, false otherwise.
     */
    public boolean layerExists(ViewLayerType viewLayerType) {
        return layers.containsKey(viewLayerType);
    }

    /**
     * Two level layer check method. For turkish, persian and english_semantics layers, if the layer does not exist,
     * returns english layer. For part_of_speech, inflectional_group, meta_morpheme, semantics, propbank, shallow_parse,
     * english_propbank layers, if the layer does not exist, it checks turkish layer. For meta_morpheme_moved, if the
     * layer does not exist, it checks meta_morpheme layer.
     * @param viewLayer Layer to be checked.
     * @return Returns the original layer if the layer exists. For turkish, persian and english_semantics layers, if the
     * layer  does not exist, returns english layer. For part_of_speech, inflectional_group, meta_morpheme, semantics,
     * propbank,  shallow_parse, english_propbank layers, if the layer does not exist, it checks turkish layer
     * recursively. For meta_morpheme_moved, if the layer does not exist, it checks meta_morpheme layer recursively.
     */
    public ViewLayerType checkLayer(ViewLayerType viewLayer) {
        switch (viewLayer) {
            case TURKISH_WORD:
            case PERSIAN_WORD:
            case ENGLISH_SEMANTICS:
                if (!layers.containsKey(viewLayer)) {
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

    /**
     * Returns number of words in the Turkish or Persian layer, whichever exists.
     * @return Number of words in the Turkish or Persian layer, whichever exists.
     */
    public int getNumberOfWords() throws LayerNotExistsException {
        if (layers.containsKey(ViewLayerType.TURKISH_WORD)) {
            return ((TurkishWordLayer) layers.get(ViewLayerType.TURKISH_WORD)).size();
        } else {
            if (layers.containsKey(ViewLayerType.PERSIAN_WORD)) {
                return ((PersianWordLayer) layers.get(ViewLayerType.PERSIAN_WORD)).size();
            } else {
                throw new LayerNotExistsException("Turkish");
            }
        }
    }

    /**
     * Returns the layer value at the given index.
     * @param viewLayerType Layer for which the value at the given word index will be returned.
     * @param index Word Position of the layer value.
     * @param layerName Name of the layer.
     * @throws LayerNotExistsException If the layer does not exist, it throws LayerNotExistsException. If the layer is
     * not a MultiWordLayer, it throws LayerNotExistsException exception.
     * @throws WordNotExistsException If the index is out of bounds, it throws WordNotExistsException.
     * @return Layer info at word position index for a multiword layer.
     */
    private String getMultiWordAt(ViewLayerType viewLayerType, int index, String layerName) throws WordNotExistsException, LayerNotExistsException {
        if (layers.containsKey(viewLayerType)) {
            if (layers.get(viewLayerType) instanceof MultiWordLayer) {
                MultiWordLayer<String> multiWordLayer = (MultiWordLayer<String>) layers.get(viewLayerType);
                if (index < multiWordLayer.size() && index >= 0) {
                    return multiWordLayer.getItemAt(index);
                } else {
                    if (viewLayerType.equals(ViewLayerType.SEMANTICS)) {
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

    /**
     * Layers may contain multiple Turkish words. This method returns the Turkish word at position index.
     * @param index Position of the Turkish word.
     * @throws LayerNotExistsException If the layer does not exist, it throws LayerNotExistsException.
     * @throws WordNotExistsException If the index is out of bounds, it throws WordNotExistsException.
     * @return The Turkish word at position index.
     */
    public String getTurkishWordAt(int index) throws LayerNotExistsException, WordNotExistsException {
        return getMultiWordAt(ViewLayerType.TURKISH_WORD, index, "turkish");
    }

    /**
     * Returns number of meanings in the Turkish layer.
     * @return Number of meanings in the Turkish layer.
     */
    public int getNumberOfMeanings() {
        if (layers.containsKey(ViewLayerType.SEMANTICS)) {
            return ((TurkishSemanticLayer) layers.get(ViewLayerType.SEMANTICS)).size();
        } else {
            return 0;
        }
    }

    /**
     * Layers may contain multiple semantic information corresponding to multiple Turkish words. This method returns
     * the sense id at position index.
     * @param index Position of the Turkish word.
     * @throws LayerNotExistsException If the layer does not exist, it throws LayerNotExistsException.
     * @throws WordNotExistsException If the index is out of bounds, it throws WordNotExistsException.
     * @return The Turkish sense id at position index.
     */
    public String getSemanticAt(int index) throws LayerNotExistsException, WordNotExistsException {
        return getMultiWordAt(ViewLayerType.SEMANTICS, index, "semantics");
    }

    /**
     * Layers may contain multiple shallow parse information corresponding to multiple Turkish words. This method
     * returns the shallow parse tag at position index.
     * @param index Position of the Turkish word.
     * @throws LayerNotExistsException If the layer does not exist, it throws LayerNotExistsException.
     * @throws WordNotExistsException If the index is out of bounds, it throws WordNotExistsException.
     * @return The shallow parse tag at position index.
     */
    public String getShallowParseAt(int index) throws LayerNotExistsException, WordNotExistsException {
        return getMultiWordAt(ViewLayerType.SHALLOW_PARSE, index, "shallowParse");
    }

    /**
     * Returns the Turkish PropBank argument info.
     * @return Turkish PropBank argument info.
     */
    public Argument getArgument() {
        if (layers.containsKey(ViewLayerType.PROPBANK)) {
            if (layers.get(ViewLayerType.PROPBANK) instanceof TurkishPropbankLayer) {
                TurkishPropbankLayer argumentLayer = (TurkishPropbankLayer) layers.get(ViewLayerType.PROPBANK);
                return argumentLayer.getArgument();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * A word may have multiple English propbank info. This method returns the English PropBank argument info at
     * position index.
     * @param index Position of the English argument.
     * @return English PropBank argument info at position index.
     */
    public Argument getArgumentAt(int index) throws LayerNotExistsException {
        if (layers.containsKey(ViewLayerType.ENGLISH_PROPBANK)) {
            if (layers.get(ViewLayerType.ENGLISH_PROPBANK) instanceof SingleWordMultiItemLayer) {
                SingleWordMultiItemLayer<Argument> multiArgumentLayer = (SingleWordMultiItemLayer<Argument>) layers.get(ViewLayerType.ENGLISH_PROPBANK);
                return multiArgumentLayer.getItemAt(index);
            } else {
                throw new LayerNotExistsException("EnglishPropbank");
            }
        } else {
            throw new LayerNotExistsException("EnglishPropbank");
        }
    }

    /**
     * Layers may contain multiple morphological parse information corresponding to multiple Turkish words. This method
     * returns the morphological parse at position index.
     * @param index Position of the Turkish word.
     * @throws LayerNotExistsException If the layer does not exist, it throws LayerNotExistsException.
     * @throws WordNotExistsException If the index is out of bounds, it throws WordNotExistsException.
     * @return The morphological parse at position index.
     */
    public MorphologicalParse getMorphologicalParseAt(int index) throws LayerNotExistsException, WordNotExistsException {
        if (layers.containsKey(ViewLayerType.INFLECTIONAL_GROUP)) {
            if (layers.get(ViewLayerType.INFLECTIONAL_GROUP) instanceof MultiWordLayer) {
                MultiWordLayer<MorphologicalParse> multiWordLayer = (MultiWordLayer<MorphologicalParse>) layers.get(ViewLayerType.INFLECTIONAL_GROUP);
                if (index < multiWordLayer.size() && index >= 0) {
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

    /**
     * Layers may contain multiple metamorphic parse information corresponding to multiple Turkish words. This method
     * returns the metamorphic parse at position index.
     * @param index Position of the Turkish word.
     * @throws LayerNotExistsException If the layer does not exist, it throws LayerNotExistsException.
     * @throws WordNotExistsException If the index is out of bounds, it throws WordNotExistsException.
     * @return The metamorphic parse at position index.
     */
    public MetamorphicParse getMetamorphicParseAt(int index) throws WordNotExistsException, LayerNotExistsException {
        if (layers.containsKey(ViewLayerType.META_MORPHEME)) {
            if (layers.get(ViewLayerType.META_MORPHEME) instanceof MultiWordLayer) {
                MultiWordLayer<MetamorphicParse> multiWordLayer = (MultiWordLayer<MetamorphicParse>) layers.get(ViewLayerType.META_MORPHEME);
                if (index < multiWordLayer.size() && index >= 0) {
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

    /**
     * Layers may contain multiple metamorphemes corresponding to one or multiple Turkish words. This method
     * returns the metamorpheme at position index.
     * @param index Position of the metamorpheme.
     * @throws LayerNotExistsException If the layer does not exist, it throws LayerNotExistsException.
     * @throws LayerItemNotExistsException If the index is out of bounds, it throws LayerItemNotExistsException.
     * @return The metamorpheme at position index.
     */
    public String getMetaMorphemeAtIndex(int index) throws LayerItemNotExistsException, LayerNotExistsException {
        if (layers.containsKey(ViewLayerType.META_MORPHEME)) {
            if (layers.get(ViewLayerType.META_MORPHEME) instanceof MetaMorphemeLayer) {
                MetaMorphemeLayer metaMorphemeLayer = (MetaMorphemeLayer) layers.get(ViewLayerType.META_MORPHEME);
                if (index < metaMorphemeLayer.getLayerSize(ViewLayerType.META_MORPHEME) && index >= 0) {
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

    /**
     * Layers may contain multiple metamorphemes corresponding to one or multiple Turkish words. This method
     * returns all metamorphemes from position index.
     * @param index Start position of the metamorpheme.
     * @throws LayerNotExistsException If the layer does not exist, it throws LayerNotExistsException.
     * @throws LayerItemNotExistsException If the index is out of bounds, it throws LayerItemNotExistsException.
     * @return All metamorphemes from position index.
     */
    public String getMetaMorphemeFromIndex(int index) throws LayerItemNotExistsException, LayerNotExistsException {
        if (layers.containsKey(ViewLayerType.META_MORPHEME)) {
            if (layers.get(ViewLayerType.META_MORPHEME) instanceof MetaMorphemeLayer) {
                MetaMorphemeLayer metaMorphemeLayer = (MetaMorphemeLayer) layers.get(ViewLayerType.META_MORPHEME);
                if (index < metaMorphemeLayer.getLayerSize(ViewLayerType.META_MORPHEME) && index >= 0) {
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

    /**
     * For layers with multiple item information, this method returns total items in that layer.
     * @param viewLayer Layer name
     * @return Total items in the given layer.
     */
    public int getLayerSize(ViewLayerType viewLayer) {
        if (layers.get(viewLayer) instanceof MultiWordMultiItemLayer) {
            return ((MultiWordMultiItemLayer) layers.get(viewLayer)).getLayerSize(viewLayer);
        } else {
            if (layers.get(viewLayer) instanceof SingleWordMultiItemLayer) {
                return ((SingleWordMultiItemLayer) layers.get(viewLayer)).getLayerSize(viewLayer);
            }
        }
        return 0;
    }

    /**
     * For layers with multiple item information, this method returns the item at position index.
     * @param viewLayer Layer name
     * @param index Position of the item.
     * @return The item at position index.
     * @throws LayerNotExistsException If the layer does not exist, it throws LayerNotExistsException.
     * @throws LayerItemNotExistsException If the index is out of bounds, it throws LayerItemNotExistsException.
     */
    public String getLayerInfoAt(ViewLayerType viewLayer, int index) throws LayerNotExistsException, LayerItemNotExistsException {
        switch (viewLayer) {
            case META_MORPHEME_MOVED:
            case PART_OF_SPEECH:
            case INFLECTIONAL_GROUP:
                if (layers.get(viewLayer) instanceof MultiWordMultiItemLayer) {
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

    /**
     * Returns the string form of all layer information except part_of_speech layer.
     * @return The string form of all layer information except part_of_speech layer.
     */
    public String getLayerDescription() {
        StringBuilder result = new StringBuilder();
        for (ViewLayerType viewLayerType : layers.keySet()) {
            if (viewLayerType != ViewLayerType.PART_OF_SPEECH) {
                result.append(layers.get(viewLayerType).getLayerDescription());
            }
        }
        return result.toString();
    }

    /**
     * Returns the layer info for the given layer.
     * @param viewLayer Layer name.
     * @return Layer info for the given layer.
     */
    public String getLayerData(ViewLayerType viewLayer) {
        if (layers.containsKey(viewLayer)) {
            return layers.get(viewLayer).getLayerValue();
        } else {
            return null;
        }
    }

    /**
     * Returns the layer info for the given layer, if that layer exists. Otherwise, it returns the fallback layer info
     * determined by the checkLayer.
     * @param viewLayer Layer name
     * @return Layer info for the given layer if it exists. Otherwise, it returns the fallback layer info determined by
     * the checkLayer.
     */
    public String getRobustLayerData(ViewLayerType viewLayer) {
        viewLayer = checkLayer(viewLayer);
        return getLayerData(viewLayer);
    }

    /**
     * Initializes the metamorphemesmoved layer with metamorpheme layer except the root word.
     * @throws LayerNotExistsException If the layer does not exist, it throws LayerNotExistsException.
     * @throws WordNotExistsException If the root word does not exist, it throws WordNotExistsException.
     */
    private void updateMetaMorphemesMoved() throws LayerNotExistsException, WordNotExistsException {
        if (layers.containsKey(ViewLayerType.META_MORPHEME)) {
            MetaMorphemeLayer metaMorphemeLayer = (MetaMorphemeLayer) layers.get(ViewLayerType.META_MORPHEME);
            if (metaMorphemeLayer.size() > 0) {
                StringBuilder result = new StringBuilder(metaMorphemeLayer.getItemAt(0).toString());
                for (int i = 1; i < metaMorphemeLayer.size(); i++) {
                    result.append(" ").append(metaMorphemeLayer.getItemAt(i).toString());
                }
                layers.put(ViewLayerType.META_MORPHEME_MOVED, new MetaMorphemesMovedLayer(result.toString()));
            } else {
                throw new WordNotExistsException(metaMorphemeLayer, 0);
            }
        } else {
            throw new LayerNotExistsException("MetaMorphemes");
        }
    }

    /**
     * Removes the given layer from hash map.
     * @param layerType Layer to be removed.
     */
    public void removeLayer(ViewLayerType layerType) {
        layers.remove(layerType);
    }

    /**
     * Removes metamorpheme and metamorphemesmoved layers.
     */
    public void metaMorphemeClear() {
        layers.remove(ViewLayerType.META_MORPHEME);
        layers.remove(ViewLayerType.META_MORPHEME_MOVED);
    }

    /**
     * Removes English layer.
     */
    public void englishClear() {
        layers.remove(ViewLayerType.ENGLISH_WORD);
    }

    /**
     * Removes the dependency layer.
     */
    public void dependencyClear() {
        layers.remove(ViewLayerType.DEPENDENCY);
    }

    /**
     * Removes metamorphemesmoved layer.
     */
    public void metaMorphemesMovedClear() {
        layers.remove(ViewLayerType.META_MORPHEME_MOVED);
    }

    /**
     * Removes the Turkish semantic layer.
     */
    public void semanticClear() {
        layers.remove(ViewLayerType.SEMANTICS);
    }

    /**
     * Removes the English semantic layer.
     */
    public void englishSemanticClear() {
        layers.remove(ViewLayerType.ENGLISH_SEMANTICS);
    }

    /**
     * Removes the morphological analysis, part of speech, metamorpheme, and metamorphemesmoved layers.
     */
    public void morphologicalAnalysisClear() {
        layers.remove(ViewLayerType.INFLECTIONAL_GROUP);
        layers.remove(ViewLayerType.PART_OF_SPEECH);
        layers.remove(ViewLayerType.META_MORPHEME);
        layers.remove(ViewLayerType.META_MORPHEME_MOVED);
    }

    /**
     * Removes the metamorpheme at position index.
     * @param index Position of the metamorpheme to be removed.
     * @return Metamorphemes concatenated as a string after the removed metamorpheme.
     * @throws LayerNotExistsException If the layer does not exist, it throws LayerNotExistsException.
     * @throws WordNotExistsException If the root word does not exist, it throws WordNotExistsException.
     * @throws LayerItemNotExistsException If the index is out of bounds, it throws LayerItemNotExistsException.
     */
    public MetamorphicParse metaMorphemeRemove(int index) throws LayerNotExistsException, WordNotExistsException, LayerItemNotExistsException {
        MetamorphicParse removedParse;
        if (layers.containsKey(ViewLayerType.META_MORPHEME)) {
            MetaMorphemeLayer metaMorphemeLayer = (MetaMorphemeLayer) layers.get(ViewLayerType.META_MORPHEME);
            if (index >= 0 && index < metaMorphemeLayer.getLayerSize(ViewLayerType.META_MORPHEME)) {
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

    /**
     * Checks if the last inflectional group contains VERB tag.
     * @return True if the last inflectional group contains VERB tag, false otherwise.
     */
    public boolean isVerbal() {
        if (layers.containsKey(ViewLayerType.INFLECTIONAL_GROUP)) {
            return ((MorphologicalAnalysisLayer) layers.get(ViewLayerType.INFLECTIONAL_GROUP)).isVerbal();
        } else {
            return false;
        }
    }

    /**
     * Checks if the last verbal inflectional group contains ZERO tag.
     * @return True if the last verbal inflectional group contains ZERO tag, false otherwise.
     */
    public boolean isNominal() {
        if (layers.containsKey(ViewLayerType.INFLECTIONAL_GROUP)) {
            return ((MorphologicalAnalysisLayer) layers.get(ViewLayerType.INFLECTIONAL_GROUP)).isNominal();
        } else {
            return false;
        }
    }

    /**
     * Creates an array list of LayerInfo objects, where each object correspond to one word in the tree node. Turkish
     * words, morphological parses, metamorpheme parses, semantic senses, shallow parses are divided into corresponding
     * words. Named entity tags and propbank arguments are the same for all words.
     * @return An array list of LayerInfo objects created from the layer info of the node.
     * @throws LayerNotExistsException If the layer does not exist, it throws LayerNotExistsException.
     */
    public ArrayList<LayerInfo> divideIntoWords() throws LayerNotExistsException {
        ArrayList<LayerInfo> result = new ArrayList<>();
        for (int i = 0; i < getNumberOfWords(); i++) {
            try {
                LayerInfo layerInfo = new LayerInfo();
                layerInfo.setLayerData(ViewLayerType.TURKISH_WORD, getTurkishWordAt(i));
                layerInfo.setLayerData(ViewLayerType.ENGLISH_WORD, getLayerData(ViewLayerType.ENGLISH_WORD));
                if (layerExists(ViewLayerType.INFLECTIONAL_GROUP)) {
                    layerInfo.setMorphologicalAnalysis(getMorphologicalParseAt(i));
                }
                if (layerExists(ViewLayerType.META_MORPHEME)) {
                    layerInfo.setMetaMorphemes(getMetamorphicParseAt(i));
                }
                if (layerExists(ViewLayerType.ENGLISH_PROPBANK)) {
                    layerInfo.setLayerData(ViewLayerType.ENGLISH_PROPBANK, getLayerData(ViewLayerType.ENGLISH_PROPBANK));
                }
                if (layerExists(ViewLayerType.ENGLISH_SEMANTICS)) {
                    layerInfo.setLayerData(ViewLayerType.ENGLISH_SEMANTICS, getLayerData(ViewLayerType.ENGLISH_SEMANTICS));
                }
                if (layerExists(ViewLayerType.NER)) {
                    layerInfo.setLayerData(ViewLayerType.NER, getLayerData(ViewLayerType.NER));
                }
                if (layerExists(ViewLayerType.SEMANTICS)) {
                    layerInfo.setLayerData(ViewLayerType.SEMANTICS, getSemanticAt(i));
                }
                if (layerExists(ViewLayerType.PROPBANK)) {
                    layerInfo.setLayerData(ViewLayerType.PROPBANK, getArgument().toString());
                }
                if (layerExists(ViewLayerType.SHALLOW_PARSE)) {
                    layerInfo.setLayerData(ViewLayerType.SHALLOW_PARSE, getShallowParseAt(i));
                }
                result.add(layerInfo);
            } catch (WordNotExistsException ignored) {
            }
        }
        return result;
    }

    /**
     * Converts layer info of the word at position wordIndex to an AnnotatedWord. Layers are converted to their
     * counterparts in the AnnotatedWord.
     * @param wordIndex Index of the word to be converted.
     * @return Converted annotatedWord
     * @throws LayerNotExistsException If the layer does not exist, it throws LayerNotExistsException.
     */
    public AnnotatedWord toAnnotatedWord(int wordIndex) throws LayerNotExistsException {
        try {
            AnnotatedWord annotatedWord = new AnnotatedWord(getTurkishWordAt(wordIndex));
            if (layerExists(ViewLayerType.INFLECTIONAL_GROUP)) {
                annotatedWord.setParse(getMorphologicalParseAt(wordIndex).toString());
            }
            if (layerExists(ViewLayerType.META_MORPHEME)) {
                annotatedWord.setMetamorphicParse(getMetamorphicParseAt(wordIndex).toString());
            }
            if (layerExists(ViewLayerType.SEMANTICS)) {
                annotatedWord.setSemantic(getSemanticAt(wordIndex));
            }
            if (layerExists(ViewLayerType.NER)) {
                annotatedWord.setNamedEntityType(getLayerData(ViewLayerType.NER));
            }
            if (layerExists(ViewLayerType.PROPBANK)) {
                annotatedWord.setArgument(getArgument().toString());
            }
            if (layerExists(ViewLayerType.SHALLOW_PARSE)) {
                annotatedWord.setShallowParse(getShallowParseAt(wordIndex));
            }
            return annotatedWord;
        } catch (WordNotExistsException ignored) {
        }
        return null;
    }

}
