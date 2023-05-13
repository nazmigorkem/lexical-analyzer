package src.Grammer;

import src.Exceptions.SyntaxException;
import src.SemanticResult;
import src.Synthesizer;
import src.TokenTypes.Token;

import java.util.concurrent.Callable;

public class Program {

    private SemanticResult checkParenthesis(Callable<SemanticResult> callable) throws SyntaxException {
        if (checkTokenInvert("LEFTPAR")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken();

        try {
            callable.call();
        } catch (SyntaxException syntaxException) {
            throw syntaxException;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Something went wrong.");
        }

        checkTerminalAndThrow("RIGHTPAR");

        return new SemanticResult(true);
    }

    private boolean checkTokenInvert(String tokenValue) {
        Token token = Synthesizer.getNextToken();
        return !token.typeName.equals(tokenValue);
    }

    private SemanticResult checkNonTerminalAndThrow(Callable<SemanticResult> nonTerminal, String exceptionMessage) throws SyntaxException {
        try {
            SemanticResult argListResult = nonTerminal.call();
            if (!argListResult.isParsed()) {
                throw SyntaxException.NonTerminalException(exceptionMessage);
            }
        } catch (SyntaxException syntaxException) {
            throw syntaxException;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Something went wrong.");
        }

        return new SemanticResult(true);
    }

    private SemanticResult checkTerminalAndThrow(String tokenValue) throws SyntaxException {
        if (checkTokenInvert(tokenValue)) {
            throw SyntaxException.TokenException(Synthesizer.getNextToken(), tokenValue);
        }
        Synthesizer.consumeToken();
        return new SemanticResult(true);
    }

    //////////////////////////////

    public SemanticResult program() throws SyntaxException {
        topLevelForm();
        return new SemanticResult(true);
    }

    private SemanticResult topLevelForm() throws SyntaxException {
        return checkParenthesis(this::secondLevelForm);
    }

    private SemanticResult secondLevelForm() throws SyntaxException {
        SemanticResult result = definition();
        if (result.isParsed()) {
            return result;
        }

        return checkParenthesis(this::funCall);
    }

    private SemanticResult definition() throws SyntaxException {
        if (checkTokenInvert("DEFINE")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken();

        return checkNonTerminalAndThrow(this::definitionRight, "Definition is expected.");
    }

    private SemanticResult definitionRight() throws SyntaxException {
        if (!checkTokenInvert("IDENTIFIER")) {
            Synthesizer.consumeToken();

            checkNonTerminalAndThrow(this::expression, "Expression expected.");
            return new SemanticResult(true);
        }

        if (checkTokenInvert("LEFTPAR")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken();

        checkTerminalAndThrow("IDENTIFIER");

        checkNonTerminalAndThrow(this::argList, "ArgList expected.");

        checkTerminalAndThrow("RIGHTPAR");

        return checkNonTerminalAndThrow(this::statements, "Statements expected.");
    }

    private SemanticResult argList() {
        if (checkTokenInvert("IDENTIFIER")) {
            return new SemanticResult(true);
        }
        Synthesizer.consumeToken();

        return argList();
    }

    private SemanticResult statements() throws SyntaxException {
        SemanticResult expressionResult = expression();
        if (expressionResult.isParsed()) {
            return expressionResult;
        }

        SemanticResult definitionResult = definition();
        if (!definitionResult.isParsed()) {
            return definitionResult;
        }

        return checkNonTerminalAndThrow(this::statements, "Statements expected.");
    }

    private SemanticResult expressions() throws SyntaxException {
        SemanticResult expressionResult = expression();
        if (!expressionResult.isParsed()) {
            return new SemanticResult(true);
        }

        return expressions();
    }

    private SemanticResult expression() throws SyntaxException {
        if (
                !checkTokenInvert("IDENTIFIER") ||
                        !checkTokenInvert("NUMBER") ||
                        !checkTokenInvert("CHAR") ||
                        !checkTokenInvert("BOOLEAN") ||
                        !checkTokenInvert("STRING")) {
            Synthesizer.consumeToken();
            return new SemanticResult(true);
        }

        return checkParenthesis(this::expr);
    }

    private SemanticResult expr() throws SyntaxException {
        return new SemanticResult(letExpression().isParsed() ||
                condExpression().isParsed() ||
                ifExpression().isParsed() ||
                beginExpression().isParsed() ||
                funCall().isParsed());
    }

    private SemanticResult funCall() throws SyntaxException {
        if (checkTokenInvert("IDENTIFIER")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken();

        return checkNonTerminalAndThrow(this::expressions, "Expression expected.");
    }

    private SemanticResult letExpression() throws SyntaxException {
        if (checkTokenInvert("LET")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken();

        return checkNonTerminalAndThrow(this::letExpr, "Let expression expected.");
    }

    private SemanticResult letExpr() throws SyntaxException {
        if (checkParenthesis(this::varDefs).isParsed()) {
            return checkNonTerminalAndThrow(this::statements, "Statements expected.");
        }

        if (checkTokenInvert("IDENTIFIER")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken();

        if (!checkParenthesis(this::varDefs).isParsed()) {
            throw SyntaxException.TokenException(Synthesizer.getNextToken(), "(");
        }

        return checkNonTerminalAndThrow(this::statements, "Statements expected.");
    }

    private SemanticResult varDefs() throws SyntaxException {
        if (checkTokenInvert("LEFTPAR")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken();

        checkTerminalAndThrow("IDENTIFIER");

        checkNonTerminalAndThrow(this::expression, "Expression expected.");

        checkTerminalAndThrow("RIGHTPAR");

        // TODO WILL DO LIKE IN PDF
        varDefs();

        return new SemanticResult(true);
    }

    private SemanticResult condExpression() throws SyntaxException {
        if (checkTokenInvert("COND")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken();

        return checkNonTerminalAndThrow(this::condBranches, "Condition body expected.");
    }

    private SemanticResult condBranches() throws SyntaxException {
        if (checkTokenInvert("LEFTPAR")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken();

        checkNonTerminalAndThrow(this::expression, "Expression expected.");
        checkNonTerminalAndThrow(this::statements, "Statements expected.");

        checkTerminalAndThrow("RIGHTPAR");

        return checkNonTerminalAndThrow(this::condBranch, "Condition branch expected.");
    }

    private SemanticResult condBranch() throws SyntaxException {
        // TODO ASK INSTRUCTOR FOR COND BRANCH
        if (checkTokenInvert("LEFTPAR")) {
            return new SemanticResult(true);
        }
        Synthesizer.consumeToken();

        checkNonTerminalAndThrow(this::expression, "Expression expected.");
        checkNonTerminalAndThrow(this::statements, "Statements expected.");

        return checkTerminalAndThrow("RIGHTPAR");
    }

    private SemanticResult ifExpression() throws SyntaxException {
        if (checkTokenInvert("IF")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken();

        checkNonTerminalAndThrow(this::expression, "Expression expected.");
        checkNonTerminalAndThrow(this::expression, "Expression expected.");
        // TODO WILL DO LIKE IN PDF
        expression();
        return new SemanticResult(true);
    }

    private SemanticResult beginExpression() throws SyntaxException {
        if (checkTokenInvert("BEGIN")) {
            return new SemanticResult(false);
        }
        Synthesizer.consumeToken();

        return checkNonTerminalAndThrow(this::statements, "Statements expected.");
    }


}
