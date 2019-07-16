package weirdoparens;

import org.junit.Test;
import weirdoparens.Interpreter;

import static org.junit.Assert.*;

public class InterpreterTest {

    private void 검사(String expected, String 계산식) {
        assertEquals(계산식, expected, Interpreter.evaluate(계산식));
    }

    @Test
    public void 상수식_10점() {
        검사("8", "8");
        검사("3", "3");
    }

    @Test
    public void 사칙연산_15점() {
        검사("8", "(+ 6 2)");
        검사("6", "(* 3 2)");
        검사("4", "(- 6 2)");
        검사("3", "(/ 6 2)");
    }

    @Test
    public void 나누기0처리_5점() {
        검사("E", "(/ 1 0)");
    }

    @Test
    public void 조건식_10점() {
        검사("4", "(? 0 3 4)");
        검사("3", "(? 1 3 4)");
    }

    @Test
    public void 중첩사칙연산_10점() {
        검사("8", "(+ (* 2 3) 2)");
        검사("6", "(* (+ 1 2) 2)");
        검사("3", "(- 6 (/ 6 2))");
        검사("3", "(/ (* 2 3) (- 4 2))");
    }

    @Test
    public void 중첩조건식_10점() {
        검사("-1", "(? (* 2 3) (- 1 2) (+ 1 2))");
        검사("6", "(* (? 1 2 3) 3)");
        검사("3", "(- 6 (/ 6 2))");
        검사("3", "(/ (* 2 3) (- 4 2))");
    }

    @Test
    public void 알파컨버전_20점() {
        검사("3", "(! a 3 a)");
        검사("7", "(! a 3 (+ a 4))");
        검사("9", "(! b 5 (* 3 3))");
        검사("36","(! a (* 2 3) (* a a))");
    }

    @Test
    public void 중첩알파컨버전_10점() {
        검사("8", "(! a 2 (! b 4 (* a b)))");
        검사("3", "(! a 2 (+ (! a 3 (- 4 a)) a))");
    }

    @Test
    public void 예외전파수동확인_10점() {
        fail("채점자가 코드를 봅니다. 예외전파를 일일이 하지 않고, 상위에서 한번만 했으면 10점 줍니다.");
    }
}
