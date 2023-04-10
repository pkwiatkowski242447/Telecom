package tips.zadanie3.tips_zadanie3.model;

public class Node implements Comparable<Node> {

    private final short character;
    private final int importanceValue;
    private Node parentNode;
    private Node leftChildNode;
    private Node rightChildNode;

    /*
        @ Constructors:
     */

    public Node(short character, int importanceValue) {
        this.character = character;
        this.importanceValue = importanceValue;
    }

    public Node(short character, int importanceValue, Node leftChildNode, Node rightChildNode) {
        this.character = character;
        this.importanceValue = importanceValue;
        this.leftChildNode = leftChildNode;
        this.rightChildNode = rightChildNode;
    }

    /*
        @ Getters:
     */

    public Node getParentNode() {
        return parentNode;
    }

    public Node getLeftChildNode() {
        return leftChildNode;
    }

    public Node getRightChildNode() {
        return rightChildNode;
    }

    public short getCharacter() {
        return character;
    }

    public int getImportanceValue() {
        return importanceValue;
    }

    /*
        @ Setters:
     */

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public boolean isLeaf() {
        return this.getLeftChildNode() == null && this.getRightChildNode() == null;
    }

    public boolean isRootNode() {
        return this.getParentNode() == null;
    }

    @Override
    public int compareTo(Node otherNode) {
        return this.getImportanceValue() - otherNode.getImportanceValue();
    }
}
