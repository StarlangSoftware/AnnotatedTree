package AnnotatedTree.Util;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.Processor.LayerExist.ContainsLayerInformation;
import AnnotatedTree.Processor.Condition.IsLeafNode;
import AnnotatedTree.Processor.NodeDrawableCollector;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class DoneFileFilter extends FileFilter{

    private final String path;
    private final ViewLayerType viewLayerType;

    /**
     * Constructor for DoneFileFilter. DoneFileFilter is used to filter out the files whose leaf nodes are not annotated
     * at all, or some of them are annotated. The files shown to the user are those that are fully annotated with the
     * given layer info.
     * @param path Path of the files.
     * @param viewLayerType Name of the layer for which annotation check is done.
     */
    public DoneFileFilter(String path, ViewLayerType viewLayerType){
        this.path = path;
        this.viewLayerType = viewLayerType;
    }

    /**
     * Checks if the file satisfies the condition of the filter.
     * @param f File to be checked.
     * @return True if the file satisfies the condition of the filter, false otherwise.
     */
    public boolean accept(File f) {
        File file = new File(path + f.getName());
        try {
            ParseTreeDrawable tree = new ParseTreeDrawable(new FileInputStream(file));
            NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) tree.getRoot(), new IsLeafNode());
            ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
            return new ContainsLayerInformation(viewLayerType).satisfies(leafList);
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    /**
     * Returns "Done".
     * @return "Done".
     */
    public String getDescription() {
        return "Done";
    }
}
