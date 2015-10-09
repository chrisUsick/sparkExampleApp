import java.util.function.*;

public class Validator<T> {
	public Validation<T> validate(T item, Function<T,Validation<T>> test)  {
		return test.apply(item);
	}
}
