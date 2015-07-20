package eventmgr

class FixtureController {

    def index() {
        Venue.deleteAll(Venue.list())
        Speaker.deleteAll(Speaker.list())
        Event.deleteAll(Event.list())

        new Event(name: "Shoes 2005", start: new Date()).save(flush: true)
        new Speaker(name: "Dave", company: "Dell").save()
        new Speaker(name: "Trevor", company: "Apple").save()
        new Venue(name: "Wembley", address: "London").save()
        redirect(uri: "/")

    }
}
