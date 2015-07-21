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

    Object deepClone(domainInstanceToClone) {

        if (domainInstanceToClone.getClass().name.contains("_javassist")) {
            return null
        }

        def newDomainInstance = domainInstanceToClone.getClass().newInstance()
        deepCopy(newDomainInstance, domainInstanceToClone)
        return newDomainInstance
    }

    void deepCopy(newDomainInstance, domainInstanceToClone) {
        println "Copying $domainInstanceToClone to $newDomainInstance"

        //Returns a DefaultGrailsDomainClass (as interface GrailsDomainClass) for inspecting properties
        def domainClass = grailsApplication.getDomainClass(newDomainInstance.getClass().name)

        for (def prop in domainClass?.getPersistentProperties()) {

            if (prop.association) {

                if (prop.owningSide) {
                    //we have to deep clone owned associations
                    if (prop.oneToOne) {
                        def newAssociationInstance = deepClone(domainInstanceToClone?."${prop.name}")
                        newDomainInstance."${prop.name}" = newAssociationInstance
                    } else {

                        domainInstanceToClone."${prop.name}".each { associationInstance ->
                            def newAssociationInstance = deepClone(associationInstance)

                            if (newAssociationInstance) {
                                newDomainInstance."addTo${prop.name.capitalize()}"(newAssociationInstance)
                            }
                        }
                    }
                } else {

                    if (!prop.bidirectional) {
                        if (prop.oneToMany || prop.manyToMany) {
                            newDomainInstance."${prop.name}".clear();
                            domainInstanceToClone."${prop.name}".each {
                                newDomainInstance."${prop.name}".add(it)
                            }
                        }
                    }
                    // Yes bidirectional and not owning. E.g. clone Report, belongsTo Organisation which hasMany
                    // manyToOne. Just add to the owning objects collection.
                    else {
                        if (prop.manyToOne) {
                            newDomainInstance."${prop.name}" = domainInstanceToClone."${prop.name}"
                            def owningInstance = domainInstanceToClone."${prop.name}"
                            // Need to find the collection.
                            String otherSide = prop.otherSide.name.capitalize()
                            //println otherSide
                            //owningInstance."addTo${otherSide}"(newDomainInstance)
                        } else if (prop.manyToMany) {
                            //newDomainInstance."${prop.name}" = [] as Set
                            domainInstanceToClone."${prop.name}".each {
                                //newDomainInstance."${prop.name}".add(it)
                            }
                        } else if (prop.oneToMany) {
                            domainInstanceToClone."${prop.name}".each { associationInstance ->
                                def newAssociationInstance = deepClone(associationInstance)
                                newDomainInstance."addTo${prop.name.capitalize()}"(newAssociationInstance)
                            }
                        }
                    }
                }
            } else {
                //If the property isn't an association then simply copy the value
                newDomainInstance."${prop.name}" = domainInstanceToClone."${prop.name}"

                if (prop.name == "dateCreated" || prop.name == "lastUpdated") {
                    newDomainInstance."${prop.name}" = null
                }
            }
        }
    }

}