import com.gaurav.tree.ArrayListTree;
import com.gaurav.tree.NodeNotFoundException;
import com.gaurav.tree.Tree;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created with IntelliJ IDEA.
 * User: grainierp
 * Date: 9/17/13
 * Time: 9:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class DomDiff {
    public void getDom() throws IOException {
        Document doc = Jsoup.connect("http://localhost/iframe/").get();
        HTMLDom dom = toTree(doc);
        //Elements newsHeadlines = doc.select("#mp-itn b a");

        Tree<String> tree1 = new ArrayListTree<String>();
        Tree<String> tree2 = new ArrayListTree<String>();
        tree1.add("1");
        tree2.add("8");

        try {
            tree1.add("1", "2");
            tree1.add("1", "3");
            tree1.add("2", "4");
            tree1.add("2", "5");
            // tree2.add("2", "4");
            tree2.add("8", "5");
            tree2.add("8", "6");
            tree1.addAll("4", tree2);

        } catch (NodeNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        System.out.println(tree1.levelOrderTraversal().toString());
        System.out.println("this");
    }

    public HTMLDom toTree(Document doc) {
        final HTMLDom dom = new HTMLDom();

        AtomicReference<NodeTraversor> traversorAtomicReference  = new AtomicReference<NodeTraversor>(new NodeTraversor(new NodeVisitor() {
            ArrayList<Node> tempStack = new ArrayList();
            Node tempNode;
            Node currentRoot = null;

            @Override
            public void tail(Node node, int depth) {
                // Callback for when a node is last visited, after all of its descendants have been visited.
                if (!(node instanceof TextNode)) {
                    tempNode = tempStack.remove(tempStack.size() - 1);
                    if (tempStack.size() > 0) {
                        currentRoot = tempStack.get(tempStack.size() - 1);
                        try {
                            dom.getRootDom().add(currentRoot, tempNode);
                        } catch (NodeNotFoundException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                }
            }

            @Override
            public void head(Node node, int depth) {
                // Callback for when a node is first visited.
                if (!(node instanceof TextNode)) {

                    if (node instanceof Element) {
                        Element element = (Element) node;
                        if (element.tagName() == "iframe") {
                            String src = element.attr("src");
                            if (src != null) {
                                try {
                                    Document iframeDoc = Jsoup.connect(src).get();
                                    HTMLDom iframeDom = toTree(iframeDoc);
                                    // add root dom of iframe to parents dom's iframes list
                                    dom.getIframes().add(iframeDom.getRootDom());
                                    if (iframeDom.hasIframes()) {
                                        dom.getIframes().addAll(iframeDom.getIframes());
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                }
                                System.out.println(src);
                            }
                        }
                    }

                    try {
                        dom.getRootDom().add(currentRoot, node);
                        currentRoot = node;
                        tempStack.add(node);
                    } catch (NodeNotFoundException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        }));

        traversorAtomicReference.get().traverse(doc.body());


        // put every tree in to level order


        // testing
        System.out.println("==================================================================");
        System.out.println(dom.getRootDom().leaves().toString());
        System.out.println("==================================================================");
        System.out.println("depth :" + dom.getRootDom().depth() );

        return dom;

    }

    public Boolean containsIframe(Document document) {
        Elements es = document.select("iframe");
        if (es.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
