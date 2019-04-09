package flycat.bean;

public enum AuthScheme {
    WORLD("world"), IP("ip"), AUTH("auth"), DIGEST("digest");
    private final String alias;
    AuthScheme(String alias){
        this.alias = alias;
    }
    public String getAlias(){
        return this.alias;
    }
}
