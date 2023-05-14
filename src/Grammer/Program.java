package src.Grammer;

import src.Exceptions.SyntaxException;
import src.ParseTree.Tree;
import src.ParseTree.TreeNodeValue;
import src.SemanticResult;
import src.Synthesizer;
import src.TokenTypes.Token;

@FunctionalInterface
interface CheckedFunction<T, R> {
    R apply(T t) throws SyntaxException;
}

public class Program {

    private SemanticResult checkParenthesis(int level, TreeNodeValue treeNodeValue, CheckedFunction<Integer, SemanticResult> callable) throws SyntaxException {
        if (checkTokenInvert("LEFTPAR")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken(level);

        Tree.addTreeNode(level, treeNodeValue, null);
        callable.apply(level);

        checkTerminalAndThrow(level, "RIGHTPAR");

        return new SemanticResult(true);
    }

    private boolean checkTokenInvert(String tokenValue) {
        Token token = Synthesizer.getNextToken();
        return !token.typeName.equals(tokenValue);
    }

    private SemanticResult checkNonTerminalAndThrow(int level, TreeNodeValue treeNodeValue, CheckedFunction<Integer, SemanticResult> callable, String exceptionMessage) throws SyntaxException {
        Tree.addTreeNode(level, treeNodeValue, null);
        SemanticResult argListResult = callable.apply(level);
        if (!argListResult.isParsed()) {
            throw SyntaxException.NonTerminalException(exceptionMessage);
        }
        return new SemanticResult(true);
    }

    private SemanticResult checkNonTerminal(int level, TreeNodeValue treeNodeValue, CheckedFunction<Integer, SemanticResult> callable) throws SyntaxException {
        int index = Tree.addTreeNode(level, treeNodeValue, null);
        SemanticResult expressionResult = callable.apply(level);
        if (!expressionResult.isParsed()) {
            Tree.removeTreeNodesAfterIndex(index);
        }

        return expressionResult;
    }

    private SemanticResult checkTerminalAndThrow(int level, String tokenValue) throws SyntaxException {
        if (checkTokenInvert(tokenValue)) {
            throw SyntaxException.TokenException(Synthesizer.getNextToken(), tokenValue);
        }
        Synthesizer.consumeToken(level);
        return new SemanticResult(true);
    }

    //////////////////////////////

    public void synthesize() throws SyntaxException {
        checkNonTerminal(0, TreeNodeValue.Program, this::program);
    }

    public SemanticResult program(int level) throws SyntaxException {
        if (Synthesizer.getNextToken().typeName.equals("EoF")) {
            return new SemanticResult(true);
        }
        checkNonTerminal(level + 1, TreeNodeValue.TopLevelForm, this::topLevelForm);
        return checkNonTerminal(level + 1, TreeNodeValue.Program, this::program);
    }

    private SemanticResult topLevelForm(int level) throws SyntaxException {
        return checkParenthesis(level + 1, TreeNodeValue.SecondLevelForm, this::secondLevelForm);
    }

    private SemanticResult secondLevelForm(int level) throws SyntaxException {
        checkNonTerminal(level + 1, TreeNodeValue.Definition, this::definition);

        return checkParenthesis(level + 1, TreeNodeValue.FunCall, this::funCall);
    }

    private SemanticResult definition(int level) throws SyntaxException {
        if (checkTokenInvert("DEFINE")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken(level + 1);

        return checkNonTerminalAndThrow(level + 1, TreeNodeValue.DefinitionRight, this::definitionRight, "Definition is expected.");
    }

    private SemanticResult definitionRight(int level) throws SyntaxException {
        if (!checkTokenInvert("IDENTIFIER")) {
            Synthesizer.consumeToken(level + 1);

            checkNonTerminalAndThrow(level + 1, TreeNodeValue.Expression, this::expression, "Expression expected.");
            return new SemanticResult(true);
        }

        if (checkTokenInvert("LEFTPAR")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken(level + 1);

        checkTerminalAndThrow(level + 1, "IDENTIFIER");

        checkNonTerminalAndThrow(level + 1, TreeNodeValue.ArgList, this::argList, "ArgList expected.");

        checkTerminalAndThrow(level + 1, "RIGHTPAR");

        return checkNonTerminalAndThrow(level + 1, TreeNodeValue.Statements, this::statements, "Statements expected.");
    }

    private SemanticResult argList(int level) {
        if (checkTokenInvert("IDENTIFIER")) {
            return new SemanticResult(true);
        }
        Synthesizer.consumeToken(level + 1);
        Tree.addTreeNode(level + 1, TreeNodeValue.ArgList, null);
        return argList(level + 1);
    }

    private SemanticResult statements(int level) throws SyntaxException {
        SemanticResult expressionResult = checkNonTerminal(level + 1, TreeNodeValue.Expression, this::expression);
        if (expressionResult.isParsed()) {
            return expressionResult;
        }

        SemanticResult definitionResult = checkNonTerminal(level + 1, TreeNodeValue.Definition, this::expression);
        if (definitionResult.isParsed()) {
            return definitionResult;
        }


        return checkNonTerminalAndThrow(level + 1, TreeNodeValue.Statements, this::statements, "Statements expected.");
    }

    private SemanticResult expressions(int level) throws SyntaxException {
        SemanticResult expressionResult = checkNonTerminal(level + 1, TreeNodeValue.Expression, this::expression);
        if (!expressionResult.isParsed()) {
            return new SemanticResult(true);
        }

        return checkNonTerminal(level + 1, TreeNodeValue.Expressions, this::expressions);
    }

    private SemanticResult expression(int level) throws SyntaxException {
        if (
                !checkTokenInvert("IDENTIFIER") ||
                        !checkTokenInvert("NUMBER") ||
                        !checkTokenInvert("CHAR") ||
                        !checkTokenInvert("BOOLEAN") ||
                        !checkTokenInvert("STRING")) {
            Synthesizer.consumeToken(level + 1);
            return new SemanticResult(true);
        }

        return checkParenthesis(level + 1, TreeNodeValue.Expr, this::expr);
    }


    private SemanticResult expr(int level) throws SyntaxException {
        return new SemanticResult(checkNonTerminal(level + 1, TreeNodeValue.LetExpression, this::letExpression).isParsed() ||
                checkNonTerminal(level + 1, TreeNodeValue.CondBranch, this::condExpression).isParsed() ||
                checkNonTerminal(level + 1, TreeNodeValue.IfExpression, this::ifExpression).isParsed() ||
                checkNonTerminal(level + 1, TreeNodeValue.LetExpression, this::letExpression).isParsed() ||
                checkNonTerminal(level + 1, TreeNodeValue.BeginExpression, this::beginExpression).isParsed() ||
                checkNonTerminal(level + 1, TreeNodeValue.FunCall, this::funCall).isParsed());
    }

    private SemanticResult funCall(int level) throws SyntaxException {
        if (checkTokenInvert("IDENTIFIER")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken(level + 1);

        return checkNonTerminalAndThrow(level + 1, TreeNodeValue.Expressions, this::expressions, "Expression expected.");
    }

    private SemanticResult letExpression(int level) throws SyntaxException {
        if (checkTokenInvert("LET")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken(level + 1);

        return checkNonTerminalAndThrow(level + 1, TreeNodeValue.LetExpr, this::letExpr, "Let expression expected.");
    }

    private SemanticResult letExpr(int level) throws SyntaxException {
        if (checkParenthesis(level + 1, TreeNodeValue.VarDefs, this::varDefs).isParsed()) {
            return checkNonTerminalAndThrow(level + 1, TreeNodeValue.Statements, this::statements, "Statements expected.");
        }

        if (checkTokenInvert("IDENTIFIER")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken(level + 1);

        if (!checkParenthesis(level + 1, TreeNodeValue.VarDefs, this::varDefs).isParsed()) {
            throw SyntaxException.TokenException(Synthesizer.getNextToken(), "(");
        }

        return checkNonTerminalAndThrow(level + 1, TreeNodeValue.Statements, this::statements, "Statements expected.");
    }

    private SemanticResult varDefs(int level) throws SyntaxException {
        if (checkTokenInvert("LEFTPAR")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken(level + 1);

        checkTerminalAndThrow(level + 1, "IDENTIFIER");

        checkNonTerminalAndThrow(level + 1, TreeNodeValue.Expression, this::expression, "Expression expected.");

        checkTerminalAndThrow(level + 1, "RIGHTPAR");


        return checkNonTerminal(level + 1, TreeNodeValue.VarDef, this::varDef);
    }

    private SemanticResult varDef(int level) throws SyntaxException {
        checkNonTerminal(level + 1, TreeNodeValue.VarDefs, this::varDefs);
        return new SemanticResult(true);
    }

    private SemanticResult condExpression(int level) throws SyntaxException {
        if (checkTokenInvert("COND")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken(level + 1);

        return checkNonTerminalAndThrow(level + 1, TreeNodeValue.CondBranches, this::condBranches, "Condition body expected.");
    }

    private SemanticResult condBranches(int level) throws SyntaxException {
        if (checkTokenInvert("LEFTPAR")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken(level + 1);

        checkNonTerminalAndThrow(level + 1, TreeNodeValue.Expression, this::expression, "Expression expected.");
        checkNonTerminalAndThrow(level + 1, TreeNodeValue.Statements, this::statements, "Statements expected.");

        checkTerminalAndThrow(level + 1, "RIGHTPAR");

        return checkNonTerminalAndThrow(level + 1, TreeNodeValue.CondBranch, this::condBranch, "Condition branch expected.");
    }

    private SemanticResult condBranch(int level) throws SyntaxException {
        // TODO ASK INSTRUCTOR FOR COND BRANCH
        if (checkTokenInvert("LEFTPAR")) {
            return new SemanticResult(true);
        }
        Synthesizer.consumeToken(level + 1);

        checkNonTerminalAndThrow(level + 1, TreeNodeValue.Expression, this::expression, "Expression expected.");
        checkNonTerminalAndThrow(level + 1, TreeNodeValue.Statements, this::statements, "Statements expected.");

        return checkTerminalAndThrow(level + 1, "RIGHTPAR");
    }

    private SemanticResult ifExpression(int level) throws SyntaxException {
        if (checkTokenInvert("IF")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken(level + 1);

        checkNonTerminalAndThrow(level + 1, TreeNodeValue.Expression, this::expression, "Expression expected.");
        checkNonTerminalAndThrow(level + 1, TreeNodeValue.Expression, this::expression, "Expression expected.");
        return checkNonTerminal(level + 1, TreeNodeValue.EndExpression, this::endExpression);
    }

    private SemanticResult endExpression(int level) throws SyntaxException {
        checkNonTerminal(level + 1, TreeNodeValue.Expression, this::expression);
        return new SemanticResult(true);
    }

    private SemanticResult beginExpression(int level) throws SyntaxException {
        if (checkTokenInvert("BEGIN")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken(level + 1);

        return checkNonTerminalAndThrow(level + 1, TreeNodeValue.Statements, this::statements, "Statements expected.");
    }


}
