package com.java.demo.binarytree;

import lombok.Data;

@Data
public class BinaryTree {

    private BinaryTree leftNode;

    private BinaryTree rightNode;

    private Object data;

}
