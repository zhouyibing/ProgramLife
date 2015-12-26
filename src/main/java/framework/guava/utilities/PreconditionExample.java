package framework.guava.utilities;

import com.google.common.base.Preconditions;
/**
 * 	 checkNotNull ( T object, Object message): This method returns the object
if it is not null; otherwise a NullPointerException error is thrown.
•	 checkElementIndex (int index , int size , Object message): In this
method, the value of the index variable is the position of the element you
are trying to access and the value of the size variable is the length of the
array, list, or string. The index variable is retuned if valid; otherwise an
IndexOutOfBoundsException error is thrown.
•	 checkArgument (Boolean expression, Object message): This method
evaluates a Boolean expression involving the state of a variable passed
to a method. The Boolean expression is expected to evaluate to true ,
otherwise an IllegalArgumentException error is thrown.
•	 checkState (Boolean expression, Object message): This method evaluates
a Boolean expression involving the state of the object, not the arguments.
Again, the Boolean expression is expected to evaluate to true , otherwise
an IllegalArgumentException error is thrown.
 *
 */
public class PreconditionExample {

	public static void main(String[] args) {
		Preconditions.checkNotNull(null,"someObj must not be null");
	}
	
	private String label;
	private int[] values = new int[5];
	private int currentIndex;
	public PreconditionExample(String label) {
	//returns value of object if not null
	this.label = Preconditions.checkNotNull(label,"Label can''t be null");
	}
	public void updateCurrentIndexValue(int index, int valueToSet) {
	//Check index valid first
	this.currentIndex = Preconditions.checkElementIndex(index, values.length,
	"Index out of bounds for values");
	//Validate valueToSet
	Preconditions.checkArgument(valueToSet <= 100,"Value can't be more than 100");
	values[this.currentIndex] = valueToSet;
	}
	public void doOperation(){
		Preconditions.checkState(validateObjectState(),"Can't perform operation");
	}
	private boolean validateObjectState(){
	return this.label.equalsIgnoreCase("open") && values[this.
	currentIndex]==10;
	}
}
