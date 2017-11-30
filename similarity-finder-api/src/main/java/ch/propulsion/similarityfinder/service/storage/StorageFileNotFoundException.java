package ch.propulsion.similarityfinder.service.storage;

public class StorageFileNotFoundException extends StorageException {

	private static final long serialVersionUID = -8663952017986232813L;

	public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
