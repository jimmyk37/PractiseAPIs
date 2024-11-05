package sample.Product;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Test {

	static void removeDuplicate(String name) {
		char[] arr=name.toCharArray();
		String s="";
		
		Set<Character> set=new LinkedHashSet<Character>();
		
		for (int i = 0; i < arr.length; i++) {
			set.add(arr[i]);
		}
		
		for(Character c:set) {
			s+=c;
		}
		
		System.out.println(s);
		
	}
	
	public static void main(String[] args) {
		removeDuplicate("karmchandra");
	}

}
