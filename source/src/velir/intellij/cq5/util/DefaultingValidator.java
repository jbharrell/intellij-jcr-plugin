package velir.intellij.cq5.util;

public class DefaultingValidator<T> {
	private T defaultValue;
	private Anonymous<T,Boolean> validator;

	public DefaultingValidator(T defaultValue, Anonymous<T,Boolean> validator) {
		if (! validator.call(defaultValue)) {
			throw new IllegalArgumentException("what are you doing? the default isn't valid");
		}

		this.defaultValue = defaultValue;
		this.validator = validator;
	}

	public T validate (T testVal) {
		if (validator.call(testVal)) return testVal;
		return defaultValue;
	}

	public T validateSave (T testVal) {
		if (validator.call(testVal)) defaultValue = testVal;
		return defaultValue;
	}

	public T getDefaultValue() {
		return defaultValue;
	}
}

