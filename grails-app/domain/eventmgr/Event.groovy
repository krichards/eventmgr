package eventmgr

class Event extends Publishable {

    String name
    Date start
    Date end
    Venue venue
    List<Speaker> speakers = []

    static constraints = {
        name required: true
        start required: true
        end required: false, nullable: true
        venue nullable: true, required: false
        workingEdition  isEditable: false
    }
    static hasMany = [speakers: Speaker]

    public String toString() {
        return "$name ${start}"
    }

}
