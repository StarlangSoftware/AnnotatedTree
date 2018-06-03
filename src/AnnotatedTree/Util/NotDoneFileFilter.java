package AnnotatedTree.Util;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.Processor.Condition.IsLeafNode;
import AnnotatedTree.Processor.NodeDrawableCollector;
import AnnotatedTree.Processor.LayerExist.NotContainsLayerInformation;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class NotDoneFileFilter extends FileFilter {

    private String path;
    private ViewLayerType viewLayerType;

    public NotDoneFileFilter(String path, ViewLayerType viewLayerType){
        this.path = path;
        this.viewLayerType = viewLayerType;
    }

    public boolean accept(File f) {
        File file = new File(path + f.getName());
        try {
            ParseTreeDrawable tree = new ParseTreeDrawable(new FileInputStream(file));
            NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) tree.getRoot(), new IsLeafNode());
            ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
            return new NotContainsLayerInformation(viewLayerType).satisfies(leafList);
        } catch (FileNotFoundException e) {
            return true;
        }
    }

    public String getDescription() {
        return "Not Done";
    }
}
