package com.java.demo.binarytree;

import org.junit.Test;

public class Junit {

    @Test
    public void test() {
        BinaryTree tree = new BinaryTree();
        checkNode(tree);
    }

    public boolean checkNode(BinaryTree node) {
        BinaryTree leftNode = node.getLeftNode();
        BinaryTree rightNode = node.getRightNode();

        if(leftNode != null && rightNode != null) {
            boolean b = checkTree(leftNode, rightNode);

            return b;
        }
        else {
            return false;
        }
    }

    public boolean checkTree(BinaryTree leftNode, BinaryTree rightNode) {
        boolean left = checkNode(leftNode);
        boolean right = checkNode(rightNode);

        if(left && right) {
            return true;
        }
        else {
            return false;
        }

    }





}
