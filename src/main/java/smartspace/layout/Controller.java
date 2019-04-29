package smartspace.layout;

public interface Controller <K> {

    public K [] getAll(String adminSmartSpace, String adminMail, int size, int page);
    public K [] store(String adminSmartSpace, String adminMail, K[] boundary);
}
