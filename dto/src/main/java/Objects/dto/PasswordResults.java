package Objects.dto;

import java.util.List;


public class PasswordResults {

    private final List<String> listOfImprovements;
    private final PasswordStrength passwordStrength;

    public PasswordResults(PasswordStrength strength, List<String> improvements) {
        passwordStrength = strength;
        listOfImprovements = improvements;
    }

    public List<String> getListOfImprovements() { return listOfImprovements; }

    public PasswordStrength getPasswordStrength() { return passwordStrength; }

    @Override
    public String toString() {
        StringBuilder improvements = new StringBuilder();
        listOfImprovements.forEach(advice -> improvements.append("\t").append(advice).append("\n"));

        return String.format("Password Strength: %s\nList Of improvements:\n%s",
                passwordStrength.toString(), improvements);
    }
}
