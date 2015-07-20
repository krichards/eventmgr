package eventmgr

class FixtureController {

    def index() {
        Venue.deleteAll(Venue.list())
        Speaker.deleteAll(Speaker.list())
        Event.deleteAll(Event.list())

        def save = new Event(uri: 'shoes', name: "Shoes 2005", start: new Date()).save(flush: true)

        println save

        new Speaker(name: "Dave", company: "Dell").save()
        new Speaker(name: "Trevor", company: "Apple").save()
        new Venue(name: "Wembley", address: "London").save()
        redirect(uri: "/")

    }
}
