import com.gaurav.tree.ArrayListTree;
import com.gaurav.tree.Tree;
import org.jsoup.nodes.Node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: grainierp
 * Date: 9/18/13
 * Time: 8:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class HTMLDom implements Serializable {
    private String identifier;
    private Tree<Node> rootDom;
    private ArrayList<Tree<Node>> iframes;

    HTMLDom() {
        this.setRootDom(new ArrayListTree<Node>());
        this.setIframes(new ArrayList<Tree<Node>>());
    }

    public Boolean hasIframes() {
        return this.getIframes().size() > 0;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Tree<Node> getRootDom() {
        return rootDom;
    }

    public void setRootDom(Tree<Node> rootDom) {
        this.rootDom = rootDom;
    }

    public ArrayList<Tree<Node>> getIframes() {
        return iframes;
    }

    public void setIframes(ArrayList<Tree<Node>> iframes) {
        this.iframes = iframes;
    }

    @Override
    public boolean equals(HTMLDom htmlDom) {
        //return super.equals(htmlDom);    //To change body of overridden methods use File | Settings | File Templates.
        if (this.rootDom.levelOrderTraversal().size() == htmlDom.rootDom.levelOrderTraversal().size()) {
            if (compareNodeCollection(this.rootDom.leaves(), htmlDom.rootDom.leaves())) {

            }
        }
    }

    private boolean compareNodeCollection(Collection<Node> a, Collection<Node> b) {
        ArrayList<Node> nodesA = new ArrayList<Node>(a);
        ArrayList<Node> nodesB = new ArrayList<Node>(b);
        if (nodesA.size() == nodesB.size()) {
            for (int i = 0; i < nodesA.size(); i ++) {
                if (nodesA.get(i).hashCode() != nodesB.get(i).hashCode()) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
