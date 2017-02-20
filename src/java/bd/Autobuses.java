package bd;

/**
 *
 * @author Eric
 */
public class Autobuses {
    
    private String matricula, password;

    public Autobuses(String matricula, String password) {
        this.matricula = matricula;
        this.password = password;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
}
