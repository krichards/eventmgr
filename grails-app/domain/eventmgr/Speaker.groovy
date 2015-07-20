package eventmgr

class Speaker {

    String name
    String company

    static constraints = {
    }

    public String toString() {
        "$name [ $company ]"
    }
}
