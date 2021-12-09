package Objects.interfaces;

public interface AccountInterDto extends Comparable<AccountInterDto>{
    String getLink();
    String getNotes();
    String getUsername();
    String getPassword();
    String getAccountName();
    AccountCategory getCategory();

    String toString();
}
