package src.ParseTree;

import src.TokenTypes.Token;

import java.util.Optional;

public record TreeNode(int level, TreeNodeValue nodeType, Token token) {
}
