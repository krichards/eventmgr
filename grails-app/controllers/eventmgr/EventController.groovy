package eventmgr

import grails.transaction.Transactional

@Transactional(readOnly = true)
class EventController {
    static scaffold = true
}
