import grails.converters.JSON

class BootStrap {

    def grailsApplication

    def init = { servletContext ->

        JSON.registerObjectMarshaller(Date) { Date it ->
            def returnArray = [:]

            def calendar = it.toCalendar()
            returnArray['day'] = calendar.get(Calendar.DAY_OF_MONTH)
            returnArray['year'] = calendar.get(Calendar.YEAR)
            returnArray['month'] = calendar.get(Calendar.MONTH) + 1
            return returnArray
        }

        grailsApplication.getDomainClasses().each { cls ->
            cls.metaClass.clone = {
                return deepClone(delegate);
            }
            cls.metaClass.copy = {
                return deepCopy(delegate, it);
            }
        }
    }

    def destroy = {
    }


}