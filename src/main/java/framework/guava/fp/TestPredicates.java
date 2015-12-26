package framework.guava.fp;

import java.util.Date;

import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class TestPredicates {

	@Test
	public void testPredicatesAnd(){
		Predicate<Date> low = new Predicate<Date>(){
			@Override
			public boolean apply(Date input) {
				return input.getHours()>=12;
			}
		};
		
		Predicate<Date> high = new Predicate<Date>(){
			@Override
			public boolean apply(Date input) {
				return input.getHours()<18;
			}
		};
		Date now = new Date();
		Predicate<Date> pmPredicate = Predicates.and(low,high);
		Predicate<Date> pmPredicateOr = Predicates.or(low,high);
		System.out.println("now("+now.getHours()+") is pm? "+pmPredicate.apply(now));
		System.out.println("now("+now.getHours()+") maybe is pm? "+pmPredicateOr.apply(now));
		
		//The Predicates.notmethod takes a Predicate object and performs a logical negation of the component Predicate.
		//Predicate largeCityPredicate = Predicate.not(smallPopulationPredicate);
		
		//Predicate that will evaluate whether the state returned from our function is located in either the midwest or the southwest:
		//Predicate<String> predicate = Predicates.compose(southwestOrMidwestRegionPredicate,lookup);
	}
	
}