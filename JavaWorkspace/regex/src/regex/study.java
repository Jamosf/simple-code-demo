package regex;

public class study {

	public static void main(String[] args){
		// 简单的正则表达式
		p("abc".matches("..."));
		p("3fffd5".replaceAll("\\d", "-"));
	}
	public static void p(Object o){
		System.out.println(o);
	}
}
