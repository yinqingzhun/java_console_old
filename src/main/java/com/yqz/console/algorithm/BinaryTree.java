package com.yqz.console.algorithm;

import ch.qos.logback.core.joran.conditional.ElseAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * 1
 * /       \
 * 2          3
 * /    \      /     \
 * 4      5    6       7
 * /
 * 8
 */
public class BinaryTree {
    static TreeNode root = null;

    static {
        root = new TreeNode(1
                , new TreeNode(2, new TreeNode(4), new TreeNode(5))
                , new TreeNode(3, new TreeNode(6), new TreeNode(7, new TreeNode(8), null)));
    }

    public static void main(String[] args) {
        postorder2(root);
        System.out.println("-----------");
        postorder(root);
    }

    /**
     * 先序 非递归
     */
    public static String preorder2(TreeNode node) {
        StringBuilder stringBuilder = new StringBuilder();

        Stack<TreeNode> stack = new Stack<>();
        stack.add(node);

        while (!stack.isEmpty()) {
            TreeNode treeNode = stack.pop();

            stringBuilder.append(treeNode.value).append(",");

            if (treeNode.right != null) {
                stack.push(treeNode.right);
            }

            if (treeNode.left != null) {
                stack.push(treeNode.left);
            }
        }
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * 后序 非递归
     */
    public static String inorder2(TreeNode node) {
        StringBuilder stringBuilder = new StringBuilder();

        Stack<TreeNode> stack = new Stack<>();
        stack.add(node);

        while (!stack.isEmpty()) {
            TreeNode treeNode = stack.peek();

            while (treeNode.left != null) {
                stack.push(treeNode.left);
                treeNode = treeNode.left;
            }

            while (!stack.isEmpty()) {
                treeNode = stack.pop();
                stringBuilder.append(treeNode.value).append(",");

                if (treeNode.right != null) {
                    stack.push(treeNode.right);
                    break;
                }

            }
        }
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * 中序 非递归
     */
    public static String postorder2(TreeNode node) {
        StringBuilder stringBuilder = new StringBuilder();

        Stack<TreeNode> stack = new Stack<>();
        stack.add(node);

        TreeNode last = null;
        while (!stack.isEmpty()) {

            while (stack.peek().left != null) {
                stack.push(stack.peek().left);
            }

            while (!stack.isEmpty()) {
                if (stack.peek().right == null || stack.peek().right == last) {
                    last = stack.pop();
                    stringBuilder.append(last.value).append(",");
                } else if (stack.peek().right != null) {
                    stack.push(stack.peek().right);
                    break;
                }
            }
        }
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static void preorder(TreeNode treeNode) {
        System.out.println(treeNode.value);
        if (treeNode.left != null)
            preorder(treeNode.left);
        if (treeNode.right != null)
            preorder(treeNode.right);
    }

    public static void postorder(TreeNode treeNode) {
        if (treeNode.left != null)
            postorder(treeNode.left);
        if (treeNode.right != null)
            postorder(treeNode.right);
        System.out.println(treeNode.value);
    }

    public static void inorder(TreeNode treeNode) {
        if (treeNode.left != null)
            inorder(treeNode.left);
        System.out.println(treeNode.value);
        if (treeNode.right != null)
            inorder(treeNode.right);
    }


    public static void levelTraversal() {
        List<TreeNode> level = new ArrayList<>();
        level.add(root);

        System.out.println(root.value);

        while (!level.isEmpty()) {
            List<TreeNode> children = new ArrayList<>();
            for (int i = 0; i < level.size(); i++) {
                TreeNode node = level.get(i);

                if (node.right != null) {
                    children.add(node.right);
                    System.out.println(node.right.value);
                }

                if (node.left != null) {
                    children.add(node.left);
                    System.out.println(node.left.value);
                }
            }
            level = children;
        }

    }

    /*
     * 打印二叉树从左视角看到节点
     * 给定一颗普通二叉树，请输出二叉树左视角能看到的节点
     * 例如，普通二叉树
     *                  1
     *               /       \
     *             2          3
     *          /    \      /     \
     *         4      5    6       7
     *                            /
     *                          8
     * 从左边看，输出能看到的 1，2，4，8 这四个节点。
     * 使用层序遍历,从右到左,然后将最左侧放入结果集中
     * */
    public static void aVoid() {


        List<TreeNode> level = new ArrayList<>();
        level.add(root);

        while (!level.isEmpty()) {
            System.out.println(level.get(0).value);
            level = level.stream().flatMap(p -> Arrays.asList(p.left, p.right).stream().filter(q -> q != null)).collect(Collectors.toList());
        }
    }


    public static class TreeNode {
        int value;
        TreeNode left;
        TreeNode right;

        public TreeNode(int v) {
            this(v, null, null);
        }

        public TreeNode(int v, TreeNode left, TreeNode right) {
            this.value = v;
            this.left = left;
            this.right = right;
        }

    }


}
