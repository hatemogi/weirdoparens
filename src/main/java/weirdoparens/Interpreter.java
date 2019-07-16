package weirdoparens;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toCollection;

class Interpreter {
    private Queue<Character> que;
    private SymbolTable table = new SymbolTable();

    private Interpreter(String expression) {
        que = expression.replaceAll("[() ]", "") // 공백과 괄호 제거
                .chars().mapToObj(c -> (char)c)
                .collect(toCollection(LinkedList::new));
    }

    private Integer eval() {
        Character c = que.poll();
        if (c == '+') {
            return eval() + eval();
        } else if (c == '-') {
            return eval() - eval();
        } else if (c == '*') {
            return eval() * eval();
        } else if (c == '/') {
            return eval() / eval();
        } else if (c >= '0' && c <= '9') {
            return c - '0';
        } else if (c == '?') {
            int cond = eval();
            int te = eval();
            int fe = eval();
            return cond > 0 ? te : fe;
        } else if (c == '!') {
            return table.scope(s -> {
                s.bind(que.poll(), eval());
                return eval();
            });
        } else if (c >= 'a' && c <= 'z') {
            return table.lookup(c);
        }
        throw new IllegalArgumentException("구문 오류");
    }

    private String apply() {
        try {
            return eval().toString();
        } catch (ArithmeticException|IllegalArgumentException|NoSuchElementException e) {
            return "E";
        }
    }

    static String evaluate(String 식) {
        return new Interpreter(식).apply();
    }
}

class SymbolTable {
    private List<Map<Character, Integer>> scopes = new LinkedList<>();

    Integer scope(Function<SymbolTable, Integer> block) {
        try {
            scopes.add(0, new HashMap<>());
            return block.apply(this);
        } finally {
            scopes.remove(0);
        }
    }

    void bind(Character symbol, Integer value) {
        scopes.get(0).put(symbol, value);
    }

    Integer lookup(Character symbol) {
        return scopes.stream()
                .filter(t -> t.containsKey(symbol))
                .findFirst()
                .map(t -> t.get(symbol))
                .orElseThrow(() -> new NoSuchElementException("바인딩값 없음"));
    }
}