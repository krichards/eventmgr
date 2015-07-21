<%=packageName ? "package ${packageName}\n\n" : ''%>


import eventmgr.Publishable

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ${className}Controller {

    static allowedMethods = [save: "POST", update: "PUT", delete: ["POST","DELETE"]]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def list = []
//        if (Publishable.isAssignableFrom( ${className} )) {
            // Only show working editions
//            list = ${className}.findAllWhere([workingEdition:null], params)
//        } else {
            list = ${className}.list(params)
//        }
        respond list, model:[${propertyName}Count: ${className}.count()]
    }

    def show(${className} ${propertyName}) {
        respond ${propertyName}
    }

    def create() {
        respond new ${className}(params)
    }

    @Transactional
    def save(${className} ${propertyName}) {
        if (${propertyName} == null) {
            notFound()
            return
        }

        if (${propertyName}.hasErrors()) {
            respond ${propertyName}.errors, view:'create'
            return
        }

        ${propertyName}.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), ${propertyName}.id])
                redirect ${propertyName}
            }
            '*' { respond ${propertyName}, [status: CREATED] }
        }
    }

    def edit(${className} ${propertyName}) {
        respond ${propertyName}
    }

    @Transactional
    def update(${className} ${propertyName}) {
        if (${propertyName} == null) {
            notFound()
            return
        }

        if (${propertyName}.hasErrors()) {
            respond ${propertyName}.errors, view:'edit'
            return
        }

        ${propertyName}.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: '${className}.label', default: '${className}'), ${propertyName}.id])
                redirect ${propertyName}
            }
            '*'{ respond ${propertyName}, [status: OK] }
        }
    }

    @Transactional
    def delete(${className} ${propertyName}) {

        if (${propertyName} == null) {
            notFound()
            return
        }

        ${propertyName}.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: '${className}.label', default: '${className}'), ${propertyName}.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    @Transactional
    def publish(${className} ${propertyName}) {

        if (!${propertyName} instanceof Publishable) {
            notFound()
            return
        }

        if (${propertyName}.publishableType == PublishableType.PUBLISHED) {
            throw new IllegalStateException("Cannot publish something that's published")
        }

        if (${propertyName}.hasErrors()) {
            respond ${propertyName}.errors, view:'edit'
            return
        }

        def publishedRevision = ${className}.findByWorkingEdition(${propertyName})
        if (publishedRevision == null) {
            println "Cloning"
            publishedRevision = ${propertyName}.clone()
            publishedRevision.publishableType = PublishableType.PUBLISHED
        } else {
            println "Copying"
            publishedRevision.copy(${propertyName})
            publishedRevision.publishableType = PublishableType.PUBLISHED
        }
        publishedRevision.workingEdition=${propertyName}
        publishedRevision.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.published.message', args: [message(code: '${className}.label', default: '${className}'), ${propertyName}.id])
                redirect ${propertyName}
            }
            '*'{ respond ${propertyName}, [status: OK] }
        }
    }

}
