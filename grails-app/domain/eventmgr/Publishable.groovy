package eventmgr

class Publishable {

    def grailsApplication

    Publishable workingEdition
    PublishableType publishableType = PublishableType.WORKING

    static constraints = {
        workingEdition required: false, unique: true, nullable: true
        publishableType editable: false
    }

    static mapping = {
        // if false 2 different table will be created
        // if true only one table will be created with a 'class' column used by hibernate to cast the record to the right domain class
        tablePerHierarchy false
    }

    Publishable getPublishedEdition() {
        if (workingEdition != null) {
            return this
        } else {
            return Publishable.findByWorkingEdition(this)
        }
    }

    void publish() {
        if (publishableType == PublishableType.PUBLISHED) {
            throw new IllegalStateException("Cannot publish something that's published")
        }

        Publishable publishedRevision = getPublishedEdition()
        if (publishedRevision == null) {
            println "Cloning"
            publishedRevision = (Publishable) deepClone(this)
            publishedRevision.publishableType = PublishableType.PUBLISHED
        } else {
            println "Copying"
            deepCopy(publishedRevision, this)
            publishedRevision.publishableType = PublishableType.PUBLISHED
        }
        publishedRevision.workingEdition=this
        publishedRevision.save(flush:true)
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
