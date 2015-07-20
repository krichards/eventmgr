import grails.converters.JSON
import org.joda.time.Months

import java.time.MonthDay

class BootStrap {

    def init = { servletContext ->

        JSON.registerObjectMarshaller(Date) { Date it ->
            def returnArray = [:]

            def calendar = it.toCalendar()
            returnArray['day'] = calendar.get(Calendar.DAY_OF_MONTH)
            returnArray['year'] = calendar.get(Calendar.YEAR)
            returnArray['month'] = calendar.get(Calendar.MONTH) + 1
            return returnArray
        }
    }
    def destroy = {
    }
}

