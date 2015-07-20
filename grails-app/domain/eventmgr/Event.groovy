package eventmgr

class Event extends Publishable  {

    String uri
    String name
    Date start
    Date end
    Venue venue
    List<Speaker> speakers = []

    static constraints = {
        publicationId editable: false
        revision editable:false
        uri required: true
        name required: true
        start required: true
        end required: false, nullable: true
        venue nullable: true, required: false
    }

    static hasMany = [speakers: Speaker]

    public String toString() {
        return "$name ${start}"
    }


}
