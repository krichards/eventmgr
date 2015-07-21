package eventmgr



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class VenueController {
    static scaffold = true
}
