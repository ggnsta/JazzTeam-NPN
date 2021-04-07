import java.util.*;
import java.util.stream.Collectors;

public class PrefixNotation {

    public String Start(String inputExpression)
    {
        validate(inputExpression);
        convert();
        return calculate();
    }


    public List<String> inputExpression;
    public List<String> outputExpression = new ArrayList<>();
    public String resultPN;
    public String result;

    
    public void validate(String inputExpression)  {
        checkInputArg(inputExpression);

        //inputExpression = inputExpression.replaceAll("\\s+","");
        //inputExpression = inputExpression.replaceAll(".(?!$)", "$0 ");

        inputExpression= inputExpression.replaceAll("\\D", " $0 ");
        inputExpression = inputExpression.replaceAll("\\s+"," ");
        inputExpression=inputExpression.trim();


        List<String> validatedExpression = Arrays.asList(inputExpression.split(" "));

        this.inputExpression=validatedExpression;
    }

    public void convert(){

        Stack<String> expressionStack = new Stack<>();
        reverseExpression(inputExpression);
        for (String character : inputExpression)
        {
            if(isNumeric(character)){
                outputExpression.add(character);
                continue;
            }
            if (character.equals("(")){
                expressionStack.push(character);
                continue;
            }
            if (character.equals(")"))
            {
                outputExpression.addAll(popUntilBracket(expressionStack));
                continue;
            }
            if (isOperand(character)){
                outputExpression.addAll(manipulateOperands(expressionStack, character));
            }
        }
        while (!expressionStack.empty()) {
            outputExpression.add(expressionStack.pop());
        }
        reverseExpression(outputExpression);
        resultPN = outputExpression.stream().map(Object::toString).collect(Collectors.joining(" "));
    }

    public static Boolean isNumeric(String s) {
        if (s.matches("-?\\d+(\\.\\d+)?")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    public static Boolean isOperand(String s) {
        if ("()+*-/".contains(s)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    public static Byte getPriority(String s) {
        switch (s.charAt(0)) {
            case '(':
                return 0;
            case ')':
                return 1;
            case '+':
            case '-':
                return 2;
            case '*':
            case '/':
                return 3;
            default:
                return 4;
        }
    }
    public List<String> popUntilBracket(Stack<String> expressionStack) {
        List<String> popped = new ArrayList<>();

        while (!expressionStack.peek().equals("(")) {
            popped.add(expressionStack.pop());
        }
        expressionStack.pop();

        return popped;
    }
    public List<String> manipulateOperands(Stack<String> expressionStack, String operand) {
        List<String> popped = new ArrayList<>();

        while (!(expressionStack.empty() || getNotationComparingCondition(operand, expressionStack.peek()))) {
            popped.add(expressionStack.pop());
        }
        expressionStack.push(operand);

        return popped;
    }
    public Boolean getNotationComparingCondition(String operand1, String operand2)
    {
        return getPriority(operand1) >= getPriority(operand2);
    }
    private void reverseExpression(List<String> expression) {
        expression = reverseElements(expression);
        expression = replaceBrackets(expression);
    }
    private List<String> reverseElements(List<String> expression) {
        Collections.reverse(expression);
        return expression;
    }
    private List<String> replaceBrackets(List<String> expression) {
        Collections.replaceAll(expression, "(", "!");
        Collections.replaceAll(expression, ")", "(");
        Collections.replaceAll(expression, "!", ")");
        return expression;
    }

    private String calculate() {
        reverseExpression(outputExpression);
        Stack<Double> resultStack = new Stack<>();

        for (String character : outputExpression) {
            if (isNumeric(character)) {
                resultStack.push(Double.valueOf(character));
                continue;
            }
            if (isOperand(character)) {
                Double operator1 = resultStack.pop();
                Double operator2 = resultStack.pop();
                manipulateNotationDependent(resultStack, character, operator1, operator2);
            }

        }
        result = String.valueOf(popAndCastResult(resultStack));
        // System.out.println(result);
        return result;
    }
    public void manipulateNotationDependent(Stack<Double> resultStack, String character, Double operator1, Double operator2) {
        resultStack.push(manipulate(character, operator1, operator2));
    }

    public static Double manipulate(String operand, Double operator1, Double operator2) {
        switch (operand) {
            case "+":
                return operator1 + operator2;
            case "-":
                return operator1 - operator2;
            case "*":
                return operator1 * operator2;
            case "/": {
                if (operator2==0) throw new ArithmeticException();
                return operator1 / operator2;
            }
            default:
                return null;
        }
    }
    public Number popAndCastResult(Stack<Double> resultStack) {
        Double result = resultStack.pop();
        if (result == Math.floor(result)) {
            return result.intValue();
        }
        return result;
    }
    public void checkInputArg(String inputExpression){
        long countLeftScope = inputExpression.chars().filter(ch->ch=='(').count();
        long countRightScope = inputExpression.chars().filter(ch->ch==')').count();
        if (countLeftScope!=countRightScope)throw  new IllegalArgumentException();

    }
}
