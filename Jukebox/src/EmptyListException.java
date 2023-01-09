public class EmptyListException extends Exception{
    String message;
    public EmptyListException(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
