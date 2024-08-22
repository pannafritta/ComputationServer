package expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionParser {
    String string;
    int cursor = 0;

    public Expression parse(String string) {
        cursor = 0;
        this.string = string;
        return parse();
    }

    private Expression parse() throws IllegalArgumentException {
        Token token;

        token = Token.Type.CONSTANT.next(string, cursor);
        if (token != null && token.start == cursor) {
            cursor = token.end;
            return new Constant(Double.parseDouble(string.substring(token.start, token.end)));
        }

        token = Token.Type.VARIABLE.next(string, cursor);
        if (token != null && token.start == cursor) {
            cursor = token.end;
            return new Variable(string.substring(token.start, token.end));
        }

        token = Token.Type.OPEN_BRACKET.next(string, cursor);
        if (token != null && token.start == cursor) {
            cursor = token.end;

            Expression left = parse();

            Token operatorToken = Token.Type.OPERATOR.next(string, cursor);
            if (operatorToken != null && operatorToken.start == cursor) {
                cursor = operatorToken.end;
            } else {
                throw new IllegalArgumentException(String.format("Unexpected char at %d instead of operator: '%s'", cursor, string.charAt(cursor)));
            }

            Expression right = parse();

            Token closedBracketToken = Token.Type.CLOSED_BRACKET.next(string, cursor);
            if (closedBracketToken != null && closedBracketToken.start == cursor) {
                cursor = closedBracketToken.end;
            } else {
                throw new IllegalArgumentException(String.format("(Expression exception) Unexpected char at %d instead of closed bracket: '%s'", cursor, string.charAt(cursor)));
            }
            Operator.Type operatorType = null;
            String operatorString = string.substring(operatorToken.start, operatorToken.end);
            for (Operator.Type type : Operator.Type.values()) {
                if (operatorString.equals(Character.toString(type.getSymbol()))) {
                    operatorType = type;
                    break;
                }
            }
            if (operatorType == null) {
                throw new IllegalArgumentException(String.format("(Expression exception) Unknown operator at %d: '%s'", operatorToken.start, operatorString));
            }
            return new Operator(operatorType, left, right);
        }
        throw new IllegalArgumentException(String.format("(Expression exception) Unexpected char at %d: '%s'", cursor, string.charAt(cursor)));
    }

    public static class Token {
        final int start;
        final int end;

        public Token(int start, int end) {
            this.start = start;
            this.end = end;
        }

        enum Type {
            CONSTANT("[0-9]+(\\.[0-9]+)?"),
            VARIABLE("[a-z][a-z0-9]*"),
            OPERATOR("[+\\-\\*/\\^]"),
            OPEN_BRACKET("\\("),
            CLOSED_BRACKET("\\)");
            private final String regex;

            Type(String regex) {
                this.regex = regex;
            }

            Token next(String s, int i) {
                Matcher matcher = Pattern.compile(regex).matcher(s);
                if (!matcher.find(i)) {
                    return null;
                }
                return new Token(matcher.start(), matcher.end());
            }
        }
    }
}
