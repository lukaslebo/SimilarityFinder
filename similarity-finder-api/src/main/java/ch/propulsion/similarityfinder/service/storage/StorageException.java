package ch.propulsion.similarityfinder.service.storage;

public class StorageException extends RuntimeException {

	private static final long serialVersionUID = 3501685094793029288L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
