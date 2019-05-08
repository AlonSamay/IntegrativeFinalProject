package smartspace.layout;

public interface Controller <K> {

    public K [] getAllByAdmin(String adminSmartSpace, String adminMail, int size, int page);
    public K [] storeByAdmin(String adminSmartSpace, String adminMail, K[] boundary);
}
