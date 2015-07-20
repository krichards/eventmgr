package eventmgr

import static eventmgr.PublicationRevision.PUBLISHED
import static eventmgr.PublicationState.UNPUBLISHED

class Publishable implements Serializable {

    String publicationId
    PublicationRevision revision = PublicationRevision.WORKING
    PublicationState state = UNPUBLISHED

    static mapping = {
        id composite: ['publicationId', 'revision']
    }

    Publishable getVersion(PublicationRevision revision) {
        if (revision == this.revision) {
            return this
        } else {
            return this.findByPublicationIdAndRevision(this.publicationId, revision);
        }
    }

    void publish() {

        if (this.revision == PUBLISHED) {
            throw new RuntimeException("You can't publish version")
        }

        Publishable publishedVersion = getVersion(PUBLISHED)

        if (publishedVersion == null) {
            state = PublicationState.PUBLISHED
            save()

            publishedVersion = (Publishable) clone(this)
            publishedVersion.revision = PUBLISHED
            publishedVersion.save()
        } else {
            publishedVersion.copy(this)
        }

        // audit data includes name etc

        // Json converter uses revision to determine conversion ...
        // Web page uses tags to render revision
    }


}