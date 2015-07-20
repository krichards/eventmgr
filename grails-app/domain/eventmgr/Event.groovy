package eventmgr

class Event {

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
    }
    static hasMany = [speakers: Speaker]

    public String toString() {
        return "$name ${start}"
    }

    void publish() {
        // Insert JSON representation

        // or

        // Finds existing published or creates new published version copyng current state
        // Id is assigned including published state
        // audit data includes name etc

        // Json converter uses state to determine conversion ...
        // Web page uses tags to render state
    }

//    Event getVersion(PublishedState published) {
//        if (published == this.state) {
//            return this
//        } else {
//            return Event.findByIdAndPublishedState(this.id, published);
//        }
//    }

}
