package calculator;

import java.util.Stack;
import java.util.StringTokenizer;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String s = "1+2×3+1";

		StringTokenizer num = new StringTokenizer(s, "+-/× ");
		StringTokenizer oper = new StringTokenizer(s, "1234567890 ");

		String[] expression = new String[num.countTokens()+oper.countTokens()];
		String[] expression2 = new String[expression.length];

		Stack<Object> stack = new Stack<>();
		Stack<Integer> stack2 = new Stack<>();

		int i = 0;
		while(num.hasMoreTokens()){
			String data = num.nextToken();
			expression[i] = data;
			i  = i+2;
		}

		i = 1;
		while(oper.hasMoreTokens()) {
			String data = oper.nextToken();
			expression[i] = data;
			i = i+2;
		}

		i = 0;
		for (String s1 : expression) {
			switch(s1) {
			case "+":
			case "-":
				if(stack.isEmpty()) {
					stack.push(s1);
					break;
				}
				while(!stack.isEmpty()) {
					expression2[i] = (String) stack.pop();
					i++;
				}
				stack.push(s1);
				break;

			case "×":
			case "/":
				if (stack.isEmpty()) {
					stack.push(s1);
					break;
				}
				String operator = (String)stack.peek();
				if (operator == "×" || operator == "/" ) {
					while(!stack.isEmpty()) {
						expression2[i] = (String) stack.pop();
						i++;
					}
				}
				else {
					stack.push(s1);
					break;
				}
			default:
				expression2[i] = s1;
				i++;
			}
		}
		while(!stack.isEmpty()) {
			expression2[i] = (String) stack.pop();
			i++;
		}

		System.out.print("후위 표기법 : ");
		for(String s2 : expression2)
			System.out.print(s2);
		System.out.println();

		int num1, num2;
		int result;


		for(i = 0; i<expression2.length; i++) {
			if(expression2[i].equals("+")) {
				num1 = stack2.pop();
				num2 = stack2.pop();
				result = num1 + num2;
				stack2.push(result);
			}
			else if(expression2[i].equals("-")) {
				num1 = stack2.pop();
				num2 = stack2.pop();
				result = num2 - num1;
				stack2.push(result);
			}
			else if(expression2[i].equals("×")) {
				num1 = stack2.pop();
				num2 = stack2.pop();
				result = num1 * num2;
				stack2.push(result);
			}
			else if(expression2[i].equals("/")) {
				num1 = stack2.pop();
				num2 = stack2.pop();
				result = num2 / num1;
				stack2.push(result);
			}
			else
				stack2.push(Integer.parseInt(expression2[i]));
		}
		System.out.println("\n결과 = " + stack2.pop());
	}
}