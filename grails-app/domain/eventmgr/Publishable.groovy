package eventmgr

class Publishable {

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
            findByWorkingEdition(this)
        }
    }

}
